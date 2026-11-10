package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.h3;
import com.larpclient.hl;
import com.larpclient.hw;
import com.larpclient.q8;
import com.larpclient.t9;
import com.larpclient.ug;
import com.larpclient.uu;
import com.larpclient.w6;
import com.larpclient.wc;
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
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1511;
import net.minecraft.class_1569;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2663;
import net.minecraft.class_2680;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class AutoCrystalModule extends Module implements uu.m {
   private final uu A;
   private class_746 J;
   private class_638 Z;
   private final lo Or = this.s("Range", "Maximum crystal interaction distance", 4.5, 2.0, 6.0, 0.5);
   private final lo OL = this.s("Target Range", "Search distance for opponents", 10.0, 2.0, 16.0, 1.0);
   private final lo Q = this.s("Break Delay", "Ticks between executed attacks", 2.0, 0.0, 10.0, 1.0);
   private final lo o = this.s("Place Delay", "Ticks between placement attempts", 2.0, 0.0, 10.0, 1.0);
   private final lo Om = this.s("Min Damage", "Minimum estimated target damage in HP; a hit that kills or pops always qualifies", 4.0, 0.0, 20.0, 0.5);
   private final lo T = this.s(
      "Face Place Health", "Once a target's health and absorption fall to this, crystals down to 1.5 HP qualify; 0 turns it off", 8.0, 0.0, 36.0, 0.5
   );
   private final lo L = this.s(
      "Predict Ticks", "Score new crystals and obsidian against where a moving target will be after this many ticks", 2.0, 0.0, 10.0, 1.0
   );
   private final lo OT = this.s("Max Self Damage", "Maximum estimated self damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final l3 k = this.t("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final l3 r = this.t("Players", "Target other survival players", true);
   private final l3 U = this.t("Mobs", "Also target hostile mobs for combat or testing", false);
   private final l3 O2 = this.t("Auto Switch", "Use hotbar crystals and restore the selected slot", true);
   private final l3 X = this.t("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final l3 P = this.t("Place", "Place crystals on existing obsidian or bedrock", true);
   private final l3 O1 = this.t("Place Obsidian", "Place an obsidian base first when no existing base is good enough", true);
   private final li H = this.addSetting(
      new li(
         "Obsidian Mode",
         "Best places obsidian when no base qualifies, or when a new one kills or adds Obsidian Gain; Lethal only where its crystal kills or pops; Damage only when no existing base qualifies",
         "Best",
         "Best",
         "Lethal",
         "Damage"
      )
   );
   private final lo v = this.s("Obsidian Gain", "Extra target damage in HP a new obsidian base must add over the best existing base", 3.0, 0.0, 20.0, 0.5);
   private final lo On = this.s("Obsidian Delay", "Ticks after placing obsidian before the crystal goes on it", 1.0, 0.0, 10.0, 1.0);
   private final l3 B = this.t("Break", "Attack crystals that can damage a target", true);
   private final li w = this.addSetting(
      new li("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final lo C = this.s("Turn Speed", "Maximum visible turn in degrees per tick", 45.0, 5.0, 180.0, 5.0);
   private final lo b = this.s("Aim Hold", "Consecutive aligned ticks before acting", 1.0, 1.0, 5.0, 1.0);
   private final lo F = this.s("Place Range", "Placement reach, capped by Range and native block reach", 4.5, 2.0, 6.0, 0.5);
   private final lo Oy = this.s("Break Range", "Attack reach, capped by Range and native entity reach", 3.0, 2.0, 6.0, 0.5);
   private final lo d = this.s("Scan Interval", "Ticks to retain a valid selected action before rescoring", 2.0, 1.0, 10.0, 1.0);
   private final lo Oe = this.s("Response Timeout", "Ticks to wait for server crystal spawn or removal before retrying", 10.0, 2.0, 40.0, 1.0);
   private final l3 Y = this.t("Pause Weakness", "Pause placement too when weakness prevents crystal attacks", true);
   private final l3 R = this.t("Pause On Kill", "Stop placing and breaking after a nearby opponent dies, so explosions spare their drops", false);
   private final lo h = this.s("Kill Pause", "Seconds to stay paused after a kill", 10.0, 1.0, 60.0, 1.0);
   private final l3 OH = this.t("Work with Crystal", "Start while holding an end crystal", true);
   private final l3 i = this.t("Work with Sword", "Start while holding a sword", true);
   private final l3 K = this.t("Work with Totem", "Start while holding a totem", true);
   private final l3 Oz = this.t("Work with Fist", "Start with an empty main hand", true);
   private final lo y = this.a("Switch Delay Min", 1.0);
   private final lo M = this.a("Switch Delay Max", 50.0);
   private final lo q = this.a("Place Delay Min", 50.0);
   private final lo OY = this.a("Place Delay Max", 100.0);
   private final lo a = this.a("Break Delay Min", 50.0);
   private final lo I = this.a("Break Delay Max", 100.0);
   private final l3 e = this.t("Swap Back", "Restore your starting slot after releasing use", true);
   private final lo V = this.a("Swap Back Delay Min", 50.0);
   private final lo O_ = this.a("Swap Back Delay Max", 100.0);
   private static final double[][] W = new double[][]{{0.5, 0.5}, {0.15, 0.15}, {0.15, 0.85}, {0.85, 0.15}, {0.85, 0.85}};
   private static final class_2350[] N = new class_2350[]{
      class_2350.field_11036, class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034, class_2350.field_11033
   };
   private static final int s = 24;
   private static final int G = 96;
   public static final double l = 1.5;
   private AutoCrystalModule.a S;
   private long OR;
   private String c = "";
   private final Map<class_1309, class_243> E = new IdentityHashMap<>();
   private class_2338 D;
   private class_1309 m;
   private Object p;
   private Object g;
   private int j;
   private String Of = "";
   private Object z;
   private class_1309 n;
   private long OG;
   private long f;
   private final hl O = new hl();
   private static AutoCrystalModule x;
   private final h3 t = new h3();
   private String u = "Waiting for a target";

   @Override
   public uu placementHighlight() {
      return this.A;
   }

   public static AutoCrystalModule l() {
      return x;
   }

   public List<lb<?>> s() {
      return List.of(this.P, this.B, this.Or, this.F, this.Oy, this.o, this.Q, this.Oe, this.d, this.O2);
   }

   public List<lb<?>> r() {
      return List.of(this.O1, this.H, this.v, this.On);
   }

   public List<lb<?>> f() {
      return List.of(this.r, this.U, this.OL, this.L);
   }

   public List<lb<?>> b() {
      return List.of(this.w, this.C, this.b);
   }

   public List<lb<?>> m() {
      return List.of(this.Om, this.T, this.k, this.OT, this.X, this.Y, this.R, this.h);
   }

   public AutoCrystalModule() {
      super("AutoCrystal", "Automatically place and break end crystals", Category.COMBAT, -1);
      x = this;
      this.C.O(() -> this.w.G("Visible"));
      this.F.O(this.P::getValue);
      this.o.O(this.P::getValue);
      this.O1.O(this.P::getValue);
      this.H.O(() -> this.P.getValue() && this.O1.getValue());
      this.v.O(() -> this.P.getValue() && this.O1.getValue() && this.H.G("Best"));
      this.On.O(() -> this.P.getValue() && this.O1.getValue());
      this.Oy.O(this.B::getValue);
      this.Q.O(this.B::getValue);
      this.OT.O(this.k::getValue);
      this.h.O(this.R::getValue);
      this.R.a(var1 -> {
         if (!var1) {
            this.f = 0L;
         }
      });
      this.w.a(var1 -> {
         this.x();
         this.R();
      });

      for (lb var2 : List.of(this.OH, this.i, this.K, this.Oz, this.y, this.M, this.q, this.OY, this.a, this.I, this.e, this.V, this.O_)) {
         var2.O(() -> false);
      }

      AutoCrystalModule var3 = this;
      this.A = new uu(this, var1 -> var3.addSetting(var1), 0.5, 0.3);
   }

   private lo s(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   private l3 t(String var1, String var2, boolean var3) {
      return this.addSetting(new l3(var1, var2, var3));
   }

   private lo a(String var1, double var2) {
      return this.s(var1, "Random delay endpoint in milliseconds; reversed endpoints are ordered automatically", var2, 0.0, 500.0, 1.0);
   }

   public static long q(double var0, double var2, double var4) {
      long var6 = Math.round(Math.min(var0, var2));
      long var8 = Math.round(Math.max(var0, var2));
      return var6 + Math.min(var8 - var6, (long)(Math.clamp(var4, 0.0, 1.0) * (var8 - var6 + 1L)));
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.u : "";
   }

   @Override
   public void onEnable() {
      this.A();
      this.A.d();
      q8.G(
         "AutoCrystal",
         "Autonomous target selection enabled; native reach, damage limits and final look-ray validation; obsidian bases {}",
         this.O1.getValue() ? this.H.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.A();
      q8.G("AutoCrystal", "Autonomous actions cancelled; target and server-response waits cleared");
   }

   private void A() {
      this.t.N();
      this.x();
      this.R();
      this.A.p();
      this.J = null;
      this.Z = null;
      this.D = null;
      this.m = null;
      this.f = 0L;
      this.OR = 0L;
      this.E.clear();
      this.u = "Waiting for a target";
   }

   private boolean r() {
      return this.isRuntimeActive()
         && this.isInGame()
         && cr.f()
         && mc.method_1569()
         && this.m()
         && (!this.X.getValue() || !mc.field_1724.method_6115())
         && (!this.Y.getValue() || !this.a());
   }

   private boolean m() {
      wp.z var1 = wp.o();
      return this.w.G("Visible") ? wp.h(wp.z.h) : var1 == wp.z.D;
   }

   private void R() {
      this.p = this.g = null;
      wp.k(wp.z.h);
   }

   public boolean q() {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && this.w.G("Visible")
         && mc.field_1724 != null
         && this.p == mc.field_1724
         && this.g == mc.field_1687
         && mc.field_1724.field_6012 >= this.j
         && (!this.X.getValue() || !mc.field_1724.method_6115())) {
         int var1 = mc.field_1724.field_6012 - this.j;
         return var1 <= 1 || (this.t.q() || this.t.z()) && var1 <= this.Oe.getValue();
      } else {
         return false;
      }
   }

   public float R(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && wp.o() == wp.z.h) {
            mc.field_1724.method_5847(mc.field_1724.method_36454());
            return mc.field_1724.method_36454();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean C(class_1309 var1) {
      return var1.method_5805() && !var1.method_31481() && this.q(var1);
   }

   private boolean q(class_1309 var1) {
      if (var1 == mc.field_1724
         || var1.method_7325()
         || var1.method_73183() != mc.field_1687
         || mc.field_1724.method_5739(var1) > this.OL.getValue()
         || mc.field_1724.method_5722(var1)) {
         return false;
      }

      if (var1 instanceof class_1657 var2) {
         if (this.r.getValue() && !var2.method_31549().field_7480) {
            MCFriendModule var3 = LarpClient.b().G().b(MCFriendModule.class);
            return var3 == null || !var3.M(var2.method_5477().getString());
         } else {
            return false;
         }
      } else {
         return this.U.getValue() && var1 instanceof class_1569;
      }
   }

   public static double e(double var0, double var2, double var4) {
      double var6 = var4 > 0.0 && var0 <= var4 ? Math.min(var2, 1.5) : var2;
      return Math.min(var6, var0);
   }

   public static boolean y(String var0, double var1, boolean var3, double var4, boolean var6, double var7) {
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

   private AutoCrystalModule.f b(class_243 var1, List<class_1309> var2) {
      return this.y(var1, var2, null, null, false, false);
   }

   private static double z(class_1309 var0) {
      return var0.method_6032() + var0.method_6067();
   }

   private double H(class_1309 var1) {
      return e(z(var1), this.Om.getValue(), this.T.getValue());
   }

   private class_243 E(class_1309 var1) {
      int var2 = this.L.getValue().intValue();
      return var2 <= 0 ? class_243.field_1353 : this.E.computeIfAbsent(var1, var1x -> {
         class_243 var2x = new class_243(var1x.method_23317() - var1x.field_6014, 0.0, var1x.method_23321() - var1x.field_5969);
         if (!(var2x.method_1027() < 1.0E-6) && !(var2x.method_1027() > 1.0)) {
            class_243 var3 = var2x.method_1021(var2);
            return mc.field_1687.method_8587(var1x, var1x.method_5829().method_997(var3)) ? var3 : class_243.field_1353;
         } else {
            return class_243.field_1353;
         }
      });
   }

   private AutoCrystalModule.f y(class_243 var1, List<class_1309> var2, AutoCrystalModule.m var3, class_2338 var4, boolean var5, boolean var6) {
      double var7 = hw.G(mc.field_1724, var1, var4);
      if (this.k.getValue() && !hw.t(var7, this.OT.getValue(), mc.field_1724.method_6032() + mc.field_1724.method_6067())) {
         if (var3 != null) {
            var3.P++;
            var3.m = Math.min(var3.m, var7);
         }

         return null;
      } else {
         AutoCrystalModule.f var9 = null;

         for (class_1309 var11 : var2) {
            if (this.C(var11)) {
               double var12 = hw.r(var11, var1, var4, var5 ? this.E(var11) : class_243.field_1353);
               if (!(var12 <= 0.0) && !(var12 < this.H(var11))) {
                  if (!var6 || !(var12 < z(var11))) {
                     AutoCrystalModule.f var14 = new AutoCrystalModule.f(var11, var12, var7, var12 >= z(var11));
                     if (var9 == null || var14.value() > var9.value()) {
                        var9 = var14;
                     }
                  } else if (var3 != null && (Double.isNaN(var3.q) || var12 - z(var11) > var3.p - var3.q)) {
                     var3.p = var12;
                     var3.q = z(var11);
                  }
               } else if (var3 != null) {
                  var3.g = Math.max(var3.g, var12);
               }
            }
         }

         return var9;
      }
   }

   private Optional<class_243> c(class_1511 var1, class_243 var2) {
      return cr.D(var1, this.r(), var2 == null ? var1.method_5829().method_1005() : var2, var2);
   }

   public static class_238 b(class_2338 var0) {
      return class_1299.field_6110.method_18386().method_30757(class_243.method_24955(var0.method_10084()));
   }

   public static List<class_243> g(class_2338 var0, class_243 var1) {
      ArrayList var2 = new ArrayList(15);
      class_243 var3 = class_243.method_24953(var0);

      for (class_2350 var7 : N) {
         int var8 = var7.method_10148();
         int var9 = var7.method_10164();
         int var10 = var7.method_10165();
         if (!(
            (var1.field_1352 - var3.field_1352 - var8 * 0.5) * var8
                  + (var1.field_1351 - var3.field_1351 - var9 * 0.5) * var9
                  + (var1.field_1350 - var3.field_1350 - var10 * 0.5) * var10
               <= 0.0
         )) {
            for (double[] var14 : W) {
               double var15 = var14[0];
               double var17 = var14[1];

               var2.add(switch (var7.method_10166()) {
                  case field_11052 -> new class_243(var0.method_10263() + var15, var0.method_10264() + (var9 > 0 ? 1 : 0), var0.method_10260() + var17);
                  case field_11048 -> new class_243(var0.method_10263() + (var8 > 0 ? 1 : 0), var0.method_10264() + var15, var0.method_10260() + var17);
                  case field_11051 -> new class_243(var0.method_10263() + var15, var0.method_10264() + var17, var0.method_10260() + (var10 > 0 ? 1 : 0));
                  default -> throw new MatchException(null, null);
               });
            }
         }
      }

      return var2;
   }

   @Override
   public void onTick() {
      this.A.x(null);
      if (!this.isInGame()) {
         this.A();
      } else {
         if (this.J != mc.field_1724 || this.Z != mc.field_1687) {
            this.A();
            this.J = mc.field_1724;
            this.Z = mc.field_1687;
         }

         this.c();
         int var10000 = this.u.contains(":");
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         String var1 = ((var10000 | -var10000) >>> 31 ^ 1) == 0 ? this.u.substring(0, this.u.indexOf(58)) : this.u;
         int var7 = var1.equals(this.Of);
         boolean var3 = false;
         byte var2 = (byte)var7;
         var7 = Integer.signum(var2);
         if ((var7 | -var7) >>> 31 == 0) {
            this.Of = var1;
            q8.v("AutoCrystal", "Status: {}", this.u);
         }
      }
   }

   private void c() {
      this.E.clear();
      if (!this.r()) {
         this.x();
         this.u = "Paused: held-use optimizer, screen, focus, camera, item use or weakness";
      } else {
         long var1 = mc.field_1687.method_75260();
         if (var1 < this.f) {
            this.x();
            this.u = String.format(Locale.ROOT, "Paused after kill: %.1f s left", (this.f - var1) / 20.0);
         } else {
            this.t
               .q(
                  var1,
                  var0 -> mc.field_1687.method_8469(var0) != null,
                  var0 -> !mc.field_1687
                     .method_8333((class_1297)null, new class_238(var0.method_10084()).method_1012(0.0, 1.0, 0.0), var0x -> var0x instanceof class_1511)
                     .isEmpty()
               );
            if (!this.t.q() && !this.t.z()) {
               this.D = null;
            } else {
               this.A.x(this.D);
               if (this.m != null && this.C(this.m)) {
                  wc.t(this.m);
               }
            }

            if (this.S != null && var1 - this.S.created() < this.d.getValue() && this.E(this.S)) {
               this.a(this.S, var1);
            } else {
               this.S = null;
               ArrayList var3 = new ArrayList();

               for (class_1297 var5 : mc.field_1687.method_18112()) {
                  if (var5 instanceof class_1309 var6 && this.C(var6)) {
                     var3.add(var6);
                  }
               }

               var3.sort(Comparator.comparingDouble(mc.field_1724::method_5858));
               if (var3.size() > 8) {
                  var3 = new ArrayList(var3.subList(0, 8));
               }

               if (var3.isEmpty()) {
                  this.u = "No target (enable Mobs to test on hostile mobs)";
               } else {
                  if (this.B.getValue() && !this.a()) {
                     class_1511 var14 = null;
                     AutoCrystalModule.f var16 = null;
                     class_243 var18 = null;

                     for (class_1297 var8 : mc.field_1687.method_18112()) {
                        if (var8 instanceof class_1511 var9 && this.t.T(var1, var8.method_5628()) && !(cr.M(var8) > this.r())) {
                           AutoCrystalModule.f var10 = this.b(var8.method_73189(), var3);
                           if (var10 != null && (var16 == null || !(var10.value() <= var16.value()))) {
                              Optional var11 = this.c(var9, null);
                              if (!var11.isEmpty()) {
                                 var14 = var9;
                                 var16 = var10;
                                 var18 = (class_243)var11.get();
                              }
                           }
                        }
                     }

                     if (var14 != null) {
                        this.S = new AutoCrystalModule.a(null, var14, var16.target(), var1, var18, null);
                        this.a(this.S, var1);
                        return;
                     }
                  }

                  if (this.t.z()) {
                     this.u = "Waiting for crystal removal";
                  } else if (!this.P.getValue()) {
                     this.u = "No suitable crystal to break";
                  } else if (this.t.q()) {
                     this.u = "Waiting for crystal spawn";
                  } else if (!this.t.f(var1)) {
                     this.u = "Placement cooldown";
                  } else if (this.b() == -1) {
                     this.u = "No crystals in hands or hotbar";
                  } else {
                     class_3965 var15 = null;
                     AutoCrystalModule.f var17 = null;
                     AutoCrystalModule.m var19 = new AutoCrystalModule.m();
                     int var20 = (int)Math.ceil(this.N());
                     class_2338 var21 = class_2338.method_49638(mc.field_1724.method_33571());

                     for (class_2338 var24 : class_2338.method_10097(var21.method_10069(-var20, -var20, -var20), var21.method_10069(var20, var20, var20))) {
                        class_2338 var25 = var24.method_10062();
                        class_3965 var12 = this.S(var25, null);
                        if (var12 != null) {
                           var19.U++;
                           AutoCrystalModule.f var13 = this.y(class_243.method_24955(var25.method_10084()), var3, var19, null, true, false);
                           if (var13 != null && (var17 == null || var13.value() > var17.value())) {
                              var15 = var12;
                              var17 = var13;
                           }
                        }
                     }

                     if (this.O1.getValue() && (var17 == null || !var17.lethal() && !this.H.G("Damage"))) {
                        AutoCrystalModule.a var23 = this.u(var3, var17, var1, var19);
                        if (var23 != null) {
                           this.S = var23;
                           this.a(this.S, var1);
                           return;
                        }

                        if (var15 == null) {
                           this.u = this.c;
                           return;
                        }
                     }

                     if (var15 == null) {
                        this.u = this.j(var19);
                     } else {
                        this.S = new AutoCrystalModule.a(var15.method_17777(), null, var17.target(), var1, var15.method_17784(), null);
                        this.a(this.S, var1);
                     }
                  }
               }
            }
         }
      }
   }

   private AutoCrystalModule.a u(List<class_1309> var1, AutoCrystalModule.f var2, long var3, AutoCrystalModule.m var5) {
      boolean var6 = this.H.G("Lethal");
      double var7 = var2 == null ? Double.NaN : var2.damage();
      if (var3 < this.OR) {
         this.c = "Waiting for a crystal on the new obsidian";
         return null;
      }

      if (fk.T(class_1802.field_8281, this.O2.getValue()) == -1) {
         this.c = this.j(var5) + "; no obsidian in hands or hotbar";
         return null;
      }

      class_243 var9 = mc.field_1724.method_33571();
      double var10 = this.N();
      int var12 = (int)Math.ceil(var10);
      class_2338 var13 = class_2338.method_49638(var9);
      ArrayList var14 = new ArrayList();

      for (class_2338 var16 : class_2338.method_10097(var13.method_10069(-var12, -var12, -var12), var13.method_10069(var12, var12, var12))) {
         if (this.H(var16, var9, var10)) {
            class_243 var17 = class_243.method_24955(var16.method_10084());
            double var18 = Double.MAX_VALUE;

            for (class_1309 var21 : var1) {
               double var22 = var21.method_73189().method_1019(this.E(var21)).method_1022(var17);
               double var24 = var6 ? z(var21) : this.H(var21);
               if (var2 != null && this.H.G("Best")) {
                  var24 = Math.max(var24, Math.min(z(var21), var7 + this.v.getValue()));
               }

               if (hw.y(var22, 1.0) * 1.5 >= var24) {
                  var18 = Math.min(var18, var22);
               }
            }

            if (var18 < Double.MAX_VALUE) {
               var14.add(new AutoCrystalModule.b(var16.method_10062(), var18));
            }
         }
      }

      var14.sort(Comparator.comparingDouble(AutoCrystalModule.b::distance));
      AutoCrystalModule.m var26 = new AutoCrystalModule.m();
      AutoCrystalModule.f var27 = null;
      class_3965 var28 = null;
      class_2338 var29 = null;
      int var19 = 0;

      for (AutoCrystalModule.b var31 : var14.subList(0, Math.min(96, var14.size()))) {
         if (var19 >= 24) {
            break;
         }

         class_2338 var32 = var31.pos();
         if (this.N(var32) && this.n(var32, var9, var10)) {
            class_3965 var23 = fk.M(var32, class_1802.field_8281, var10, true, this.O2.getValue());
            if (var23 != null) {
               var26.U++;
               var19++;
               AutoCrystalModule.f var33 = this.y(class_243.method_24955(var32.method_10084()), var1, var26, var32, true, var6);
               if (var33 != null
                  && y(this.H.getValue(), var7, false, var33.damage(), var33.lethal(), this.v.getValue())
                  && (var27 == null || !(var33.value() <= var27.value()))) {
                  var27 = var33;
                  var28 = var23;
                  var29 = var32;
               }
            }
         }
      }

      if (var27 == null) {
         this.c = this.Z(var26, var6);
         return null;
      } else {
         return new AutoCrystalModule.a(var29, null, var27.target(), var3, var28.method_17784(), var28);
      }
   }

   private boolean H(class_2338 var1, class_243 var2, double var3) {
      if (!(var2.method_1025(class_243.method_24953(var1)) > (var3 + 1.0) * (var3 + 1.0)) && fk.v(var1) && mc.field_1687.method_22347(var1.method_10084())) {
         if (this.B.getValue() && ug.L(var2, b(var1)) > this.r()) {
            return false;
         }

         for (class_2350 var8 : class_2350.values()) {
            if (fk.V(var1.method_10093(var8))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean N(class_2338 var1) {
      return mc.field_1687.method_8335((class_1297)null, new class_238(var1).method_1012(0.0, 2.0, 0.0)).isEmpty();
   }

   private boolean n(class_2338 var1, class_243 var2, double var3) {
      for (class_243 var6 : g(var1, var2)) {
         if (!(var2.method_1025(var6) > var3 * var3)
            && mc.field_1687.method_17742(new class_3959(var2, var6, class_3960.field_17559, class_242.field_1348, mc.field_1724)).method_17783()
               == class_240.field_1333) {
            return true;
         }
      }

      return false;
   }

   private class_3965 w(class_3965 var1, class_2338 var2, class_243 var3) {
      class_3965 var4 = fk.q(var1.method_17777(), var3, this.N());
      if (var4 != null && var4.method_17780() == var1.method_17780() && fk.V(var4.method_17777())) {
         fk.p var5 = fk.A(var4, class_1802.field_8281, this.O2.getValue());
         return var5 != null && var5.target().equals(var2) ? var4 : null;
      } else {
         return null;
      }
   }

   private String Z(AutoCrystalModule.m var1, boolean var2) {
      if (var1.U == 0) {
         return "No reachable obsidian or bedrock base, or open spot to place one";
      } else {
         return var2 && !Double.isNaN(var1.q) && var1.P < var1.U
            ? String.format(Locale.ROOT, "No lethal obsidian spot: best %.1f HP against %.1f HP health", var1.p, var1.q)
            : this.j(var1);
      }
   }

   public void L(class_2663 var1) {
      if (this.isEnabled()) {
         if (var1.method_11470() == 3 && this.R.getValue() && this.isRuntimeActive() && this.isInGame()) {
            if (var1.method_11469(mc.field_1687) instanceof class_1309 var2 && this.q(var2)) {
               this.f = mc.field_1687.method_75260() + Math.round(this.h.getValue() * 20.0);
               this.t.N();
               this.x();
               this.R();
               this.D = null;
               this.m = null;
               this.u = String.format(Locale.ROOT, "Paused after kill: %.1f s left", this.h.getValue());
               q8.G(
                  "AutoCrystal",
                  "{} died nearby; placing and breaking paused for {} s to protect drops",
                  var2.method_5477().getString(),
                  this.h.getValue().intValue()
               );
            }
         }
      }
   }

   private String j(AutoCrystalModule.m var1) {
      if (var1.U == 0) {
         return "No reachable obsidian or bedrock base";
      } else {
         return var1.P == var1.U
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var1.m, this.OT.getValue())
            : String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var1.g, this.Om.getValue());
      }
   }

   private void x() {
      this.S = null;
      this.z = null;
      this.n = null;
      this.O.z();
      cr.p(this);
   }

   private double N() {
      return Math.min(this.Or.getValue(), Math.min(this.F.getValue(), mc.field_1724.method_55754()));
   }

   private double r() {
      return Math.min(this.Or.getValue(), Math.min(this.Oy.getValue(), mc.field_1724.method_55755()));
   }

   private boolean a() {
      return mc.field_1724.method_6059(class_1294.field_5911) && mc.field_1724.method_45325(class_5134.field_23721) <= 0.0;
   }

   private boolean E(AutoCrystalModule.a var1) {
      if (this.r() && this.C(var1.victim())) {
         long var2 = mc.field_1687.method_75260();
         if (var1.crystal() != null) {
            return this.B.getValue()
               && !this.a()
               && this.t.T(var2, var1.crystal().method_5628())
               && this.c(var1.crystal(), var1.aim()).isPresent()
               && this.b(var1.crystal().method_73189(), List.of(var1.victim())) != null;
         } else if (var1.support() != null) {
            class_243 var4 = mc.field_1724.method_33571();
            return this.P.getValue()
               && this.O1.getValue()
               && this.t.f(var2)
               && var2 >= this.OR
               && this.b() != -1
               && fk.T(class_1802.field_8281, this.O2.getValue()) != -1
               && this.H(var1.base(), var4, this.N())
               && this.N(var1.base())
               && this.n(var1.base(), var4, this.N())
               && this.w(var1.support(), var1.base(), var1.aim().method_1020(var4)) != null
               && this.y(class_243.method_24955(var1.base().method_10084()), List.of(var1.victim()), null, var1.base(), true, this.H.G("Lethal")) != null;
         } else {
            return this.P.getValue()
               && this.t.f(var2)
               && this.b() != -1
               && this.S(var1.base(), var1.aim()) != null
               && this.y(class_243.method_24955(var1.base().method_10084()), List.of(var1.victim()), null, null, true, false) != null;
         }
      } else {
         return false;
      }
   }

   private boolean O(AutoCrystalModule.a var1) {
      if (var1.support() != null) {
         return this.w(var1.support(), var1.base(), mc.field_1724.method_5720()) != null;
      } else {
         return var1.crystal() != null ? cr.B(var1.crystal(), this.r()) : cr.Y(var1.base(), this.N()) != null;
      }
   }

   public void K() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.S != null && this.OG == mc.field_1687.method_75260()) {
            if (cr.a(this) && this.E(this.S)) {
               if (this.w.G("Visible")) {
                  if (!wp.h(wp.z.h)) {
                     this.O.z();
                     return;
                  }

                  wp.S(wp.z.h);
                  this.p = mc.field_1724;
                  this.g = mc.field_1687;
                  this.j = mc.field_1724.field_6012;
                  float[] var1 = t9.X(mc.field_1724.method_33571(), this.S.aim());
                  float[] var2 = hl.m(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var1[0], var1[1], this.C.getValue());
                  t9.X(t9.v.y, var2[0], var2[1], 1.0);
                  t9.d();
               }

               this.O.x(mc.field_1724.field_6012, this.O(this.S));
               this.u = this.O.p(mc.field_1724.field_6012, this.b.getValue().intValue())
                  ? "Aim ready"
                  : (!this.w.G("Visible") ? "Waiting for your crosshair" : (this.S.support() != null ? "Aiming at obsidian spot" : "Aiming at crystal action"));
            } else {
               this.O.z();
            }
         }
      }
   }

   private boolean G(AutoCrystalModule.a var1) {
      return this.S == var1
         && this.OG == mc.field_1687.method_75260()
         && this.E(var1)
         && this.O.p(mc.field_1724.field_6012, this.b.getValue().intValue())
         && this.O(var1);
   }

   private void a(AutoCrystalModule.a var1, long var2) {
      class_2338 var4 = var1.base() != null ? var1.base() : var1.crystal().method_24515().method_10074();
      this.A.x(var4);
      wc.t(var1.victim());
      this.m = var1.victim();
      this.OG = var2;
      Object var5 = var1.support() != null ? new AutoCrystalModule.n(var1.base()) : (var1.crystal() == null ? var1.base() : var1.crystal());
      if (!Objects.equals(this.z, var5) || this.n != var1.victim()) {
         this.O.z();
      }

      this.z = var5;
      this.n = var1.victim();
      if (var1.crystal() != null) {
         cr.U(this, var1.crystal(), this.r(), false, () -> this.G(var1), 1, () -> {
            this.t.P(mc.field_1687.method_75260(), var1.crystal().method_5628(), this.Q.getValue().intValue(), this.Oe.getValue().intValue());
            this.D = var4;
            this.u = "Waiting for crystal removal";
            this.x();
         }, var2x -> {
            if (this.G(var1)) {
               this.u = "Waiting: " + var2x;
            }
         });
      } else if (var1.support() != null) {
         cr.y(this, () -> this.F(var1), () -> this.G(var1));
      } else {
         cr.y(this, () -> this.K(var1.base()), () -> this.G(var1));
      }
   }

   private void F(AutoCrystalModule.a var1) {
      class_3965 var2 = this.w(var1.support(), var1.base(), mc.field_1724.method_5720());
      int var3 = fk.T(class_1802.field_8281, this.O2.getValue());
      if (var2 != null && var3 != -1) {
         int var4 = mc.field_1724.method_31548().method_67532();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.field_1724.method_31548().method_61496(var3);
            }

            class_1268 var6 = var3 == -2 ? class_1268.field_5810 : class_1268.field_5808;
            class_1269 var7 = mc.field_1761.method_2896(mc.field_1724, var6, var2);
            long var8 = mc.field_1687.method_75260();
            if (var7.method_23665()) {
               w6.H(mc.field_1724, var6);
               this.t.B(var8, this.On.getValue().intValue());
               this.OR = var8 + this.Oe.getValue().intValue();
               this.u = "Placed obsidian; crystal next";
               q8.v(
                  "AutoCrystal", "Obsidian base at {} for {} ({} mode)", var1.base().method_23854(), var1.victim().method_5477().getString(), this.H.getValue()
               );
            } else {
               this.t.B(var8, this.o.getValue().intValue());
               this.u = "Obsidian placement rejected; retrying";
            }

            this.x();
         } finally {
            if (var5 != 0) {
               mc.field_1724.method_31548().method_61496(var4);
            }
         }
      }
   }

   private int b() {
      if (mc.field_1724.method_6079().method_31574(class_1802.field_8301)) {
         return -2;
      }

      int var1 = mc.field_1724.method_31548().method_67532();
      if (mc.field_1724.method_6047().method_31574(class_1802.field_8301)) {
         return var1;
      }

      if (this.O2.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            if (mc.field_1724.method_31548().method_5438(var2).method_31574(class_1802.field_8301)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private class_3965 S(class_2338 var1, class_243 var2) {
      if (mc.field_1687.method_22340(var1) && mc.field_1687.method_8621().method_11952(var1)) {
         class_2680 var3 = mc.field_1687.method_8320(var1);
         if ((var3.method_27852(class_2246.field_10540) || var3.method_27852(class_2246.field_9987))
            && mc.field_1687.method_22347(var1.method_10084())
            && mc.field_1687.method_8335(null, new class_238(var1.method_10084()).method_1012(0.0, 1.0, 0.0)).isEmpty()) {
            class_243 var4 = mc.field_1724.method_33571();
            double var5 = this.N();
            if (var4.method_1025(class_243.method_24953(var1)) > (var5 + 1.0) * (var5 + 1.0)) {
               return null;
            }

            if (this.B.getValue() && ug.L(var4, b(var1)) > this.r()) {
               return null;
            }

            if (var2 != null) {
               class_3965 var7 = cr.S(var1, var5, var2.method_1020(var4));
               if (var7 != null) {
                  return var7;
               }
            }

            for (class_243 var8 : g(var1, var4)) {
               class_3965 var9 = cr.S(var1, var5, var8.method_1020(var4));
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

   private void K(class_2338 var1) {
      class_3965 var2 = cr.Y(var1, this.N());
      int var3 = this.b();
      if (var2 != null && var3 != -1 && this.S(var1, var2.method_17784()) != null) {
         int var4 = mc.field_1724.method_31548().method_67532();
         int var5 = var3 >= 0 && var3 != var4 ? 1 : 0;

         try {
            if (var5 != 0) {
               mc.field_1724.method_31548().method_61496(var3);
            }

            class_1268 var6 = var3 == -2 ? class_1268.field_5810 : class_1268.field_5808;
            class_1269 var7 = mc.field_1761.method_2896(mc.field_1724, var6, var2);
            this.t.J(mc.field_1687.method_75260(), var1, this.o.getValue().intValue(), var7.method_23665(), this.Oe.getValue().intValue());
            if (var7.method_23665()) {
               w6.H(mc.field_1724, var6);
               this.D = var1;
               this.OR = 0L;
               this.u = "Waiting for crystal spawn";
            } else {
               this.u = "Placement rejected; retrying";
            }

            this.x();
         } finally {
            if (var5 != 0) {
               mc.field_1724.method_31548().method_61496(var4);
            }
         }
      }
   }

   private record a(class_2338 base, class_1511 crystal, class_1309 victim, long created, class_243 aim, class_3965 support) {
   }

   private record b(class_2338 pos, double distance) {
   }

   private record f(class_1309 target, double damage, double self, boolean lethal) {
      double value() {
         return this.lethal ? 1000.0 - this.self : this.damage - this.self * 0.25;
      }
   }

   private static final class m {
      int U;
      int P;
      double m = Double.MAX_VALUE;
      double g;
      double p;
      double q = Double.NaN;
   }

   private record n(class_2338 pos) {
   }
}
