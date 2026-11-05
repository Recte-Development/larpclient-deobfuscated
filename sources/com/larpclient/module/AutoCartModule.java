package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._8;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.hq;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.pi;
import com.larpclient.pl;
import com.larpclient.pp;
import com.larpclient.tq;
import com.larpclient.tu;
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

public final class AutoCartModule extends Module implements pp.x {
   private static final Item[] Y = new Item[]{Items.RAIL, Items.POWERED_RAIL, Items.DETECTOR_RAIL, Items.ACTIVATOR_RAIL};
   private static final int Z = 20;
   private static final int x = 40;
   private static AutoCartModule b;
   private final d6 G = this.addSetting(
      new d6(
         "Rotation", "Visible picks a rail spot by an opponent and turns the real view; Crosshair uses the block you aim at", "Visible", "Visible", "Crosshair"
      )
   );
   private final d9 m = this.G("Turn Speed", "Maximum visible turn in degrees per tick", 60.0, 5.0, 180.0, 5.0);
   private final d9 i = this.G("Charge", "Bow draw ticks before release; 20 is full power", 20.0, 3.0, 20.0, 1.0);
   private final d9 S = this.G("Range", "Rail and cart reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final dh E = this.H("Players", "Target other survival players", true);
   private final dh q = this.H("Mobs", "Also target hostile mobs for combat or testing", false);
   private final d9 k = this.G("Target Range", "Search distance for opponents", 7.0, 2.0, 12.0, 0.5);
   private final d9 H = this.G("Min Damage", "Minimum expected target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final dh A = this.H("Anti Self", "Reject shots whose strongest possible blast is excessive or lethal for you", true);
   private final d9 c = this.G("Max Self Damage", "Maximum self damage from the strongest possible blast, in HP", 10.0, 0.0, 36.0, 0.5);
   private final pp Q;
   private AutoCartModule.v r = AutoCartModule.v.E;
   private AutoCartModule.b B;
   private AutoCartModule.b g;
   private AutoCartModule.y O;
   private double[] n;
   private BlockPos l;
   private Vec3 V;
   private long a;
   private long o = Long.MIN_VALUE;
   private float j = -1.0F;
   private Object p;
   private Object W;
   private String U = "Draw a Flame bow to arm";
   private String f = "";

   public AutoCartModule() {
      super("AutoCart", "Release a drawn Flame bow into a freshly placed TNT cart", Category.COMBAT, -1);
      b = this;
      this.m.H(this::T);

      for (df var2 : List.of(this.E, this.q, this.k, this.H)) {
         var2.H(this::T);
      }

      this.c.H(this.A::P);
      this.G.h(var1 -> this.F("Rotation changed"));
      AutoCartModule var3 = this;
      this.Q = new pp(this, var1 -> var3.addSetting(var1));
   }

   private d9 G(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   private dh H(String var1, String var2, boolean var3) {
      return this.addSetting(new dh(var1, var2, var3));
   }

   public List<df<?>> s() {
      return List.of(this.G, this.m);
   }

   public List<df<?>> I() {
      return List.of(this.i, this.S);
   }

   public List<df<?>> P() {
      return List.of(this.E, this.q, this.k, this.H);
   }

   public List<df<?>> k() {
      return List.of(this.A, this.c);
   }

   @Override
   public pp placementHighlight() {
      return this.Q;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.U : "";
   }

   public static void E(Vec3 var0, float var1) {
      if (b != null && b.isEnabled()) {
         AutoCartModule var2 = b;
         if (var2 != null && var2.r == AutoCartModule.v.F && var2.V != null && var0.distanceToSqr(var2.V) < 1.0) {
            var2.j = var1;
         }
      }
   }

   @Override
   public void onTick() {
      this.Q.m(null);
      if (!this.isInGame()) {
         this.F("Waiting for a world");
      } else {
         if (this.p != mc.player || this.W != mc.level) {
            this.F("Draw a Flame bow to arm");
            this.p = mc.player;
            this.W = mc.level;
         }

         long var1 = mc.level.getGameTime();
         if (this.r == AutoCartModule.v.F) {
            int var10000 = this.q(var1);
            boolean var9 = false;
            byte var8 = (byte)var10000;
            var10000 = Integer.signum(var8);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               return;
            }
         }

         int var18 = _m.z();
         boolean var15 = false;
         byte var14 = (byte)var18;
         var18 = Integer.signum(var14);
         if (((var18 | -var18) >>> 31 ^ 1) == 0 && mc.isWindowActive() && tq.l() == tq.y.A) {
            int var3 = this.J();
            if (var3 < 0) {
               this.p("Draw a Flame bow to arm");
            } else {
               Item var4 = M();
               if (var4 != null) {
                  var18 = _d.B(Items.TNT_MINECART, true);
                  byte var11 = -1;
                  int var10 = var18;
                  var18 = Integer.compare(var10, var11);
                  if ((var18 | -var18) >>> 31 != 0) {
                     if (AuraModule.i()) {
                        this.p("Paused while Kill Aura lowers your shield");
                        return;
                     }

                     double[] var5 = pi.k(pi.M(Math.max(var3, this.i.P().intValue())));
                     this.B = this.T() ? this.o(var4, var5) : this.b(var4, var5);
                     if (this.B == null) {
                        this.r = AutoCartModule.v.E;
                        this.O = null;
                        this.h(this.f);
                        return;
                     }

                     this.r = AutoCartModule.v.V;
                     this.Q.m(this.B.rail());
                     if (this.T() && _m.f(this)) {
                        float[] var6 = m9.Z(mc.player.getEyePosition(), this.B.aim());
                        float[] var7 = hq.A(mc.player.getYRot(), mc.player.getXRot(), var6[0], var6[1], this.m.P());
                        m9.k(this, m9.s.o, var7[0], var7[1], 1.0);
                     }

                     double var23;
                     var18 = (var23 = var3 - this.i.P()) == 0.0 ? 0 : (var23 < 0.0 ? -1 : 1);
                     boolean var13 = false;
                     int var12 = var18;
                     if ((Integer.signum(var12) >>> 31 ^ 1) == 0) {
                        this.U = String.format(Locale.ROOT, "Drawing %d/%d; expected %.1f HP", var3, this.i.P().intValue(), this.B.damage());
                        return;
                     }

                     this.U = String.format(Locale.ROOT, "Armed; expected %.1f HP, worst self %.1f HP", this.B.damage(), this.B.self());
                     AutoCartModule.b var16 = this.B;
                     _m.F(this, this::V, () -> this.B == var16 && this.r == AutoCartModule.v.V && this.r());
                     return;
                  }
               }

               this.p("Rails and TNT carts must be in your hotbar or hands");
            }
         } else {
            this.p("Paused: screen, focus or another camera owner");
         }
      }
   }

   private AutoCartModule.b o(Item var1, double[] var2) {
      List var3 = this.U();
      if (var3.isEmpty()) {
         return this.R(this.q.P() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)");
      }

      if (this.B != null && this.B.target() != null && var3.contains(this.B.target())) {
         AutoCartModule.b var4 = this.l(this.B.rail(), this.B.aim(), var1, var2, var3);
         if (var4 != null) {
            return var4;
         }
      }

      AutoCartModule.b var13 = null;
      HashSet var5 = new HashSet();

      for (LivingEntity var7 : var3) {
         for (int var8 = -1; var8 <= 0; var8++) {
            for (int var9 = -1; var9 <= 1; var9++) {
               for (int var10 = -1; var10 <= 1; var10++) {
                  BlockPos var11 = var7.blockPosition().offset(var9, var8, var10);
                  if (var5.add(var11)) {
                     AutoCartModule.b var12 = this.l(var11, null, var1, var2, var3);
                     if (var12 != null && (var13 == null || d(var12) > d(var13))) {
                        var13 = var12;
                     }
                  }
               }
            }
         }
      }

      return var13 != null ? var13 : this.R("No reachable rail spot meets the damage limits");
   }

   private static double d(AutoCartModule.b var0) {
      return var0.damage() - var0.self() * 0.25;
   }

   private AutoCartModule.b l(BlockPos var1, Vec3 var2, Item var3, double[] var4, List<LivingEntity> var5) {
      if (!mc.level.hasChunkAt(var1)) {
         return null;
      }

      if (!mc.level.getBlockState(var1).is(BlockTags.RAILS) && !_d.Z(var1)) {
         return null;
      }

      Vec3 var6 = mc.player.getEyePosition();
      double var7 = this.L();
      if (var6.distanceToSqr(Vec3.atCenterOf(var1)) > (var7 + 1.0) * (var7 + 1.0)) {
         return null;
      }

      for (Vec3 var10 : var2 != null ? List.of(var2) : z(var1)) {
         Vec3 var11 = var10.subtract(var6);
         AutoCartModule.y var12 = this.i(var11, var11, var1, var3);
         if (var12 != null) {
            return this.X(var12, var10, var4, var5, true);
         }
      }

      return null;
   }

   private static List<Vec3> z(BlockPos var0) {
      ArrayList var1 = new ArrayList(9);

      for (double var5 : new double[]{0.0, -0.42, 0.42}) {
         for (double var10 : new double[]{0.0, -0.42, 0.42}) {
            var1.add(new Vec3(var0.getX() + 0.5 + var5, var0.getY() + 0.0625, var0.getZ() + 0.5 + var10));
         }
      }

      return var1;
   }

   private AutoCartModule.b b(Item var1, double[] var2) {
      Vec3 var3 = mc.player.getLookAngle();
      AutoCartModule.y var4 = this.i(var3, var3, null, var1);
      return var4 == null ? null : this.X(var4, var4.center(), var2, this.U(), false);
   }

   private AutoCartModule.b X(AutoCartModule.y var1, Vec3 var2, double[] var3, List<LivingEntity> var4, boolean var5) {
      double var6 = tu.H(mc.player, var1.center(), var3[2]);
      if (this.A.P() && !tu.b(var6, this.c.P(), mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         return this.R(String.format(Locale.ROOT, "Worst-case self damage %.1f HP is over the limit", var6));
      }

      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var4) {
         double var13 = pi.X(tu.h(var12, var1.center(), var3));
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var5 && (var8 == null || var9 < this.H.P())
         ? this.R(String.format(Locale.ROOT, "Expected %.1f HP is below Min Damage", var9))
         : new AutoCartModule.b(var1.rail(), var2, var8, var9, var6);
   }

   private AutoCartModule.y i(Vec3 var1, Vec3 var2, BlockPos var3, Item var4) {
      Vec3 var5 = mc.player.getEyePosition();
      double var6 = this.L();
      Vec3 var8 = var5.add(var1.normalize().scale(var6));
      BlockHitResult var9 = mc.level.clip(new ClipContext(var5, var8, Block.OUTLINE, Fluid.NONE, mc.player));
      if (var9.getType() != Type.BLOCK) {
         return this.R("Aim at the top of a block within reach");
      }

      BlockHitResult var12 = null;
      BlockState var13 = mc.level.getBlockState(var9.getBlockPos());
      BlockPos var10;
      BlockState var11;
      if (var13.is(BlockTags.RAILS)) {
         if (_d.Y(var9.getBlockPos(), var1, var6) == null) {
            return this.R("That rail is outside native reach");
         }

         var10 = var9.getBlockPos();
         var11 = var13;
      } else {
         var12 = _d.Y(var9.getBlockPos(), var1, var6);
         _d.p var14 = var12 == null ? null : _d.s(var12, var4, true);
         if (var14 == null) {
            return this.R("A rail cannot be placed on that support");
         }

         var10 = var14.target();
         var11 = var14.state();
      }

      if (var3 != null && !var10.equals(var3)) {
         return this.R("Aim does not reach the planned rail");
      }

      if (var12 != null && var11.getShape(mc.level, var10).clip(var5, var8, var10) == null) {
         return this.R("Aim at a top face so the cart lands on the new rail");
      }

      int var23 = var11.getBlock() instanceof BaseRailBlock var15 && ((RailShape)var11.getValue(var15.getShapeProperty())).isSlope() ? 1 : 0;
      Vec3 var24 = new Vec3(var10.getX() + 0.5, var10.getY() + 0.0625 + (var23 != 0 ? 0.5 : 0.0), var10.getZ() + 0.5);
      AABB var25 = EntityTypes.TNT_MINECART.getDimensions().makeBoundingBox(var24);
      if (!mc.level.getEntities((Entity)null, var25, var0 -> var0 instanceof AbstractMinecart).isEmpty()) {
         return this.R("A minecart already occupies that rail");
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

      if (!pi.S(var17, var18, var25, var20)) {
         return this.R("The arrow would hit something before the cart");
      } else {
         return !this.f(var17) && !this.f(var24) ? new AutoCartModule.y(var10, var12, var4, var24) : this.R("Rain or water would put out the Flame arrow");
      }
   }

   private boolean f(Vec3 var1) {
      BlockPos var2 = BlockPos.containing(var1);
      return mc.level.isRainingAt(var2) || mc.level.getFluidState(var2).is(FluidTags.WATER);
   }

   private boolean r() {
      this.O = null;
      int var1 = this.J();
      if (this.isRuntimeActive()
         && this.B != null
         && !(var1 < this.i.P())
         && !AuraModule.i()
         && (this.B.target() == null || !this.T() || this.d(this.B.target()))) {
         Item var2 = M();
         if (var2 != null && _d.B(Items.TNT_MINECART, true) != -1) {
            Vec3 var3 = mc.player.getLookAngle();
            Vec3 var4 = pl.b(var3);
            AutoCartModule.y var5 = this.i(var3, var4, this.B.rail(), var2);
            if (var5 == null) {
               this.h(var4.dot(var3) < Math.cos(Math.toRadians(0.5)) ? "Waiting for the aim to reach the server" : this.f);
               return false;
            } else {
               double[] var6 = pi.k(pi.M(var1));
               AutoCartModule.b var7 = this.X(var5, this.B.aim(), var6, this.B.target() == null ? List.of() : List.of(this.B.target()), this.T());
               if (var7 == null) {
                  this.h(this.f);
                  return false;
               } else {
                  this.O = var5;
                  this.g = var7;
                  this.n = var6;
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

   private void V() {
      AutoCartModule.y var1 = this.O;
      this.O = null;
      if (var1 != null) {
         Inventory var2 = mc.player.getInventory();
         int var3 = var2.getSelectedSlot();
         int var4 = var1.railHit() == null ? var3 : _d.B(var1.railItem(), true);
         int var5 = _d.B(Items.TNT_MINECART, true);
         if (var4 != -1 && var5 != -1) {
            int var6 = this.J();

            try {
               mc.gameMode.releaseUsingItem(mc.player);
               if (var1.railHit() != null && !this.p(var4, var1.railHit())) {
                  this.l("Rail placement rejected; the arrow flew without a cart");
                  return;
               }

               BlockHitResult var7 = _d.Y(var1.rail(), mc.player.getLookAngle(), this.L());
               if (var7 == null || !mc.level.getBlockState(var1.rail()).is(BlockTags.RAILS)) {
                  this.l("Placed rail is off the look; the arrow flew without a cart");
                  return;
               }

               if (!this.p(var5, var7)) {
                  this.l("Cart use rejected; the arrow flew without a cart");
                  return;
               }
            } finally {
               var2.setSelectedSlot(var3);
            }

            this.r = AutoCartModule.v.F;
            this.l = var1.rail();
            this.V = var1.center();
            this.a = mc.level.getGameTime();
            this.j = -1.0F;
            this.U = "Released; waiting for detonation";
            mt.K(
               "AutoCart",
               "Released after {} draw ticks into a cart on {}{}; power {} to {}, expected {} HP{}, worst self {} HP",
               var6,
               var1.rail(),
               var1.railHit() == null ? " (existing rail)" : "",
               L(this.n[0]),
               L(this.n[2]),
               L(this.g.damage()),
               this.g.target() == null ? "" : " to " + this.g.target().getName().getString(),
               L(this.g.self())
            );
         }
      }
   }

   private boolean p(int var1, BlockHitResult var2) {
      if (var1 >= 0) {
         mc.player.getInventory().setSelectedSlot(var1);
      }

      InteractionHand var3 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      boolean var4 = mc.gameMode.useItemOn(mc.player, var3, var2).consumesAction();
      if (var4) {
         _8.G(mc.player, var3);
      }

      return var4;
   }

   private boolean q(long var1) {
      if (this.j >= 0.0F) {
         mt.K("AutoCart", "Cart on {} detonated {} ticks after release with server power {}", this.l, var1 - this.a, L(this.j));
         this.B(String.format(Locale.ROOT, "Detonated with power %.2f", this.j));
         return false;
      }

      if (var1 - this.a <= 20L) {
         return true;
      }

      BlockPos var3 = this.l;
      int var4 = !mc.level.getEntities((Entity)null, new AABB(var3), var0 -> var0 instanceof MinecartTNT).isEmpty() ? 1 : 0;
      this.l(
         (var4 != 0 ? "No detonation; the cart remains, so the arrow missed or was put out" : "No detonation observed within one second")
            + " at "
            + var3.toShortString()
      );
      return false;
   }

   private boolean d(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != mc.level
         || mc.player.distanceTo(var1) > this.k.P()
         || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.E.P() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.F().x().V(MCFriendModule.class);
            return var3 == null || !var3.V(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.q.P() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> U() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.d(var4)) {
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
         return var2.is(Items.BOW) && Q(var2) ? var1.getTicksUsingItem() : -1;
      } else {
         return -1;
      }
   }

   private static boolean Q(ItemStack var0) {
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

   private static Item M() {
      for (Item var3 : Y) {
         if (_d.B(var3, true) != -1) {
            return var3;
         }
      }

      return null;
   }

   private boolean T() {
      return this.G.S("Visible");
   }

   private double L() {
      return Math.min(this.S.P(), mc.player.blockInteractionRange());
   }

   private static String L(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private <T> T R(String var1) {
      this.f = var1;
      return null;
   }

   private void h(String var1) {
      long var2 = mc.level == null ? 0L : mc.level.getGameTime();
      if (!var1.equals(this.U) && (this.o == Long.MIN_VALUE || var2 < this.o || var2 - this.o >= 40L)) {
         mt.U("AutoCart", "{}", var1);
         this.o = var2;
      }

      this.U = var1;
   }

   private void p(String var1) {
      this.r = AutoCartModule.v.E;
      this.B = null;
      this.O = null;
      this.h(var1);
   }

   private void l(String var1) {
      mt.K("AutoCart", "{}", var1);
      this.B(var1);
   }

   private void B(String var1) {
      this.r = AutoCartModule.v.E;
      this.B = null;
      this.O = null;
      this.l = null;
      this.V = null;
      this.j = -1.0F;
      this.U = var1;
   }

   private void F(String var1) {
      _m.w(this);
      this.Q.P();
      this.B(var1);
   }

   @Override
   public void onEnable() {
      this.p = this.W = null;
      this.o = Long.MIN_VALUE;
      this.F("Draw a Flame bow to arm");
      this.Q.w();
      mt.K("AutoCart", "Insta-cart armed; rotation={}, charge={} ticks, range={}", this.G.P(), this.i.P().intValue(), this.S.P());
   }

   @Override
   public void onDisable() {
      this.F("Draw a Flame bow to arm");
      this.p = this.W = null;
      mt.K("AutoCart", "Insta-cart disarmed; aim, pending release and detonation wait cleared");
   }

   private record b(BlockPos rail, Vec3 aim, LivingEntity target, double damage, double self) {
   }

   private enum v {
      E,
      V,
      F;
   }

   private record y(BlockPos rail, BlockHitResult railHit, Item railItem, Vec3 center) {
   }
}
