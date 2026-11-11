package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.render.world.PlacementHighlight;
import com.larpclient.util.entity.CombatState;
import com.larpclient.util.entity.ExplosionDamage2;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.math.BowMath;
import com.larpclient.util.math.RotationLimiter;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoCartModule extends Module implements PlacementHighlight.PlacementHighlightHolder {
   private static final Item[] railItems = new Item[]{Items.RAIL, Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL};
   private static final int fullDrawTicks = 20;
   private static final int logIntervalTicks = 40;
   private static AutoCartModule instance;
   private final ModeSetting rotationMode = this.addSetting(
      new ModeSetting(
         "Rotation", "Visible picks a rail spot by an opponent and turns the real view; Crosshair uses the block you aim at", "Visible", "Visible", "Crosshair"
      )
   );
   private final NumberSetting turnSpeed = this.addNumberSetting("Turn Speed", "Maximum visible turn in degrees per tick", 60.0, 5.0, 180.0, 5.0);
   private final NumberSetting charge = this.addNumberSetting("Charge", "Bow draw ticks before release; 20 is full power", 20.0, 3.0, 20.0, 1.0);
   private final NumberSetting range = this.addNumberSetting("Range", "Rail and cart reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final BooleanSetting targetPlayers = this.setStatus("Players", "Target other survival players", true);
   private final BooleanSetting targetMobs = this.setStatus("Mobs", "Also target hostile mobs for combat or testing", false);
   private final NumberSetting targetRange = this.addNumberSetting("Target Range", "Search distance for opponents", 7.0, 2.0, 12.0, 0.5);
   private final NumberSetting minDamage = this.addNumberSetting("Min Damage", "Minimum expected target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final BooleanSetting antiSelf = this.setStatus("Anti Self", "Reject shots whose strongest possible blast is excessive or lethal for you", true);
   private final NumberSetting maxSelfDamage = this.addNumberSetting(
      "Max Self Damage", "Maximum self damage from the strongest possible blast, in HP", 10.0, 0.0, 36.0, 0.5
   );
   private final PlacementHighlight placementHighlight;
   private AutoCartModule.State state = AutoCartModule.State.IDLE;
   private AutoCartModule.CartPlan plan;
   private AutoCartModule.CartPlan releasePlan;
   private AutoCartModule.RailPlacement pendingPlacement;
   private double[] releasePowerRange;
   private BlockPos detonationRail;
   private Vec3 cartCenter;
   private long releaseTick;
   private long lastLogTick = Long.MIN_VALUE;
   private float detonationPower = -1.0F;
   private Object lastPlayer;
   private Object lastLevel;
   private String status = "Draw a Flame bow to arm";
   private String failureReason = "";

   public AutoCartModule() {
      super("AutoCart", "Release a drawn Flame bow into a freshly placed TNT cart", Category.COMBAT, -1);
      instance = this;
      this.turnSpeed.visibleWhen(this::isVisibleRotation);

      for (Setting var2 : List.of(this.targetPlayers, this.targetMobs, this.targetRange, this.minDamage)) {
         var2.visibleWhen(this::isVisibleRotation);
      }

      this.maxSelfDamage.visibleWhen(this.antiSelf::getValue);
      this.rotationMode.onChange(var1 -> this.cancel("Rotation changed"));
      AutoCartModule var3 = this;
      this.placementHighlight = new PlacementHighlight(this, var1 -> var3.addSetting(var1));
   }

   private NumberSetting addNumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   private BooleanSetting setStatus(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public List<Setting<?>> getRotationSettings() {
      return List.of(this.rotationMode, this.turnSpeed);
   }

   public List<Setting<?>> getChargeSettings() {
      return List.of(this.charge, this.range);
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.targetPlayers, this.targetMobs, this.targetRange, this.minDamage);
   }

   public List<Setting<?>> getSafetySettings() {
      return List.of(this.antiSelf, this.maxSelfDamage);
   }

   @Override
   public PlacementHighlight placementHighlight() {
      return this.placementHighlight;
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.status : "";
   }

   public static void fail(Vec3 var0, float var1) {
      if (instance != null && instance.isEnabled()) {
         AutoCartModule var2 = instance;
         if (var2 != null && var2.state == AutoCartModule.State.AWAITING_DETONATION && var2.cartCenter != null && var0.distanceToSqr(var2.cartCenter) < 1.0) {
            var2.detonationPower = var1;
         }
      }
   }

   @Override
   public void onTick() {
      this.placementHighlight.setTarget(null);
      if (!this.isInGame()) {
         this.cancel("Waiting for a world");
      } else {
         if (this.lastPlayer != minecraft.player || this.lastLevel != minecraft.level) {
            this.cancel("Draw a Flame bow to arm");
            this.lastPlayer = minecraft.player;
            this.lastLevel = minecraft.level;
         }

         long var1 = minecraft.level.getGameTime();
         if (this.state != AutoCartModule.State.AWAITING_DETONATION || !this.planVisibleCart(var1)) {
            int var10000 = InteractionArbiter.canInteract();
            boolean var9 = false;
            byte var8 = (byte)var10000;
            var10000 = Integer.signum(var8);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               int var18 = minecraft.isWindowActive();
               boolean var11 = false;
               byte var10 = (byte)var18;
               var18 = Integer.signum(var10);
               if (((var18 | -var18) >>> 31 ^ 1) == 0 && ViewController.update() == ViewController.Owner.NONE) {
                  int var3 = this.getDrawTicks();
                  if (var3 < 0) {
                     this.idle("Draw a Flame bow to arm");
                     return;
                  }

                  Item var4 = releaseArrow();
                  if (var4 != null) {
                     var18 = NativePlacement.findItemSlot(Items.TNT_MINECART, true);
                     byte var15 = -1;
                     int var14 = var18;
                     var18 = Integer.compare(var14, var15);
                     if ((var18 | -var18) >>> 31 != 0) {
                        if (AuraModule.isShieldUnpressActive()) {
                           this.idle("Paused while Kill Aura lowers your shield");
                           return;
                        }

                        double[] var5 = BowMath.effectiveRange(BowMath.arrowVelocity(Math.max(var3, this.charge.getValue().intValue())));
                        this.plan = this.isVisibleRotation() ? this.planVisibleCart(var4, var5) : this.planCrosshair(var4, var5);
                        if (this.plan == null) {
                           this.state = AutoCartModule.State.IDLE;
                           this.pendingPlacement = null;
                           this.setStatus(this.failureReason);
                           return;
                        }

                        this.state = AutoCartModule.State.PLACING_CART;
                        this.placementHighlight.setTarget(this.plan.rail());
                        if (this.isVisibleRotation() && InteractionArbiter.ownsInteraction(this)) {
                           float[] var6 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), this.plan.aim());
                           float[] var7 = RotationLimiter.stepToward(
                              minecraft.player.getYRot(), minecraft.player.getXRot(), var6[0], var6[1], this.turnSpeed.getValue()
                           );
                           RotationManager.requestForModule(this, RotationManager.Priority.CART, var7[0], var7[1], 1.0);
                        }

                        double var23;
                        var18 = (var23 = var3 - this.charge.getValue()) == 0.0 ? 0 : (var23 < 0.0 ? -1 : 1);
                        boolean var13 = false;
                        int var12 = var18;
                        if ((Integer.signum(var12) >>> 31 ^ 1) == 0) {
                           this.status = String.format(
                              Locale.ROOT, "Drawing %d/%d; expected %.1f HP", var3, this.charge.getValue().intValue(), this.plan.damage()
                           );
                           return;
                        }

                        this.status = String.format(Locale.ROOT, "Armed; expected %.1f HP, worst self %.1f HP", this.plan.damage(), this.plan.self());
                        AutoCartModule.CartPlan var16 = this.plan;
                        InteractionArbiter.queueAction(
                           this, this::releaseArrow, () -> this.plan == var16 && this.state == AutoCartModule.State.PLACING_CART && this.prepareRelease()
                        );
                        return;
                     }
                  }

                  this.idle("Rails and TNT carts must be in your hotbar or hands");
                  return;
               }
            }

            this.idle("Paused: screen, focus or another camera owner");
         }
      }
   }

   private AutoCartModule.CartPlan planVisibleCart(Item var1, double[] var2) {
      List var3 = this.findTargets();
      if (var3.isEmpty()) {
         return this.fail(this.targetMobs.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)");
      }

      if (this.plan != null && this.plan.target() != null && var3.contains(this.plan.target())) {
         AutoCartModule.CartPlan var4 = this.findTargets(this.plan.rail(), this.plan.aim(), var1, var2, var3);
         if (var4 != null) {
            return var4;
         }
      }

      AutoCartModule.CartPlan var13 = null;
      HashSet var5 = new HashSet();

      for (LivingEntity var7 : var3) {
         for (int var8 = -1; var8 <= 0; var8++) {
            for (int var9 = -1; var9 <= 1; var9++) {
               for (int var10 = -1; var10 <= 1; var10++) {
                  BlockPos var11 = var7.blockPosition().offset(var9, var8, var10);
                  if (var5.add(var11)) {
                     AutoCartModule.CartPlan var12 = this.findTargets(var11, null, var1, var2, var3);
                     if (var12 != null && (var13 == null || getTargetSettings(var12) > getTargetSettings(var13))) {
                        var13 = var12;
                     }
                  }
               }
            }
         }
      }

      return var13 != null ? var13 : this.fail("No reachable rail spot meets the damage limits");
   }

   private static double getTargetSettings(AutoCartModule.CartPlan var0) {
      return var0.damage() - var0.self() * 0.25;
   }

   private AutoCartModule.CartPlan findTargets(BlockPos var1, Vec3 var2, Item var3, double[] var4, List<LivingEntity> var5) {
      if (!minecraft.level.hasChunkAt(var1)) {
         return null;
      }

      if (!minecraft.level.getBlockState(var1).is(BlockTags.RAILS) && !NativePlacement.isReplaceable(var1)) {
         return null;
      }

      Vec3 var6 = minecraft.player.getEyePosition();
      double var7 = this.getRange();
      if (var6.distanceToSqr(Vec3.atCenterOf(var1)) > (var7 + 1.0) * (var7 + 1.0)) {
         return null;
      }

      for (Vec3 var10 : var2 != null ? List.of(var2) : findTargets(var1)) {
         Vec3 var11 = var10.subtract(var6);
         AutoCartModule.RailPlacement var12 = this.planRailPlacement(var11, var11, var1, var3);
         if (var12 != null) {
            return this.getDrawTicks(var12, var10, var4, var5, true);
         }
      }

      return null;
   }

   private static List<Vec3> findTargets(BlockPos var0) {
      ArrayList var1 = new ArrayList(9);

      for (double var5 : new double[]{0.0, -0.42, 0.42}) {
         for (double var10 : new double[]{0.0, -0.42, 0.42}) {
            var1.add(new Vec3(var0.getX() + 0.5 + var5, var0.getY() + 0.0625, var0.getZ() + 0.5 + var10));
         }
      }

      return var1;
   }

   private AutoCartModule.CartPlan planCrosshair(Item var1, double[] var2) {
      Vec3 var3 = minecraft.player.getLookAngle();
      AutoCartModule.RailPlacement var4 = this.planRailPlacement(var3, var3, null, var1);
      return var4 == null ? null : this.getDrawTicks(var4, var4.center(), var2, this.findTargets(), false);
   }

   private AutoCartModule.CartPlan getDrawTicks(AutoCartModule.RailPlacement var1, Vec3 var2, double[] var3, List<LivingEntity> var4, boolean var5) {
      double var6 = ExplosionDamage2.damageToWithPower(minecraft.player, var1.center(), var3[2]);
      if (this.antiSelf.getValue()
         && !ExplosionDamage2.isWithinRange(var6, this.maxSelfDamage.getValue(), minecraft.player.getHealth() + minecraft.player.getAbsorptionAmount())) {
         return this.fail(String.format(Locale.ROOT, "Worst-case self damage %.1f HP is over the limit", var6));
      }

      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var4) {
         double var13 = BowMath.simpsonAverage(ExplosionDamage2.damagesForPowers(var12, var1.center(), var3));
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var5 && (var8 == null || var9 < this.minDamage.getValue())
         ? this.fail(String.format(Locale.ROOT, "Expected %.1f HP is below Min Damage", var9))
         : new AutoCartModule.CartPlan(var1.rail(), var2, var8, var9, var6);
   }

   private AutoCartModule.RailPlacement planRailPlacement(Vec3 var1, Vec3 var2, BlockPos var3, Item var4) {
      Vec3 var5 = minecraft.player.getEyePosition();
      double var6 = this.getRange();
      Vec3 var8 = var5.add(var1.normalize().scale(var6));
      BlockHitResult var9 = minecraft.level.clip(new ClipContext(var5, var8, Block.OUTLINE, Fluid.NONE, minecraft.player));
      if (var9.getType() != Type.BLOCK) {
         return this.fail("Aim at the top of a block within reach");
      }

      BlockHitResult var12 = null;
      BlockState var13 = minecraft.level.getBlockState(var9.getBlockPos());
      BlockPos var10;
      BlockState var11;
      if (var13.is(BlockTags.RAILS)) {
         if (NativePlacement.raycastLook(var9.getBlockPos(), var1, var6) == null) {
            return this.fail("That rail is outside native reach");
         }

         var10 = var9.getBlockPos();
         var11 = var13;
      } else {
         var12 = NativePlacement.raycastLook(var9.getBlockPos(), var1, var6);
         NativePlacement.SimulatedPlacement var14 = var12 == null ? null : NativePlacement.simulatePlacement(var12, var4, true);
         if (var14 == null) {
            return this.fail("A rail cannot be placed on that support");
         }

         var10 = var14.target();
         var11 = var14.state();
      }

      if (var3 != null && !var10.equals(var3)) {
         return this.fail("Aim does not reach the planned rail");
      }

      if (var12 != null && var11.getShape(minecraft.level, var10).clip(var5, var8, var10) == null) {
         return this.fail("Aim at a top face so the cart lands on the new rail");
      }

      int var23 = var11.getBlock() instanceof BaseRailBlock var15 && ((RailShape)var11.getValue(var15.getShapeProperty())).isSlope() ? 1 : 0;
      Vec3 var24 = new Vec3(var10.getX() + 0.5, var10.getY() + 0.0625 + (var23 != 0 ? 0.5 : 0.0), var10.getZ() + 0.5);
      AABB var25 = EntityTypes.TNT_MINECART.getDimensions().makeBoundingBox(var24);
      if (!minecraft.level.getEntities((Entity)null, var25, var0 -> var0 instanceof AbstractMinecart).isEmpty()) {
         return this.fail("A minecart already occupies that rail");
      }

      Vec3 var17 = var5.subtract(0.0, 0.1, 0.0);
      Vec3 var18 = var17.add(var2.normalize().scale(var17.distanceTo(var24) + 2.0));
      BlockHitResult var19 = minecraft.level.clip(new ClipContext(var17, var18, Block.COLLIDER, Fluid.NONE, minecraft.player));
      if (var19.getType() != Type.MISS) {
         var18 = var19.getLocation();
      }

      ArrayList var20 = new ArrayList();

      for (Entity var22 : minecraft.level
         .getEntities(minecraft.player, new AABB(var17, var18).inflate(1.0), var0 -> var0.canBeHitByProjectile() && !var0.isSpectator())) {
         var20.add(var22.getBoundingBox());
      }

      if (!BowMath.hasClearLine(var17, var18, var25, var20)) {
         return this.fail("The arrow would hit something before the cart");
      } else {
         return !this.findTargets(var17) && !this.findTargets(var24)
            ? new AutoCartModule.RailPlacement(var10, var12, var4, var24)
            : this.fail("Rain or water would put out the Flame arrow");
      }
   }

   private boolean findTargets(Vec3 var1) {
      BlockPos var2 = BlockPos.containing(var1);
      return minecraft.level.isRainingAt(var2) || minecraft.level.getFluidState(var2).is(FluidTags.WATER);
   }

   private boolean prepareRelease() {
      this.pendingPlacement = null;
      int var1 = this.getDrawTicks();
      if (this.isRuntimeActive()
         && this.plan != null
         && !(var1 < this.charge.getValue())
         && !AuraModule.isShieldUnpressActive()
         && (this.plan.target() == null || !this.isVisibleRotation() || this.isValidTarget(this.plan.target()))) {
         Item var2 = releaseArrow();
         if (var2 != null && NativePlacement.findItemSlot(Items.TNT_MINECART, true) != -1) {
            Vec3 var3 = minecraft.player.getLookAngle();
            Vec3 var4 = CombatState.getLookVector(var3);
            AutoCartModule.RailPlacement var5 = this.planRailPlacement(var3, var4, this.plan.rail(), var2);
            if (var5 == null) {
               this.setStatus(var4.dot(var3) < Math.cos(Math.toRadians(0.5)) ? "Waiting for the aim to reach the server" : this.failureReason);
               return false;
            } else {
               double[] var6 = BowMath.effectiveRange(BowMath.arrowVelocity(var1));
               AutoCartModule.CartPlan var7 = this.getDrawTicks(
                  var5, this.plan.aim(), var6, this.plan.target() == null ? List.of() : List.of(this.plan.target()), this.isVisibleRotation()
               );
               if (var7 == null) {
                  this.setStatus(this.failureReason);
                  return false;
               } else {
                  this.pendingPlacement = var5;
                  this.releasePlan = var7;
                  this.releasePowerRange = var6;
                  return true;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void releaseArrow() {
      AutoCartModule.RailPlacement var1 = this.pendingPlacement;
      this.pendingPlacement = null;
      if (var1 != null) {
         Inventory var2 = minecraft.player.getInventory();
         int var3 = var2.getSelectedSlot();
         int var4 = var1.railHit() == null ? var3 : NativePlacement.findItemSlot(var1.railItem(), true);
         int var5 = NativePlacement.findItemSlot(Items.TNT_MINECART, true);
         if (var4 != -1 && var5 != -1) {
            int var6 = this.getDrawTicks();

            try {
               minecraft.gameMode.releaseUsingItem(minecraft.player);
               if (var1.railHit() != null && !this.useItemOnBlock(var4, var1.railHit())) {
                  this.isValidTarget("Rail placement rejected; the arrow flew without a cart");
                  return;
               }

               BlockHitResult var7 = NativePlacement.raycastLook(var1.rail(), minecraft.player.getLookAngle(), this.getRange());
               if (var7 == null || !minecraft.level.getBlockState(var1.rail()).is(BlockTags.RAILS)) {
                  this.isValidTarget("Placed rail is off the look; the arrow flew without a cart");
                  return;
               }

               if (!this.useItemOnBlock(var5, var7)) {
                  this.isValidTarget("Cart use rejected; the arrow flew without a cart");
                  return;
               }
            } finally {
               var2.setSelectedSlot(var3);
            }

            this.state = AutoCartModule.State.AWAITING_DETONATION;
            this.detonationRail = var1.rail();
            this.cartCenter = var1.center();
            this.releaseTick = minecraft.level.getGameTime();
            this.detonationPower = -1.0F;
            this.status = "Released; waiting for detonation";
            LarpLog.info(
               "AutoCart",
               "Released after {} draw ticks into a cart on {}{}; power {} to {}, expected {} HP{}, worst self {} HP",
               var6,
               var1.rail(),
               var1.railHit() == null ? " (existing rail)" : "",
               isValidTarget(this.releasePowerRange[0]),
               isValidTarget(this.releasePowerRange[2]),
               isValidTarget(this.releasePlan.damage()),
               this.releasePlan.target() == null ? "" : " to " + this.releasePlan.target().getName().getString(),
               isValidTarget(this.releasePlan.self())
            );
         }
      }
   }

   private boolean useItemOnBlock(int var1, BlockHitResult var2) {
      if (var1 >= 0) {
         minecraft.player.getInventory().setSelectedSlot(var1);
      }

      InteractionHand var3 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      boolean var4 = minecraft.gameMode.useItemOn(minecraft.player, var3, var2).consumesAction();
      if (var4) {
         SwingHelper.swingInteract(minecraft.player, var3);
      }

      return var4;
   }

   private boolean planVisibleCart(long var1) {
      if (this.detonationPower >= 0.0F) {
         LarpLog.info(
            "AutoCart",
            "Cart on {} detonated {} ticks after release with server power {}",
            this.detonationRail,
            var1 - this.releaseTick,
            isValidTarget(this.detonationPower)
         );
         this.finish(String.format(Locale.ROOT, "Detonated with power %.2f", this.detonationPower));
         return false;
      }

      if (var1 - this.releaseTick <= 20L) {
         return true;
      }

      BlockPos var3 = this.detonationRail;
      int var4 = !minecraft.level.getEntities((Entity)null, new AABB(var3), var0 -> var0 instanceof MinecartTNT).isEmpty() ? 1 : 0;
      this.isValidTarget(
         (var4 != 0 ? "No detonation; the cart remains, so the arrow missed or was put out" : "No detonation observed within one second")
            + " at "
            + var3.toShortString()
      );
      return false;
   }

   private boolean isValidTarget(LivingEntity var1) {
      if (var1 == minecraft.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != minecraft.level
         || minecraft.player.distanceTo(var1) > this.targetRange.getValue()
         || minecraft.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.targetPlayers.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.getInstance().getModuleManager().getModule(MCFriendModule.class);
            return var3 == null || !var3.isFriend(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.targetMobs.getValue() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> findTargets() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : minecraft.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.isValidTarget(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(minecraft.player::distanceToSqr));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private int getDrawTicks() {
      LocalPlayer var1 = minecraft.player;
      if (var1 != null && var1.isUsingItem()) {
         ItemStack var2 = var1.getUseItem();
         return var2.is(Items.BOW) && hasFlame(var2) ? var1.getTicksUsingItem() : -1;
      } else {
         return -1;
      }
   }

   private static boolean hasFlame(ItemStack var0) {
      ItemEnchantments var1 = (ItemEnchantments)var0.get(DataComponents.ENCHANTMENTS);
      if (var1 == null) {
         return false;
      }

      for (Entry var3 : var1.entrySet()) {
         if (((Holder)var3.getKey()).is(Enchantments.FLAME)) {
            return true;
         }
      }

      return false;
   }

   private static Item releaseArrow() {
      for (Item var3 : railItems) {
         if (NativePlacement.findItemSlot(var3, true) != -1) {
            return var3;
         }
      }

      return null;
   }

   private boolean isVisibleRotation() {
      return this.rotationMode.isMode("Visible");
   }

   private double getRange() {
      return Math.min(this.range.getValue(), minecraft.player.blockInteractionRange());
   }

   private static String isValidTarget(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private <T> T fail(String var1) {
      this.failureReason = var1;
      return null;
   }

   private void setStatus(String var1) {
      long var2 = minecraft.level == null ? 0L : minecraft.level.getGameTime();
      if (!var1.equals(this.status) && (this.lastLogTick == Long.MIN_VALUE || var2 < this.lastLogTick || var2 - this.lastLogTick >= 40L)) {
         LarpLog.debug("AutoCart", "{}", var1);
         this.lastLogTick = var2;
      }

      this.status = var1;
   }

   private void idle(String var1) {
      this.state = AutoCartModule.State.IDLE;
      this.plan = null;
      this.pendingPlacement = null;
      this.setStatus(var1);
   }

   private void isValidTarget(String var1) {
      LarpLog.info("AutoCart", "{}", var1);
      this.finish(var1);
   }

   private void finish(String var1) {
      this.state = AutoCartModule.State.IDLE;
      this.plan = null;
      this.pendingPlacement = null;
      this.detonationRail = null;
      this.cartCenter = null;
      this.detonationPower = -1.0F;
      this.status = var1;
   }

   private void cancel(String var1) {
      InteractionArbiter.findAttackBlocker(this);
      this.placementHighlight.clear();
      this.finish(var1);
   }

   @Override
   public void onEnable() {
      this.lastPlayer = this.lastLevel = null;
      this.lastLogTick = Long.MIN_VALUE;
      this.cancel("Draw a Flame bow to arm");
      this.placementHighlight.reset();
      LarpLog.info(
         "AutoCart",
         "Insta-cart armed; rotation={}, charge={} ticks, range={}",
         this.rotationMode.getValue(),
         this.charge.getValue().intValue(),
         this.range.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.cancel("Draw a Flame bow to arm");
      this.lastPlayer = this.lastLevel = null;
      LarpLog.info("AutoCart", "Insta-cart disarmed; aim, pending release and detonation wait cleared");
   }

   private record CartPlan(BlockPos rail, Vec3 aim, LivingEntity target, double damage, double self) {
   }

   private record RailPlacement(BlockPos rail, BlockHitResult railHit, Item railItem, Vec3 center) {
   }

   private enum State {
      IDLE,
      PLACING_CART,
      AWAITING_DETONATION;
   }
}
