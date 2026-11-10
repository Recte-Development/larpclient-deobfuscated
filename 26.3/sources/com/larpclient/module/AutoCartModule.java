package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.ai;
import com.larpclient.b6;
import com.larpclient.h8;
import com.larpclient.hy;
import com.larpclient.ij;
import com.larpclient.nx;
import com.larpclient.q1;
import com.larpclient.vi;
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

public final class AutoCartModule extends Module implements ai.e {
   private static final Item[] K = new Item[]{Items.RAIL, Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL};
   private static final int f = 20;
   private static final int E = 40;
   private static AutoCartModule l;
   private final com.larpclient.module.o s = this.addSetting(
      new com.larpclient.module.o(
         "Rotation", "Visible picks a rail spot by an opponent and turns the real view; Crosshair uses the block you aim at", "Visible", "Visible", "Crosshair"
      )
   );
   private final b t = this.Y("Turn Speed", "Maximum visible turn in degrees per tick", 60.0, 5.0, 180.0, 5.0);
   private final b B = this.Y("Charge", "Bow draw ticks before release; 20 is full power", 20.0, 3.0, 20.0, 1.0);
   private final b g = this.Y("Range", "Rail and cart reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final m e = this.E("Players", "Target other survival players", true);
   private final m M = this.E("Mobs", "Also target hostile mobs for combat or testing", false);
   private final b i = this.Y("Target Range", "Search distance for opponents", 7.0, 2.0, 12.0, 0.5);
   private final b o = this.Y("Min Damage", "Minimum expected target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final m G = this.E("Anti Self", "Reject shots whose strongest possible blast is excessive or lethal for you", true);
   private final b T = this.Y("Max Self Damage", "Maximum self damage from the strongest possible blast, in HP", 10.0, 0.0, 36.0, 0.5);
   private final ai w;
   private AutoCartModule.h X = AutoCartModule.h.O;
   private AutoCartModule.o W;
   private AutoCartModule.o Z;
   private AutoCartModule.c Q;
   private double[] O;
   private BlockPos S;
   private Vec3 C;
   private long k;
   private long P = Long.MIN_VALUE;
   private float F = -1.0F;
   private Object x;
   private Object d;
   private String I = "Draw a Flame bow to arm";
   private String z = "";

   public AutoCartModule() {
      super("AutoCart", "Release a drawn Flame bow into a freshly placed TNT cart", Category.COMBAT, -1);
      l = this;
      this.t.a(this::W);

      for (r var2 : List.of(this.e, this.M, this.i, this.o)) {
         var2.a(this::W);
      }

      this.T.a(this.G::getValue);
      this.s.w(var1 -> this.j("Rotation changed"));
      AutoCartModule var3 = this;
      this.w = new ai(this, var1 -> var3.addSetting(var1));
   }

   private b Y(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   private m E(String var1, String var2, boolean var3) {
      return this.addSetting(new m(var1, var2, var3));
   }

   public List<r<?>> X() {
      return List.of(this.s, this.t);
   }

   public List<r<?>> o() {
      return List.of(this.B, this.g);
   }

   public List<r<?>> Q() {
      return List.of(this.e, this.M, this.i, this.o);
   }

   public List<r<?>> g() {
      return List.of(this.G, this.T);
   }

   @Override
   public ai placementHighlight() {
      return this.w;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F, com.larpclient.module.l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.I : "";
   }

   public static void h(Vec3 var0, float var1) {
      if (l != null && l.isEnabled()) {
         AutoCartModule var2 = l;
         if (var2 != null && var2.X == AutoCartModule.h.D && var2.C != null && var0.distanceToSqr(var2.C) < 1.0) {
            var2.F = var1;
         }
      }
   }

   @Override
   public void onTick() {
      this.w.H(null);
      if (!this.isInGame()) {
         this.j("Waiting for a world");
      } else {
         if (this.x != mc.player || this.d != mc.level) {
            this.j("Draw a Flame bow to arm");
            this.x = mc.player;
            this.d = mc.level;
         }

         long var1 = mc.level.getGameTime();
         if (this.X != AutoCartModule.h.D || !this.w(var1)) {
            int var10000 = _p.g();
            boolean var9 = false;
            byte var8 = (byte)var10000;
            var10000 = Integer.signum(var8);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               int var18 = mc.isWindowActive();
               boolean var11 = false;
               byte var10 = (byte)var18;
               var18 = Integer.signum(var10);
               if (((var18 | -var18) >>> 31 ^ 1) == 0 && ij.L() == ij.r.X) {
                  int var3 = this.J();
                  if (var3 < 0) {
                     this.L("Draw a Flame bow to arm");
                     return;
                  }

                  Item var4 = b();
                  if (var4 != null) {
                     var18 = vi.u(Items.TNT_MINECART, true);
                     byte var15 = -1;
                     int var14 = var18;
                     var18 = Integer.compare(var14, var15);
                     if ((var18 | -var18) >>> 31 != 0) {
                        if (AuraModule.K()) {
                           this.L("Paused while Kill Aura lowers your shield");
                           return;
                        }

                        double[] var5 = hy.N(hy.b(Math.max(var3, this.B.getValue().intValue())));
                        this.W = this.W() ? this.w(var4, var5) : this.P(var4, var5);
                        if (this.W == null) {
                           this.X = AutoCartModule.h.O;
                           this.Q = null;
                           this.E(this.z);
                           return;
                        }

                        this.X = AutoCartModule.h.k;
                        this.w.H(this.W.rail());
                        if (this.W() && _p.Q(this)) {
                           float[] var6 = b6.R(mc.player.getEyePosition(), this.W.aim());
                           float[] var7 = nx.M(mc.player.getYRot(), mc.player.getXRot(), var6[0], var6[1], this.t.getValue());
                           b6.P(this, b6.w.S, var7[0], var7[1], 1.0);
                        }

                        double var23;
                        var18 = (var23 = var3 - this.B.getValue()) == 0.0 ? 0 : (var23 < 0.0 ? -1 : 1);
                        boolean var13 = false;
                        int var12 = var18;
                        if ((Integer.signum(var12) >>> 31 ^ 1) == 0) {
                           this.I = String.format(Locale.ROOT, "Drawing %d/%d; expected %.1f HP", var3, this.B.getValue().intValue(), this.W.damage());
                           return;
                        }

                        this.I = String.format(Locale.ROOT, "Armed; expected %.1f HP, worst self %.1f HP", this.W.damage(), this.W.self());
                        AutoCartModule.o var16 = this.W;
                        _p.w(this, this::b, () -> this.W == var16 && this.X == AutoCartModule.h.k && this.q());
                        return;
                     }
                  }

                  this.L("Rails and TNT carts must be in your hotbar or hands");
                  return;
               }
            }

            this.L("Paused: screen, focus or another camera owner");
         }
      }
   }

   private AutoCartModule.o w(Item var1, double[] var2) {
      List var3 = this.x();
      if (var3.isEmpty()) {
         return this.h(this.M.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)");
      }

      if (this.W != null && this.W.target() != null && var3.contains(this.W.target())) {
         AutoCartModule.o var4 = this.x(this.W.rail(), this.W.aim(), var1, var2, var3);
         if (var4 != null) {
            return var4;
         }
      }

      AutoCartModule.o var13 = null;
      HashSet var5 = new HashSet();

      for (LivingEntity var7 : var3) {
         for (int var8 = -1; var8 <= 0; var8++) {
            for (int var9 = -1; var9 <= 1; var9++) {
               for (int var10 = -1; var10 <= 1; var10++) {
                  BlockPos var11 = var7.blockPosition().offset(var9, var8, var10);
                  if (var5.add(var11)) {
                     AutoCartModule.o var12 = this.x(var11, null, var1, var2, var3);
                     if (var12 != null && (var13 == null || Q(var12) > Q(var13))) {
                        var13 = var12;
                     }
                  }
               }
            }
         }
      }

      return var13 != null ? var13 : this.h("No reachable rail spot meets the damage limits");
   }

   private static double Q(AutoCartModule.o var0) {
      return var0.damage() - var0.self() * 0.25;
   }

   private AutoCartModule.o x(BlockPos var1, Vec3 var2, Item var3, double[] var4, List<LivingEntity> var5) {
      if (!mc.level.hasChunkAt(var1)) {
         return null;
      }

      if (!mc.level.getBlockState(var1).is(BlockTags.RAILS) && !vi.i(var1)) {
         return null;
      }

      Vec3 var6 = mc.player.getEyePosition();
      double var7 = this.B();
      if (var6.distanceToSqr(Vec3.atCenterOf(var1)) > (var7 + 1.0) * (var7 + 1.0)) {
         return null;
      }

      for (Vec3 var10 : var2 != null ? List.of(var2) : x(var1)) {
         Vec3 var11 = var10.subtract(var6);
         AutoCartModule.c var12 = this.D(var11, var11, var1, var3);
         if (var12 != null) {
            return this.J(var12, var10, var4, var5, true);
         }
      }

      return null;
   }

   private static List<Vec3> x(BlockPos var0) {
      ArrayList var1 = new ArrayList(9);

      for (double var5 : new double[]{0.0, -0.42, 0.42}) {
         for (double var10 : new double[]{0.0, -0.42, 0.42}) {
            var1.add(new Vec3(var0.getX() + 0.5 + var5, var0.getY() + 0.0625, var0.getZ() + 0.5 + var10));
         }
      }

      return var1;
   }

   private AutoCartModule.o P(Item var1, double[] var2) {
      Vec3 var3 = mc.player.getLookAngle();
      AutoCartModule.c var4 = this.D(var3, var3, null, var1);
      return var4 == null ? null : this.J(var4, var4.center(), var2, this.x(), false);
   }

   private AutoCartModule.o J(AutoCartModule.c var1, Vec3 var2, double[] var3, List<LivingEntity> var4, boolean var5) {
      double var6 = com.larpclient.t.J(mc.player, var1.center(), var3[2]);
      if (this.G.getValue() && !com.larpclient.t.x(var6, this.T.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         return this.h(String.format(Locale.ROOT, "Worst-case self damage %.1f HP is over the limit", var6));
      }

      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var4) {
         double var13 = hy.h(com.larpclient.t.O(var12, var1.center(), var3));
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var5 && (var8 == null || var9 < this.o.getValue())
         ? this.h(String.format(Locale.ROOT, "Expected %.1f HP is below Min Damage", var9))
         : new AutoCartModule.o(var1.rail(), var2, var8, var9, var6);
   }

   private AutoCartModule.c D(Vec3 var1, Vec3 var2, BlockPos var3, Item var4) {
      Vec3 var5 = mc.player.getEyePosition();
      double var6 = this.B();
      Vec3 var8 = var5.add(var1.normalize().scale(var6));
      BlockHitResult var9 = mc.level.clip(new ClipContext(var5, var8, Block.OUTLINE, Fluid.NONE, mc.player));
      if (var9.getType() != Type.BLOCK) {
         return this.h("Aim at the top of a block within reach");
      }

      BlockHitResult var12 = null;
      BlockState var13 = mc.level.getBlockState(var9.getBlockPos());
      BlockPos var10;
      BlockState var11;
      if (var13.is(BlockTags.RAILS)) {
         if (vi.W(var9.getBlockPos(), var1, var6) == null) {
            return this.h("That rail is outside native reach");
         }

         var10 = var9.getBlockPos();
         var11 = var13;
      } else {
         var12 = vi.W(var9.getBlockPos(), var1, var6);
         vi.a var14 = var12 == null ? null : vi.U(var12, var4, true);
         if (var14 == null) {
            return this.h("A rail cannot be placed on that support");
         }

         var10 = var14.target();
         var11 = var14.state();
      }

      if (var3 != null && !var10.equals(var3)) {
         return this.h("Aim does not reach the planned rail");
      }

      if (var12 != null && var11.getShape(mc.level, var10).clip(var5, var8, var10) == null) {
         return this.h("Aim at a top face so the cart lands on the new rail");
      }

      int var23 = var11.getBlock() instanceof BaseRailBlock var15 && ((RailShape)var11.getValue(var15.getShapeProperty())).isSlope() ? 1 : 0;
      Vec3 var24 = new Vec3(var10.getX() + 0.5, var10.getY() + 0.0625 + (var23 != 0 ? 0.5 : 0.0), var10.getZ() + 0.5);
      AABB var25 = EntityTypes.TNT_MINECART.getDimensions().makeBoundingBox(var24);
      if (!mc.level.getEntities((Entity)null, var25, var0 -> var0 instanceof AbstractMinecart).isEmpty()) {
         return this.h("A minecart already occupies that rail");
      }

      Vec3 var17 = var5.subtract(0.0, 0.1, 0.0);
      Vec3 var18 = var17.add(var2.normalize().scale(var17.distanceTo(var24) + 2.0));
      BlockHitResult var19 = mc.level.clip(new ClipContext(var17, var18, Block.COLLIDER, Fluid.NONE, mc.player));
      if (var19.getType() != Type.MISS) {
         var18 = var19.getLocation();
      }

      ArrayList var20 = new ArrayList();

      for (Entity var22 : mc.level.getEntities(mc.player, new AABB(var17, var18).inflate(1.0), var0 -> var0.canBeHitByProjectile() && !var0.isSpectator())) {
         var20.add(var22.getBoundingBox());
      }

      if (!hy.a(var17, var18, var25, var20)) {
         return this.h("The arrow would hit something before the cart");
      } else {
         return !this.x(var17) && !this.x(var24) ? new AutoCartModule.c(var10, var12, var4, var24) : this.h("Rain or water would put out the Flame arrow");
      }
   }

   private boolean x(Vec3 var1) {
      BlockPos var2 = BlockPos.containing(var1);
      return mc.level.isRainingAt(var2) || mc.level.getFluidState(var2).is(FluidTags.WATER);
   }

   private boolean q() {
      this.Q = null;
      int var1 = this.J();
      if (this.isRuntimeActive()
         && this.W != null
         && !(var1 < this.B.getValue())
         && !AuraModule.K()
         && (this.W.target() == null || !this.W() || this.T(this.W.target()))) {
         Item var2 = b();
         if (var2 != null && vi.u(Items.TNT_MINECART, true) != -1) {
            Vec3 var3 = mc.player.getLookAngle();
            Vec3 var4 = q1.Q(var3);
            AutoCartModule.c var5 = this.D(var3, var4, this.W.rail(), var2);
            if (var5 == null) {
               this.E(var4.dot(var3) < Math.cos(Math.toRadians(0.5)) ? "Waiting for the aim to reach the server" : this.z);
               return false;
            } else {
               double[] var6 = hy.N(hy.b(var1));
               AutoCartModule.o var7 = this.J(var5, this.W.aim(), var6, this.W.target() == null ? List.of() : List.of(this.W.target()), this.W());
               if (var7 == null) {
                  this.E(this.z);
                  return false;
               } else {
                  this.Q = var5;
                  this.Z = var7;
                  this.O = var6;
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

   private void b() {
      AutoCartModule.c var1 = this.Q;
      this.Q = null;
      if (var1 != null) {
         Inventory var2 = mc.player.getInventory();
         int var3 = var2.getSelectedSlot();
         int var4 = var1.railHit() == null ? var3 : vi.u(var1.railItem(), true);
         int var5 = vi.u(Items.TNT_MINECART, true);
         if (var4 != -1 && var5 != -1) {
            int var6 = this.J();

            try {
               mc.gameMode.releaseUsingItem(mc.player);
               if (var1.railHit() != null && !this.O(var4, var1.railHit())) {
                  this.T("Rail placement rejected; the arrow flew without a cart");
                  return;
               }

               BlockHitResult var7 = vi.W(var1.rail(), mc.player.getLookAngle(), this.B());
               if (var7 == null || !mc.level.getBlockState(var1.rail()).is(BlockTags.RAILS)) {
                  this.T("Placed rail is off the look; the arrow flew without a cart");
                  return;
               }

               if (!this.O(var5, var7)) {
                  this.T("Cart use rejected; the arrow flew without a cart");
                  return;
               }
            } finally {
               var2.setSelectedSlot(var3);
            }

            this.X = AutoCartModule.h.D;
            this.S = var1.rail();
            this.C = var1.center();
            this.k = mc.level.getGameTime();
            this.F = -1.0F;
            this.I = "Released; waiting for detonation";
            _q.z(
               "AutoCart",
               "Released after {} draw ticks into a cart on {}{}; power {} to {}, expected {} HP{}, worst self {} HP",
               var6,
               var1.rail(),
               var1.railHit() == null ? " (existing rail)" : "",
               T(this.O[0]),
               T(this.O[2]),
               T(this.Z.damage()),
               this.Z.target() == null ? "" : " to " + this.Z.target().getName().getString(),
               T(this.Z.self())
            );
         }
      }
   }

   private boolean O(int var1, BlockHitResult var2) {
      if (var1 >= 0) {
         mc.player.getInventory().setSelectedSlot(var1);
      }

      InteractionHand var3 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      boolean var4 = mc.gameMode.useItemOn(mc.player, var3, var2).consumesAction();
      if (var4) {
         h8.i(mc.player, var3);
      }

      return var4;
   }

   private boolean w(long var1) {
      if (this.F >= 0.0F) {
         _q.z("AutoCart", "Cart on {} detonated {} ticks after release with server power {}", this.S, var1 - this.k, T(this.F));
         this.n(String.format(Locale.ROOT, "Detonated with power %.2f", this.F));
         return false;
      }

      if (var1 - this.k <= 20L) {
         return true;
      }

      BlockPos var3 = this.S;
      int var4 = !mc.level.getEntities((Entity)null, new AABB(var3), var0 -> var0 instanceof MinecartTNT).isEmpty() ? 1 : 0;
      this.T(
         (var4 != 0 ? "No detonation; the cart remains, so the arrow missed or was put out" : "No detonation observed within one second")
            + " at "
            + var3.toShortString()
      );
      return false;
   }

   private boolean T(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != mc.level
         || mc.player.distanceTo(var1) > this.i.getValue()
         || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.e.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.N().j().y(MCFriendModule.class);
            return var3 == null || !var3.M(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.M.getValue() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> x() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.T(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private int J() {
      LocalPlayer var1 = mc.player;
      if (var1 != null && var1.isUsingItem()) {
         ItemStack var2 = var1.getUseItem();
         return var2.is(Items.BOW) && f(var2) ? var1.getTicksUsingItem() : -1;
      } else {
         return -1;
      }
   }

   private static boolean f(ItemStack var0) {
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

   private static Item b() {
      for (Item var3 : K) {
         if (vi.u(var3, true) != -1) {
            return var3;
         }
      }

      return null;
   }

   private boolean W() {
      return this.s.w("Visible");
   }

   private double B() {
      return Math.min(this.g.getValue(), mc.player.blockInteractionRange());
   }

   private static String T(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private <T> T h(String var1) {
      this.z = var1;
      return null;
   }

   private void E(String var1) {
      long var2 = mc.level == null ? 0L : mc.level.getGameTime();
      if (!var1.equals(this.I) && (this.P == Long.MIN_VALUE || var2 < this.P || var2 - this.P >= 40L)) {
         _q.c("AutoCart", "{}", var1);
         this.P = var2;
      }

      this.I = var1;
   }

   private void L(String var1) {
      this.X = AutoCartModule.h.O;
      this.W = null;
      this.Q = null;
      this.E(var1);
   }

   private void T(String var1) {
      _q.z("AutoCart", "{}", var1);
      this.n(var1);
   }

   private void n(String var1) {
      this.X = AutoCartModule.h.O;
      this.W = null;
      this.Q = null;
      this.S = null;
      this.C = null;
      this.F = -1.0F;
      this.I = var1;
   }

   private void j(String var1) {
      _p.e(this);
      this.w.m();
      this.n(var1);
   }

   @Override
   public void onEnable() {
      this.x = this.d = null;
      this.P = Long.MIN_VALUE;
      this.j("Draw a Flame bow to arm");
      this.w.b();
      _q.z("AutoCart", "Insta-cart armed; rotation={}, charge={} ticks, range={}", this.s.getValue(), this.B.getValue().intValue(), this.g.getValue());
   }

   @Override
   public void onDisable() {
      this.j("Draw a Flame bow to arm");
      this.x = this.d = null;
      _q.z("AutoCart", "Insta-cart disarmed; aim, pending release and detonation wait cleared");
   }

   private record c(BlockPos rail, BlockHitResult railHit, Item railItem, Vec3 center) {
   }

   private enum h {
      O,
      k,
      D;
   }

   private record o(BlockPos rail, Vec3 aim, LivingEntity target, double damage, double self) {
   }
}
