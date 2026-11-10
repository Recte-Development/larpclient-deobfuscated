package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.j6;
import com.larpclient.jb;
import com.larpclient.kq;
import com.larpclient.ks;
import com.larpclient.mr;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
import com.larpclient.u6;
import com.larpclient.zu;
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

public final class AutoCartModule extends Module implements mr.n {
   private static final Item[] w = new Item[]{Items.RAIL, Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL};
   private static final int T = 20;
   private static final int d = 40;
   private static AutoCartModule z;
   private final com.larpclient.module.l C = this.addSetting(
      new com.larpclient.module.l(
         "Rotation", "Visible picks a rail spot by an opponent and turns the real view; Crosshair uses the block you aim at", "Visible", "Visible", "Crosshair"
      )
   );
   private final e f = this.e("Turn Speed", "Maximum visible turn in degrees per tick", 60.0, 5.0, 180.0, 5.0);
   private final e n = this.e("Charge", "Bow draw ticks before release; 20 is full power", 20.0, 3.0, 20.0, 1.0);
   private final e h = this.e("Range", "Rail and cart reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final s l = this.V("Players", "Target other survival players", true);
   private final s s = this.V("Mobs", "Also target hostile mobs for combat or testing", false);
   private final e V = this.e("Target Range", "Search distance for opponents", 7.0, 2.0, 12.0, 0.5);
   private final e J = this.e("Min Damage", "Minimum expected target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final s a = this.V("Anti Self", "Reject shots whose strongest possible blast is excessive or lethal for you", true);
   private final e b = this.e("Max Self Damage", "Maximum self damage from the strongest possible blast, in HP", 10.0, 0.0, 36.0, 0.5);
   private final mr v;
   private AutoCartModule.r B = AutoCartModule.r.w;
   private AutoCartModule.l R;
   private AutoCartModule.l k;
   private AutoCartModule.w c;
   private double[] M;
   private BlockPos i;
   private Vec3 g;
   private long e;
   private long X = Long.MIN_VALUE;
   private float H = -1.0F;
   private Object p;
   private Object O;
   private String t = "Draw a Flame bow to arm";
   private String j = "";

   public AutoCartModule() {
      super("AutoCart", "Release a drawn Flame bow into a freshly placed TNT cart", Category.COMBAT, -1);
      z = this;
      this.f.P(this::D);

      for (q var2 : List.of(this.l, this.s, this.V, this.J)) {
         var2.P(this::D);
      }

      this.b.P(this.a::getValue);
      this.C.o(var1 -> this.E("Rotation changed"));
      AutoCartModule var3 = this;
      this.v = new mr(this, var1 -> var3.addSetting(var1));
   }

   private e e(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   private s V(String var1, String var2, boolean var3) {
      return this.addSetting(new s(var1, var2, var3));
   }

   public List<q<?>> y() {
      return List.of(this.C, this.f);
   }

   public List<q<?>> K() {
      return List.of(this.n, this.h);
   }

   public List<q<?>> f() {
      return List.of(this.l, this.s, this.V, this.J);
   }

   public List<q<?>> b() {
      return List.of(this.a, this.b);
   }

   @Override
   public mr placementHighlight() {
      return this.v;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K, m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.t : "";
   }

   public static void c(Vec3 var0, float var1) {
      if (z != null && z.isEnabled()) {
         AutoCartModule var2 = z;
         if (var2 != null && var2.B == AutoCartModule.r.E && var2.g != null && var0.distanceToSqr(var2.g) < 1.0) {
            var2.H = var1;
         }
      }
   }

   @Override
   public void onTick() {
      this.v.q(null);
      if (!this.isInGame()) {
         this.E("Waiting for a world");
      } else {
         if (this.p != mc.player || this.O != mc.level) {
            this.E("Draw a Flame bow to arm");
            this.p = mc.player;
            this.O = mc.level;
         }

         long var1 = mc.level.getGameTime();
         if (this.B != AutoCartModule.r.E || !this.i(var1)) {
            int var10000 = ta.Y();
            boolean var11 = false;
            byte var10 = (byte)var10000;
            var10000 = Integer.signum(var10);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && mc.isWindowActive() && p_.A() == p_.o.T) {
               int var3 = this.S();
               boolean var9 = false;
               int var8 = var3;
               if ((Integer.signum(var8) >>> 31 ^ 1) == 0) {
                  this.t("Draw a Flame bow to arm");
               } else {
                  Item var4 = p();
                  if (var4 != null) {
                     var10000 = t1.M(Items.TNT_MINECART, true);
                     byte var13 = -1;
                     int var12 = var10000;
                     var10000 = Integer.compare(var12, var13);
                     if ((var10000 | -var10000) >>> 31 != 0) {
                        if (AuraModule.N()) {
                           this.t("Paused while Kill Aura lowers your shield");
                           return;
                        }

                        double[] var5 = jb.A(jb.u(Math.max(var3, this.n.getValue().intValue())));
                        this.R = this.D() ? this.e(var4, var5) : this.v(var4, var5);
                        if (this.R == null) {
                           this.B = AutoCartModule.r.w;
                           this.c = null;
                           this.w(this.j);
                           return;
                        }

                        this.B = AutoCartModule.r.m;
                        this.v.q(this.R.rail());
                        int var20 = this.D();
                        boolean var15 = false;
                        byte var14 = (byte)var20;
                        var20 = Integer.signum(var14);
                        if (((var20 | -var20) >>> 31 ^ 1) == 0 && ta.E(this)) {
                           float[] var6 = j6.s(mc.player.getEyePosition(), this.R.aim());
                           float[] var7 = u6.A(mc.player.getYRot(), mc.player.getXRot(), var6[0], var6[1], this.f.getValue());
                           j6.U(this, j6.l.T, var7[0], var7[1], 1.0);
                        }

                        if (var3 < this.n.getValue()) {
                           this.t = String.format(Locale.ROOT, "Drawing %d/%d; expected %.1f HP", var3, this.n.getValue().intValue(), this.R.damage());
                           return;
                        }

                        this.t = String.format(Locale.ROOT, "Armed; expected %.1f HP, worst self %.1f HP", this.R.damage(), this.R.self());
                        AutoCartModule.l var16 = this.R;
                        ta.F(this, this::t, () -> this.R == var16 && this.B == AutoCartModule.r.m && this.H());
                        return;
                     }
                  }

                  this.t("Rails and TNT carts must be in your hotbar or hands");
               }
            } else {
               this.t("Paused: screen, focus or another camera owner");
            }
         }
      }
   }

   private AutoCartModule.l e(Item var1, double[] var2) {
      List var3 = this.L();
      if (var3.isEmpty()) {
         return this.w(this.s.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)");
      }

      if (this.R != null && this.R.target() != null && var3.contains(this.R.target())) {
         AutoCartModule.l var4 = this.T(this.R.rail(), this.R.aim(), var1, var2, var3);
         if (var4 != null) {
            return var4;
         }
      }

      AutoCartModule.l var13 = null;
      HashSet var5 = new HashSet();

      for (LivingEntity var7 : var3) {
         for (int var8 = -1; var8 <= 0; var8++) {
            for (int var9 = -1; var9 <= 1; var9++) {
               for (int var10 = -1; var10 <= 1; var10++) {
                  BlockPos var11 = var7.blockPosition().offset(var9, var8, var10);
                  if (var5.add(var11)) {
                     AutoCartModule.l var12 = this.T(var11, null, var1, var2, var3);
                     if (var12 != null && (var13 == null || B(var12) > B(var13))) {
                        var13 = var12;
                     }
                  }
               }
            }
         }
      }

      return var13 != null ? var13 : this.w("No reachable rail spot meets the damage limits");
   }

   private static double B(AutoCartModule.l var0) {
      return var0.damage() - var0.self() * 0.25;
   }

   private AutoCartModule.l T(BlockPos var1, Vec3 var2, Item var3, double[] var4, List<LivingEntity> var5) {
      if (!mc.level.hasChunkAt(var1)) {
         return null;
      }

      if (!mc.level.getBlockState(var1).is(BlockTags.RAILS) && !t1.t(var1)) {
         return null;
      }

      Vec3 var6 = mc.player.getEyePosition();
      double var7 = this.g();
      if (var6.distanceToSqr(Vec3.atCenterOf(var1)) > (var7 + 1.0) * (var7 + 1.0)) {
         return null;
      }

      for (Vec3 var10 : var2 != null ? List.of(var2) : x(var1)) {
         Vec3 var11 = var10.subtract(var6);
         AutoCartModule.w var12 = this.R(var11, var11, var1, var3);
         if (var12 != null) {
            return this.h(var12, var10, var4, var5, true);
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

   private AutoCartModule.l v(Item var1, double[] var2) {
      Vec3 var3 = mc.player.getLookAngle();
      AutoCartModule.w var4 = this.R(var3, var3, null, var1);
      return var4 == null ? null : this.h(var4, var4.center(), var2, this.L(), false);
   }

   private AutoCartModule.l h(AutoCartModule.w var1, Vec3 var2, double[] var3, List<LivingEntity> var4, boolean var5) {
      double var6 = zu.z(mc.player, var1.center(), var3[2]);
      if (this.a.getValue() && !zu.P(var6, this.b.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         return this.w(String.format(Locale.ROOT, "Worst-case self damage %.1f HP is over the limit", var6));
      }

      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var4) {
         double var13 = jb.W(zu.z(var12, var1.center(), var3));
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return !var5 || var8 != null && !(var9 < this.J.getValue())
         ? new AutoCartModule.l(var1.rail(), var2, var8, var9, var6)
         : this.w(String.format(Locale.ROOT, "Expected %.1f HP is below Min Damage", var9));
   }

   private AutoCartModule.w R(Vec3 var1, Vec3 var2, BlockPos var3, Item var4) {
      Vec3 var5 = mc.player.getEyePosition();
      double var6 = this.g();
      Vec3 var8 = var5.add(var1.normalize().scale(var6));
      BlockHitResult var9 = mc.level.clip(new ClipContext(var5, var8, Block.OUTLINE, Fluid.NONE, mc.player));
      if (var9.getType() != Type.BLOCK) {
         return this.w("Aim at the top of a block within reach");
      }

      BlockHitResult var12 = null;
      BlockState var13 = mc.level.getBlockState(var9.getBlockPos());
      BlockPos var10;
      BlockState var11;
      if (var13.is(BlockTags.RAILS)) {
         if (t1.Z(var9.getBlockPos(), var1, var6) == null) {
            return this.w("That rail is outside native reach");
         }

         var10 = var9.getBlockPos();
         var11 = var13;
      } else {
         var12 = t1.Z(var9.getBlockPos(), var1, var6);
         t1.j var14 = var12 == null ? null : t1.W(var12, var4, true);
         if (var14 == null) {
            return this.w("A rail cannot be placed on that support");
         }

         var10 = var14.target();
         var11 = var14.state();
      }

      if (var3 != null && !var10.equals(var3)) {
         return this.w("Aim does not reach the planned rail");
      }

      if (var12 != null && var11.getShape(mc.level, var10).clip(var5, var8, var10) == null) {
         return this.w("Aim at a top face so the cart lands on the new rail");
      }

      int var23 = var11.getBlock() instanceof BaseRailBlock var15 && ((RailShape)var11.getValue(var15.getShapeProperty())).isSlope() ? 1 : 0;
      Vec3 var24 = new Vec3(var10.getX() + 0.5, var10.getY() + 0.0625 + (var23 != 0 ? 0.5 : 0.0), var10.getZ() + 0.5);
      AABB var25 = EntityTypes.TNT_MINECART.getDimensions().makeBoundingBox(var24);
      if (!mc.level.getEntities((Entity)null, var25, var0 -> var0 instanceof AbstractMinecart).isEmpty()) {
         return this.w("A minecart already occupies that rail");
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

      if (!jb.P(var17, var18, var25, var20)) {
         return this.w("The arrow would hit something before the cart");
      } else {
         return !this.Z(var17) && !this.Z(var24) ? new AutoCartModule.w(var10, var12, var4, var24) : this.w("Rain or water would put out the Flame arrow");
      }
   }

   private boolean Z(Vec3 var1) {
      BlockPos var2 = BlockPos.containing(var1);
      return mc.level.isRainingAt(var2) || mc.level.getFluidState(var2).is(FluidTags.WATER);
   }

   private boolean H() {
      this.c = null;
      int var1 = this.S();
      if (this.isRuntimeActive()
         && this.R != null
         && !(var1 < this.n.getValue())
         && !AuraModule.N()
         && (this.R.target() == null || !this.D() || this.f(this.R.target()))) {
         Item var2 = p();
         if (var2 != null && t1.M(Items.TNT_MINECART, true) != -1) {
            Vec3 var3 = mc.player.getLookAngle();
            Vec3 var4 = kq.r(var3);
            AutoCartModule.w var5 = this.R(var3, var4, this.R.rail(), var2);
            if (var5 == null) {
               this.w(var4.dot(var3) < Math.cos(Math.toRadians(0.5)) ? "Waiting for the aim to reach the server" : this.j);
               return false;
            } else {
               double[] var6 = jb.A(jb.u(var1));
               AutoCartModule.l var7 = this.h(var5, this.R.aim(), var6, this.R.target() == null ? List.of() : List.of(this.R.target()), this.D());
               if (var7 == null) {
                  this.w(this.j);
                  return false;
               } else {
                  this.c = var5;
                  this.k = var7;
                  this.M = var6;
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

   private void t() {
      AutoCartModule.w var1 = this.c;
      this.c = null;
      if (var1 != null) {
         Inventory var2 = mc.player.getInventory();
         int var3 = var2.getSelectedSlot();
         int var4 = var1.railHit() == null ? var3 : t1.M(var1.railItem(), true);
         int var5 = t1.M(Items.TNT_MINECART, true);
         if (var4 != -1 && var5 != -1) {
            int var6 = this.S();

            try {
               mc.gameMode.releaseUsingItem(mc.player);
               if (var1.railHit() != null && !this.D(var4, var1.railHit())) {
                  this.N("Rail placement rejected; the arrow flew without a cart");
                  return;
               }

               BlockHitResult var7 = t1.Z(var1.rail(), mc.player.getLookAngle(), this.g());
               if (var7 == null || !mc.level.getBlockState(var1.rail()).is(BlockTags.RAILS)) {
                  this.N("Placed rail is off the look; the arrow flew without a cart");
                  return;
               }

               if (!this.D(var5, var7)) {
                  this.N("Cart use rejected; the arrow flew without a cart");
                  return;
               }
            } finally {
               var2.setSelectedSlot(var3);
            }

            this.B = AutoCartModule.r.E;
            this.i = var1.rail();
            this.g = var1.center();
            this.e = mc.level.getGameTime();
            this.H = -1.0F;
            this.t = "Released; waiting for detonation";
            com.larpclient.h.A(
               "AutoCart",
               "Released after {} draw ticks into a cart on {}{}; power {} to {}, expected {} HP{}, worst self {} HP",
               var6,
               var1.rail(),
               var1.railHit() == null ? " (existing rail)" : "",
               c(this.M[0]),
               c(this.M[2]),
               c(this.k.damage()),
               this.k.target() == null ? "" : " to " + this.k.target().getName().getString(),
               c(this.k.self())
            );
         }
      }
   }

   private boolean D(int var1, BlockHitResult var2) {
      if (var1 >= 0) {
         mc.player.getInventory().setSelectedSlot(var1);
      }

      InteractionHand var3 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      boolean var4 = mc.gameMode.useItemOn(mc.player, var3, var2).consumesAction();
      if (var4) {
         ks.p(mc.player, var3);
      }

      return var4;
   }

   private boolean i(long var1) {
      if (this.H >= 0.0F) {
         com.larpclient.h.A("AutoCart", "Cart on {} detonated {} ticks after release with server power {}", this.i, var1 - this.e, c(this.H));
         this.S(String.format(Locale.ROOT, "Detonated with power %.2f", this.H));
         return false;
      }

      if (var1 - this.e <= 20L) {
         return true;
      }

      BlockPos var3 = this.i;
      int var4 = !mc.level.getEntities((Entity)null, new AABB(var3), var0 -> var0 instanceof MinecartTNT).isEmpty() ? 1 : 0;
      this.N(
         (var4 != 0 ? "No detonation; the cart remains, so the arrow missed or was put out" : "No detonation observed within one second")
            + " at "
            + var3.toShortString()
      );
      return false;
   }

   private boolean f(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != mc.level
         || mc.player.distanceTo(var1) > this.V.getValue()
         || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.l.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.R().z().p(MCFriendModule.class);
            return var3 == null || !var3.S(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.s.getValue() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> L() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.f(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private int S() {
      LocalPlayer var1 = mc.player;
      if (var1 != null && var1.isUsingItem()) {
         ItemStack var2 = var1.getUseItem();
         return var2.is(Items.BOW) && V(var2) ? var1.getTicksUsingItem() : -1;
      } else {
         return -1;
      }
   }

   private static boolean V(ItemStack var0) {
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

   private static Item p() {
      for (Item var3 : w) {
         if (t1.M(var3, true) != -1) {
            return var3;
         }
      }

      return null;
   }

   private boolean D() {
      return this.C.O("Visible");
   }

   private double g() {
      return Math.min(this.h.getValue(), mc.player.blockInteractionRange());
   }

   private static String c(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private <T> T w(String var1) {
      this.j = var1;
      return null;
   }

   private void w(String var1) {
      long var2 = mc.level == null ? 0L : mc.level.getGameTime();
      if (!var1.equals(this.t) && (this.X == Long.MIN_VALUE || var2 < this.X || var2 - this.X >= 40L)) {
         com.larpclient.h.U("AutoCart", "{}", var1);
         this.X = var2;
      }

      this.t = var1;
   }

   private void t(String var1) {
      this.B = AutoCartModule.r.w;
      this.R = null;
      this.c = null;
      this.w(var1);
   }

   private void N(String var1) {
      com.larpclient.h.A("AutoCart", "{}", var1);
      this.S(var1);
   }

   private void S(String var1) {
      this.B = AutoCartModule.r.w;
      this.R = null;
      this.c = null;
      this.i = null;
      this.g = null;
      this.H = -1.0F;
      this.t = var1;
   }

   private void E(String var1) {
      ta.s(this);
      this.v.c();
      this.S(var1);
   }

   @Override
   public void onEnable() {
      this.p = this.O = null;
      this.X = Long.MIN_VALUE;
      this.E("Draw a Flame bow to arm");
      this.v.t();
      com.larpclient.h.A(
         "AutoCart", "Insta-cart armed; rotation={}, charge={} ticks, range={}", this.C.getValue(), this.n.getValue().intValue(), this.h.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.E("Draw a Flame bow to arm");
      this.p = this.O = null;
      com.larpclient.h.A("AutoCart", "Insta-cart disarmed; aim, pending release and detonation wait cleared");
   }

   private record l(BlockPos rail, Vec3 aim, LivingEntity target, double damage, double self) {
   }

   private enum r {
      w,
      m,
      E;
   }

   private record w(BlockPos rail, BlockHitResult railHit, Item railItem, Vec3 center) {
   }
}
