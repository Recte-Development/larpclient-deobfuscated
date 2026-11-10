package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.ai;
import com.larpclient.b6;
import com.larpclient.bv;
import com.larpclient.h8;
import com.larpclient.ij;
import com.larpclient.jz;
import com.larpclient.nx;
import com.larpclient.vi;
import com.larpclient.wp;
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

public final class AutoCrystalModule extends Module implements ai.e {
   private final ai R;
   private LocalPlayer k;
   private ClientLevel x;
   private final b u = this.x("Range", "Maximum crystal interaction distance", 4.5, 2.0, 6.0, 0.5);
   private final b t = this.x("Target Range", "Search distance for opponents", 10.0, 2.0, 16.0, 1.0);
   private final b F3 = this.x("Break Delay", "Ticks between executed attacks", 2.0, 0.0, 10.0, 1.0);
   private final b O = this.x("Place Delay", "Ticks between placement attempts", 2.0, 0.0, 10.0, 1.0);
   private final b w = this.x("Min Damage", "Minimum estimated target damage in HP; a hit that kills or pops always qualifies", 4.0, 0.0, 20.0, 0.5);
   private final b o = this.x(
      "Face Place Health", "Once a target's health and absorption fall to this, crystals down to 1.5 HP qualify; 0 turns it off", 8.0, 0.0, 36.0, 0.5
   );
   private final b I = this.x(
      "Predict Ticks", "Score new crystals and obsidian against where a moving target will be after this many ticks", 2.0, 0.0, 10.0, 1.0
   );
   private final b F2 = this.x("Max Self Damage", "Maximum estimated self damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final m E = this.x("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final m r = this.x("Players", "Target other survival players", true);
   private final m Y = this.x("Mobs", "Also target hostile mobs for combat or testing", false);
   private final m n = this.x("Auto Switch", "Use hotbar crystals and restore the selected slot", true);
   private final m FP = this.x("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final m q = this.x("Place", "Place crystals on existing obsidian or bedrock", true);
   private final m A = this.x("Place Obsidian", "Place an obsidian base first when no existing base is good enough", true);
   private final o a = this.addSetting(
      new o(
         "Obsidian Mode",
         "Best places obsidian when no base qualifies, or when a new one kills or adds Obsidian Gain; Lethal only where its crystal kills or pops; Damage only when no existing base qualifies",
         "Best",
         "Best",
         "Lethal",
         "Damage"
      )
   );
   private final b Fw = this.x("Obsidian Gain", "Extra target damage in HP a new obsidian base must add over the best existing base", 3.0, 0.0, 20.0, 0.5);
   private final b g = this.x("Obsidian Delay", "Ticks after placing obsidian before the crystal goes on it", 1.0, 0.0, 10.0, 1.0);
   private final m T = this.x("Break", "Attack crystals that can damage a target", true);
   private final o FB = this.addSetting(
      new o("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final b K = this.x("Turn Speed", "Maximum visible turn in degrees per tick", 45.0, 5.0, 180.0, 5.0);
   private final b m = this.x("Aim Hold", "Consecutive aligned ticks before acting", 1.0, 1.0, 5.0, 1.0);
   private final b h = this.x("Place Range", "Placement reach, capped by Range and native block reach", 4.5, 2.0, 6.0, 0.5);
   private final b FE = this.x("Break Range", "Attack reach, capped by Range and native entity reach", 3.0, 2.0, 6.0, 0.5);
   private final b B = this.x("Scan Interval", "Ticks to retain a valid selected action before rescoring", 2.0, 1.0, 10.0, 1.0);
   private final b S = this.x("Response Timeout", "Ticks to wait for server crystal spawn or removal before retrying", 10.0, 2.0, 40.0, 1.0);
   private final m G = this.x("Pause Weakness", "Pause placement too when weakness prevents crystal attacks", true);
   private final m FI = this.x("Pause On Kill", "Stop placing and breaking after a nearby opponent dies, so explosions spare their drops", false);
   private final b M = this.x("Kill Pause", "Seconds to stay paused after a kill", 10.0, 1.0, 60.0, 1.0);
   private final m U = this.x("Work with Crystal", "Start while holding an end crystal", true);
   private final m i = this.x("Work with Sword", "Start while holding a sword", true);
   private final m F8 = this.x("Work with Totem", "Start while holding a totem", true);
   private final m d = this.x("Work with Fist", "Start with an empty main hand", true);
   private final b W = this.N("Switch Delay Min", 1.0);
   private final b j = this.N("Switch Delay Max", 50.0);
   private final b b = this.N("Place Delay Min", 50.0);
   private final b J = this.N("Place Delay Max", 100.0);
   private final b FD = this.N("Break Delay Min", 50.0);
   private final b FM = this.N("Break Delay Max", 100.0);
   private final m y = this.x("Swap Back", "Restore your starting slot after releasing use", true);
   private final b H = this.N("Swap Back Delay Min", 50.0);
   private final b V = this.N("Swap Back Delay Max", 100.0);
   private static final double[][] C = new double[][]{{0.5, 0.5}, {0.15, 0.15}, {0.15, 0.85}, {0.85, 0.15}, {0.85, 0.85}};
   private static final Direction[] FH = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};
   private static final int Z = 24;
   private static final int f = 96;
   public static final double z = 1.5;
   private AutoCrystalModule.i F7;
   private long X;
   private String L = "";
   private final Map<LivingEntity, Vec3> D = new IdentityHashMap<>();
   private BlockPos F;
   private LivingEntity p;
   private Object Fp;
   private Object e;
   private int FR;
   private String Fc = "";
   private Object v;
   private LivingEntity N;
   private long P;
   private long Q;
   private final nx Fn = new nx();
   private static AutoCrystalModule l;
   private final bv c = new bv();
   private String s = "Waiting for a target";

   @Override
   public ai placementHighlight() {
      return this.R;
   }

   public static AutoCrystalModule d() {
      return l;
   }

   public List<r<?>> x() {
      return List.of(this.q, this.T, this.u, this.h, this.FE, this.O, this.F3, this.S, this.B, this.n);
   }

   public List<r<?>> B() {
      return List.of(this.A, this.a, this.Fw, this.g);
   }

   public List<r<?>> Q() {
      return List.of(this.r, this.Y, this.t, this.I);
   }

   public List<r<?>> z() {
      return List.of(this.FB, this.K, this.m);
   }

   public List<r<?>> Z() {
      return List.of(this.w, this.o, this.E, this.F2, this.FP, this.G, this.FI, this.M);
   }

   public AutoCrystalModule() {
      super("AutoCrystal", "Automatically place and break end crystals", Category.COMBAT, -1);
      l = this;
      this.K.a(() -> this.FB.w("Visible"));
      this.h.a(this.q::getValue);
      this.O.a(this.q::getValue);
      this.A.a(this.q::getValue);
      this.a.a(() -> this.q.getValue() && this.A.getValue());
      this.Fw.a(() -> this.q.getValue() && this.A.getValue() && this.a.w("Best"));
      this.g.a(() -> this.q.getValue() && this.A.getValue());
      this.FE.a(this.T::getValue);
      this.F3.a(this.T::getValue);
      this.F2.a(this.E::getValue);
      this.M.a(this.FI::getValue);
      this.FI.w(var1 -> {
         if (!var1) {
            this.Q = 0L;
         }
      });
      this.FB.w(var1 -> {
         this.k();
         this.s();
      });

      for (r var2 : List.of(this.U, this.i, this.F8, this.d, this.W, this.j, this.b, this.J, this.FD, this.FM, this.y, this.H, this.V)) {
         var2.a(() -> false);
      }

      AutoCrystalModule var3 = this;
      this.R = new ai(this, var1 -> var3.addSetting(var1), 0.5, 0.3);
   }

   private b x(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   private m x(String var1, String var2, boolean var3) {
      return this.addSetting(new m(var1, var2, var3));
   }

   private b N(String var1, double var2) {
      return this.x(var1, "Random delay endpoint in milliseconds; reversed endpoints are ordered automatically", var2, 0.0, 500.0, 1.0);
   }

   public static long M(double var0, double var2, double var4) {
      long var6 = Math.round(Math.min(var0, var2));
      long var8 = Math.round(Math.max(var0, var2));
      return var6 + Math.min(var8 - var6, (long)(Math.clamp(var4, 0.0, 1.0) * (var8 - var6 + 1L)));
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<com.larpclient.module.l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F, com.larpclient.module.l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.s : "";
   }

   @Override
   public void onEnable() {
      this.f();
      this.R.b();
      _q.z(
         "AutoCrystal",
         "Autonomous target selection enabled; native reach, damage limits and final look-ray validation; obsidian bases {}",
         this.A.getValue() ? this.a.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.f();
      _q.z("AutoCrystal", "Autonomous actions cancelled; target and server-response waits cleared");
   }

   private void f() {
      this.c.W();
      this.k();
      this.s();
      this.R.m();
      this.k = null;
      this.x = null;
      this.F = null;
      this.p = null;
      this.Q = 0L;
      this.X = 0L;
      this.D.clear();
      this.s = "Waiting for a target";
   }

   private boolean p() {
      return this.isRuntimeActive()
         && this.isInGame()
         && _p.g()
         && mc.isWindowActive()
         && this.V()
         && (!this.FP.getValue() || !mc.player.isUsingItem())
         && (!this.G.getValue() || !this.k());
   }

   private boolean V() {
      ij.r var1 = ij.L();
      return this.FB.w("Visible") ? ij.a(ij.r.p) : var1 == ij.r.X;
   }

   private void s() {
      this.Fp = this.e = null;
      ij.W(ij.r.p);
   }

   public boolean n() {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && this.FB.w("Visible")
         && mc.player != null
         && this.Fp == mc.player
         && this.e == mc.level
         && mc.player.tickCount >= this.FR
         && (!this.FP.getValue() || !mc.player.isUsingItem())) {
         int var1 = mc.player.tickCount - this.FR;
         return var1 <= 1 || (this.c.n() || this.c.u()) && var1 <= this.S.getValue();
      } else {
         return false;
      }
   }

   public float a(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ij.L() == ij.r.p) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean l(LivingEntity var1) {
      return var1.isAlive() && !var1.isRemoved() && this.b(var1);
   }

   private boolean b(LivingEntity var1) {
      if (var1 == mc.player || var1.isSpectator() || var1.level() != mc.level || mc.player.distanceTo(var1) > this.t.getValue() || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.r.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.N().j().y(MCFriendModule.class);
            return var3 == null || !var3.M(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.Y.getValue() && var1 instanceof Enemy;
      }
   }

   public static double X(double var0, double var2, double var4) {
      double var6 = var4 > 0.0 && var0 <= var4 ? Math.min(var2, 1.5) : var2;
      return Math.min(var6, var0);
   }

   public static boolean q(String var0, double var1, boolean var3, double var4, boolean var6, double var7) {
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

   private AutoCrystalModule.l l(Vec3 var1, List<LivingEntity> var2) {
      return this.Z(var1, var2, null, null, false, false);
   }

   private static double M(LivingEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   private double C(LivingEntity var1) {
      return X(M(var1), this.w.getValue(), this.o.getValue());
   }

   private Vec3 u(LivingEntity var1) {
      int var2 = this.I.getValue().intValue();
      return var2 <= 0 ? Vec3.ZERO : this.D.computeIfAbsent(var1, var1x -> {
         Vec3 var2x = new Vec3(var1x.getX() - var1x.xo, 0.0, var1x.getZ() - var1x.zo);
         if (!(var2x.lengthSqr() < 1.0E-6) && !(var2x.lengthSqr() > 1.0)) {
            Vec3 var3 = var2x.scale(var2);
            return mc.level.noCollision(var1x, var1x.getBoundingBox().move(var3)) ? var3 : Vec3.ZERO;
         } else {
            return Vec3.ZERO;
         }
      });
   }

   private AutoCrystalModule.l Z(Vec3 var1, List<LivingEntity> var2, AutoCrystalModule.x var3, BlockPos var4, boolean var5, boolean var6) {
      double var7 = com.larpclient.t.b(mc.player, var1, var4);
      if (this.E.getValue() && !com.larpclient.t.x(var7, this.F2.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         if (var3 != null) {
            var3.I++;
            var3.Y = Math.min(var3.Y, var7);
         }

         return null;
      } else {
         AutoCrystalModule.l var9 = null;

         for (LivingEntity var11 : var2) {
            if (this.l(var11)) {
               double var12 = com.larpclient.t.c(var11, var1, var4, var5 ? this.u(var11) : Vec3.ZERO);
               if (!(var12 <= 0.0) && !(var12 < this.C(var11))) {
                  if (!var6 || !(var12 < M(var11))) {
                     AutoCrystalModule.l var14 = new AutoCrystalModule.l(var11, var12, var7, var12 >= M(var11));
                     if (var9 == null || var14.value() > var9.value()) {
                        var9 = var14;
                     }
                  } else if (var3 != null && (Double.isNaN(var3.t) || var12 - M(var11) > var3.m - var3.t)) {
                     var3.m = var12;
                     var3.t = M(var11);
                  }
               } else if (var3 != null) {
                  var3.J = Math.max(var3.J, var12);
               }
            }
         }

         return var9;
      }
   }

   private Optional<Vec3> f(EndCrystal var1, Vec3 var2) {
      return _p.V(var1, this.D(), var2 == null ? var1.getBoundingBox().getCenter() : var2, var2);
   }

   public static AABB y(BlockPos var0) {
      return EntityTypes.END_CRYSTAL.getDimensions().makeBoundingBox(Vec3.atBottomCenterOf(var0.above()));
   }

   public static List<Vec3> Y(BlockPos var0, Vec3 var1) {
      ArrayList var2 = new ArrayList(15);
      Vec3 var3 = Vec3.atCenterOf(var0);

      for (Direction var7 : FH) {
         int var8 = var7.getStepX();
         int var9 = var7.getStepY();
         int var10 = var7.getStepZ();
         if (!((var1.x - var3.x - var8 * 0.5) * var8 + (var1.y - var3.y - var9 * 0.5) * var9 + (var1.z - var3.z - var10 * 0.5) * var10 <= 0.0)) {
            for (double[] var14 : C) {
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
      this.R.H(null);
      if (!this.isInGame()) {
         this.f();
      } else {
         if (this.k != mc.player || this.x != mc.level) {
            this.f();
            this.k = mc.player;
            this.x = mc.level;
         }

         this.H();
         int var10000 = this.s.contains(":");
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         String var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? this.s.substring(0, this.s.indexOf(58)) : this.s;
         int var7 = var1.equals(this.Fc);
         boolean var3 = false;
         byte var2 = (byte)var7;
         var7 = Integer.signum(var2);
         if ((var7 | -var7) >>> 31 == 0) {
            this.Fc = var1;
            _q.c("AutoCrystal", "Status: {}", this.s);
         }
      }
   }

   private void H() {
      this.D.clear();
      if (!this.p()) {
         this.k();
         this.s = "Paused: held-use optimizer, screen, focus, camera, item use or weakness";
      } else {
         long var1 = mc.level.getGameTime();
         if (var1 < this.Q) {
            this.k();
            this.s = String.format(Locale.ROOT, "Paused after kill: %.1f s left", (this.Q - var1) / 20.0);
         } else {
            this.c
               .W(
                  var1,
                  var0 -> mc.level.getEntity(var0) != null,
                  var0 -> !mc.level
                     .getEntities((Entity)null, new AABB(var0.above()).expandTowards(0.0, 1.0, 0.0), var0x -> var0x instanceof EndCrystal)
                     .isEmpty()
               );
            if (!this.c.n() && !this.c.u()) {
               this.F = null;
            } else {
               this.R.H(this.F);
               if (this.p != null && this.l(this.p)) {
                  wp.S(this.p);
               }
            }

            if (this.F7 != null && var1 - this.F7.created() < this.B.getValue() && this.a(this.F7)) {
               this.j(this.F7, var1);
            } else {
               this.F7 = null;
               ArrayList var3 = new ArrayList();

               for (Entity var5 : mc.level.entitiesForRendering()) {
                  if (var5 instanceof LivingEntity var6 && this.l(var6)) {
                     var3.add(var6);
                  }
               }

               var3.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
               if (var3.size() > 8) {
                  var3 = new ArrayList(var3.subList(0, 8));
               }

               if (var3.isEmpty()) {
                  this.s = "No target (enable Mobs to test on hostile mobs)";
               } else {
                  if (this.T.getValue() && !this.k()) {
                     EndCrystal var14 = null;
                     AutoCrystalModule.l var16 = null;
                     Vec3 var18 = null;

                     for (Entity var8 : mc.level.entitiesForRendering()) {
                        if (var8 instanceof EndCrystal var9 && this.c.R(var1, var8.getId()) && !(_p.y(var8) > this.D())) {
                           AutoCrystalModule.l var10 = this.l(var8.position(), var3);
                           if (var10 != null && (var16 == null || !(var10.value() <= var16.value()))) {
                              Optional var11 = this.f(var9, null);
                              if (!var11.isEmpty()) {
                                 var14 = var9;
                                 var16 = var10;
                                 var18 = (Vec3)var11.get();
                              }
                           }
                        }
                     }

                     if (var14 != null) {
                        this.F7 = new AutoCrystalModule.i(null, var14, var16.target(), var1, var18, null);
                        this.j(this.F7, var1);
                        return;
                     }
                  }

                  if (this.c.u()) {
                     this.s = "Waiting for crystal removal";
                  } else if (!this.q.getValue()) {
                     this.s = "No suitable crystal to break";
                  } else if (this.c.n()) {
                     this.s = "Waiting for crystal spawn";
                  } else if (!this.c.P(var1)) {
                     this.s = "Placement cooldown";
                  } else if (this.A() == -1) {
                     this.s = "No crystals in hands or hotbar";
                  } else {
                     BlockHitResult var15 = null;
                     AutoCrystalModule.l var17 = null;
                     AutoCrystalModule.x var19 = new AutoCrystalModule.x();
                     int var20 = (int)Math.ceil(this.l());
                     BlockPos var21 = BlockPos.containing(mc.player.getEyePosition());

                     for (BlockPos var24 : BlockPos.betweenClosed(var21.offset(-var20, -var20, -var20), var21.offset(var20, var20, var20))) {
                        BlockPos var25 = var24.immutable();
                        BlockHitResult var12 = this.O(var25, null);
                        if (var12 != null) {
                           var19.h++;
                           AutoCrystalModule.l var13 = this.Z(Vec3.atBottomCenterOf(var25.above()), var3, var19, null, true, false);
                           if (var13 != null && (var17 == null || var13.value() > var17.value())) {
                              var15 = var12;
                              var17 = var13;
                           }
                        }
                     }

                     if (this.A.getValue() && (var17 == null || !var17.lethal() && !this.a.w("Damage"))) {
                        AutoCrystalModule.i var23 = this.m(var3, var17, var1, var19);
                        if (var23 != null) {
                           this.F7 = var23;
                           this.j(this.F7, var1);
                           return;
                        }

                        if (var15 == null) {
                           this.s = this.L;
                           return;
                        }
                     }

                     if (var15 == null) {
                        this.s = this.V(var19);
                     } else {
                        this.F7 = new AutoCrystalModule.i(var15.getBlockPos(), null, var17.target(), var1, var15.getLocation(), null);
                        this.j(this.F7, var1);
                     }
                  }
               }
            }
         }
      }
   }

   private AutoCrystalModule.i m(List<LivingEntity> var1, AutoCrystalModule.l var2, long var3, AutoCrystalModule.x var5) {
      boolean var6 = this.a.w("Lethal");
      double var7 = var2 == null ? Double.NaN : var2.damage();
      if (var3 < this.X) {
         this.L = "Waiting for a crystal on the new obsidian";
         return null;
      }

      if (vi.u(Items.OBSIDIAN, this.n.getValue()) == -1) {
         this.L = this.V(var5) + "; no obsidian in hands or hotbar";
         return null;
      }

      Vec3 var9 = mc.player.getEyePosition();
      double var10 = this.l();
      int var12 = (int)Math.ceil(var10);
      BlockPos var13 = BlockPos.containing(var9);
      ArrayList var14 = new ArrayList();

      for (BlockPos var16 : BlockPos.betweenClosed(var13.offset(-var12, -var12, -var12), var13.offset(var12, var12, var12))) {
         if (this.G(var16, var9, var10)) {
            Vec3 var17 = Vec3.atBottomCenterOf(var16.above());
            double var18 = Double.MAX_VALUE;

            for (LivingEntity var21 : var1) {
               double var22 = var21.position().add(this.u(var21)).distanceTo(var17);
               double var24 = var6 ? M(var21) : this.C(var21);
               if (var2 != null && this.a.w("Best")) {
                  var24 = Math.max(var24, Math.min(M(var21), var7 + this.Fw.getValue()));
               }

               if (com.larpclient.t.C(var22, 1.0) * 1.5 >= var24) {
                  var18 = Math.min(var18, var22);
               }
            }

            if (var18 < Double.MAX_VALUE) {
               var14.add(new AutoCrystalModule.k(var16.immutable(), var18));
            }
         }
      }

      var14.sort(Comparator.comparingDouble(AutoCrystalModule.k::distance));
      AutoCrystalModule.x var26 = new AutoCrystalModule.x();
      AutoCrystalModule.l var27 = null;
      BlockHitResult var28 = null;
      BlockPos var29 = null;
      int var19 = 0;

      for (AutoCrystalModule.k var31 : var14.subList(0, Math.min(96, var14.size()))) {
         if (var19 >= 24) {
            break;
         }

         BlockPos var32 = var31.pos();
         if (this.v(var32) && this.n(var32, var9, var10)) {
            BlockHitResult var23 = vi.X(var32, Items.OBSIDIAN, var10, true, this.n.getValue());
            if (var23 != null) {
               var26.h++;
               var19++;
               AutoCrystalModule.l var33 = this.Z(Vec3.atBottomCenterOf(var32.above()), var1, var26, var32, true, var6);
               if (var33 != null
                  && q(this.a.getValue(), var7, false, var33.damage(), var33.lethal(), this.Fw.getValue())
                  && (var27 == null || !(var33.value() <= var27.value()))) {
                  var27 = var33;
                  var28 = var23;
                  var29 = var32;
               }
            }
         }
      }

      if (var27 == null) {
         this.L = this.F(var26, var6);
         return null;
      } else {
         return new AutoCrystalModule.i(var29, null, var27.target(), var3, var28.getLocation(), var28);
      }
   }

   private boolean G(BlockPos var1, Vec3 var2, double var3) {
      if (!(var2.distanceToSqr(Vec3.atCenterOf(var1)) > (var3 + 1.0) * (var3 + 1.0)) && vi.i(var1) && mc.level.isEmptyBlock(var1.above())) {
         if (this.T.getValue() && jz.j(var2, y(var1)) > this.D()) {
            return false;
         }

         for (Direction var8 : Direction.values()) {
            if (vi.k(var1.relative(var8))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean v(BlockPos var1) {
      return mc.level.getEntities((Entity)null, new AABB(var1).expandTowards(0.0, 2.0, 0.0)).isEmpty();
   }

   private boolean n(BlockPos var1, Vec3 var2, double var3) {
      for (Vec3 var6 : Y(var1, var2)) {
         if (!(var2.distanceToSqr(var6) > var3 * var3)
            && mc.level.clip(new ClipContext(var2, var6, Block.OUTLINE, Fluid.NONE, mc.player)).getType() == Type.MISS) {
            return true;
         }
      }

      return false;
   }

   private BlockHitResult t(BlockHitResult var1, BlockPos var2, Vec3 var3) {
      BlockHitResult var4 = vi.W(var1.getBlockPos(), var3, this.l());
      if (var4 != null && var4.getDirection() == var1.getDirection() && vi.k(var4.getBlockPos())) {
         vi.a var5 = vi.U(var4, Items.OBSIDIAN, this.n.getValue());
         return var5 != null && var5.target().equals(var2) ? var4 : null;
      } else {
         return null;
      }
   }

   private String F(AutoCrystalModule.x var1, boolean var2) {
      if (var1.h == 0) {
         return "No reachable obsidian or bedrock base, or open spot to place one";
      } else {
         return var2 && !Double.isNaN(var1.t) && var1.I < var1.h
            ? String.format(Locale.ROOT, "No lethal obsidian spot: best %.1f HP against %.1f HP health", var1.m, var1.t)
            : this.V(var1);
      }
   }

   public void M(ClientboundEntityEventPacket var1) {
      if (this.isEnabled()) {
         if (var1.getEventId() == 3 && this.FI.getValue() && this.isRuntimeActive() && this.isInGame()) {
            if (var1.getEntity(mc.level) instanceof LivingEntity var2 && this.b(var2)) {
               this.Q = mc.level.getGameTime() + Math.round(this.M.getValue() * 20.0);
               this.c.W();
               this.k();
               this.s();
               this.F = null;
               this.p = null;
               this.s = String.format(Locale.ROOT, "Paused after kill: %.1f s left", this.M.getValue());
               _q.z(
                  "AutoCrystal",
                  "{} died nearby; placing and breaking paused for {} s to protect drops",
                  var2.getName().getString(),
                  this.M.getValue().intValue()
               );
            }
         }
      }
   }

   private String V(AutoCrystalModule.x var1) {
      if (var1.h == 0) {
         return "No reachable obsidian or bedrock base";
      } else {
         return var1.I == var1.h
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var1.Y, this.F2.getValue())
            : String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var1.J, this.w.getValue());
      }
   }

   private void k() {
      this.F7 = null;
      this.v = null;
      this.N = null;
      this.Fn.J();
      _p.e(this);
   }

   private double l() {
      return Math.min(this.u.getValue(), Math.min(this.h.getValue(), mc.player.blockInteractionRange()));
   }

   private double D() {
      return Math.min(this.u.getValue(), Math.min(this.FE.getValue(), mc.player.entityInteractionRange()));
   }

   private boolean k() {
      return mc.player.hasEffect(MobEffects.WEAKNESS) && mc.player.getAttributeValue(Attributes.ATTACK_DAMAGE) <= 0.0;
   }

   private boolean a(AutoCrystalModule.i var1) {
      if (this.p() && this.l(var1.victim())) {
         long var2 = mc.level.getGameTime();
         if (var1.crystal() != null) {
            return this.T.getValue()
               && !this.k()
               && this.c.R(var2, var1.crystal().getId())
               && this.f(var1.crystal(), var1.aim()).isPresent()
               && this.l(var1.crystal().position(), List.of(var1.victim())) != null;
         } else if (var1.support() != null) {
            Vec3 var4 = mc.player.getEyePosition();
            return this.q.getValue()
               && this.A.getValue()
               && this.c.P(var2)
               && var2 >= this.X
               && this.A() != -1
               && vi.u(Items.OBSIDIAN, this.n.getValue()) != -1
               && this.G(var1.base(), var4, this.l())
               && this.v(var1.base())
               && this.n(var1.base(), var4, this.l())
               && this.t(var1.support(), var1.base(), var1.aim().subtract(var4)) != null
               && this.Z(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, var1.base(), true, this.a.w("Lethal")) != null;
         } else {
            return this.q.getValue()
               && this.c.P(var2)
               && this.A() != -1
               && this.O(var1.base(), var1.aim()) != null
               && this.Z(Vec3.atBottomCenterOf(var1.base().above()), List.of(var1.victim()), null, null, true, false) != null;
         }
      } else {
         return false;
      }
   }

   private boolean r(AutoCrystalModule.i var1) {
      if (var1.support() != null) {
         return this.t(var1.support(), var1.base(), mc.player.getLookAngle()) != null;
      } else {
         return var1.crystal() != null ? _p.f(var1.crystal(), this.D()) : _p.A(var1.base(), this.l()) != null;
      }
   }

   public void z() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.F7 != null && this.P == mc.level.getGameTime()) {
            if (_p.Q(this) && this.a(this.F7)) {
               if (this.FB.w("Visible")) {
                  if (!ij.a(ij.r.p)) {
                     this.Fn.J();
                     return;
                  }

                  ij.V(ij.r.p);
                  this.Fp = mc.player;
                  this.e = mc.level;
                  this.FR = mc.player.tickCount;
                  float[] var1 = b6.R(mc.player.getEyePosition(), this.F7.aim());
                  float[] var2 = nx.M(mc.player.getYRot(), mc.player.getXRot(), var1[0], var1[1], this.K.getValue());
                  b6.f(b6.w.W, var2[0], var2[1], 1.0);
                  b6.e();
               }

               this.Fn.f(mc.player.tickCount, this.r(this.F7));
               this.s = this.Fn.C(mc.player.tickCount, this.m.getValue().intValue())
                  ? "Aim ready"
                  : (
                     !this.FB.w("Visible")
                        ? "Waiting for your crosshair"
                        : (this.F7.support() != null ? "Aiming at obsidian spot" : "Aiming at crystal action")
                  );
            } else {
               this.Fn.J();
            }
         }
      }
   }

   private boolean D(AutoCrystalModule.i var1) {
      return this.F7 == var1
         && this.P == mc.level.getGameTime()
         && this.a(var1)
         && this.Fn.C(mc.player.tickCount, this.m.getValue().intValue())
         && this.r(var1);
   }

   private void j(AutoCrystalModule.i var1, long var2) {
      BlockPos var4 = var1.base() != null ? var1.base() : var1.crystal().blockPosition().below();
      this.R.H(var4);
      wp.S(var1.victim());
      this.p = var1.victim();
      this.P = var2;
      Object var5 = var1.support() != null ? new AutoCrystalModule.z(var1.base()) : (var1.crystal() == null ? var1.base() : var1.crystal());
      if (!Objects.equals(this.v, var5) || this.N != var1.victim()) {
         this.Fn.J();
      }

      this.v = var5;
      this.N = var1.victim();
      if (var1.crystal() != null) {
         _p.U(this, var1.crystal(), this.D(), false, () -> this.D(var1), 1, () -> {
            this.c.s(mc.level.getGameTime(), var1.crystal().getId(), this.F3.getValue().intValue(), this.S.getValue().intValue());
            this.F = var4;
            this.s = "Waiting for crystal removal";
            this.k();
         }, var2x -> {
            if (this.D(var1)) {
               this.s = "Waiting: " + var2x;
            }
         });
      } else if (var1.support() != null) {
         _p.w(this, () -> this.E(var1), () -> this.D(var1));
      } else {
         _p.w(this, () -> this.d(var1.base()), () -> this.D(var1));
      }
   }

   private void E(AutoCrystalModule.i var1) {
      BlockHitResult var2 = this.t(var1.support(), var1.base(), mc.player.getLookAngle());
      int var3 = vi.u(Items.OBSIDIAN, this.n.getValue());
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
               h8.i(mc.player, var6);
               this.c.i(var8, this.g.getValue().intValue());
               this.X = var8 + this.S.getValue().intValue();
               this.s = "Placed obsidian; crystal next";
               _q.c("AutoCrystal", "Obsidian base at {} for {} ({} mode)", var1.base().toShortString(), var1.victim().getName().getString(), this.a.getValue());
            } else {
               this.c.i(var8, this.O.getValue().intValue());
               this.s = "Obsidian placement rejected; retrying";
            }

            this.k();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private int A() {
      if (mc.player.getOffhandItem().is(Items.END_CRYSTAL)) {
         return -2;
      }

      int var1 = mc.player.getInventory().getSelectedSlot();
      if (mc.player.getMainHandItem().is(Items.END_CRYSTAL)) {
         return var1;
      }

      if (this.n.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            if (mc.player.getInventory().getItem(var2).is(Items.END_CRYSTAL)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private BlockHitResult O(BlockPos var1, Vec3 var2) {
      if (mc.level.hasChunkAt(var1) && mc.level.getWorldBorder().isWithinBounds(var1)) {
         BlockState var3 = mc.level.getBlockState(var1);
         if ((var3.is(Blocks.OBSIDIAN) || var3.is(Blocks.BEDROCK))
            && mc.level.isEmptyBlock(var1.above())
            && mc.level.getEntities(null, new AABB(var1.above()).expandTowards(0.0, 1.0, 0.0)).isEmpty()) {
            Vec3 var4 = mc.player.getEyePosition();
            double var5 = this.l();
            if (var4.distanceToSqr(Vec3.atCenterOf(var1)) > (var5 + 1.0) * (var5 + 1.0)) {
               return null;
            }

            if (this.T.getValue() && jz.j(var4, y(var1)) > this.D()) {
               return null;
            }

            if (var2 != null) {
               BlockHitResult var7 = _p.X(var1, var5, var2.subtract(var4));
               if (var7 != null) {
                  return var7;
               }
            }

            for (Vec3 var8 : Y(var1, var4)) {
               BlockHitResult var9 = _p.X(var1, var5, var8.subtract(var4));
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

   private void d(BlockPos var1) {
      BlockHitResult var2 = _p.A(var1, this.l());
      int var3 = this.A();
      if (var2 != null && var3 != -1 && this.O(var1, var2.getLocation()) != null) {
         int var4 = mc.player.getInventory().getSelectedSlot();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var3);
            }

            InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            InteractionResult var7 = mc.gameMode.useItemOn(mc.player, var6, var2);
            this.c.b(mc.level.getGameTime(), var1, this.O.getValue().intValue(), var7.consumesAction(), this.S.getValue().intValue());
            if (var7.consumesAction()) {
               h8.i(mc.player, var6);
               this.F = var1;
               this.X = 0L;
               this.s = "Waiting for crystal spawn";
            } else {
               this.s = "Placement rejected; retrying";
            }

            this.k();
         } finally {
            if (var5 != 0) {
               mc.player.getInventory().setSelectedSlot(var4);
            }
         }
      }
   }

   private record i(BlockPos base, EndCrystal crystal, LivingEntity victim, long created, Vec3 aim, BlockHitResult support) {
   }

   private record k(BlockPos pos, double distance) {
   }

   private record l(LivingEntity target, double damage, double self, boolean lethal) {
      double value() {
         return this.lethal ? 1000.0 - this.self : this.damage - this.self * 0.25;
      }
   }

   private static final class x {
      int h;
      int I;
      double Y = Double.MAX_VALUE;
      double J;
      double m;
      double t = Double.NaN;
   }

   private record z(BlockPos pos) {
   }
}
