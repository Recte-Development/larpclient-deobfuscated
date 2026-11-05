package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.ep;
import com.larpclient.hp;
import com.larpclient.hy;
import com.larpclient.jg;
import com.larpclient.od;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.ru;
import com.larpclient.xq;
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

public final class AutoCartModule extends Module implements hy.c {
   private static final Item[] f = new Item[]{Items.RAIL, Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL};
   private static final int w = 20;
   private static final int z = 40;
   private static AutoCartModule d;
   private final lp k = this.addSetting(
      new lp(
         "Rotation", "Visible picks a rail spot by an opponent and turns the real view; Crosshair uses the block you aim at", "Visible", "Visible", "Crosshair"
      )
   );
   private final lc G = this.U("Turn Speed", "Maximum visible turn in degrees per tick", 60.0, 5.0, 180.0, 5.0);
   private final lc S = this.U("Charge", "Bow draw ticks before release; 20 is full power", 20.0, 3.0, 20.0, 1.0);
   private final lc C = this.U("Range", "Rail and cart reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final ln T = this.E("Players", "Target other survival players", true);
   private final ln L = this.E("Mobs", "Also target hostile mobs for combat or testing", false);
   private final lc h = this.U("Target Range", "Search distance for opponents", 7.0, 2.0, 12.0, 0.5);
   private final lc D = this.U("Min Damage", "Minimum expected target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final ln M = this.E("Anti Self", "Reject shots whose strongest possible blast is excessive or lethal for you", true);
   private final lc g = this.U("Max Self Damage", "Maximum self damage from the strongest possible blast, in HP", 10.0, 0.0, 36.0, 0.5);
   private final hy s;
   private AutoCartModule.m Z = AutoCartModule.m.M;
   private AutoCartModule.l e;
   private AutoCartModule.l I;
   private AutoCartModule.e j;
   private double[] A;
   private BlockPos v;
   private Vec3 y;
   private long H;
   private long K = Long.MIN_VALUE;
   private float m = -1.0F;
   private Object V;
   private Object o;
   private String c = "Draw a Flame bow to arm";
   private String p = "";

   public AutoCartModule() {
      super("AutoCart", "Release a drawn Flame bow into a freshly placed TNT cart", Category.COMBAT, -1);
      d = this;
      this.G.x(this::I);

      for (l6 var2 : List.of(this.T, this.L, this.h, this.D)) {
         var2.x(this::I);
      }

      this.g.x(this.M::getValue);
      this.k.W(var1 -> this.f("Rotation changed"));
      AutoCartModule var3 = this;
      this.s = new hy(this, var1 -> var3.addSetting(var1));
   }

   private lc U(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   private ln E(String var1, String var2, boolean var3) {
      return this.addSetting(new ln(var1, var2, var3));
   }

   public List<l6<?>> U() {
      return List.of(this.k, this.G);
   }

   public List<l6<?>> u() {
      return List.of(this.S, this.C);
   }

   public List<l6<?>> g() {
      return List.of(this.T, this.L, this.h, this.D);
   }

   public List<l6<?>> Q() {
      return List.of(this.M, this.g);
   }

   @Override
   public hy placementHighlight() {
      return this.s;
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.c : "";
   }

   public static void E(Vec3 var0, float var1) {
      if (d != null && d.isEnabled()) {
         AutoCartModule var2 = d;
         if (var2 != null && var2.Z == AutoCartModule.m.y && var2.y != null && var0.distanceToSqr(var2.y) < 1.0) {
            var2.m = var1;
         }
      }
   }

   @Override
   public void onTick() {
      this.s.i(null);
      if (!this.isInGame()) {
         this.f("Waiting for a world");
      } else {
         if (this.V != mc.player || this.o != mc.level) {
            this.f("Draw a Flame bow to arm");
            this.V = mc.player;
            this.o = mc.level;
         }

         long var1 = mc.level.getGameTime();
         if (this.Z == AutoCartModule.m.y) {
            int var10000 = this.g(var1);
            boolean var9 = false;
            byte var8 = (byte)var10000;
            var10000 = Integer.signum(var8);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               return;
            }
         }

         if (d8.a() && mc.isWindowActive() && rr.u() == rr.f.i) {
            int var3 = this.V();
            if (var3 < 0) {
               this.D("Draw a Flame bow to arm");
            } else {
               Item var4 = Z();
               if (var4 != null) {
                  int var18 = com.larpclient.m.Q(Items.TNT_MINECART, true);
                  byte var15 = -1;
                  int var14 = var18;
                  var18 = Integer.compare(var14, var15);
                  if ((var18 | -var18) >>> 31 != 0) {
                     if (AuraModule.W()) {
                        this.D("Paused while Kill Aura lowers your shield");
                        return;
                     }

                     double[] var5 = jg.q(jg.d(Math.max(var3, this.S.getValue().intValue())));
                     int var10001 = this.I();
                     boolean var11 = false;
                     byte var10 = (byte)var10001;
                     var10001 = Integer.signum(var10);
                     this.e = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? this.u(var4, var5) : this.s(var4, var5);
                     if (this.e == null) {
                        this.Z = AutoCartModule.m.M;
                        this.j = null;
                        this.a(this.p);
                        return;
                     }

                     this.Z = AutoCartModule.m.C;
                     this.s.i(this.e.rail());
                     int var20 = this.I();
                     boolean var13 = false;
                     byte var12 = (byte)var20;
                     var20 = Integer.signum(var12);
                     if (((var20 | -var20) >>> 31 ^ 1) == 0 && d8.m(this)) {
                        float[] var6 = ep.g(mc.player.getEyePosition(), this.e.aim());
                        float[] var7 = ru.Y(mc.player.getYRot(), mc.player.getXRot(), var6[0], var6[1], this.G.getValue());
                        ep.t(this, ep.z.D, var7[0], var7[1], 1.0);
                     }

                     if (var3 < this.S.getValue()) {
                        this.c = String.format(Locale.ROOT, "Drawing %d/%d; expected %.1f HP", var3, this.S.getValue().intValue(), this.e.damage());
                        return;
                     }

                     this.c = String.format(Locale.ROOT, "Armed; expected %.1f HP, worst self %.1f HP", this.e.damage(), this.e.self());
                     AutoCartModule.l var16 = this.e;
                     d8.A(this, this::Z, () -> this.e == var16 && this.Z == AutoCartModule.m.C && this.M());
                     return;
                  }
               }

               this.D("Rails and TNT carts must be in your hotbar or hands");
            }
         } else {
            this.D("Paused: screen, focus or another camera owner");
         }
      }
   }

   private AutoCartModule.l u(Item var1, double[] var2) {
      List var3 = this.N();
      if (var3.isEmpty()) {
         return this.c(this.L.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)");
      }

      if (this.e != null && this.e.target() != null && var3.contains(this.e.target())) {
         AutoCartModule.l var4 = this.Q(this.e.rail(), this.e.aim(), var1, var2, var3);
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
                     AutoCartModule.l var12 = this.Q(var11, null, var1, var2, var3);
                     if (var12 != null && (var13 == null || K(var12) > K(var13))) {
                        var13 = var12;
                     }
                  }
               }
            }
         }
      }

      return var13 != null ? var13 : this.c("No reachable rail spot meets the damage limits");
   }

   private static double K(AutoCartModule.l var0) {
      return var0.damage() - var0.self() * 0.25;
   }

   private AutoCartModule.l Q(BlockPos var1, Vec3 var2, Item var3, double[] var4, List<LivingEntity> var5) {
      if (!mc.level.hasChunkAt(var1)) {
         return null;
      }

      if (!mc.level.getBlockState(var1).is(BlockTags.RAILS) && !com.larpclient.m.Q(var1)) {
         return null;
      }

      Vec3 var6 = mc.player.getEyePosition();
      double var7 = this.o();
      if (var6.distanceToSqr(Vec3.atCenterOf(var1)) > (var7 + 1.0) * (var7 + 1.0)) {
         return null;
      }

      for (Vec3 var10 : var2 != null ? List.of(var2) : R(var1)) {
         Vec3 var11 = var10.subtract(var6);
         AutoCartModule.e var12 = this.T(var11, var11, var1, var3);
         if (var12 != null) {
            return this.L(var12, var10, var4, var5, true);
         }
      }

      return null;
   }

   private static List<Vec3> R(BlockPos var0) {
      ArrayList var1 = new ArrayList(9);

      for (double var5 : new double[]{0.0, -0.42, 0.42}) {
         for (double var10 : new double[]{0.0, -0.42, 0.42}) {
            var1.add(new Vec3(var0.getX() + 0.5 + var5, var0.getY() + 0.0625, var0.getZ() + 0.5 + var10));
         }
      }

      return var1;
   }

   private AutoCartModule.l s(Item var1, double[] var2) {
      Vec3 var3 = mc.player.getLookAngle();
      AutoCartModule.e var4 = this.T(var3, var3, null, var1);
      return var4 == null ? null : this.L(var4, var4.center(), var2, this.N(), false);
   }

   private AutoCartModule.l L(AutoCartModule.e var1, Vec3 var2, double[] var3, List<LivingEntity> var4, boolean var5) {
      double var6 = od.e(mc.player, var1.center(), var3[2]);
      if (this.M.getValue() && !od.p(var6, this.g.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         return this.c(String.format(Locale.ROOT, "Worst-case self damage %.1f HP is over the limit", var6));
      }

      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var4) {
         double var13 = jg.N(od.C(var12, var1.center(), var3));
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return !var5 || var8 != null && !(var9 < this.D.getValue())
         ? new AutoCartModule.l(var1.rail(), var2, var8, var9, var6)
         : this.c(String.format(Locale.ROOT, "Expected %.1f HP is below Min Damage", var9));
   }

   private AutoCartModule.e T(Vec3 var1, Vec3 var2, BlockPos var3, Item var4) {
      Vec3 var5 = mc.player.getEyePosition();
      double var6 = this.o();
      Vec3 var8 = var5.add(var1.normalize().scale(var6));
      BlockHitResult var9 = mc.level.clip(new ClipContext(var5, var8, Block.OUTLINE, Fluid.NONE, mc.player));
      if (var9.getType() != Type.BLOCK) {
         return this.c("Aim at the top of a block within reach");
      }

      BlockHitResult var12 = null;
      BlockState var13 = mc.level.getBlockState(var9.getBlockPos());
      BlockPos var10;
      BlockState var11;
      if (var13.is(BlockTags.RAILS)) {
         if (com.larpclient.m.c(var9.getBlockPos(), var1, var6) == null) {
            return this.c("That rail is outside native reach");
         }

         var10 = var9.getBlockPos();
         var11 = var13;
      } else {
         var12 = com.larpclient.m.c(var9.getBlockPos(), var1, var6);
         com.larpclient.m.r var14 = var12 == null ? null : com.larpclient.m.D(var12, var4, true);
         if (var14 == null) {
            return this.c("A rail cannot be placed on that support");
         }

         var10 = var14.target();
         var11 = var14.state();
      }

      if (var3 != null && !var10.equals(var3)) {
         return this.c("Aim does not reach the planned rail");
      }

      if (var12 != null && var11.getShape(mc.level, var10).clip(var5, var8, var10) == null) {
         return this.c("Aim at a top face so the cart lands on the new rail");
      }

      int var23 = var11.getBlock() instanceof BaseRailBlock var15 && ((RailShape)var11.getValue(var15.getShapeProperty())).isSlope() ? 1 : 0;
      Vec3 var24 = new Vec3(var10.getX() + 0.5, var10.getY() + 0.0625 + (var23 != 0 ? 0.5 : 0.0), var10.getZ() + 0.5);
      AABB var25 = EntityTypes.TNT_MINECART.getDimensions().makeBoundingBox(var24);
      if (!mc.level.getEntities((Entity)null, var25, var0 -> var0 instanceof AbstractMinecart).isEmpty()) {
         return this.c("A minecart already occupies that rail");
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

      if (!jg.L(var17, var18, var25, var20)) {
         return this.c("The arrow would hit something before the cart");
      } else {
         return !this.h(var17) && !this.h(var24) ? new AutoCartModule.e(var10, var12, var4, var24) : this.c("Rain or water would put out the Flame arrow");
      }
   }

   private boolean h(Vec3 var1) {
      BlockPos var2 = BlockPos.containing(var1);
      return mc.level.isRainingAt(var2) || mc.level.getFluidState(var2).is(FluidTags.WATER);
   }

   private boolean M() {
      this.j = null;
      int var1 = this.V();
      if (this.isRuntimeActive()
         && this.e != null
         && !(var1 < this.S.getValue())
         && !AuraModule.W()
         && (this.e.target() == null || !this.I() || this.n(this.e.target()))) {
         Item var2 = Z();
         if (var2 != null && com.larpclient.m.Q(Items.TNT_MINECART, true) != -1) {
            Vec3 var3 = mc.player.getLookAngle();
            Vec3 var4 = xq.K(var3);
            AutoCartModule.e var5 = this.T(var3, var4, this.e.rail(), var2);
            if (var5 == null) {
               this.a(var4.dot(var3) < Math.cos(Math.toRadians(0.5)) ? "Waiting for the aim to reach the server" : this.p);
               return false;
            } else {
               double[] var6 = jg.q(jg.d(var1));
               AutoCartModule.l var7 = this.L(var5, this.e.aim(), var6, this.e.target() == null ? List.of() : List.of(this.e.target()), this.I());
               if (var7 == null) {
                  this.a(this.p);
                  return false;
               } else {
                  this.j = var5;
                  this.I = var7;
                  this.A = var6;
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

   private void Z() {
      AutoCartModule.e var1 = this.j;
      this.j = null;
      if (var1 != null) {
         Inventory var2 = mc.player.getInventory();
         int var3 = var2.getSelectedSlot();
         int var4 = var1.railHit() == null ? var3 : com.larpclient.m.Q(var1.railItem(), true);
         int var5 = com.larpclient.m.Q(Items.TNT_MINECART, true);
         if (var4 != -1 && var5 != -1) {
            int var6 = this.V();

            try {
               mc.gameMode.releaseUsingItem(mc.player);
               if (var1.railHit() != null && !this.W(var4, var1.railHit())) {
                  this.B("Rail placement rejected; the arrow flew without a cart");
                  return;
               }

               BlockHitResult var7 = com.larpclient.m.c(var1.rail(), mc.player.getLookAngle(), this.o());
               if (var7 == null || !mc.level.getBlockState(var1.rail()).is(BlockTags.RAILS)) {
                  this.B("Placed rail is off the look; the arrow flew without a cart");
                  return;
               }

               if (!this.W(var5, var7)) {
                  this.B("Cart use rejected; the arrow flew without a cart");
                  return;
               }
            } finally {
               var2.setSelectedSlot(var3);
            }

            this.Z = AutoCartModule.m.y;
            this.v = var1.rail();
            this.y = var1.center();
            this.H = mc.level.getGameTime();
            this.m = -1.0F;
            this.c = "Released; waiting for detonation";
            r4.V(
               "AutoCart",
               "Released after {} draw ticks into a cart on {}{}; power {} to {}, expected {} HP{}, worst self {} HP",
               var6,
               var1.rail(),
               var1.railHit() == null ? " (existing rail)" : "",
               G(this.A[0]),
               G(this.A[2]),
               G(this.I.damage()),
               this.I.target() == null ? "" : " to " + this.I.target().getName().getString(),
               G(this.I.self())
            );
         }
      }
   }

   private boolean W(int var1, BlockHitResult var2) {
      if (var1 >= 0) {
         mc.player.getInventory().setSelectedSlot(var1);
      }

      InteractionHand var3 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      boolean var4 = mc.gameMode.useItemOn(mc.player, var3, var2).consumesAction();
      if (var4) {
         hp.Q(mc.player, var3);
      }

      return var4;
   }

   private boolean g(long var1) {
      if (this.m >= 0.0F) {
         r4.V("AutoCart", "Cart on {} detonated {} ticks after release with server power {}", this.v, var1 - this.H, G(this.m));
         this.s(String.format(Locale.ROOT, "Detonated with power %.2f", this.m));
         return false;
      }

      if (var1 - this.H <= 20L) {
         return true;
      }

      BlockPos var3 = this.v;
      int var4 = !mc.level.getEntities((Entity)null, new AABB(var3), var0 -> var0 instanceof MinecartTNT).isEmpty() ? 1 : 0;
      this.B(
         (var4 != 0 ? "No detonation; the cart remains, so the arrow missed or was put out" : "No detonation observed within one second")
            + " at "
            + var3.toShortString()
      );
      return false;
   }

   private boolean n(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != mc.level
         || mc.player.distanceTo(var1) > this.h.getValue()
         || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.T.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.y().e().y(MCFriendModule.class);
            return var3 == null || !var3.e(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.L.getValue() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> N() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.n(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private int V() {
      LocalPlayer var1 = mc.player;
      if (var1 != null && var1.isUsingItem()) {
         ItemStack var2 = var1.getUseItem();
         return var2.is(Items.BOW) && E(var2) ? var1.getTicksUsingItem() : -1;
      } else {
         return -1;
      }
   }

   private static boolean E(ItemStack var0) {
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

   private static Item Z() {
      for (Item var3 : f) {
         if (com.larpclient.m.Q(var3, true) != -1) {
            return var3;
         }
      }

      return null;
   }

   private boolean I() {
      return this.k.i("Visible");
   }

   private double o() {
      return Math.min(this.C.getValue(), mc.player.blockInteractionRange());
   }

   private static String G(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private <T> T c(String var1) {
      this.p = var1;
      return null;
   }

   private void a(String var1) {
      long var2 = mc.level == null ? 0L : mc.level.getGameTime();
      if (!var1.equals(this.c) && (this.K == Long.MIN_VALUE || var2 < this.K || var2 - this.K >= 40L)) {
         r4.g("AutoCart", "{}", var1);
         this.K = var2;
      }

      this.c = var1;
   }

   private void D(String var1) {
      this.Z = AutoCartModule.m.M;
      this.e = null;
      this.j = null;
      this.a(var1);
   }

   private void B(String var1) {
      r4.V("AutoCart", "{}", var1);
      this.s(var1);
   }

   private void s(String var1) {
      this.Z = AutoCartModule.m.M;
      this.e = null;
      this.j = null;
      this.v = null;
      this.y = null;
      this.m = -1.0F;
      this.c = var1;
   }

   private void f(String var1) {
      d8.K(this);
      this.s.C();
      this.s(var1);
   }

   @Override
   public void onEnable() {
      this.V = this.o = null;
      this.K = Long.MIN_VALUE;
      this.f("Draw a Flame bow to arm");
      this.s.S();
      r4.V("AutoCart", "Insta-cart armed; rotation={}, charge={} ticks, range={}", this.k.getValue(), this.S.getValue().intValue(), this.C.getValue());
   }

   @Override
   public void onDisable() {
      this.f("Draw a Flame bow to arm");
      this.V = this.o = null;
      r4.V("AutoCart", "Insta-cart disarmed; aim, pending release and detonation wait cleared");
   }

   private record e(BlockPos rail, BlockHitResult railHit, Item railItem, Vec3 center) {
   }

   private record l(BlockPos rail, Vec3 aim, LivingEntity target, double damage, double self) {
   }

   private enum m {
      M,
      C,
      y;
   }
}
