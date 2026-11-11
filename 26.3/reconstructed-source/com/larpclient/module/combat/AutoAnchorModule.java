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
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.world.PlacementHighlight;
import com.larpclient.util.entity.ExplosionDamage;
import com.larpclient.util.entity.ExplosionDamage2;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoAnchorModule extends Module implements PlacementHighlight.PlacementHighlightHolder, NativePlacement.PlacementAimer {
   private static final int sERVER_TIMEOUT_TICKS = 12;
   private static final int sEARCH_PADDING = 2;
   private static AutoAnchorModule instance;
   private final NumberSetting range = this.hasRequiredItems("Range", "Anchor reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final NumberSetting delay = this.hasRequiredItems("Delay", "Ticks between confirmed anchor steps", 0.0, 0.0, 20.0, 1.0);
   private final BooleanSetting autoSwitch = this.addToggle("Auto Switch", "Use hotbar items and restore the selected slot after each step", true);
   private final BooleanSetting safeAnchor = this.addToggle(
      "Safe Anchor", "Place a block between you and the anchor before detonating, where it leaves you least exposed", true
   );
   private final ModeSetting shieldBlock = this.addSetting(
      new ModeSetting("Shield Block", "Block placed as the shield; the blast hurts players before it breaks blocks", "Glowstone", "Glowstone", "Obsidian")
   );
   private final BooleanSetting players = this.addToggle("Players", "Target other survival players", true);
   private final BooleanSetting mobs = this.addToggle("Mobs", "Also target hostile mobs for combat or testing", false);
   private final NumberSetting targetRange = this.hasRequiredItems("Target Range", "Search distance for opponents", 8.0, 2.0, 14.0, 0.5);
   private final NumberSetting minDamage = this.hasRequiredItems("Min Damage", "Minimum estimated target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final BooleanSetting antiSelf = this.addToggle("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final NumberSetting maxSelfDamage = this.hasRequiredItems("Max Self Damage", "Maximum estimated self damage in HP", 8.0, 0.0, 36.0, 0.5);
   private final BooleanSetting pauseUsingItem = this.addToggle("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final PlacementHighlight placementHighlight;
   private AutoAnchorModule.AnchorPlan plan;
   private String pendingStep;
   private String failureReason = "";
   private String lastLoggedStatus = "";
   private String status = "Waiting for a target";
   private BooleanSupplier pendingCondition;
   private long pendingSinceTick;
   private long nextStepTick;
   private Object lastPlayer;
   private Object lastLevel;
   private Object aimPlayer;
   private Object aimLevel;
   private int aimTick;

   public AutoAnchorModule() {
      super("AutoAnchor", "Automatically place, charge and detonate respawn anchors", Category.COMBAT, -1);
      instance = this;
      this.shieldBlock.visibleWhen(this.safeAnchor::getValue);
      this.maxSelfDamage.visibleWhen(this.antiSelf::getValue);
      this.rotation.onChange(var1 -> {
         this.resetPlan("Rotation changed");
         this.releaseView();
      });
      this.placementHighlight = new PlacementHighlight(this, var1 -> this.addSetting(var1), 0.5, 0.3);
   }

   private NumberSetting hasRequiredItems(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new NumberSetting(var1, var2, var3, var5, var7, var9));
   }

   private BooleanSetting addToggle(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public static AutoAnchorModule getInstance() {
      return instance;
   }

   public List<Setting<?>> canPlaceShield() {
      return List.of(this.range, this.delay, this.autoSwitch, this.safeAnchor, this.shieldBlock);
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.players, this.mobs, this.targetRange);
   }

   public List<Setting<?>> getDamageSettings() {
      return List.of(this.minDamage, this.antiSelf, this.maxSelfDamage, this.pauseUsingItem);
   }

   public List<Setting<?>> getRotationSettings() {
      return List.of(this.rotation);
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

   @Override
   public void onEnable() {
      this.lastPlayer = this.lastLevel = null;
      this.resetPlan("Waiting for a target");
      this.placementHighlight.reset();
      LarpLog.info(
         "AutoAnchor",
         "Enabled; rotation={}, range={}, safeAnchor={}, minDamage={}, maxSelf={}",
         this.rotation.getValue(),
         this.range.getValue(),
         this.safeAnchor.getValue(),
         this.minDamage.getValue(),
         this.antiSelf.getValue() ? this.maxSelfDamage.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.resetPlan("Waiting for a target");
      this.releaseView();
      this.placementHighlight.clear();
      this.lastPlayer = this.lastLevel = null;
      LarpLog.info("AutoAnchor", "Disabled; anchor intents and server waits cleared");
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (ViewController.canAcquire(ViewController.Owner.AUTO_ANCHOR)) {
            RotationManager.request(RotationManager.Priority.DEFENSE, var1, var2, 1.0);
            if (RotationManager.isActive(RotationManager.Priority.DEFENSE)) {
               this.aimPlayer = minecraft.player;
               this.aimLevel = minecraft.level;
               this.aimTick = minecraft.player.tickCount;
               ViewController.acquire(ViewController.Owner.AUTO_ANCHOR);
            } else {
               this.releaseView();
            }
         }
      }
   }

   private void releaseView() {
      this.aimPlayer = this.aimLevel = null;
      ViewController.release(ViewController.Owner.AUTO_ANCHOR);
   }

   public boolean isRotationActive() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.rotation.isMode("Visible")
            && minecraft.player != null
            && this.aimPlayer == minecraft.player
            && this.aimLevel == minecraft.level
            && minecraft.player.tickCount >= this.aimTick
            && minecraft.player.tickCount - this.aimTick <= 1
            && (!this.pauseUsingItem.getValue() || !minecraft.player.isUsingItem());
   }

   public float getAnchorYaw(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()
            && ViewController.update() == ViewController.Owner.AUTO_ANCHOR
            && RotationManager.isActive(RotationManager.Priority.DEFENSE)) {
            minecraft.player.setYHeadRot(minecraft.player.getYRot());
            return minecraft.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onTick() {
      this.placementHighlight.setTarget(null);
      NativePlacement.cancel(this);
      if (!this.isInGame()) {
         this.resetPlan("Waiting for a world");
      } else {
         if (this.lastPlayer != minecraft.player || this.lastLevel != minecraft.level) {
            this.resetPlan("Waiting for a target");
            this.lastPlayer = minecraft.player;
            this.lastLevel = minecraft.level;
         }

         try {
            this.tickPlan();
         } finally {
            int var10000 = this.status.equals(this.lastLoggedStatus);
            boolean var5 = false;
            byte var4 = (byte)var10000;
            var10000 = Integer.signum(var4);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.lastLoggedStatus = this.status;
               LarpLog.debug("AutoAnchor", "Status: {}", this.status);
            }
         }
      }
   }

   private void tickPlan() {
      long var1 = minecraft.level.getGameTime();
      if (this.pendingStep != null) {
         if (!this.pendingCondition.getAsBoolean()) {
            if (var1 - this.pendingSinceTick > 12L) {
               LarpLog.info("AutoAnchor", "No server update after {} ticks for: {}; replanning", 12, this.pendingStep);
               this.resetPlan("Server did not confirm the last step");
               return;
            }

            if (this.plan != null) {
               this.placementHighlight.setTarget(this.plan.anchor());
            }

            this.status = "Waiting for " + this.pendingStep;
            return;
         }

         LarpLog.debug("AutoAnchor", "Server confirmed: {}", this.pendingStep);
         if (this.pendingStep.equals("detonation")) {
            this.plan = null;
         }

         this.pendingStep = null;
         this.nextStepTick = var1 + this.delay.getValue().intValue();
      }

      String var3 = this.getPauseReason();
      if (var3 != null) {
         this.plan = null;
         this.status = var3;
      } else if (var1 < this.nextStepTick) {
         if (this.plan != null) {
            this.placementHighlight.setTarget(this.plan.anchor());
         }
      } else {
         List var4 = this.findTargets();
         if (var4.isEmpty()) {
            this.plan = null;
            this.status = this.mobs.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)";
         } else {
            if (this.plan != null) {
               this.plan = this.revalidatePlan(this.plan, var4);
            }

            if (this.plan == null) {
               this.plan = this.findBestPlan(var4);
            }

            if (this.plan == null) {
               this.status = this.failureReason;
            } else {
               this.placementHighlight.setTarget(this.plan.anchor());
               VisualsRenderer.setLastTarget(this.plan.target());
               this.executePlan(this.plan);
            }
         }
      }
   }

   private String getPauseReason() {
      if (!InteractionArbiter.canInteract() || !minecraft.isWindowActive()) {
         return "Paused: screen, focus or detached camera";
      } else if (this.pauseUsingItem.getValue() && minecraft.player.isUsingItem()) {
         return "Paused while using an item";
      } else if (minecraft.player.isSecondaryUseActive()) {
         return "Paused while sneaking";
      } else if (this.isSpawnSetDimension(minecraft.player.blockPosition())) {
         return "Anchors set spawn here, so they cannot explode";
      } else {
         return this.rotation.isMode("Visible") && !ViewController.canAcquire(ViewController.Owner.AUTO_ANCHOR) ? "Paused for another camera owner" : null;
      }
   }

   private void executePlan(AutoAnchorModule.AnchorPlan var1) {
      BlockState var2 = minecraft.level.getBlockState(var1.anchor());
      BlockPos var3 = var1.anchor();
      if (!var2.is(Blocks.RESPAWN_ANCHOR)) {
         this.getPauseReason(true, var3, Items.RESPAWN_ANCHOR, "anchor placement", () -> minecraft.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      } else if ((Integer)var2.getValue(RespawnAnchorBlock.CHARGE) == 0) {
         this.getPauseReason(false, var3, Items.GLOWSTONE, "anchor charge", () -> this.getAnchorCharge(var3) > 0);
      } else if (var1.shield() != null && NativePlacement.isReplaceable(var1.shield())) {
         BlockPos var5 = var1.shield();
         this.getPauseReason(true, var5, this.isSpawnSetDimension(), "shield placement", () -> !NativePlacement.isReplaceable(var5));
      } else {
         if (minecraft.player.getOffhandItem().is(Items.GLOWSTONE) && (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) < 4) {
            this.status = "Move offhand glowstone before detonation";
            return;
         }

         Item var4 = this.findDetonationItem();
         if (var4 == null) {
            this.status = "A hotbar item other than glowstone is required to detonate";
            return;
         }

         this.getPauseReason(false, var3, var4, "detonation", () -> !minecraft.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      }
   }

   private void getPauseReason(boolean var1, BlockPos var2, Item var3, String var4, BooleanSupplier var5) {
      AutoAnchorModule.AnchorPlan var6 = this.plan;
      BooleanSupplier var7 = () -> this.plan == var6 && this.pendingStep == null && this.getPauseReason() == null;
      Consumer var8 = var5x -> {
         if (!var5x) {
            this.status = "Anchor " + var4 + " rejected; retrying";
         } else {
            this.pendingStep = var4;
            this.pendingCondition = var5;
            this.pendingSinceTick = minecraft.level.getGameTime();
            if (var4.equals("detonation")) {
               LarpLog.info(
                  "AutoAnchor",
                  "Detonating anchor at {}: {} HP on {}, {} HP to you, {}% exposed{}",
                  var2.toShortString(),
                  String.format(Locale.ROOT, "%.1f", var6.damage()),
                  var6.target().getName().getString(),
                  String.format(Locale.ROOT, "%.1f", var6.self()),
                  Math.round(var6.exposure() * 100.0),
                  var6.shield() == null ? "" : " behind a shield at " + var6.shield().toShortString()
               );
            }
         }
      };
      boolean var9 = this.rotation.isMode("Visible");
      boolean var10 = var1
         ? NativePlacement.requestPlacement(this, var2, var3, this.range.getValue(), var9, this.autoSwitch.getValue(), var7, var8)
         : NativePlacement.pause(this, var2, var3, this.range.getValue(), var9, this.autoSwitch.getValue(), var7, var8);
      this.status = var10
         ? String.format(
            Locale.ROOT,
            "%s%s; %.1f HP on %s",
            Character.toUpperCase(var4.charAt(0)),
            var4.substring(1),
            this.plan.damage(),
            this.plan.target().getName().getString()
         )
         : (var9 ? "No reachable face for " + var4 : "Aim at the " + (var1 ? "support" : "anchor") + " for " + var4);
   }

   private boolean isSpawnSetDimension(BlockPos var1) {
      return Boolean.TRUE.equals(minecraft.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, var1));
   }

   private int getAnchorCharge(BlockPos var1) {
      BlockState var2 = minecraft.level.getBlockState(var1);
      return var2.is(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   private Item isSpawnSetDimension() {
      return this.shieldBlock.isMode("Obsidian") ? Items.OBSIDIAN : Items.GLOWSTONE;
   }

   private Item findDetonationItem() {
      ItemStack var1 = minecraft.player.getOffhandItem();
      ItemStack var2 = minecraft.player.getMainHandItem();
      if (!var2.is(Items.GLOWSTONE)) {
         return var2.isEmpty() ? Items.AIR : var2.getItem();
      }

      if (this.autoSwitch.getValue()) {
         for (int var3 = 0; var3 < 9; var3++) {
            ItemStack var4 = minecraft.player.getInventory().getItem(var3);
            if (!var4.is(Items.GLOWSTONE) && (var4.isEmpty() ? !var1.isEmpty() : !var1.is(var4.getItem()))) {
               return var4.isEmpty() ? Items.AIR : var4.getItem();
            }
         }
      }

      return null;
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
         if (this.players.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.getInstance().getModuleManager().getModule(MCFriendModule.class);
            return var3 == null || !var3.isFriend(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.mobs.getValue() && var1 instanceof Enemy;
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

   private double getInteractionRange() {
      return Math.min(this.range.getValue(), minecraft.player.blockInteractionRange());
   }

   private boolean getRotationSettings(double var1) {
      return !this.antiSelf.getValue()
         || ExplosionDamage2.isWithinRange(var1, this.maxSelfDamage.getValue(), minecraft.player.getHealth() + minecraft.player.getAbsorptionAmount());
   }

   private AutoAnchorModule.AnchorPlan computePlan(BlockPos var1, BlockPos var2, List<LivingEntity> var3, double var4, double var6) {
      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var3) {
         double var13 = ExplosionDamage.calculateDamage(var12, var1, var2);
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var8 == null ? null : new AutoAnchorModule.AnchorPlan(var1, var2, var8, var9, var4, var6);
   }

   private AutoAnchorModule.AnchorPlan revalidatePlan(AutoAnchorModule.AnchorPlan var1, List<LivingEntity> var2) {
      boolean var3 = minecraft.level.getBlockState(var1.anchor()).is(Blocks.RESPAWN_ANCHOR);
      if ((var3 || NativePlacement.isReplaceable(var1.anchor())) && !this.isSpawnSetDimension(var1.anchor())) {
         if (this.hasRequiredItems(var1.anchor(), var3) && this.findTargets(var1.anchor(), var3)) {
            BlockPos var4 = var1.shield() == null
                  || NativePlacement.isReplaceable(var1.shield()) && (!this.canPlaceShield(var1.shield()) || !this.getInstance(var1.anchor(), var1.shield()))
               ? null
               : var1.shield();
            float var5 = ExplosionDamage.getExposure(minecraft.player, var1.anchor(), var4);
            double var6 = ExplosionDamage.damageFromExposure(minecraft.player, var1.anchor(), var5);
            if (!this.getRotationSettings(var6)) {
               return null;
            }

            AutoAnchorModule.AnchorPlan var8 = this.computePlan(var1.anchor(), var4, var2, var6, var5);
            return var8 != null && var8.damage() >= this.minDamage.getValue() ? var8 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private AutoAnchorModule.AnchorPlan findBestPlan(List<LivingEntity> var1) {
      Vec3 var2 = minecraft.player.getEyePosition();
      double var3 = this.getInteractionRange();
      int var5 = NativePlacement.findItemSlot(Items.RESPAWN_ANCHOR, this.autoSwitch.getValue()) != -1 ? 1 : 0;
      int var6 = NativePlacement.findItemSlot(Items.GLOWSTONE, this.autoSwitch.getValue()) != -1 ? 1 : 0;
      HashSet var7 = new HashSet();
      ArrayList var8 = new ArrayList();
      int var9 = 0;

      record l(BlockPos pos, double bound) {
      }

      for (LivingEntity var11 : var1) {
         BlockPos var12 = var11.blockPosition();

         for (int var13 = -1; var13 <= 2; var13++) {
            for (int var14 = -2; var14 <= 2; var14++) {
               for (int var15 = -2; var15 <= 2; var15++) {
                  BlockPos var16 = var12.offset(var14, var13, var15);
                  if (var7.add(var16)
                     && minecraft.level.hasChunkAt(var16)
                     && !(var2.distanceToSqr(Vec3.atCenterOf(var16)) > (var3 + 1.0) * (var3 + 1.0))
                     && !this.isSpawnSetDimension(var16)) {
                     boolean var17 = minecraft.level.getBlockState(var16).is(Blocks.RESPAWN_ANCHOR);
                     if (var17) {
                        var9++;
                     }

                     if (var17 ? this.hasRequiredItems(var16, true) : var5 != 0 && var6 != 0 && NativePlacement.isReplaceable(var16)) {
                        double var18 = 0.0;

                        for (LivingEntity var21 : var1) {
                           var18 = Math.max(
                              var18,
                              ExplosionDamage2.applyMitigation(
                                 var21, ExplosionDamage2.rawDamageWithPower(var21.position().distanceTo(Vec3.atCenterOf(var16)), 1.0, 5.0)
                              )
                           );
                        }

                        if (var18 >= this.minDamage.getValue()) {
                           var8.add(new l(var16, var18));
                        }
                     }
                  }
               }
            }
         }
      }

      if (var8.isEmpty()) {
         this.failureReason = var5 == 0 && var9 == 0
            ? "No respawn anchors in hotbar or hands"
            : (var6 == 0 && var9 == 0 ? "No glowstone in hotbar or hands" : "No anchor spot in reach can reach Min Damage");
         return null;
      }

      var8.sort(Comparator.comparingDouble(l::bound).reversed());
      AutoAnchorModule.AnchorPlan var28 = null;
      double var29 = Double.MAX_VALUE;
      double var30 = 0.0;

      for (l var32 : var8) {
         if (var28 != null && var32.bound() <= var28.value()) {
            break;
         }

         BlockPos var33 = var32.pos();
         boolean var34 = minecraft.level.getBlockState(var33).is(Blocks.RESPAWN_ANCHOR);
         AutoAnchorModule.AnchorPlan var19 = this.computePlan(var33, null, var1, 0.0, 0.0);
         if (var19 != null) {
            var30 = Math.max(var30, var19.damage());
            if (!(var19.damage() < this.minDamage.getValue()) && (var28 == null || !(var19.damage() <= var28.value()))) {
               float var35 = ExplosionDamage.getExposure(minecraft.player, var33, null);
               ArrayList var36 = new ArrayList();
               if (this.safeAnchor.getValue() && var35 > 0.0F && NativePlacement.findItemSlot(this.isSpawnSetDimension(), this.autoSwitch.getValue()) != -1) {
                  for (BlockPos var23 : ExplosionDamage.neighborsFacing(var33, minecraft.player.getEyePosition())) {
                     if (NativePlacement.isReplaceable(var23)) {
                        float var24 = ExplosionDamage.getExposure(minecraft.player, var33, var23);
                        if (var24 < var35) {
                           var36.add(new AutoAnchorModule.ShieldCandidate(var23, var24));
                        }
                     }
                  }

                  var36.sort(Comparator.comparingDouble(AutoAnchorModule.ShieldCandidate::exposure));
               }

               var36.add(new AutoAnchorModule.ShieldCandidate(null, var35));
               Boolean var37 = null;

               for (AutoAnchorModule.ShieldCandidate var39 : var36) {
                  double var25 = ExplosionDamage.damageFromExposure(minecraft.player, var33, var39.exposure());
                  AutoAnchorModule.AnchorPlan var27 = var39.pos() == null
                     ? new AutoAnchorModule.AnchorPlan(var33, null, var19.target(), var19.damage(), var25, var35)
                     : this.computePlan(var33, var39.pos(), var1, var25, var39.exposure());
                  if (var27 != null) {
                     var29 = Math.min(var29, var27.self());
                     if (this.getRotationSettings(var27.self())
                        && !(var27.damage() < this.minDamage.getValue())
                        && (var28 == null || !(var27.value() <= var28.value()))) {
                        if (var37 == null) {
                           var37 = this.findTargets(var33, var34);
                        }

                        if (!var37) {
                           break;
                        }

                        if (var27.shield() == null || this.canPlaceShield(var27.shield()) && this.getInstance(var33, var27.shield())) {
                           var28 = var27;
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

      if (var28 == null) {
         this.failureReason = var29 != Double.MAX_VALUE && !this.getRotationSettings(var29)
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var29, this.maxSelfDamage.getValue())
            : (
               var30 < this.minDamage.getValue()
                  ? String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var30, this.minDamage.getValue())
                  : (
                     this.rotation.isMode("Visible")
                        ? "No anchor spot with a reachable face or support"
                        : "Aim at an anchor or at a support beside an anchor spot"
                  )
            );
      } else {
         LarpLog.debug(
            "AutoAnchor",
            "Planned anchor at {}{}: {} HP on {}, {} HP to you",
            var28.anchor().toShortString(),
            var28.shield() == null ? "" : " with shield at " + var28.shield().toShortString(),
            String.format(Locale.ROOT, "%.1f", var28.damage()),
            var28.target().getName().getString(),
            String.format(Locale.ROOT, "%.1f", var28.self())
         );
      }

      return var28;
   }

   private boolean hasRequiredItems(BlockPos var1, boolean var2) {
      int var3 = NativePlacement.findItemSlot(Items.GLOWSTONE, this.autoSwitch.getValue()) != -1 ? 1 : 0;
      return !var2
         ? var3 != 0 && NativePlacement.findItemSlot(Items.RESPAWN_ANCHOR, this.autoSwitch.getValue()) != -1
         : this.getAnchorCharge(var1) > 0 || var3 != 0;
   }

   private boolean findTargets(BlockPos var1, boolean var2) {
      boolean var3 = this.rotation.isMode("Visible");
      if (!var2) {
         return NativePlacement.findPlacementWithSwitch(var1, Items.RESPAWN_ANCHOR, this.range.getValue(), var3, this.autoSwitch.getValue()) != null;
      }

      if (!var3) {
         return InteractionArbiter.wasManualClaimed(var1, this.getInteractionRange()) != null;
      }

      Vec3 var4 = minecraft.player.getEyePosition();

      for (Vec3 var6 : AutoCrystalModule.aimPoints(var1, var4)) {
         if (InteractionArbiter.reset(var1, this.getInteractionRange(), var6.subtract(var4)) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean canPlaceShield(BlockPos var1) {
      return NativePlacement.findPlacementWithSwitch(
            var1, this.isSpawnSetDimension(), this.range.getValue(), this.rotation.isMode("Visible"), this.autoSwitch.getValue()
         )
         != null;
   }

   private boolean getInstance(BlockPos var1, BlockPos var2) {
      Vec3 var3 = minecraft.player.getEyePosition();
      AABB var4 = new AABB(var2);
      double var5 = this.getInteractionRange();

      for (Vec3 var8 : AutoCrystalModule.aimPoints(var1, var3)) {
         if (!(var3.distanceToSqr(var8) > var5 * var5) && !var4.clip(var3, var8).isPresent()) {
            BlockHitResult var9 = minecraft.level.clip(new ClipContext(var3, var8, Block.OUTLINE, Fluid.NONE, minecraft.player));
            if (var9.getType() == Type.MISS || var9.getBlockPos().equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   private void resetPlan(String var1) {
      NativePlacement.cancel(this);
      this.plan = null;
      this.pendingStep = null;
      this.pendingCondition = null;
      this.pendingSinceTick = this.nextStepTick = 0L;
      this.status = var1;
   }

   private record AnchorPlan(BlockPos anchor, BlockPos shield, LivingEntity target, double damage, double self, double exposure) {
      double value() {
         return this.damage - this.self * 0.25;
      }
   }

   private record ShieldCandidate(BlockPos pos, float exposure) {
   }
}
