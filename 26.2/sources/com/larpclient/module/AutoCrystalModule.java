package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cv;
import com.larpclient.j6;
import com.larpclient.ks;
import com.larpclient.kz;
import com.larpclient.mr;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
import com.larpclient.u6;
import com.larpclient.wi;
import com.larpclient.zu;
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

public final class AutoCrystalModule extends Module implements mr.n {
   private final mr t;
   private LocalPlayer f;
   private ClientLevel D_;
   private final e q = this.K("Range", "Maximum crystal interaction distance", 4.5, 2.0, 6.0, 0.5);
   private final e DN = this.K("Target Range", "Search distance for opponents", 10.0, 2.0, 16.0, 1.0);
   private final e U = this.K("Break Delay", "Ticks between executed attacks", 2.0, 0.0, 10.0, 1.0);
   private final e c = this.K("Place Delay", "Ticks between placement attempts", 2.0, 0.0, 10.0, 1.0);
   private final e Ds = this.K("Min Damage", "Minimum estimated target damage in HP; a hit that kills or pops always qualifies", 4.0, 0.0, 20.0, 0.5);
   private final e i = this.K(
      "Face Place Health", "Once a target's health and absorption fall to this, crystals down to 1.5 HP qualify; 0 turns it off", 8.0, 0.0, 36.0, 0.5
   );
   private final e E = this.K(
      "Predict Ticks", "Score new crystals and obsidian against where a moving target will be after this many ticks", 2.0, 0.0, 10.0, 1.0
   );
   private final e R = this.K("Max Self Damage", "Maximum estimated self damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final s C = this.R("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final s W = this.R("Players", "Target other survival players", true);
   private final s o = this.R("Mobs", "Also target hostile mobs for combat or testing", false);
   private final s d = this.R("Auto Switch", "Use hotbar crystals and restore the selected slot", true);
   private final s r = this.R("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final s Di = this.R("Place", "Place crystals on existing obsidian or bedrock", true);
   private final s N = this.R("Place Obsidian", "Place an obsidian base first when no existing base is good enough", true);
   private final l DV = this.addSetting(
      new l(
         "Obsidian Mode",
         "Best places obsidian when no base qualifies, or when a new one kills or adds Obsidian Gain; Lethal only where its crystal kills or pops; Damage only when no existing base qualifies",
         "Best",
         "Best",
         "Lethal",
         "Damage"
      )
   );
   private final e DY = this.K("Obsidian Gain", "Extra target damage in HP a new obsidian base must add over the best existing base", 3.0, 0.0, 20.0, 0.5);
   private final e Dn = this.K("Obsidian Delay", "Ticks after placing obsidian before the crystal goes on it", 1.0, 0.0, 10.0, 1.0);
   private final s e = this.R("Break", "Attack crystals that can damage a target", true);
   private final l S = this.addSetting(
      new l("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final e k = this.K("Turn Speed", "Maximum visible turn in degrees per tick", 45.0, 5.0, 180.0, 5.0);
   private final e F = this.K("Aim Hold", "Consecutive aligned ticks before acting", 1.0, 1.0, 5.0, 1.0);
   private final e O = this.K("Place Range", "Placement reach, capped by Range and native block reach", 4.5, 2.0, 6.0, 0.5);
   private final e D8 = this.K("Break Range", "Attack reach, capped by Range and native entity reach", 3.0, 2.0, 6.0, 0.5);
   private final e w = this.K("Scan Interval", "Ticks to retain a valid selected action before rescoring", 2.0, 1.0, 10.0, 1.0);
   private final e Da = this.K("Response Timeout", "Ticks to wait for server crystal spawn or removal before retrying", 10.0, 2.0, 40.0, 1.0);
   private final s Q = this.R("Pause Weakness", "Pause placement too when weakness prevents crystal attacks", true);
   private final s P = this.R("Pause On Kill", "Stop placing and breaking after a nearby opponent dies, so explosions spare their drops", false);
   private final e y = this.K("Kill Pause", "Seconds to stay paused after a kill", 10.0, 1.0, 60.0, 1.0);
   private final s l = this.R("Work with Crystal", "Start while holding an end crystal", true);
   private final s K = this.R("Work with Sword", "Start while holding a sword", true);
   private final s s = this.R("Work with Totem", "Start while holding a totem", true);
   private final s D = this.R("Work with Fist", "Start with an empty main hand", true);
   private final e DF = this.B("Switch Delay Min", 1.0);
   private final e T = this.B("Switch Delay Max", 50.0);
   private final e H = this.B("Place Delay Min", 50.0);
   private final e v = this.B("Place Delay Max", 100.0);
   private final e Du = this.B("Break Delay Min", 50.0);
   private final e a = this.B("Break Delay Max", 100.0);
   private final s j = this.R("Swap Back", "Restore your starting slot after releasing use", true);
   private final e g = this.B("Swap Back Delay Min", 50.0);
   private final e I = this.B("Swap Back Delay Max", 100.0);
   private static final double[][] z = new double[][]{{0.5, 0.5}, {0.15, 0.15}, {0.15, 0.85}, {0.85, 0.15}, {0.85, 0.85}};
   private static final Direction[] D1 = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};
   private static final int u = 24;
   private static final int M = 96;
   public static final double DZ = 1.5;
   private AutoCrystalModule.w B;
   private long D4;
   private String Z = "";
   private final Map<LivingEntity, Vec3> m = new IdentityHashMap<>();
   private BlockPos J;
   private LivingEntity V;
   private Object h;
   private Object Y;
   private int n;
   private String DI = "";
   private Object A;
   private LivingEntity b;
   private long G;
   private long p;
   private final u6 X = new u6();
   private static AutoCrystalModule Dq;
   private final cv L = new cv();
   private String x = "Waiting for a target";

   @Override
   public mr placementHighlight() {
      return this.t;
   }

   public static AutoCrystalModule S() {
      return Dq;
   }

   public List<com.larpclient.module.q<?>> r() {
      return List.of(this.Di, this.e, this.q, this.O, this.D8, this.c, this.U, this.Da, this.w, this.d);
   }

   public List<com.larpclient.module.q<?>> l() {
      return List.of(this.N, this.DV, this.DY, this.Dn);
   }

   public List<com.larpclient.module.q<?>> C() {
      return List.of(this.W, this.o, this.DN, this.E);
   }

   public List<com.larpclient.module.q<?>> n() {
      return List.of(this.S, this.k, this.F);
   }

   public List<com.larpclient.module.q<?>> v() {
      return List.of(this.Ds, this.i, this.C, this.R, this.r, this.Q, this.P, this.y);
   }

   public AutoCrystalModule() {
      super("AutoCrystal", "Automatically place and break end crystals", Category.COMBAT, -1);
      Dq = this;
      this.k.P(() -> this.S.O("Visible"));
      this.O.P(this.Di::getValue);
      this.c.P(this.Di::getValue);
      this.N.P(this.Di::getValue);
      this.DV.P(() -> this.Di.getValue() && this.N.getValue());
      this.DY.P(() -> this.Di.getValue() && this.N.getValue() && this.DV.O("Best"));
      this.Dn.P(() -> this.Di.getValue() && this.N.getValue());
      this.D8.P(this.e::getValue);
      this.U.P(this.e::getValue);
      this.R.P(this.C::getValue);
      this.y.P(this.P::getValue);
      this.P.o(var1 -> {
         if (!var1) {
            this.p = 0L;
         }
      });
      this.S.o(var1 -> {
         this.q();
         this.M();
      });

      for (com.larpclient.module.q var2 : List.of(this.l, this.K, this.s, this.D, this.DF, this.T, this.H, this.v, this.Du, this.a, this.j, this.g, this.I)) {
         var2.P(() -> false);
      }

      AutoCrystalModule var3 = this;
      this.t = new mr(this, var1 -> var3.addSetting(var1), 0.5, 0.3);
   }

   private e K(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   private s R(String var1, String var2, boolean var3) {
      return this.addSetting(new s(var1, var2, var3));
   }

   private e B(String var1, double var2) {
      return this.K(var1, "Random delay endpoint in milliseconds; reversed endpoints are ordered automatically", var2, 0.0, 500.0, 1.0);
   }

   public static long f(double var0, double var2, double var4) {
      long var6 = Math.round(Math.min(var0, var2));
      long var8 = Math.round(Math.max(var0, var2));
      return var6 + Math.min(var8 - var6, (long)(Math.clamp(var4, 0.0, 1.0) * (var8 - var6 + 1L)));
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K, com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.x : "";
   }

   @Override
   public void onEnable() {
      this.X();
      this.t.t();
      com.larpclient.h.A(
         "AutoCrystal",
         "Autonomous target selection enabled; native reach, damage limits and final look-ray validation; obsidian bases {}",
         this.N.getValue() ? this.DV.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.X();
      com.larpclient.h.A("AutoCrystal", "Autonomous actions cancelled; target and server-response waits cleared");
   }

   private void X() {
      this.L.a();
      this.q();
      this.M();
      this.t.c();
      this.f = null;
      this.D_ = null;
      this.J = null;
      this.V = null;
      this.p = 0L;
      this.D4 = 0L;
      this.m.clear();
      this.x = "Waiting for a target";
   }

   private boolean I() {
      return this.isRuntimeActive()
         && this.isInGame()
         && ta.Y()
         && mc.isWindowActive()
         && this.M()
         && (!this.r.getValue() || !mc.player.isUsingItem())
         && (!this.Q.getValue() || !this.m());
   }

   private boolean M() {
      p_.o var1 = p_.A();
      return this.S.O("Visible") ? p_.I(p_.o.D) : var1 == p_.o.T;
   }

   private void M() {
      this.h = this.Y = null;
      p_.Y(p_.o.D);
   }

   public boolean L() {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && this.S.O("Visible")
         && mc.player != null
         && this.h == mc.player
         && this.Y == mc.level
         && mc.player.tickCount >= this.n
         && (!this.r.getValue() || !mc.player.isUsingItem())) {
         int var1 = mc.player.tickCount - this.n;
         return var1 <= 1 || (this.L.o() || this.L.v()) && var1 <= this.Da.getValue();
      } else {
         return false;
      }
   }

   public float f(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && p_.A() == p_.o.D) {
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
      return var1.isAlive() && !var1.isRemoved() && this.w(var1);
   }

   private boolean w(LivingEntity var1) {
      if (var1 == mc.player || var1.isSpectator() || var1.level() != mc.level || mc.player.distanceTo(var1) > this.DN.getValue() || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.W.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.R().z().p(MCFriendModule.class);
            return var3 == null || !var3.S(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.o.getValue() && var1 instanceof Enemy;
      }
   }

   public static double H(double var0, double var2, double var4) {
      double var6 = var4 > 0.0 && var0 <= var4 ? Math.min(var2, 1.5) : var2;
      return Math.min(var6, var0);
   }

   public static boolean S(String var0, double var1, boolean var3, double var4, boolean var6, double var7) {
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

   private AutoCrystalModule.g c(Vec3 var1, List<LivingEntity> var2) {
      return this.G(var1, var2, null, null, false, false);
   }

   private static double V(LivingEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   private double u(LivingEntity var1) {
      return H(V(var1), this.Ds.getValue(), this.i.getValue());
   }

   private Vec3 N(LivingEntity var1) {
      int var2 = this.E.getValue().intValue();
      return var2 <= 0 ? Vec3.ZERO : this.m.computeIfAbsent(var1, var1x -> {
         Vec3 var2x = new Vec3(var1x.getX() - var1x.xo, 0.0, var1x.getZ() - var1x.zo);
         if (!(var2x.lengthSqr() < 1.0E-6) && !(var2x.lengthSqr() > 1.0)) {
            Vec3 var3 = var2x.scale(var2);
            return mc.level.noCollision(var1x, var1x.getBoundingBox().move(var3)) ? var3 : Vec3.ZERO;
         } else {
            return Vec3.ZERO;
         }
      });
   }

   private AutoCrystalModule.g G(Vec3 var1, List<LivingEntity> var2, AutoCrystalModule.b var3, BlockPos var4, boolean var5, boolean var6) {
      double var7 = zu.V(mc.player, var1, var4);
      if (this.C.getValue() && !zu.P(var7, this.R.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         if (var3 != null) {
            var3.v++;
            var3.e = Math.min(var3.e, var7);
         }

         return null;
      } else {
         AutoCrystalModule.g var9 = null;

         for (LivingEntity var11 : var2) {
            if (this.k(var11)) {
               double var12 = zu.w(var11, var1, var4, var5 ? this.N(var11) : Vec3.ZERO);
               if (!(var12 <= 0.0) && !(var12 < this.u(var11))) {
                  if (!var6 || !(var12 < V(var11))) {
                     AutoCrystalModule.g var14 = new AutoCrystalModule.g(var11, var12, var7, var12 >= V(var11));
                     if (var9 == null || var14.value() > var9.value()) {
                        var9 = var14;
                     }
                  } else if (var3 != null && (Double.isNaN(var3.t) || var12 - V(var11) > var3.S - var3.t)) {
                     var3.S = var12;
                     var3.t = V(var11);
                  }
               } else if (var3 != null) {
                  var3.n = Math.max(var3.n, var12);
               }
            }
         }

         return var9;
      }
   }

   private Optional<Vec3> T(EndCrystal var1, Vec3 var2) {
      return ta.m(var1, this.q(), var2 == null ? var1.getBoundingBox().getCenter() : var2, var2);
   }

   public static AABB v(BlockPos var0) {
      return EntityTypes.END_CRYSTAL.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(var0.above()));
   }

   public static List<Vec3> B(BlockPos var0, Vec3 var1) {
      ArrayList var2 = new ArrayList(15);
      Vec3 var3 = Vec3.atCenterOf(var0);

      for (Direction var7 : D1) {
         int var8 = var7.getStepX();
         int var9 = var7.getStepY();
         int var10 = var7.getStepZ();
         if (!((var1.x - var3.x - var8 * 0.5) * var8 + (var1.y - var3.y - var9 * 0.5) * var9 + (var1.z - var3.z - var10 * 0.5) * var10 <= 0.0)) {
            for (double[] var14 : z) {
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
      this.t.q(null);
      if (!this.isInGame()) {
         this.X();
      } else {
         if (this.f != mc.player || this.D_ != mc.level) {
            this.X();
            this.f = mc.player;
            this.D_ = mc.level;
         }

         this.A();
         int var10000 = this.x.contains(":");
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         String var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? this.x.substring(0, this.x.indexOf(58)) : this.x;
         int var7 = var1.equals(this.DI);
         boolean var3 = false;
         byte var2 = (byte)var7;
         var7 = Integer.signum(var2);
         if ((var7 | -var7) >>> 31 == 0) {
            this.DI = var1;
            com.larpclient.h.U("AutoCrystal", "Status: {}", this.x);
         }
      }
   }

   private void A() {
      this.m.clear();
      if (!this.I()) {
         this.q();
         this.x = "Paused: held-use optimizer, screen, focus, camera, item use or weakness";
      } else {
         long var1 = mc.level.getGameTime();
         if (var1 < this.p) {
            this.q();
            this.x = String.format(Locale.ROOT, "Paused after kill: %.1f s left", (this.p - var1) / 20.0);
         } else {
            this.L
               .E(
                  var1,
                  var0 -> mc.level.getEntity(var0) != null,
                  var0 -> !mc.level
                     .getEntities((Entity)null, new AABB(var0.above()).expandTowards(0.0, 1.0, 0.0), var0x -> var0x instanceof EndCrystal)
                     .isEmpty()
               );
            if (!this.L.o() && !this.L.v()) {
               this.J = null;
            } else {
               this.t.q(this.J);
               if (this.V != null && this.k(this.V)) {
                  kz.H(this.V);
               }
            }

            if (this.B != null && var1 - this.B.created() < this.w.getValue() && this.e(this.B)) {
               this.y(this.B, var1);
            } else {
               this.B = null;
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
                  this.x = "No target (enable Mobs to test on hostile mobs)";
               } else {
                  if (this.e.getValue() && !this.m()) {
                     EndCrystal var14 = null;
                     AutoCrystalModule.g var16 = null;
                     Vec3 var18 = null;

                     for (Entity var8 : mc.level.entitiesForRendering()) {
                        if (var8 instanceof EndCrystal var9 && this.L.I(var1, var8.getId()) && !(ta.M(var8) > this.q())) {
                           AutoCrystalModule.g var10 = this.c(var8.position(), var3);
                           if (var10 != null && (var16 == null || !(var10.value() <= var16.value()))) {
                              Optional var11 = this.T(var9, null);
                              if (!var11.isEmpty()) {
                                 var14 = var9;
                                 var16 = var10;
                                 var18 = (Vec3)var11.get();
                              }
                           }
                        }
                     }

                     if (var14 != null) {
                        this.B = new AutoCrystalModule.w(null, var14, var16.target(), var1, var18, null);
                        this.y(this.B, var1);
                        return;
                     }
                  }

                  if (this.L.v()) {
                     this.x = "Waiting for crystal removal";
                  } else if (!this.Di.getValue()) {
                     this.x = "No suitable crystal to break";
                  } else if (this.L.o()) {
                     this.x = "Waiting for crystal spawn";
                  } else if (!this.L.R(var1)) {
                     this.x = "Placement cooldown";
                  } else if (this.V() == -1) {
                     this.x = "No crystals in hands or hotbar";
                  } else {
                     BlockHitResult var15 = null;
                     AutoCrystalModule.g var17 = null;
                     AutoCrystalModule.b var19 = new AutoCrystalModule.b();
                     int var20 = (int)Math.ceil(this.h());
                     BlockPos var21 = BlockPos.containing(mc.player.getEyePosition());

                     for (BlockPos var24 : BlockPos.betweenClosed(var21.offset(-var20, -var20, -var20), var21.offset(var20, var20, var20))) {
                        BlockPos var25 = var24.immutable();
                        BlockHitResult var12 = this.h(var25, null);
                        if (var12 != null) {
                           var19.d++;
                           AutoCrystalModule.g var13 = this.G(Vec3.atBottomCenterOf(var25.above()), var3, var19, null, true, false);
                           if (var13 != null && (var17 == null || var13.value() > var17.value())) {
                              var15 = var12;
                              var17 = var13;
                           }
                        }
                     }

                     if (this.N.getValue() && (var17 == null || !var17.lethal() && !this.DV.O("Damage"))) {
                        AutoCrystalModule.w var23 = this.j(var3, var17, var1, var19);
                        if (var23 != null) {
                           this.B = var23;
                           this.y(this.B, var1);
                           return;
                        }

                        if (var15 == null) {
                           this.x = this.Z;
                           return;
                        }
                     }

                     if (var15 == null) {
                        this.x = this.Y(var19);
                     } else {
                        this.B = new AutoCrystalModule.w(var15.getBlockPos(), null, var17.target(), var1, var15.getLocation(), null);
                        this.y(this.B, var1);
                     }
                  }
               }
            }
         }
      }
   }

   private AutoCrystalModule.w j(List<LivingEntity> var1, AutoCrystalModule.g var2, long var3, AutoCrystalModule.b var5) {
      boolean var6 = this.DV.O("Lethal");
      double var7 = var2 == null ? Double.NaN : var2.damage();
      if (var3 < this.D4) {
         this.Z = "Waiting for a crystal on the new obsidian";
         return null;
      }

      if (t1.M(Items.OBSIDIAN, this.d.getValue()) == -1) {
         this.Z = this.Y(var5) + "; no obsidian in hands or hotbar";
         return null;
      }

      Vec3 var9 = mc.player.getEyePosition();
      double var10 = this.h();
      int var12 = (int)Math.ceil(var10);
      BlockPos var13 = BlockPos.containing(var9);
      ArrayList var14 = new ArrayList();

      for (BlockPos var16 : BlockPos.betweenClosed(var13.offset(-var12, -var12, -var12), var13.offset(var12, var12, var12))) {
         if (this.U(var16, var9, var10)) {
            Vec3 var17 = Vec3.atBottomCenterOf(var16.above());
            double var18 = Double.MAX_VALUE;

            for (LivingEntity var21 : var1) {
               double var22 = var21.position().add(this.N(var21)).distanceTo(var17);
               double var24 = var6 ? V(var21) : this.u(var21);
               if (var2 != null && this.DV.O("Best")) {
                  var24 = Math.max(var24, Math.min(V(var21), var7 + this.DY.getValue()));
               }

               if (zu.s(var22, 1.0) * 1.5 >= var24) {
                  var18 = Math.min(var18, var22);
               }
            }

            if (var18 < Double.MAX_VALUE) {
               var14.add(new AutoCrystalModule.q(var16.immutable(), var18));
            }
         }
      }

      var14.sort(Comparator.comparingDouble(AutoCrystalModule.q::distance));
      AutoCrystalModule.b var26 = new AutoCrystalModule.b();
      AutoCrystalModule.g var27 = null;
      BlockHitResult var28 = null;
      BlockPos var29 = null;
      int var19 = 0;

      for (AutoCrystalModule.q var31 : var14.subList(0, Math.min(96, var14.size()))) {
         if (var19 >= 24) {
            break;
         }

         BlockPos var32 = var31.pos();
         if (this.g(var32) && this.v(var32, var9, var10)) {
            BlockHitResult var23 = t1.c(var32, Items.OBSIDIAN, var10, true, this.d.getValue());
            if (var23 != null) {
               var26.d++;
               var19++;
               AutoCrystalModule.g var33 = this.G(Vec3.atBottomCenterOf(var32.above()), var1, var26, var32, true, var6);
               if (var33 != null
                  && S(this.DV.getValue(), var7, false, var33.damage(), var33.lethal(), this.DY.getValue())
                  && (var27 == null || !(var33.value() <= var27.value()))) {
                  var27 = var33;
                  var28 = var23;
                  var29 = var32;
               }
            }
         }
      }

      if (var27 == null) {
         this.Z = this.b(var26, var6);
         return null;
      } else {
         return new AutoCrystalModule.w(var29, null, var27.target(), var3, var28.getLocation(), var28);
      }
   }

   private boolean U(BlockPos var1, Vec3 var2, double var3) {
      if (!(var2.distanceToSqr(Vec3.atCenterOf(var1)) > (var3 + 1.0) * (var3 + 1.0)) && t1.t(var1) && mc.level.isEmptyBlock(var1.above())) {
         if (this.e.getValue() && wi.j(var2, v(var1)) > this.q()) {
            return false;
         }

         for (Direction var8 : Direction.values()) {
            if (t1.j(var1.relative(var8))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean g(BlockPos var1) {
      return mc.level.getEntities((Entity)null, new AABB(var1).expandTowards(0.0, 2.0, 0.0)).isEmpty();
   }

   private boolean v(BlockPos var1, Vec3 var2, double var3) {
      for (Vec3 var6 : B(var1, var2)) {
         if (!(var2.distanceToSqr(var6) > var3 * var3)
            && mc.level.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.NONE, mc.player)).getType() == Type.MISS) {
            return true;
         }
      }

      return false;
   }

   private BlockHitResult f(BlockHitResult var1, BlockPos var2, Vec3 var3) {
      BlockHitResult var4 = t1.Z(var1.getBlockPos(), var3, this.h());
      if (var4 != null && var4.getDirection() == var1.getDirection() && t1.j(var4.getBlockPos())) {
         t1.j var5 = t1.W(var4, Items.OBSIDIAN, this.d.getValue());
         return var5 != null && var5.target().equals(var2) ? var4 : null;
      } else {
         return null;
      }
   }

   private String b(AutoCrystalModule.b var1, boolean var2) {
      if (var1.d == 0) {
         return "No reachable obsidian or bedrock base, or open spot to place one";
      } else {
         return var2 && !Double.isNaN(var1.t) && var1.v < var1.d
            ? String.format(Locale.ROOT, "No lethal obsidian spot: best %.1f HP against %.1f HP health", var1.S, var1.t)
            : this.Y(var1);
      }
   }

   public void p(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (var1.getEventId() == 3 && this.P.getValue() && this.isRuntimeActive() && this.isInGame()) {
            if (var1.getEntity(mc.level) instanceof LivingEntity var2 && this.w(var2)) {
               this.p = mc.level.getGameTime() + Math.round(this.y.getValue() * 20.0);
               this.L.a();
               this.q();
               this.M();
               this.J = null;
               this.V = null;
               this.x = String.format(Locale.ROOT, "Paused after kill: %.1f s left", this.y.getValue());
               com.larpclient.h.A(
                  "AutoCrystal",
                  "{} died nearby; placing and breaking paused for {} s to protect drops",
                  var2.getName().getString(),
                  this.y.getValue().intValue()
               );
            }
         }
      }
   }

   private String Y(AutoCrystalModule.b var1) {
      if (var1.d == 0) {
         return "No reachable obsidian or bedrock base";
      } else {
         return var1.v == var1.d
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var1.e, this.R.getValue())
            : String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var1.n, this.Ds.getValue());
      }
   }

   private void q() {
      this.B = null;
      this.A = null;
      this.b = null;
      this.X.G();
      ta.s(this);
   }

   private double h() {
      return Math.min(this.q.getValue(), Math.min(this.O.getValue(), mc.player.blockInteractionRange()));
   }

   private double q() {
      return Math.min(this.q.getValue(), Math.min(this.D8.getValue(), mc.player.entityInteractionRange()));
   }

   private boolean m() {
      return mc.player.hasEffect(MobEffects.WEAKNESS) && mc.player.getAttributeValue(Attributes.ATTACK_DAMAGE) <= 0.0;
   }

   private boolean e(AutoCrystalModule.w var1) {
      if (this.I() && this.k(var1.victim())) {
         long var2 = mc.level.getGameTime();
         if (var1.crystal() != null) {
            return this.e.getValue()
               && !this.m()
               && this.L.I(var2, var1.crystal().getId())
               && this.T(var1.crystal(), var1.aim()).isPresent()
               && this.c(var1.crystal().position(), List.of(var1.victim())) != null;
         } else if (var1.support() != null) {
            Vec3 var4 = mc.player.getEyePosition();
            return this.Di.getValue()
               && this.N.getValue()
               && this.L.R(var2)
               && var2 >= this.D4
               && this.V() != -1
               && t1.M(Items.OBSIDIAN, this.d.getValue()) != -1
               && this.U(var1.base(), var4, this.h())
               && this.g(var1.base())
               && this.v(var1.base(), var4, this.h())
               && this.f(var1.support(), var1.base(), var1.aim().subtract(var4)) != null
               && this.G(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, var1.base(), true, this.DV.O("Lethal")) != null;
         } else {
            return this.Di.getValue()
               && this.L.R(var2)
               && this.V() != -1
               && this.h(var1.base(), var1.aim()) != null
               && this.G(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, null, true, false) != null;
         }
      } else {
         return false;
      }
   }

   private boolean m(AutoCrystalModule.w var1) {
      if (var1.support() != null) {
         return this.f(var1.support(), var1.base(), mc.player.getLookAngle()) != null;
      } else {
         return var1.crystal() != null ? ta.q(var1.crystal(), this.q()) : ta.D(var1.base(), this.h()) != null;
      }
   }

   public void g() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.B != null && this.G == mc.level.getGameTime()) {
            if (ta.E(this) && this.e(this.B)) {
               if (this.S.O("Visible")) {
                  if (!p_.I(p_.o.D)) {
                     this.X.G();
                     return;
                  }

                  p_.p(p_.o.D);
                  this.h = mc.player;
                  this.Y = mc.level;
                  this.n = mc.player.tickCount;
                  float[] var1 = j6.s(mc.player.getEyePosition(), this.B.aim());
                  float[] var2 = u6.A(mc.player.getYRot(), mc.player.getXRot(), var1[0], var1[1], this.k.getValue());
                  j6.C(j6.l.N, var2[0], var2[1], 1.0);
                  j6.n();
               }

               this.X.K(mc.player.tickCount, this.m(this.B));
               this.x = this.X.b(mc.player.tickCount, this.F.getValue().intValue())
                  ? "Aim ready"
                  : (!this.S.O("Visible") ? "Waiting for your crosshair" : (this.B.support() != null ? "Aiming at obsidian spot" : "Aiming at crystal action"));
            } else {
               this.X.G();
            }
         }
      }
   }

   private boolean d(AutoCrystalModule.w var1) {
      return this.B == var1 && this.G == mc.level.getGameTime() && this.e(var1) && this.X.b(mc.player.tickCount, this.F.getValue().intValue()) && this.m(var1);
   }

   private void y(AutoCrystalModule.w var1, long var2) {
      BlockPos var4 = var1.base() != null ? var1.base() : var1.crystal().blockPosition().below();
      this.t.q(var4);
      kz.H(var1.victim());
      this.V = var1.victim();
      this.G = var2;
      Object var5 = var1.support() != null ? new AutoCrystalModule.f(var1.base()) : (var1.crystal() == null ? var1.base() : var1.crystal());
      if (!Objects.equals(this.A, var5) || this.b != var1.victim()) {
         this.X.G();
      }

      this.A = var5;
      this.b = var1.victim();
      if (var1.crystal() != null) {
         ta.M(this, var1.crystal(), this.q(), false, () -> this.d(var1), 1, () -> {
            this.L.f(mc.level.getGameTime(), var1.crystal().getId(), this.U.getValue().intValue(), this.Da.getValue().intValue());
            this.J = var4;
            this.x = "Waiting for crystal removal";
            this.q();
         }, var2x -> {
            if (this.d(var1)) {
               this.x = "Waiting: " + var2x;
            }
         });
      } else if (var1.support() != null) {
         ta.F(this, () -> this.m(var1), () -> this.d(var1));
      } else {
         ta.F(this, () -> this.P(var1.base()), () -> this.d(var1));
      }
   }

   private void m(AutoCrystalModule.w var1) {
      BlockHitResult var2 = this.f(var1.support(), var1.base(), mc.player.getLookAngle());
      int var3 = t1.M(Items.OBSIDIAN, this.d.getValue());
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
               ks.p(mc.player, var6);
               this.L.N(var8, this.Dn.getValue().intValue());
               this.D4 = var8 + this.Da.getValue().intValue();
               this.x = "Placed obsidian; crystal next";
               com.larpclient.h.U(
                  "AutoCrystal", "Obsidian base at {} for {} ({} mode)", var1.base().toShortString(), var1.victim().getName().getString(), this.DV.getValue()
               );
            } else {
               this.L.N(var8, this.c.getValue().intValue());
               this.x = "Obsidian placement rejected; retrying";
            }

            this.q();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private int V() {
      if (mc.player.getOffhandItem().is(Items.END_CRYSTAL)) {
         return -2;
      }

      int var1 = mc.player.getInventory().getSelectedSlot();
      if (mc.player.getMainHandItem().is(Items.END_CRYSTAL)) {
         return var1;
      }

      if (this.d.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            if (mc.player.getInventory().getItem(var2).is(Items.END_CRYSTAL)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private BlockHitResult h(BlockPos var1, Vec3 var2) {
      if (mc.level.hasChunkAt(var1) && mc.level.getWorldBorder().isWithinBounds(var1)) {
         BlockState var3 = mc.level.getBlockState(var1);
         if ((var3.is(Blocks.OBSIDIAN) || var3.is(Blocks.BEDROCK))
            && mc.level.isEmptyBlock(var1.above())
            && mc.level.getEntities(null, new AABB(var1.above()).expandTowards(0.0, 1.0, 0.0)).isEmpty()) {
            Vec3 var4 = mc.player.getEyePosition();
            double var5 = this.h();
            if (var4.distanceToSqr(Vec3.atCenterOf(var1)) > (var5 + 1.0) * (var5 + 1.0)) {
               return null;
            }

            if (this.e.getValue() && wi.j(var4, v(var1)) > this.q()) {
               return null;
            }

            if (var2 != null) {
               BlockHitResult var7 = ta.t(var1, var5, var2.subtract(var4));
               if (var7 != null) {
                  return var7;
               }
            }

            for (Vec3 var8 : B(var1, var4)) {
               BlockHitResult var9 = ta.t(var1, var5, var8.subtract(var4));
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

   private void P(BlockPos var1) {
      BlockHitResult var2 = ta.D(var1, this.h());
      int var3 = this.V();
      if (var2 != null && var3 != -1 && this.h(var1, var2.getLocation()) != null) {
         int var4 = mc.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var3);
            }

            InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            InteractionResult var7 = mc.gameMode.useItemOn(mc.player, var6, var2);
            this.L.w(mc.level.getGameTime(), var1, this.c.getValue().intValue(), var7.consumesAction(), this.Da.getValue().intValue());
            if (var7.consumesAction()) {
               ks.p(mc.player, var6);
               this.J = var1;
               this.D4 = 0L;
               this.x = "Waiting for crystal spawn";
            } else {
               this.x = "Placement rejected; retrying";
            }

            this.q();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private static final class b {
      int d;
      int v;
      double e = Double.MAX_VALUE;
      double n;
      double S;
      double t = Double.NaN;
   }

   private record f(BlockPos pos) {
   }

   private record g(LivingEntity target, double damage, double self, boolean lethal) {
      double value() {
         return this.lethal ? 1000.0 - this.self : this.damage - this.self * 0.25;
      }
   }

   private record q(BlockPos pos, double distance) {
   }

   private record w(BlockPos base, EndCrystal crystal, LivingEntity victim, long created, Vec3 aim, BlockHitResult support) {
   }
}
