package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.d9;
import com.larpclient.d_;
import com.larpclient.ep;
import com.larpclient.f8;
import com.larpclient.hp;
import com.larpclient.hy;
import com.larpclient.m;
import com.larpclient.od;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.ru;
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

public final class AutoCrystalModule extends Module implements hy.c {
   private final hy G;
   private LocalPlayer K;
   private ClientLevel J;
   private final lc n = this.H("Range", "Maximum crystal interaction distance", 4.5, 2.0, 6.0, 0.5);
   private final lc p = this.H("Target Range", "Search distance for opponents", 10.0, 2.0, 16.0, 1.0);
   private final lc A = this.H("Break Delay", "Ticks between executed attacks", 2.0, 0.0, 10.0, 1.0);
   private final lc C1 = this.H("Place Delay", "Ticks between placement attempts", 2.0, 0.0, 10.0, 1.0);
   private final lc B = this.H("Min Damage", "Minimum estimated target damage in HP; a hit that kills or pops always qualifies", 4.0, 0.0, 20.0, 0.5);
   private final lc y = this.H(
      "Face Place Health", "Once a target's health and absorption fall to this, crystals down to 1.5 HP qualify; 0 turns it off", 8.0, 0.0, 36.0, 0.5
   );
   private final lc c = this.H(
      "Predict Ticks", "Score new crystals and obsidian against where a moving target will be after this many ticks", 2.0, 0.0, 10.0, 1.0
   );
   private final lc CA = this.H("Max Self Damage", "Maximum estimated self damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final ln Q = this.s("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final ln H = this.s("Players", "Target other survival players", true);
   private final ln R = this.s("Mobs", "Also target hostile mobs for combat or testing", false);
   private final ln E = this.s("Auto Switch", "Use hotbar crystals and restore the selected slot", true);
   private final ln X = this.s("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final ln U = this.s("Place", "Place crystals on existing obsidian or bedrock", true);
   private final ln q = this.s("Place Obsidian", "Place an obsidian base first when no existing base is good enough", true);
   private final lp b = this.addSetting(
      new lp(
         "Obsidian Mode",
         "Best places obsidian when no base qualifies, or when a new one kills or adds Obsidian Gain; Lethal only where its crystal kills or pops; Damage only when no existing base qualifies",
         "Best",
         "Best",
         "Lethal",
         "Damage"
      )
   );
   private final lc i = this.H("Obsidian Gain", "Extra target damage in HP a new obsidian base must add over the best existing base", 3.0, 0.0, 20.0, 0.5);
   private final lc u = this.H("Obsidian Delay", "Ticks after placing obsidian before the crystal goes on it", 1.0, 0.0, 10.0, 1.0);
   private final ln v = this.s("Break", "Attack crystals that can damage a target", true);
   private final lp P = this.addSetting(
      new lp("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final lc Cz = this.H("Turn Speed", "Maximum visible turn in degrees per tick", 45.0, 5.0, 180.0, 5.0);
   private final lc Z = this.H("Aim Hold", "Consecutive aligned ticks before acting", 1.0, 1.0, 5.0, 1.0);
   private final lc N = this.H("Place Range", "Placement reach, capped by Range and native block reach", 4.5, 2.0, 6.0, 0.5);
   private final lc W = this.H("Break Range", "Attack reach, capped by Range and native entity reach", 3.0, 2.0, 6.0, 0.5);
   private final lc k = this.H("Scan Interval", "Ticks to retain a valid selected action before rescoring", 2.0, 1.0, 10.0, 1.0);
   private final lc CP = this.H("Response Timeout", "Ticks to wait for server crystal spawn or removal before retrying", 10.0, 2.0, 40.0, 1.0);
   private final ln Cc = this.s("Pause Weakness", "Pause placement too when weakness prevents crystal attacks", true);
   private final ln D = this.s("Pause On Kill", "Stop placing and breaking after a nearby opponent dies, so explosions spare their drops", false);
   private final lc CW = this.H("Kill Pause", "Seconds to stay paused after a kill", 10.0, 1.0, 60.0, 1.0);
   private final ln t = this.s("Work with Crystal", "Start while holding an end crystal", true);
   private final ln g = this.s("Work with Sword", "Start while holding a sword", true);
   private final ln o = this.s("Work with Totem", "Start while holding a totem", true);
   private final ln M = this.s("Work with Fist", "Start with an empty main hand", true);
   private final lc CQ = this.q("Switch Delay Min", 1.0);
   private final lc T = this.q("Switch Delay Max", 50.0);
   private final lc z = this.q("Place Delay Min", 50.0);
   private final lc CF = this.q("Place Delay Max", 100.0);
   private final lc h = this.q("Break Delay Min", 50.0);
   private final lc I = this.q("Break Delay Max", 100.0);
   private final ln V = this.s("Swap Back", "Restore your starting slot after releasing use", true);
   private final lc Ch = this.q("Swap Back Delay Min", 50.0);
   private final lc Cf = this.q("Swap Back Delay Max", 100.0);
   private static final double[][] CH = new double[][]{{0.5, 0.5}, {0.15, 0.15}, {0.15, 0.85}, {0.85, 0.15}, {0.85, 0.85}};
   private static final Direction[] m = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};
   private static final int Cb = 24;
   private static final int Y = 96;
   public static final double F = 1.5;
   private AutoCrystalModule.v w;
   private long e;
   private String j = "";
   private final Map<LivingEntity, Vec3> x = new IdentityHashMap<>();
   private BlockPos L;
   private LivingEntity f;
   private Object O;
   private Object C;
   private int S;
   private String Cj = "";
   private Object Cw;
   private LivingEntity a;
   private long CS;
   private long s;
   private final ru r = new ru();
   private static AutoCrystalModule l;
   private final d9 d = new d9();
   private String C4 = "Waiting for a target";

   @Override
   public hy placementHighlight() {
      return this.G;
   }

   public static AutoCrystalModule T() {
      return l;
   }

   public List<l6<?>> n() {
      return List.of(this.U, this.v, this.n, this.N, this.W, this.C1, this.A, this.CP, this.k, this.E);
   }

   public List<l6<?>> t() {
      return List.of(this.q, this.b, this.i, this.u);
   }

   public List<l6<?>> P() {
      return List.of(this.H, this.R, this.p, this.c);
   }

   public List<l6<?>> X() {
      return List.of(this.P, this.Cz, this.Z);
   }

   public List<l6<?>> T() {
      return List.of(this.B, this.y, this.Q, this.CA, this.X, this.Cc, this.D, this.CW);
   }

   public AutoCrystalModule() {
      super("AutoCrystal", "Automatically place and break end crystals", Category.COMBAT, -1);
      l = this;
      this.Cz.x(() -> this.P.i("Visible"));
      this.N.x(this.U::getValue);
      this.C1.x(this.U::getValue);
      this.q.x(this.U::getValue);
      this.b.x(() -> this.U.getValue() && this.q.getValue());
      this.i.x(() -> this.U.getValue() && this.q.getValue() && this.b.i("Best"));
      this.u.x(() -> this.U.getValue() && this.q.getValue());
      this.W.x(this.v::getValue);
      this.A.x(this.v::getValue);
      this.CA.x(this.Q::getValue);
      this.CW.x(this.D::getValue);
      this.D.W(var1 -> {
         if (!var1) {
            this.s = 0L;
         }
      });
      this.P.W(var1 -> {
         this.m();
         this.r();
      });

      for (l6 var2 : List.of(this.t, this.g, this.o, this.M, this.CQ, this.T, this.z, this.CF, this.h, this.I, this.V, this.Ch, this.Cf)) {
         var2.x(() -> false);
      }

      AutoCrystalModule var3 = this;
      this.G = new hy(this, var1 -> var3.addSetting(var1), 0.5, 0.3);
   }

   private lc H(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   private ln s(String var1, String var2, boolean var3) {
      return this.addSetting(new ln(var1, var2, var3));
   }

   private lc q(String var1, double var2) {
      return this.H(var1, "Random delay endpoint in milliseconds; reversed endpoints are ordered automatically", var2, 0.0, 500.0, 1.0);
   }

   public static long E(double var0, double var2, double var4) {
      long var6 = Math.round(Math.min(var0, var2));
      long var8 = Math.round(Math.max(var0, var2));
      return var6 + Math.min(var8 - var6, (long)(Math.clamp(var4, 0.0, 1.0) * (var8 - var6 + 1L)));
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
      return this.isEnabled() ? this.C4 : "";
   }

   @Override
   public void onEnable() {
      this.H();
      this.G.S();
      r4.V(
         "AutoCrystal",
         "Autonomous target selection enabled; native reach, damage limits and final look-ray validation; obsidian bases {}",
         this.q.getValue() ? this.b.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.H();
      r4.V("AutoCrystal", "Autonomous actions cancelled; target and server-response waits cleared");
   }

   private void H() {
      this.d.J();
      this.m();
      this.r();
      this.G.C();
      this.K = null;
      this.J = null;
      this.L = null;
      this.f = null;
      this.s = 0L;
      this.e = 0L;
      this.x.clear();
      this.C4 = "Waiting for a target";
   }

   private boolean Q() {
      return this.isRuntimeActive()
         && this.isInGame()
         && d8.a()
         && mc.isWindowActive()
         && this.r()
         && (!this.X.getValue() || !mc.player.isUsingItem())
         && (!this.Cc.getValue() || !this.o());
   }

   private boolean r() {
      rr.f var1 = rr.u();
      return this.P.i("Visible") ? rr.j(rr.f.y) : var1 == rr.f.i;
   }

   private void r() {
      this.O = this.C = null;
      rr.N(rr.f.y);
   }

   public boolean b() {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && this.P.i("Visible")
         && mc.player != null
         && this.O == mc.player
         && this.C == mc.level
         && mc.player.tickCount >= this.S
         && (!this.X.getValue() || !mc.player.isUsingItem())) {
         int var1 = mc.player.tickCount - this.S;
         return var1 <= 1 || (this.d.c() || this.d.l()) && var1 <= this.CP.getValue();
      } else {
         return false;
      }
   }

   public float C(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && rr.u() == rr.f.y) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean M(LivingEntity var1) {
      return var1.isAlive() && !var1.isRemoved() && this.y(var1);
   }

   private boolean y(LivingEntity var1) {
      if (var1 == mc.player || var1.isSpectator() || var1.level() != mc.level || mc.player.distanceTo(var1) > this.p.getValue() || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.H.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.y().e().y(MCFriendModule.class);
            return var3 == null || !var3.e(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.R.getValue() && var1 instanceof Enemy;
      }
   }

   public static double c(double var0, double var2, double var4) {
      double var6 = var4 > 0.0 && var0 <= var4 ? Math.min(var2, 1.5) : var2;
      return Math.min(var6, var0);
   }

   public static boolean E(String var0, double var1, boolean var3, double var4, boolean var6, double var7) {
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

   private AutoCrystalModule.l A(Vec3 var1, List<LivingEntity> var2) {
      return this.v(var1, var2, null, null, false, false);
   }

   private static double v(LivingEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   private double b(LivingEntity var1) {
      return c(v(var1), this.B.getValue(), this.y.getValue());
   }

   private Vec3 B(LivingEntity var1) {
      int var2 = this.c.getValue().intValue();
      return var2 <= 0 ? Vec3.ZERO : this.x.computeIfAbsent(var1, var1x -> {
         Vec3 var2x = new Vec3(var1x.getX() - var1x.xo, 0.0, var1x.getZ() - var1x.zo);
         if (!(var2x.lengthSqr() < 1.0E-6) && !(var2x.lengthSqr() > 1.0)) {
            Vec3 var3 = var2x.scale(var2);
            return mc.level.noCollision(var1x, var1x.getBoundingBox().move(var3)) ? var3 : Vec3.ZERO;
         } else {
            return Vec3.ZERO;
         }
      });
   }

   private AutoCrystalModule.l v(Vec3 var1, List<LivingEntity> var2, AutoCrystalModule.e var3, BlockPos var4, boolean var5, boolean var6) {
      double var7 = od.S(mc.player, var1, var4);
      if (this.Q.getValue() && !od.p(var7, this.CA.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         if (var3 != null) {
            var3.A++;
            var3.J = Math.min(var3.J, var7);
         }

         return null;
      } else {
         AutoCrystalModule.l var9 = null;

         for (LivingEntity var11 : var2) {
            if (this.M(var11)) {
               double var12 = od.X(var11, var1, var4, var5 ? this.B(var11) : Vec3.ZERO);
               if (!(var12 <= 0.0) && !(var12 < this.b(var11))) {
                  if (!var6 || !(var12 < v(var11))) {
                     AutoCrystalModule.l var14 = new AutoCrystalModule.l(var11, var12, var7, var12 >= v(var11));
                     if (var9 == null || var14.value() > var9.value()) {
                        var9 = var14;
                     }
                  } else if (var3 != null && (Double.isNaN(var3.K) || var12 - v(var11) > var3.f - var3.K)) {
                     var3.f = var12;
                     var3.K = v(var11);
                  }
               } else if (var3 != null) {
                  var3.m = Math.max(var3.m, var12);
               }
            }
         }

         return var9;
      }
   }

   private Optional<Vec3> j(EndCrystal var1, Vec3 var2) {
      return d8.u(var1, this.U(), var2 == null ? var1.getBoundingBox().getCenter() : var2, var2);
   }

   public static AABB o(BlockPos var0) {
      return EntityTypes.END_CRYSTAL.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(var0.above()));
   }

   public static List<Vec3> d(BlockPos var0, Vec3 var1) {
      ArrayList var2 = new ArrayList(15);
      Vec3 var3 = Vec3.atCenterOf(var0);

      for (Direction var7 : m) {
         int var8 = var7.getStepX();
         int var9 = var7.getStepY();
         int var10 = var7.getStepZ();
         if (!((var1.x - var3.x - var8 * 0.5) * var8 + (var1.y - var3.y - var9 * 0.5) * var9 + (var1.z - var3.z - var10 * 0.5) * var10 <= 0.0)) {
            for (double[] var14 : CH) {
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
      this.G.i(null);
      if (!this.isInGame()) {
         this.H();
      } else {
         if (this.K != mc.player || this.J != mc.level) {
            this.H();
            this.K = mc.player;
            this.J = mc.level;
         }

         this.a();
         int var10000 = this.C4.contains(":");
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         String var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? this.C4.substring(0, this.C4.indexOf(58)) : this.C4;
         int var7 = var1.equals(this.Cj);
         boolean var3 = false;
         byte var2 = (byte)var7;
         var7 = Integer.signum(var2);
         if ((var7 | -var7) >>> 31 == 0) {
            this.Cj = var1;
            r4.g("AutoCrystal", "Status: {}", this.C4);
         }
      }
   }

   private void a() {
      this.x.clear();
      if (!this.Q()) {
         this.m();
         this.C4 = "Paused: held-use optimizer, screen, focus, camera, item use or weakness";
      } else {
         long var1 = mc.level.getGameTime();
         if (var1 < this.s) {
            this.m();
            this.C4 = String.format(Locale.ROOT, "Paused after kill: %.1f s left", (this.s - var1) / 20.0);
         } else {
            this.d
               .H(
                  var1,
                  var0 -> mc.level.getEntity(var0) != null,
                  var0 -> !mc.level
                     .getEntities((Entity)null, new AABB(var0.above()).expandTowards(0.0, 1.0, 0.0), var0x -> var0x instanceof EndCrystal)
                     .isEmpty()
               );
            if (!this.d.c() && !this.d.l()) {
               this.L = null;
            } else {
               this.G.i(this.L);
               if (this.f != null && this.M(this.f)) {
                  f8.s(this.f);
               }
            }

            if (this.w != null && var1 - this.w.created() < this.k.getValue() && this.u(this.w)) {
               this.c(this.w, var1);
            } else {
               this.w = null;
               ArrayList var3 = new ArrayList();

               for (Entity var5 : mc.level.entitiesForRendering()) {
                  if (var5 instanceof LivingEntity var6 && this.M(var6)) {
                     var3.add(var6);
                  }
               }

               var3.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
               if (var3.size() > 8) {
                  var3 = new ArrayList(var3.subList(0, 8));
               }

               if (var3.isEmpty()) {
                  this.C4 = "No target (enable Mobs to test on hostile mobs)";
               } else {
                  if (this.v.getValue() && !this.o()) {
                     EndCrystal var14 = null;
                     AutoCrystalModule.l var16 = null;
                     Vec3 var18 = null;

                     for (Entity var8 : mc.level.entitiesForRendering()) {
                        if (var8 instanceof EndCrystal var9 && this.d.V(var1, var8.getId()) && !(d8.L(var8) > this.U())) {
                           AutoCrystalModule.l var10 = this.A(var8.position(), var3);
                           if (var10 != null && (var16 == null || !(var10.value() <= var16.value()))) {
                              Optional var11 = this.j(var9, null);
                              if (!var11.isEmpty()) {
                                 var14 = var9;
                                 var16 = var10;
                                 var18 = (Vec3)var11.get();
                              }
                           }
                        }
                     }

                     if (var14 != null) {
                        this.w = new AutoCrystalModule.v(null, var14, var16.target(), var1, var18, null);
                        this.c(this.w, var1);
                        return;
                     }
                  }

                  if (this.d.l()) {
                     this.C4 = "Waiting for crystal removal";
                  } else if (!this.U.getValue()) {
                     this.C4 = "No suitable crystal to break";
                  } else if (this.d.c()) {
                     this.C4 = "Waiting for crystal spawn";
                  } else if (!this.d.m(var1)) {
                     this.C4 = "Placement cooldown";
                  } else if (this.q() == -1) {
                     this.C4 = "No crystals in hands or hotbar";
                  } else {
                     BlockHitResult var15 = null;
                     AutoCrystalModule.l var17 = null;
                     AutoCrystalModule.e var19 = new AutoCrystalModule.e();
                     int var20 = (int)Math.ceil(this.t());
                     BlockPos var21 = BlockPos.containing(mc.player.getEyePosition());

                     for (BlockPos var24 : BlockPos.betweenClosed(var21.offset(-var20, -var20, -var20), var21.offset(var20, var20, var20))) {
                        BlockPos var25 = var24.immutable();
                        BlockHitResult var12 = this.P(var25, null);
                        if (var12 != null) {
                           var19.t++;
                           AutoCrystalModule.l var13 = this.v(Vec3.atBottomCenterOf(var25.above()), var3, var19, null, true, false);
                           if (var13 != null && (var17 == null || var13.value() > var17.value())) {
                              var15 = var12;
                              var17 = var13;
                           }
                        }
                     }

                     if (this.q.getValue() && (var17 == null || !var17.lethal() && !this.b.i("Damage"))) {
                        AutoCrystalModule.v var23 = this.f(var3, var17, var1, var19);
                        if (var23 != null) {
                           this.w = var23;
                           this.c(this.w, var1);
                           return;
                        }

                        if (var15 == null) {
                           this.C4 = this.j;
                           return;
                        }
                     }

                     if (var15 == null) {
                        this.C4 = this.u(var19);
                     } else {
                        this.w = new AutoCrystalModule.v(var15.getBlockPos(), null, var17.target(), var1, var15.getLocation(), null);
                        this.c(this.w, var1);
                     }
                  }
               }
            }
         }
      }
   }

   private AutoCrystalModule.v f(List<LivingEntity> var1, AutoCrystalModule.l var2, long var3, AutoCrystalModule.e var5) {
      boolean var6 = this.b.i("Lethal");
      double var7 = var2 == null ? Double.NaN : var2.damage();
      if (var3 < this.e) {
         this.j = "Waiting for a crystal on the new obsidian";
         return null;
      }

      if (com.larpclient.m.Q(Items.OBSIDIAN, this.E.getValue()) == -1) {
         this.j = this.u(var5) + "; no obsidian in hands or hotbar";
         return null;
      }

      Vec3 var9 = mc.player.getEyePosition();
      double var10 = this.t();
      int var12 = (int)Math.ceil(var10);
      BlockPos var13 = BlockPos.containing(var9);
      ArrayList var14 = new ArrayList();

      for (BlockPos var16 : BlockPos.betweenClosed(var13.offset(-var12, -var12, -var12), var13.offset(var12, var12, var12))) {
         if (this.F(var16, var9, var10)) {
            Vec3 var17 = Vec3.atBottomCenterOf(var16.above());
            double var18 = Double.MAX_VALUE;

            for (LivingEntity var21 : var1) {
               double var22 = var21.position().add(this.B(var21)).distanceTo(var17);
               double var24 = var6 ? v(var21) : this.b(var21);
               if (var2 != null && this.b.i("Best")) {
                  var24 = Math.max(var24, Math.min(v(var21), var7 + this.i.getValue()));
               }

               if (od.I(var22, 1.0) * 1.5 >= var24) {
                  var18 = Math.min(var18, var22);
               }
            }

            if (var18 < Double.MAX_VALUE) {
               var14.add(new AutoCrystalModule.u(var16.immutable(), var18));
            }
         }
      }

      var14.sort(Comparator.comparingDouble(AutoCrystalModule.u::distance));
      AutoCrystalModule.e var26 = new AutoCrystalModule.e();
      AutoCrystalModule.l var27 = null;
      BlockHitResult var28 = null;
      BlockPos var29 = null;
      int var19 = 0;

      for (AutoCrystalModule.u var31 : var14.subList(0, Math.min(96, var14.size()))) {
         if (var19 >= 24) {
            break;
         }

         BlockPos var32 = var31.pos();
         if (this.n(var32) && this.Y(var32, var9, var10)) {
            BlockHitResult var23 = com.larpclient.m.J(var32, Items.OBSIDIAN, var10, true, this.E.getValue());
            if (var23 != null) {
               var26.t++;
               var19++;
               AutoCrystalModule.l var33 = this.v(Vec3.atBottomCenterOf(var32.above()), var1, var26, var32, true, var6);
               if (var33 != null
                  && E(this.b.getValue(), var7, false, var33.damage(), var33.lethal(), this.i.getValue())
                  && (var27 == null || !(var33.value() <= var27.value()))) {
                  var27 = var33;
                  var28 = var23;
                  var29 = var32;
               }
            }
         }
      }

      if (var27 == null) {
         this.j = this.N(var26, var6);
         return null;
      } else {
         return new AutoCrystalModule.v(var29, null, var27.target(), var3, var28.getLocation(), var28);
      }
   }

   private boolean F(BlockPos var1, Vec3 var2, double var3) {
      if (!(var2.distanceToSqr(Vec3.atCenterOf(var1)) > (var3 + 1.0) * (var3 + 1.0)) && com.larpclient.m.Q(var1) && mc.level.isEmptyBlock(var1.above())) {
         if (this.v.getValue() && d_.T(var2, o(var1)) > this.U()) {
            return false;
         }

         for (Direction var8 : Direction.values()) {
            if (com.larpclient.m.S(var1.relative(var8))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean n(BlockPos var1) {
      return mc.level.getEntities((Entity)null, new AABB(var1).expandTowards(0.0, 2.0, 0.0)).isEmpty();
   }

   private boolean Y(BlockPos var1, Vec3 var2, double var3) {
      for (Vec3 var6 : d(var1, var2)) {
         if (!(var2.distanceToSqr(var6) > var3 * var3)
            && mc.level.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.NONE, mc.player)).getType() == Type.MISS) {
            return true;
         }
      }

      return false;
   }

   private BlockHitResult I(BlockHitResult var1, BlockPos var2, Vec3 var3) {
      BlockHitResult var4 = com.larpclient.m.c(var1.getBlockPos(), var3, this.t());
      if (var4 != null && var4.getDirection() == var1.getDirection() && com.larpclient.m.S(var4.getBlockPos())) {
         m.r var5 = com.larpclient.m.D(var4, Items.OBSIDIAN, this.E.getValue());
         return var5 != null && var5.target().equals(var2) ? var4 : null;
      } else {
         return null;
      }
   }

   private String N(AutoCrystalModule.e var1, boolean var2) {
      if (var1.t == 0) {
         return "No reachable obsidian or bedrock base, or open spot to place one";
      } else {
         return var2 && !Double.isNaN(var1.K) && var1.A < var1.t
            ? String.format(Locale.ROOT, "No lethal obsidian spot: best %.1f HP against %.1f HP health", var1.f, var1.K)
            : this.u(var1);
      }
   }

   public void w(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (var1.getEventId() == 3 && this.D.getValue() && this.isRuntimeActive() && this.isInGame()) {
            if (var1.getEntity(mc.level) instanceof LivingEntity var2 && this.y(var2)) {
               this.s = mc.level.getGameTime() + Math.round(this.CW.getValue() * 20.0);
               this.d.J();
               this.m();
               this.r();
               this.L = null;
               this.f = null;
               this.C4 = String.format(Locale.ROOT, "Paused after kill: %.1f s left", this.CW.getValue());
               r4.V(
                  "AutoCrystal",
                  "{} died nearby; placing and breaking paused for {} s to protect drops",
                  var2.getName().getString(),
                  this.CW.getValue().intValue()
               );
            }
         }
      }
   }

   private String u(AutoCrystalModule.e var1) {
      if (var1.t == 0) {
         return "No reachable obsidian or bedrock base";
      } else {
         return var1.A == var1.t
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var1.J, this.CA.getValue())
            : String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var1.m, this.B.getValue());
      }
   }

   private void m() {
      this.w = null;
      this.Cw = null;
      this.a = null;
      this.r.Y();
      d8.K(this);
   }

   private double t() {
      return Math.min(this.n.getValue(), Math.min(this.N.getValue(), mc.player.blockInteractionRange()));
   }

   private double U() {
      return Math.min(this.n.getValue(), Math.min(this.W.getValue(), mc.player.entityInteractionRange()));
   }

   private boolean o() {
      return mc.player.hasEffect(MobEffects.WEAKNESS) && mc.player.getAttributeValue(Attributes.ATTACK_DAMAGE) <= 0.0;
   }

   private boolean u(AutoCrystalModule.v var1) {
      if (this.Q() && this.M(var1.victim())) {
         long var2 = mc.level.getGameTime();
         if (var1.crystal() != null) {
            return this.v.getValue()
               && !this.o()
               && this.d.V(var2, var1.crystal().getId())
               && this.j(var1.crystal(), var1.aim()).isPresent()
               && this.A(var1.crystal().position(), List.of(var1.victim())) != null;
         } else if (var1.support() != null) {
            Vec3 var4 = mc.player.getEyePosition();
            return this.U.getValue()
               && this.q.getValue()
               && this.d.m(var2)
               && var2 >= this.e
               && this.q() != -1
               && com.larpclient.m.Q(Items.OBSIDIAN, this.E.getValue()) != -1
               && this.F(var1.base(), var4, this.t())
               && this.n(var1.base())
               && this.Y(var1.base(), var4, this.t())
               && this.I(var1.support(), var1.base(), var1.aim().subtract(var4)) != null
               && this.v(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, var1.base(), true, this.b.i("Lethal")) != null;
         } else {
            return this.U.getValue()
               && this.d.m(var2)
               && this.q() != -1
               && this.P(var1.base(), var1.aim()) != null
               && this.v(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, null, true, false) != null;
         }
      } else {
         return false;
      }
   }

   private boolean z(AutoCrystalModule.v var1) {
      if (var1.support() != null) {
         return this.I(var1.support(), var1.base(), mc.player.getLookAngle()) != null;
      } else {
         return var1.crystal() != null ? d8.M(var1.crystal(), this.U()) : d8.F(var1.base(), this.t()) != null;
      }
   }

   public void A() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.w != null && this.CS == mc.level.getGameTime()) {
            if (d8.m(this) && this.u(this.w)) {
               if (this.P.i("Visible")) {
                  if (!rr.j(rr.f.y)) {
                     this.r.Y();
                     return;
                  }

                  rr.W(rr.f.y);
                  this.O = mc.player;
                  this.C = mc.level;
                  this.S = mc.player.tickCount;
                  float[] var1 = ep.g(mc.player.getEyePosition(), this.w.aim());
                  float[] var2 = ru.Y(mc.player.getYRot(), mc.player.getXRot(), var1[0], var1[1], this.Cz.getValue());
                  ep.C(ep.z.O, var2[0], var2[1], 1.0);
                  ep.u();
               }

               this.r.F(mc.player.tickCount, this.z(this.w));
               this.C4 = this.r.y(mc.player.tickCount, this.Z.getValue().intValue())
                  ? "Aim ready"
                  : (!this.P.i("Visible") ? "Waiting for your crosshair" : (this.w.support() != null ? "Aiming at obsidian spot" : "Aiming at crystal action"));
            } else {
               this.r.Y();
            }
         }
      }
   }

   private boolean e(AutoCrystalModule.v var1) {
      return this.w == var1 && this.CS == mc.level.getGameTime() && this.u(var1) && this.r.y(mc.player.tickCount, this.Z.getValue().intValue()) && this.z(var1);
   }

   private void c(AutoCrystalModule.v var1, long var2) {
      BlockPos var4 = var1.base() != null ? var1.base() : var1.crystal().blockPosition().below();
      this.G.i(var4);
      f8.s(var1.victim());
      this.f = var1.victim();
      this.CS = var2;
      Object var5 = var1.support() != null ? new AutoCrystalModule.j(var1.base()) : (var1.crystal() == null ? var1.base() : var1.crystal());
      if (!Objects.equals(this.Cw, var5) || this.a != var1.victim()) {
         this.r.Y();
      }

      this.Cw = var5;
      this.a = var1.victim();
      if (var1.crystal() != null) {
         d8.n(this, var1.crystal(), this.U(), false, () -> this.e(var1), 1, () -> {
            this.d.m(mc.level.getGameTime(), var1.crystal().getId(), this.A.getValue().intValue(), this.CP.getValue().intValue());
            this.L = var4;
            this.C4 = "Waiting for crystal removal";
            this.m();
         }, var2x -> {
            if (this.e(var1)) {
               this.C4 = "Waiting: " + var2x;
            }
         });
      } else if (var1.support() != null) {
         d8.A(this, () -> this.e(var1), () -> this.e(var1));
      } else {
         d8.A(this, () -> this.i(var1.base()), () -> this.e(var1));
      }
   }

   private void e(AutoCrystalModule.v var1) {
      BlockHitResult var2 = this.I(var1.support(), var1.base(), mc.player.getLookAngle());
      int var3 = com.larpclient.m.Q(Items.OBSIDIAN, this.E.getValue());
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
               hp.Q(mc.player, var6);
               this.d.f(var8, this.u.getValue().intValue());
               this.e = var8 + this.CP.getValue().intValue();
               this.C4 = "Placed obsidian; crystal next";
               r4.g("AutoCrystal", "Obsidian base at {} for {} ({} mode)", var1.base().toShortString(), var1.victim().getName().getString(), this.b.getValue());
            } else {
               this.d.f(var8, this.C1.getValue().intValue());
               this.C4 = "Obsidian placement rejected; retrying";
            }

            this.m();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private int q() {
      if (mc.player.getOffhandItem().is(Items.END_CRYSTAL)) {
         return -2;
      }

      int var1 = mc.player.getInventory().getSelectedSlot();
      if (mc.player.getMainHandItem().is(Items.END_CRYSTAL)) {
         return var1;
      }

      if (this.E.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            if (mc.player.getInventory().getItem(var2).is(Items.END_CRYSTAL)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private BlockHitResult P(BlockPos var1, Vec3 var2) {
      if (mc.level.hasChunkAt(var1) && mc.level.getWorldBorder().isWithinBounds(var1)) {
         BlockState var3 = mc.level.getBlockState(var1);
         if ((var3.is(Blocks.OBSIDIAN) || var3.is(Blocks.BEDROCK))
            && mc.level.isEmptyBlock(var1.above())
            && mc.level.getEntities(null, new AABB(var1.above()).expandTowards(0.0, 1.0, 0.0)).isEmpty()) {
            Vec3 var4 = mc.player.getEyePosition();
            double var5 = this.t();
            if (var4.distanceToSqr(Vec3.atCenterOf(var1)) > (var5 + 1.0) * (var5 + 1.0)) {
               return null;
            }

            if (this.v.getValue() && d_.T(var4, o(var1)) > this.U()) {
               return null;
            }

            if (var2 != null) {
               BlockHitResult var7 = d8.e(var1, var5, var2.subtract(var4));
               if (var7 != null) {
                  return var7;
               }
            }

            for (Vec3 var8 : d(var1, var4)) {
               BlockHitResult var9 = d8.e(var1, var5, var8.subtract(var4));
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

   private void i(BlockPos var1) {
      BlockHitResult var2 = d8.F(var1, this.t());
      int var3 = this.q();
      if (var2 != null && var3 != -1 && this.P(var1, var2.getLocation()) != null) {
         int var4 = mc.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var3);
            }

            InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            InteractionResult var7 = mc.gameMode.useItemOn(mc.player, var6, var2);
            this.d.R(mc.level.getGameTime(), var1, this.C1.getValue().intValue(), var7.consumesAction(), this.CP.getValue().intValue());
            if (var7.consumesAction()) {
               hp.Q(mc.player, var6);
               this.L = var1;
               this.e = 0L;
               this.C4 = "Waiting for crystal spawn";
            } else {
               this.C4 = "Placement rejected; retrying";
            }

            this.m();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private static final class e {
      int t;
      int A;
      double J = Double.MAX_VALUE;
      double m;
      double f;
      double K = Double.NaN;
   }

   private record j(BlockPos pos) {
   }

   private record l(LivingEntity target, double damage, double self, boolean lethal) {
      double value() {
         return this.lethal ? 1000.0 - this.self : this.damage - this.self * 0.25;
      }
   }

   private record u(BlockPos pos, double distance) {
   }

   private record v(BlockPos base, EndCrystal crystal, LivingEntity victim, long created, Vec3 aim, BlockHitResult support) {
   }
}
