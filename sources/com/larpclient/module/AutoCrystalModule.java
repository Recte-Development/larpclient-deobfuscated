package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._8;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.h;
import com.larpclient.hq;
import com.larpclient.k0;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.p1;
import com.larpclient.pp;
import com.larpclient.tq;
import com.larpclient.tu;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoCrystalModule extends Module implements pp.x {
   private final pp M;
   private LocalPlayer C;
   private ClientLevel i;
   private final d9 G = this.C("Range", "Maximum crystal interaction distance", 4.5, 2.0, 6.0, 0.5);
   private final d9 m = this.C("Target Range", "Search distance for opponents", 10.0, 2.0, 16.0, 1.0);
   private final d9 R = this.C("Break Delay", "Ticks between executed attacks", 2.0, 0.0, 10.0, 1.0);
   private final d9 I = this.C("Place Delay", "Ticks between placement attempts", 2.0, 0.0, 10.0, 1.0);
   private final d9 A = this.C("Min Damage", "Minimum estimated target damage in HP; a hit that kills or pops always qualifies", 4.0, 0.0, 20.0, 0.5);
   private final d9 t = this.C(
      "Face Place Health", "Once a target's health and absorption fall to this, crystals down to 1.5 HP qualify; 0 turns it off", 8.0, 0.0, 36.0, 0.5
   );
   private final d9 Hn = this.C(
      "Predict Ticks", "Score new crystals and obsidian against where a moving target will be after this many ticks", 2.0, 0.0, 10.0, 1.0
   );
   private final d9 d = this.C("Max Self Damage", "Maximum estimated self damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final dh X = this.i("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final dh K = this.i("Players", "Target other survival players", true);
   private final dh N = this.i("Mobs", "Also target hostile mobs for combat or testing", false);
   private final dh T = this.i("Auto Switch", "Use hotbar crystals and restore the selected slot", true);
   private final dh a = this.i("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final dh L = this.i("Place", "Place crystals on existing obsidian or bedrock", true);
   private final dh V = this.i("Place Obsidian", "Place an obsidian base first when no existing base is good enough", true);
   private final d6 h = this.addSetting(
      new d6(
         "Obsidian Mode",
         "Best places obsidian when no base qualifies, or when a new one kills or adds Obsidian Gain; Lethal only where its crystal kills or pops; Damage only when no existing base qualifies",
         "Best",
         "Best",
         "Lethal",
         "Damage"
      )
   );
   private final d9 y = this.C("Obsidian Gain", "Extra target damage in HP a new obsidian base must add over the best existing base", 3.0, 0.0, 20.0, 0.5);
   private final d9 v = this.C("Obsidian Delay", "Ticks after placing obsidian before the crystal goes on it", 1.0, 0.0, 10.0, 1.0);
   private final dh HD = this.i("Break", "Attack crystals that can damage a target", true);
   private final d6 n = this.addSetting(
      new d6("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final d9 e = this.C("Turn Speed", "Maximum visible turn in degrees per tick", 45.0, 5.0, 180.0, 5.0);
   private final d9 x = this.C("Aim Hold", "Consecutive aligned ticks before acting", 1.0, 1.0, 5.0, 1.0);
   private final d9 F = this.C("Place Range", "Placement reach, capped by Range and native block reach", 4.5, 2.0, 6.0, 0.5);
   private final d9 Ho = this.C("Break Range", "Attack reach, capped by Range and native entity reach", 3.0, 2.0, 6.0, 0.5);
   private final d9 f = this.C("Scan Interval", "Ticks to retain a valid selected action before rescoring", 2.0, 1.0, 10.0, 1.0);
   private final d9 Hl = this.C("Response Timeout", "Ticks to wait for server crystal spawn or removal before retrying", 10.0, 2.0, 40.0, 1.0);
   private final dh s = this.i("Pause Weakness", "Pause placement too when weakness prevents crystal attacks", true);
   private final dh HB = this.i("Pause On Kill", "Stop placing and breaking after a nearby opponent dies, so explosions spare their drops", false);
   private final d9 c = this.C("Kill Pause", "Seconds to stay paused after a kill", 10.0, 1.0, 60.0, 1.0);
   private final dh q = this.i("Work with Crystal", "Start while holding an end crystal", true);
   private final dh O = this.i("Work with Sword", "Start while holding a sword", true);
   private final dh l = this.i("Work with Totem", "Start while holding a totem", true);
   private final dh g = this.i("Work with Fist", "Start with an empty main hand", true);
   private final d9 o = this.x("Switch Delay Min", 1.0);
   private final d9 E = this.x("Switch Delay Max", 50.0);
   private final d9 HO = this.x("Place Delay Min", 50.0);
   private final d9 B = this.x("Place Delay Max", 100.0);
   private final d9 Hc = this.x("Break Delay Min", 50.0);
   private final d9 HK = this.x("Break Delay Max", 100.0);
   private final dh D = this.i("Swap Back", "Restore your starting slot after releasing use", true);
   private final d9 Hj = this.x("Swap Back Delay Min", 50.0);
   private final d9 Hu = this.x("Swap Back Delay Max", 100.0);
   private static final double[][] Y = new double[][]{{0.5, 0.5}, {0.15, 0.15}, {0.15, 0.85}, {0.85, 0.15}, {0.85, 0.85}};
   private static final Direction[] w = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};
   private static final int HV = 24;
   private static final int HE = 96;
   public static final double b = 1.5;
   private AutoCrystalModule.j H;
   private long W;
   private String k = "";
   private final Map<LivingEntity, Vec3> u = new IdentityHashMap<>();
   private BlockPos z;
   private LivingEntity Hb;
   private Object U;
   private Object j;
   private int H4;
   private String HP = "";
   private Object Z;
   private LivingEntity H9;
   private long P;
   private long S;
   private final hq Q = new hq();
   private static AutoCrystalModule p;
   private final k0 r = new k0();
   private String J = "Waiting for a target";

   @Override
   public pp placementHighlight() {
      return this.M;
   }

   public static AutoCrystalModule S() {
      return p;
   }

   public List<df<?>> L() {
      return List.of(this.L, this.HD, this.G, this.F, this.Ho, this.I, this.R, this.Hl, this.f, this.T);
   }

   public List<df<?>> b() {
      return List.of(this.V, this.h, this.y, this.v);
   }

   public List<df<?>> O() {
      return List.of(this.K, this.N, this.m, this.Hn);
   }

   public List<df<?>> Z() {
      return List.of(this.n, this.e, this.x);
   }

   public List<df<?>> I() {
      return List.of(this.A, this.t, this.X, this.d, this.a, this.s, this.HB, this.c);
   }

   public AutoCrystalModule() {
      super("AutoCrystal", "Automatically place and break end crystals", Category.COMBAT, -1);
      p = this;
      this.e.H(() -> this.n.S("Visible"));
      this.F.H(this.L::P);
      this.I.H(this.L::P);
      this.V.H(this.L::P);
      this.h.H(() -> this.L.P() && this.V.P());
      this.y.H(() -> this.L.P() && this.V.P() && this.h.S("Best"));
      this.v.H(() -> this.L.P() && this.V.P());
      this.Ho.H(this.HD::P);
      this.R.H(this.HD::P);
      this.d.H(this.X::P);
      this.c.H(this.HB::P);
      this.HB.h(var1 -> {
         if (!var1) {
            this.S = 0L;
         }
      });
      this.n.h(var1 -> {
         this.d();
         this.D();
      });

      for (df var2 : List.of(this.q, this.O, this.l, this.g, this.o, this.E, this.HO, this.B, this.Hc, this.HK, this.D, this.Hj, this.Hu)) {
         var2.H(() -> false);
      }

      AutoCrystalModule var3 = this;
      this.M = new pp(this, var1 -> var3.addSetting(var1), 0.5, 0.3);
   }

   private d9 C(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   private dh i(String var1, String var2, boolean var3) {
      return this.addSetting(new dh(var1, var2, var3));
   }

   private d9 x(String var1, double var2) {
      return this.C(var1, "Random delay endpoint in milliseconds; reversed endpoints are ordered automatically", var2, 0.0, 500.0, 1.0);
   }

   public static long b(double var0, double var2, double var4) {
      long var6 = Math.round(Math.min(var0, var2));
      long var8 = Math.round(Math.max(var0, var2));
      return var6 + Math.min(var8 - var6, (long)(Math.clamp(var4, 0.0, 1.0) * (var8 - var6 + 1L)));
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
      return this.isEnabled() ? this.J : "";
   }

   @Override
   public void onEnable() {
      this.X();
      this.M.w();
      mt.K(
         "AutoCrystal",
         "Autonomous target selection enabled; native reach, damage limits and final look-ray validation; obsidian bases {}",
         this.V.P() ? this.h.P() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.X();
      mt.K("AutoCrystal", "Autonomous actions cancelled; target and server-response waits cleared");
   }

   private void X() {
      this.r.Z();
      this.d();
      this.D();
      this.M.P();
      this.C = null;
      this.i = null;
      this.z = null;
      this.Hb = null;
      this.S = 0L;
      this.W = 0L;
      this.u.clear();
      this.J = "Waiting for a target";
   }

   private boolean i() {
      return this.isRuntimeActive()
         && this.isInGame()
         && _m.z()
         && mc.isWindowActive()
         && this.W()
         && (!this.a.P() || !mc.player.isUsingItem())
         && (!this.s.P() || !this.z());
   }

   private boolean W() {
      tq.y var1 = tq.l();
      return this.n.S("Visible") ? tq.y(tq.y.K) : var1 == tq.y.A;
   }

   private void D() {
      this.U = this.j = null;
      tq.A(tq.y.K);
   }

   public boolean S() {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && this.n.S("Visible")
         && mc.player != null
         && this.U == mc.player
         && this.j == mc.level
         && mc.player.tickCount >= this.H4
         && (!this.a.P() || !mc.player.isUsingItem())) {
         int var1 = mc.player.tickCount - this.H4;
         return var1 <= 1 || (this.r.d() || this.r.E()) && var1 <= this.Hl.P();
      } else {
         return false;
      }
   }

   public float L(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && tq.l() == tq.y.K) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean k(LivingEntity var1) {
      return var1.isAlive() && !var1.isRemoved() && this.H(var1);
   }

   private boolean H(LivingEntity var1) {
      if (var1 == mc.player || var1.isSpectator() || var1.level() != mc.level || mc.player.distanceTo(var1) > this.m.P() || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.K.P() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.F().x().V(MCFriendModule.class);
            return var3 == null || !var3.V(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.N.P() && var1 instanceof Enemy;
      }
   }

   public static double H(double var0, double var2, double var4) {
      double var6 = var4 > 0.0 && var0 <= var4 ? Math.min(var2, 1.5) : var2;
      return Math.min(var6, var0);
   }

   public static boolean V(String var0, double var1, boolean var3, double var4, boolean var6, double var7) {
      if (Double.isNaN(var1)) {
         return true;
      }

      if (var3) {
         return false;
      }

      return (boolean)(switch (var0) {
         case "Lethal" -> var6;
         case "Damage" -> 0;
         default -> var6 == 0 && !(var4 >= var1 + var7) ? 0 : 1;
      });
   }

   private AutoCrystalModule.q T(Vec3 var1, List<LivingEntity> var2) {
      return this.a(var1, var2, null, null, false, false);
   }

   private static double J(LivingEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   private double w(LivingEntity var1) {
      return H(J(var1), this.A.P(), this.t.P());
   }

   private Vec3 B(LivingEntity var1) {
      int var2 = this.Hn.P().intValue();
      return var2 <= 0 ? Vec3.ZERO : this.u.computeIfAbsent(var1, var1x -> {
         Vec3 var2x = new Vec3(var1x.getX() - var1x.xo, 0.0, var1x.getZ() - var1x.zo);
         if (!(var2x.lengthSqr() < 1.0E-6) && !(var2x.lengthSqr() > 1.0)) {
            Vec3 var3 = var2x.scale(var2);
            return mc.level.noCollision(var1x, var1x.getBoundingBox().move(var3)) ? var3 : Vec3.ZERO;
         } else {
            return Vec3.ZERO;
         }
      });
   }

   private AutoCrystalModule.q a(Vec3 var1, List<LivingEntity> var2, AutoCrystalModule.m var3, BlockPos var4, boolean var5, boolean var6) {
      double var7 = tu.M(mc.player, var1, var4);
      if (this.X.P() && !tu.b(var7, this.d.P(), mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         if (var3 != null) {
            var3.o++;
            var3.g = Math.min(var3.g, var7);
         }

         return null;
      } else {
         AutoCrystalModule.q var9 = null;

         for (LivingEntity var11 : var2) {
            if (this.k(var11)) {
               double var12 = tu.b(var11, var1, var4, var5 ? this.B(var11) : Vec3.ZERO);
               if (!(var12 <= 0.0) && !(var12 < this.w(var11))) {
                  if (!var6 || !(var12 < J(var11))) {
                     AutoCrystalModule.q var14 = new AutoCrystalModule.q(var11, var12, var7, var12 >= J(var11));
                     if (var9 == null || var14.value() > var9.value()) {
                        var9 = var14;
                     }
                  } else if (var3 != null && (Double.isNaN(var3.s) || var12 - J(var11) > var3.L - var3.s)) {
                     var3.L = var12;
                     var3.s = J(var11);
                  }
               } else if (var3 != null) {
                  var3.r = Math.max(var3.r, var12);
               }
            }
         }

         return var9;
      }
   }

   private Optional<Vec3> v(EndCrystal var1, Vec3 var2) {
      return _m.z(var1, this.k(), var2 == null ? var1.getBoundingBox().getCenter() : var2, var2);
   }

   public static AABB R(BlockPos var0) {
      return EntityTypes.END_CRYSTAL.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(var0.above()));
   }

   public static List<Vec3> B(BlockPos var0, Vec3 var1) {
      ArrayList var2 = new ArrayList(15);
      Vec3 var3 = Vec3.atCenterOf(var0);

      for (Direction var7 : w) {
         int var8 = var7.getStepX();
         int var9 = var7.getStepY();
         int var10 = var7.getStepZ();
         if (!((var1.x - var3.x - var8 * 0.5) * var8 + (var1.y - var3.y - var9 * 0.5) * var9 + (var1.z - var3.z - var10 * 0.5) * var10 <= 0.0)) {
            for (double[] var14 : Y) {
               double var15 = var14[0];
               double var17 = var14[1];

               var2.add(switch (var7.getAxis()) {
                  case Y -> new Vec3(var0.getX() + var15, var0.getY() + (var9 > 0 ? 1 : 0), var0.getZ() + var17);
                  case X -> new Vec3(var0.getX() + (var8 > 0 ? 1 : 0), var0.getY() + var15, var0.getZ() + var17);
                  case Z -> new Vec3(var0.getX() + var15, var0.getY() + var17, var0.getZ() + (var10 > 0 ? 1 : 0));
                  default -> throw new MatchException(null, null);
               });
            }
         }
      }

      return var2;
   }

   @Override
   public void onTick() {
      this.M.m(null);
      if (!this.isInGame()) {
         this.X();
      } else {
         if (this.C != mc.player || this.i != mc.level) {
            this.X();
            this.C = mc.player;
            this.i = mc.level;
         }

         this.H();
         int var10000 = this.J.contains(":");
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         String var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? this.J.substring(0, this.J.indexOf(58)) : this.J;
         int var7 = var1.equals(this.HP);
         boolean var3 = false;
         byte var2 = (byte)var7;
         var7 = Integer.signum(var2);
         if ((var7 | -var7) >>> 31 == 0) {
            this.HP = var1;
            mt.U("AutoCrystal", "Status: {}", this.J);
         }
      }
   }

   private void H() {
      this.u.clear();
      if (!this.i()) {
         this.d();
         this.J = "Paused: held-use optimizer, screen, focus, camera, item use or weakness";
      } else {
         long var1 = mc.level.getGameTime();
         if (var1 < this.S) {
            this.d();
            this.J = String.format(Locale.ROOT, "Paused after kill: %.1f s left", (this.S - var1) / 20.0);
         } else {
            this.r
               .I(
                  var1,
                  var0 -> mc.level.getEntity(var0) != null,
                  var0 -> !mc.level
                     .getEntities((Entity)null, new AABB(var0.above()).expandTowards(0.0, 1.0, 0.0), var0x -> var0x instanceof EndCrystal)
                     .isEmpty()
               );
            if (!this.r.d() && !this.r.E()) {
               this.z = null;
            } else {
               this.M.m(this.z);
               if (this.Hb != null && this.k(this.Hb)) {
                  com.larpclient.h.z(this.Hb);
               }
            }

            if (this.H != null && var1 - this.H.created() < this.f.P() && this.U(this.H)) {
               this.W(this.H, var1);
            } else {
               this.H = null;
               ArrayList var3 = new ArrayList();

               for (Entity var5 : mc.level.entitiesForRendering()) {
                  if (var5 instanceof LivingEntity var6 && this.k(var6)) {
                     var3.add(var6);
                  }
               }

               var3.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
               if (var3.size() > 8) {
                  var3 = new ArrayList(var3.subList(0, 8));
               }

               if (var3.isEmpty()) {
                  this.J = "No target (enable Mobs to test on hostile mobs)";
               } else {
                  if (this.HD.P() && !this.z()) {
                     EndCrystal var14 = null;
                     AutoCrystalModule.q var16 = null;
                     Vec3 var18 = null;

                     for (Entity var8 : mc.level.entitiesForRendering()) {
                        if (var8 instanceof EndCrystal var9 && this.r.G(var1, var8.getId()) && !(_m.p(var8) > this.k())) {
                           AutoCrystalModule.q var10 = this.T(var8.position(), var3);
                           if (var10 != null && (var16 == null || !(var10.value() <= var16.value()))) {
                              Optional var11 = this.v(var9, null);
                              if (!var11.isEmpty()) {
                                 var14 = var9;
                                 var16 = var10;
                                 var18 = (Vec3)var11.get();
                              }
                           }
                        }
                     }

                     if (var14 != null) {
                        this.H = new AutoCrystalModule.j(null, var14, var16.target(), var1, var18, null);
                        this.W(this.H, var1);
                        return;
                     }
                  }

                  if (this.r.E()) {
                     this.J = "Waiting for crystal removal";
                  } else if (!this.L.P()) {
                     this.J = "No suitable crystal to break";
                  } else if (this.r.d()) {
                     this.J = "Waiting for crystal spawn";
                  } else if (!this.r.g(var1)) {
                     this.J = "Placement cooldown";
                  } else if (this.K() == -1) {
                     this.J = "No crystals in hands or hotbar";
                  } else {
                     BlockHitResult var15 = null;
                     AutoCrystalModule.q var17 = null;
                     AutoCrystalModule.m var19 = new AutoCrystalModule.m();
                     int var20 = (int)Math.ceil(this.p());
                     BlockPos var21 = BlockPos.containing(mc.player.getEyePosition());

                     for (BlockPos var24 : BlockPos.betweenClosed(var21.offset(-var20, -var20, -var20), var21.offset(var20, var20, var20))) {
                        BlockPos var25 = var24.immutable();
                        BlockHitResult var12 = this.e(var25, null);
                        if (var12 != null) {
                           var19.k++;
                           AutoCrystalModule.q var13 = this.a(Vec3.atBottomCenterOf(var25.above()), var3, var19, null, true, false);
                           if (var13 != null && (var17 == null || var13.value() > var17.value())) {
                              var15 = var12;
                              var17 = var13;
                           }
                        }
                     }

                     if (this.V.P() && (var17 == null || !var17.lethal() && !this.h.S("Damage"))) {
                        AutoCrystalModule.j var23 = this.d(var3, var17, var1, var19);
                        if (var23 != null) {
                           this.H = var23;
                           this.W(this.H, var1);
                           return;
                        }

                        if (var15 == null) {
                           this.J = this.k;
                           return;
                        }
                     }

                     if (var15 == null) {
                        this.J = this.Z(var19);
                     } else {
                        this.H = new AutoCrystalModule.j(var15.getBlockPos(), null, var17.target(), var1, var15.getLocation(), null);
                        this.W(this.H, var1);
                     }
                  }
               }
            }
         }
      }
   }

   private AutoCrystalModule.j d(List<LivingEntity> var1, AutoCrystalModule.q var2, long var3, AutoCrystalModule.m var5) {
      boolean var6 = this.h.S("Lethal");
      double var7 = var2 == null ? Double.NaN : var2.damage();
      if (var3 < this.W) {
         this.k = "Waiting for a crystal on the new obsidian";
         return null;
      }

      if (_d.B(Items.OBSIDIAN, this.T.P()) == -1) {
         this.k = this.Z(var5) + "; no obsidian in hands or hotbar";
         return null;
      }

      Vec3 var9 = mc.player.getEyePosition();
      double var10 = this.p();
      int var12 = (int)Math.ceil(var10);
      BlockPos var13 = BlockPos.containing(var9);
      ArrayList var14 = new ArrayList();

      for (BlockPos var16 : BlockPos.betweenClosed(var13.offset(-var12, -var12, -var12), var13.offset(var12, var12, var12))) {
         if (this.d(var16, var9, var10)) {
            Vec3 var17 = Vec3.atBottomCenterOf(var16.above());
            double var18 = Double.MAX_VALUE;

            for (LivingEntity var21 : var1) {
               double var22 = var21.position().add(this.B(var21)).distanceTo(var17);
               double var24 = var6 ? J(var21) : this.w(var21);
               if (var2 != null && this.h.S("Best")) {
                  var24 = Math.max(var24, Math.min(J(var21), var7 + this.y.P()));
               }

               if (tu.h(var22, 1.0) * 1.5 >= var24) {
                  var18 = Math.min(var18, var22);
               }
            }

            if (var18 < Double.MAX_VALUE) {
               var14.add(new AutoCrystalModule.u(var16.immutable(), var18));
            }
         }
      }

      var14.sort(Comparator.comparingDouble(AutoCrystalModule.u::distance));
      AutoCrystalModule.m var26 = new AutoCrystalModule.m();
      AutoCrystalModule.q var27 = null;
      BlockHitResult var28 = null;
      BlockPos var29 = null;
      int var19 = 0;

      for (AutoCrystalModule.u var31 : var14.subList(0, Math.min(96, var14.size()))) {
         if (var19 >= 24) {
            break;
         }

         BlockPos var32 = var31.pos();
         if (this.U(var32) && this.Y(var32, var9, var10)) {
            BlockHitResult var23 = _d.g(var32, Items.OBSIDIAN, var10, true, this.T.P());
            if (var23 != null) {
               var26.k++;
               var19++;
               AutoCrystalModule.q var33 = this.a(Vec3.atBottomCenterOf(var32.above()), var1, var26, var32, true, var6);
               if (var33 != null
                  && V(this.h.P(), var7, false, var33.damage(), var33.lethal(), this.y.P())
                  && (var27 == null || !(var33.value() <= var27.value()))) {
                  var27 = var33;
                  var28 = var23;
                  var29 = var32;
               }
            }
         }
      }

      if (var27 == null) {
         this.k = this.t(var26, var6);
         return null;
      } else {
         return new AutoCrystalModule.j(var29, null, var27.target(), var3, var28.getLocation(), var28);
      }
   }

   private boolean d(BlockPos var1, Vec3 var2, double var3) {
      if (!(var2.distanceToSqr(Vec3.atCenterOf(var1)) > (var3 + 1.0) * (var3 + 1.0)) && _d.Z(var1) && mc.level.isEmptyBlock(var1.above())) {
         if (this.HD.P() && p1.X(var2, R(var1)) > this.k()) {
            return false;
         }

         for (Direction var8 : Direction.values()) {
            if (_d.A(var1.relative(var8))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean U(BlockPos var1) {
      return mc.level.getEntities((Entity)null, new AABB(var1).expandTowards(0.0, 2.0, 0.0)).isEmpty();
   }

   private boolean Y(BlockPos var1, Vec3 var2, double var3) {
      for (Vec3 var6 : B(var1, var2)) {
         if (!(var2.distanceToSqr(var6) > var3 * var3)
            && mc.level.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.NONE, mc.player)).getType() == Type.MISS) {
            return true;
         }
      }

      return false;
   }

   private BlockHitResult f(BlockHitResult var1, BlockPos var2, Vec3 var3) {
      BlockHitResult var4 = _d.Y(var1.getBlockPos(), var3, this.p());
      if (var4 != null && var4.getDirection() == var1.getDirection() && _d.A(var4.getBlockPos())) {
         _d.p var5 = _d.s(var4, Items.OBSIDIAN, this.T.P());
         return var5 != null && var5.target().equals(var2) ? var4 : null;
      } else {
         return null;
      }
   }

   private String t(AutoCrystalModule.m var1, boolean var2) {
      if (var1.k == 0) {
         return "No reachable obsidian or bedrock base, or open spot to place one";
      } else {
         return var2 && !Double.isNaN(var1.s) && var1.o < var1.k
            ? String.format(Locale.ROOT, "No lethal obsidian spot: best %.1f HP against %.1f HP health", var1.L, var1.s)
            : this.Z(var1);
      }
   }

   public void C(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (var1.getEventId() == 3 && this.HB.P() && this.isRuntimeActive() && this.isInGame()) {
            if (var1.getEntity(mc.level) instanceof LivingEntity var2 && this.H(var2)) {
               this.S = mc.level.getGameTime() + Math.round(this.c.P() * 20.0);
               this.r.Z();
               this.d();
               this.D();
               this.z = null;
               this.Hb = null;
               this.J = String.format(Locale.ROOT, "Paused after kill: %.1f s left", this.c.P());
               mt.K("AutoCrystal", "{} died nearby; placing and breaking paused for {} s to protect drops", var2.getName().getString(), this.c.P().intValue());
            }
         }
      }
   }

   private String Z(AutoCrystalModule.m var1) {
      if (var1.k == 0) {
         return "No reachable obsidian or bedrock base";
      } else {
         return var1.o == var1.k
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var1.g, this.d.P())
            : String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var1.r, this.A.P());
      }
   }

   private void d() {
      this.H = null;
      this.Z = null;
      this.H9 = null;
      this.Q.f();
      _m.w(this);
   }

   private double p() {
      return Math.min(this.G.P(), Math.min(this.F.P(), mc.player.blockInteractionRange()));
   }

   private double k() {
      return Math.min(this.G.P(), Math.min(this.Ho.P(), mc.player.entityInteractionRange()));
   }

   private boolean z() {
      return mc.player.hasEffect(MobEffects.WEAKNESS) && mc.player.getAttributeValue(Attributes.ATTACK_DAMAGE) <= 0.0;
   }

   private boolean U(AutoCrystalModule.j var1) {
      if (this.i() && this.k(var1.victim())) {
         long var2 = mc.level.getGameTime();
         if (var1.crystal() != null) {
            return this.HD.P()
               && !this.z()
               && this.r.G(var2, var1.crystal().getId())
               && this.v(var1.crystal(), var1.aim()).isPresent()
               && this.T(var1.crystal().position(), List.of(var1.victim())) != null;
         } else if (var1.support() != null) {
            Vec3 var4 = mc.player.getEyePosition();
            return this.L.P()
               && this.V.P()
               && this.r.g(var2)
               && var2 >= this.W
               && this.K() != -1
               && _d.B(Items.OBSIDIAN, this.T.P()) != -1
               && this.d(var1.base(), var4, this.p())
               && this.U(var1.base())
               && this.Y(var1.base(), var4, this.p())
               && this.f(var1.support(), var1.base(), var1.aim().subtract(var4)) != null
               && this.a(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, var1.base(), true, this.h.S("Lethal")) != null;
         } else {
            return this.L.P()
               && this.r.g(var2)
               && this.K() != -1
               && this.e(var1.base(), var1.aim()) != null
               && this.a(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, null, true, false) != null;
         }
      } else {
         return false;
      }
   }

   private boolean y(AutoCrystalModule.j var1) {
      if (var1.support() != null) {
         return this.f(var1.support(), var1.base(), mc.player.getLookAngle()) != null;
      } else {
         return var1.crystal() != null ? _m.b(var1.crystal(), this.k()) : _m.F(var1.base(), this.p()) != null;
      }
   }

   public void A() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.H != null && this.P == mc.level.getGameTime()) {
            if (_m.f(this) && this.U(this.H)) {
               if (this.n.S("Visible")) {
                  if (!tq.y(tq.y.K)) {
                     this.Q.f();
                     return;
                  }

                  tq.i(tq.y.K);
                  this.U = mc.player;
                  this.j = mc.level;
                  this.H4 = mc.player.tickCount;
                  float[] var1 = m9.Z(mc.player.getEyePosition(), this.H.aim());
                  float[] var2 = hq.A(mc.player.getYRot(), mc.player.getXRot(), var1[0], var1[1], this.e.P());
                  m9.c(m9.s.x, var2[0], var2[1], 1.0);
                  m9.j();
               }

               this.Q.h(mc.player.tickCount, this.y(this.H));
               this.J = this.Q.E(mc.player.tickCount, this.x.P().intValue())
                  ? "Aim ready"
                  : (!this.n.S("Visible") ? "Waiting for your crosshair" : (this.H.support() != null ? "Aiming at obsidian spot" : "Aiming at crystal action"));
            } else {
               this.Q.f();
            }
         }
      }
   }

   private boolean u(AutoCrystalModule.j var1) {
      return this.H == var1 && this.P == mc.level.getGameTime() && this.U(var1) && this.Q.E(mc.player.tickCount, this.x.P().intValue()) && this.y(var1);
   }

   private void W(AutoCrystalModule.j var1, long var2) {
      BlockPos var4 = var1.base() != null ? var1.base() : var1.crystal().blockPosition().below();
      this.M.m(var4);
      com.larpclient.h.z(var1.victim());
      this.Hb = var1.victim();
      this.P = var2;
      Object var5 = var1.support() != null ? new AutoCrystalModule.g(var1.base()) : (var1.crystal() == null ? var1.base() : var1.crystal());
      if (!Objects.equals(this.Z, var5) || this.H9 != var1.victim()) {
         this.Q.f();
      }

      this.Z = var5;
      this.H9 = var1.victim();
      if (var1.crystal() != null) {
         _m.f(this, var1.crystal(), this.k(), false, () -> this.u(var1), 1, () -> {
            this.r.h(mc.level.getGameTime(), var1.crystal().getId(), this.R.P().intValue(), this.Hl.P().intValue());
            this.z = var4;
            this.J = "Waiting for crystal removal";
            this.d();
         }, var2x -> {
            if (this.u(var1)) {
               this.J = "Waiting: " + var2x;
            }
         });
      } else if (var1.support() != null) {
         _m.F(this, () -> this.r(var1), () -> this.u(var1));
      } else {
         _m.F(this, () -> this.D(var1.base()), () -> this.u(var1));
      }
   }

   private void r(AutoCrystalModule.j var1) {
      BlockHitResult var2 = this.f(var1.support(), var1.base(), mc.player.getLookAngle());
      int var3 = _d.B(Items.OBSIDIAN, this.T.P());
      if (var2 != null && var3 != -1) {
         int var4 = mc.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var3);
            }

            InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            InteractionResult var7 = mc.gameMode.useItemOn(mc.player, var6, var2);
            long var8 = mc.level.getGameTime();
            if (var7.consumesAction()) {
               _8.G(mc.player, var6);
               this.r.i(var8, this.v.P().intValue());
               this.W = var8 + this.Hl.P().intValue();
               this.J = "Placed obsidian; crystal next";
               mt.U("AutoCrystal", "Obsidian base at {} for {} ({} mode)", var1.base().toShortString(), var1.victim().getName().getString(), this.h.P());
            } else {
               this.r.i(var8, this.I.P().intValue());
               this.J = "Obsidian placement rejected; retrying";
            }

            this.d();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private int K() {
      if (mc.player.getOffhandItem().is(Items.END_CRYSTAL)) {
         return -2;
      }

      int var1 = mc.player.getInventory().getSelectedSlot();
      if (mc.player.getMainHandItem().is(Items.END_CRYSTAL)) {
         return var1;
      }

      if (this.T.P()) {
         for (int var2 = 0; var2 < 9; var2++) {
            if (mc.player.getInventory().getItem(var2).is(Items.END_CRYSTAL)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private BlockHitResult e(BlockPos var1, Vec3 var2) {
      if (mc.level.hasChunkAt(var1) && mc.level.getWorldBorder().isWithinBounds(var1)) {
         BlockState var3 = mc.level.getBlockState(var1);
         if ((var3.is(Blocks.OBSIDIAN) || var3.is(Blocks.BEDROCK))
            && mc.level.isEmptyBlock(var1.above())
            && mc.level.getEntities(null, new AABB(var1.above()).expandTowards(0.0, 1.0, 0.0)).isEmpty()) {
            Vec3 var4 = mc.player.getEyePosition();
            double var5 = this.p();
            if (var4.distanceToSqr(Vec3.atCenterOf(var1)) > (var5 + 1.0) * (var5 + 1.0)) {
               return null;
            }

            if (this.HD.P() && p1.X(var4, R(var1)) > this.k()) {
               return null;
            }

            if (var2 != null) {
               BlockHitResult var7 = _m.y(var1, var5, var2.subtract(var4));
               if (var7 != null) {
                  return var7;
               }
            }

            for (Vec3 var8 : B(var1, var4)) {
               BlockHitResult var9 = _m.y(var1, var5, var8.subtract(var4));
               if (var9 != null) {
                  return var9;
               }
            }

            return null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void D(BlockPos var1) {
      BlockHitResult var2 = _m.F(var1, this.p());
      int var3 = this.K();
      if (var2 != null && var3 != -1 && this.e(var1, var2.getLocation()) != null) {
         int var4 = mc.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var3);
            }

            InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            InteractionResult var7 = mc.gameMode.useItemOn(mc.player, var6, var2);
            this.r.c(mc.level.getGameTime(), var1, this.I.P().intValue(), var7.consumesAction(), this.Hl.P().intValue());
            if (var7.consumesAction()) {
               _8.G(mc.player, var6);
               this.z = var1;
               this.W = 0L;
               this.J = "Waiting for crystal spawn";
            } else {
               this.J = "Placement rejected; retrying";
            }

            this.d();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private record g(BlockPos pos) {
   }

   private record j(BlockPos base, EndCrystal crystal, LivingEntity victim, long created, Vec3 aim, BlockHitResult support) {
   }

   private static final class m {
      int k;
      int o;
      double g = Double.MAX_VALUE;
      double r;
      double L;
      double s = Double.NaN;
   }

   private record q(LivingEntity target, double damage, double self, boolean lethal) {
      double value() {
         return this.lethal ? 1000.0 - this.self : this.damage - this.self * 0.25;
      }
   }

   private record u(BlockPos pos, double distance) {
   }
}
