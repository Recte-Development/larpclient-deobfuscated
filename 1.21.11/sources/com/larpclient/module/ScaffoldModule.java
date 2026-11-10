package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q4aba1ac527aae1c6;
import com.larpclient.cr;
import com.larpclient.ff;
import com.larpclient.fk;
import com.larpclient.gw;
import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.t9;
import com.larpclient.uu;
import com.larpclient.wp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.class_10185;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1747;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2346;
import net.minecraft.class_2350;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2530;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_746;

public final class ScaffoldModule extends Module implements fk.h, uu.m {
   private static ScaffoldModule w;
   private final l3 e = this.addSetting(new l3("Auto Switch", "Use suitable hotbar blocks and restore your selected slot", true));
   private final l3 Q = this.addSetting(new l3("Safe Walk", "Sneak at a ledge while grounded so you stop at the edge; jumping stays manual", true));
   private final l3 E = this.addSetting(new l3("Keep Height", "Build at the height captured when enabled or when this setting changes", false));
   private final lo y = this.addSetting(new lo("Look Ahead", "Extra footprint prediction in your movement direction, in blocks", 0.35, 0.0, 0.6, 0.05));
   private final lo H = this.addSetting(new lo("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1));
   private final lo I = this.addSetting(new lo("Delay", "Extra ticks between placement attempts; zero allows one per tick", 0.0, 0.0, 10.0, 1.0));
   private final l3 s = this.addSetting(new l3("Pause Using Item", "Pause while eating or using an item", true));
   private final l3 J = this.addSetting(new l3("Pause Mining", "Pause while the attack key is held", true));
   private final uu V = new uu(this, var1 -> this.addSetting(var1));
   private Object m;
   private Object r;
   private class_2338 x;
   private class_2338 F;
   private float[] q;
   private int g;
   private int U;
   private int n;
   private int O;
   private int u;
   private boolean S;
   private String p = "Inactive";
   private String M = "";
   private long G;
   private int N;
   private int t;
   private int k;
   private int b;
   private int Z;
   private boolean W;
   private long B;
   private float D = Float.NaN;
   private float K;
   private boolean c;
   private String T = "";
   private boolean d;
   private boolean f;
   private boolean v;
   private double C;
   private int l;
   private long j;
   private static final double h = 0.026;

   public ScaffoldModule() {
      super("Scaffold", "Bridge beneath your feet with visible player aim and free mouse look", Category.MOVEMENT, -1);
      w = this;
      this.E.a(var1 -> {
         this.p();
         if (this.isInGame()) {
            this.U = gw.v(mc.field_1724.method_23318());
         }
      });
      this.e.a(var1 -> this.p());
   }

   public static ScaffoldModule y() {
      return w;
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public uu placementHighlight() {
      return this.V;
   }

   @Override
   public String getStatus() {
      return this.p;
   }

   @Override
   public boolean allowsDetachedCamera() {
      return true;
   }

   public List<lb<?>> o() {
      return List.of(this.e, this.Q, this.E, this.y, this.H, this.I, this.s, this.J);
   }

   @Override
   public void onEnable() {
      getMc();
      this.p();
      this.n = 0;
      this.m = mc.field_1687;
      this.r = mc.field_1724;
      this.U = this.u = gw.v(mc.field_1724.method_23318());
      this.p = "Ready";
      this.M = "";
      this.G = 0L;
      this.N = this.t = this.k = this.l = this.b = this.Z = 0;
      this.W = (boolean)0;
      this.B = this.j = 0L;
      this.D = Float.NaN;
      this.c = (boolean)0;
      this.d = (boolean)0;
      this.T = "";
      this.V.d();
      q8.G(
         this.getName(),
         "Enabled: real instant player aim, free camera (also behind Freelook and Freecam), native placement, safe walk (sneak at ledges)={}, keep height={}; clicks wait until the look already sent reaches the support; selected slot is restored",
         this.Q.getValue(),
         this.E.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.p();
      this.m = this.r = null;
      this.n = 0;
      this.p = "Inactive";
      this.W = (boolean)0;
      this.d = (boolean)0;
      if (this.N > 0 || this.t > 0 || this.k > 0 || this.l > 0) {
         q8.G(
            this.getName(),
            "Disabled after {} placed blocks ({} mid-air, {} of those below the feet's layer to support the jump); clicks waited {} ticks for the look already sent; Safe Walk sneaked at a ledge {} times; walked off a ledge unsneaked {} times",
            this.N,
            this.b,
            this.Z,
            this.t,
            this.k,
            this.l
         );
      }

      q8.v(this.getName(), "Released placement, independent camera and ledge protection");
   }

   private void p() {
      this.x = this.F = null;
      this.q = null;
      this.g = 0;
      this.S = (boolean)0;
      this.V.p();
      fk.N(this);
      wp.k(wp.z.e);
      this.U("placement paused or ended");
   }

   private boolean A() {
      return this.isRuntimeActive()
         && mc != null
         && mc.field_1724 != null
         && mc.field_1687 != null
         && this.m == mc.field_1687
         && this.r == mc.field_1724
         && mc.field_1724.method_5805()
         && !mc.field_1724.method_7325()
         && mc.field_1755 == null
         && mc.method_1569();
   }

   private boolean X() {
      class_746 var1 = mc.field_1724;
      return !var1.method_5765()
         && !var1.method_6128()
         && !var1.method_31549().field_7479
         && !var1.field_5960
         && !var1.method_6113()
         && !var1.method_74025()
         && !var1.method_6101()
         && !var1.method_5799()
         && !var1.method_5771();
   }

   private static boolean D() {
      FlyModule var0 = FlyModule.W();
      AirStuckModule var1 = AirStuckModule.U();
      ElytraMotionModule var2 = ElytraMotionModule.N();
      return var0 != null && var0.P() || var1 != null && var1.E() || var2 != null && var2.k() || TargetStrafeModule.b();
   }

   private boolean a() {
      if (this.A() && this.X()) {
         class_746 var1 = mc.field_1724;
         return var1.field_6017 <= 3.0 && (!this.s.getValue() || !var1.method_6115()) && (!this.J.getValue() || !mc.field_1690.field_1886.method_1434());
      } else {
         return false;
      }
   }

   private static boolean P() {
      wp.z var0 = wp.o();
      return var0 == wp.z.x || var0 == wp.z.w;
   }

   private boolean r() {
      if (!this.a() || !cr.o(this) || cr.V()) {
         return false;
      } else {
         return !P() && !wp.h(wp.z.e) ? false : !D();
      }
   }

   private String X(class_1657 var1) {
      if (!this.Q.getValue()) {
         return "Safe Walk is off";
      } else if (!this.A()) {
         return "a screen, focus loss or session change";
      } else if (!this.X()) {
         return "vehicle, flight, climbing or liquid";
      } else if (D()) {
         return "another module drives your motion";
      } else if (!var1.method_24828()) {
         return "not on the ground";
      } else {
         return !wp.d() && mc.field_1690.field_1832.method_1434() ? "you hold Shift" : null;
      }
   }

   public static float f() {
      ScaffoldModule var0 = w;
      class_310 var1 = getMc();
      return var0 != null && !Float.isNaN(var0.D) && var1 != null && var1.field_1724 != null && var1.field_1724 == var0.r && var0.isRuntimeActive()
         ? var0.D
         : Float.NaN;
   }

   public static boolean Q(class_1657 var0) {
      if (w != null && w.isEnabled()) {
         ScaffoldModule var1 = w;
         return var1 != null && var1.W && var0 == getMc().field_1724;
      } else {
         return false;
      }
   }

   public static void G() {
      if (w != null) {
         w.U("the outer camera ended");
      }
   }

   private void m() {
      this.D = mc.field_1724.method_36454();
      this.K = mc.field_1724.method_36455();
      String var1 = wp.o() == wp.z.w ? "Freecam" : "Freelook";
      if (!this.c) {
         this.c = (boolean)1;
         q8.G(
            this.getName(),
            "Bridging behind {}: the body turns to place while your keys keep walking toward {} degrees; it faces that way again when bridging pauses or the camera returns",
            var1,
            Math.round(class_3532.method_15393(this.D))
         );
      } else {
         q8.v(this.getName(), "Holding walking heading {} behind {}", Math.round(class_3532.method_15393(this.D)), var1);
      }
   }

   private void U(String var1) {
      if (!Float.isNaN(this.D)) {
         float var2 = this.D;
         float var3 = this.K;
         this.D = Float.NaN;
         class_746 var4 = mc == null ? null : mc.field_1724;
         if (var4 != null && var4 == this.r && var4.method_5805()) {
            float var5 = var4.method_36454() + class_3532.method_15393(var2 - var4.method_36454());
            var4.method_36456(var5);
            var4.method_36457(var3);
            var4.method_5847(var5);
            var4.field_5982 = var5;
            var4.field_6004 = var3;
            q8.v(this.getName(), "Body faces the walking heading again: {}", var1);
         }
      }
   }

   public boolean y() {
      return !this.isEnabled()
         ? false
         : this.a() && (this.x != null || this.S && mc.field_1724.field_6012 - this.O >= 0 && mc.field_1724.field_6012 - this.O <= 3);
   }

   private float u() {
      return gw.M(com.larpclient.j.h(), mc.field_1724.method_36454());
   }

   private class_243 V() {
      class_746 var1 = mc.field_1724;
      class_243 var2 = var1.method_18798();
      class_243 var3 = gw.J(var2, com.larpclient.j.h(), var1.method_24828() ? d(var1) : 0.026);
      double var4 = gw.K(var2.field_1351, var1.method_24828(), var1.field_3913.field_54155.comp_3163(), var1.method_45325(class_5134.field_23728));
      return var1.method_33571().method_1031(var3.field_1352, var4, var3.field_1350);
   }

   private float[] u(class_2338 var1) {
      class_243 var2 = this.V();
      double var3 = this.H.getValue();
      return gw.n(
         var1,
         mc.field_1724.method_33571(),
         this.u(),
         mc.field_1724.method_36454(),
         mc.field_1724.method_36455(),
         var5 -> fk.D(this, var1, var5, var3) != null && fk.E(this, var2, var1, var5, var3) != null,
         var4 -> fk.D(this, var1, var4, var3) != null
      );
   }

   private float[] M(class_2338 var1, class_2350 var2, boolean var3) {
      class_2338 var4 = var1.method_10093(var2);
      class_243 var5 = this.V();
      double var6 = this.H.getValue();
      Predicate var8 = var5x -> {
         class_3965 var6x = fk.D(this, var1, var5x, var6);
         return var6x != null && var6x.method_17780() == var2;
      };
      return gw.r(
         var1,
         var2,
         fk.n(var1, var2),
         mc.field_1724.method_33571(),
         this.u(),
         mc.field_1724.method_36454(),
         mc.field_1724.method_36455(),
         var7 -> var8.test(var7) && (fk.E(this, var5, var1, var7, var6) != null || gw.G(var4, var5, var7, var6)),
         var8,
         var3
      );
   }

   private static double d(class_1657 var0) {
      double var1 = var0.method_45325(class_5134.field_23719);
      return var0.method_5624() ? var1 : var1 * 1.3;
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (this.r() && cr.a(this)) {
            float[] var3 = this.F != null ? this.u(this.F) : this.q;
            if (var3 != null) {
               var1 = var3[0];
               var2 = var3[1];
            }

            boolean var4 = P();
            t9.O(this, t9.v.j, var1, var2, 1.0);
            if (!t9.k(t9.v.j)) {
               this.S = (boolean)0;
               if (!var4) {
                  wp.k(wp.z.e);
               }
            } else {
               if (var4) {
                  if (Float.isNaN(this.D)) {
                     this.m();
                  }
               } else {
                  wp.S(wp.z.e);
               }

               this.S = (boolean)1;
               this.O = mc.field_1724.field_6012;
            }
         }
      }
   }

   public float V(float var1) {
      if (!this.isEnabled()) {
         return var1;
      } else if (this.isRuntimeActive() && (wp.o() == wp.z.e || !Float.isNaN(f()))) {
         mc.field_1724.method_5847(mc.field_1724.method_36454());
         return mc.field_1724.method_36454();
      } else {
         return var1;
      }
   }

   public static boolean N(class_1657 var0) {
      if (w != null && w.isEnabled()) {
         ScaffoldModule var1 = w;
         return var1 != null && var0 == getMc().field_1724 && var1.X(var0) == null;
      } else {
         return false;
      }
   }

   public static class_10185 F(class_746 var0, class_10185 var1) {
      if (w != null && w.isEnabled()) {
         ScaffoldModule var2 = w;
         if (var2 == null) {
            return var1;
         }

         int var3 = var1.comp_3163() && gw.Q(((q4aba1ac527aae1c6)var0).larpNoJumpDelay()) ? 1 : 0;
         if (var0 == getMc().field_1724 && var2.isRuntimeActive()) {
            var2.d = (boolean)1;
            var2.f = var0.method_24828();
            var2.v = (boolean)(!var1.comp_3164() && var3 == 0 ? 0 : 1);
         }

         String var4 = N(var0) ? null : (var0 == getMc().field_1724 ? var2.X(var0) : "another player");
         if (!var1.comp_3164() && var3 == 0 && var4 == null) {
            class_241 var5 = var0.field_3913.field_55868;
            float var6 = ff.e() && !ff.C() ? wp.o() : var0.method_36454();
            double var7 = Math.toRadians(var6);
            class_243 var9 = new class_243(
               var5.field_1343 * Math.cos(var7) - var5.field_1342 * Math.sin(var7), 0.0, var5.field_1342 * Math.cos(var7) + var5.field_1343 * Math.sin(var7)
            );
            class_243 var10 = gw.J(var0.method_18798(), var9, d(var0));
            boolean var11 = gw.X(var0.method_5829(), var10, var0.method_49476(), var1x -> var0.method_73183().method_8587(var0, var1x));
            var2.L(var11);
            var2.T = var11 ? "sneaked" : "the step looked safe";
            var2.C = var10.method_37267();
            return var11
               ? new class_10185(var1.comp_3159(), var1.comp_3160(), var1.comp_3161(), var1.comp_3162(), var1.comp_3163(), true, var1.comp_3165())
               : var1;
         } else {
            var2.W = (boolean)0;
            var2.T = var1.comp_3164() ? "the body already sneaks" : (var3 != 0 ? "a jump" : var4);
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void L(boolean var1) {
      if (var1 && !this.W) {
         this.k++;
         long var2 = System.nanoTime();
         if (var2 - this.B >= 2000000000L) {
            this.B = var2;
            q8.G(this.getName(), "Safe Walk sneaked at a ledge ({} times since enable); vanilla sneak keeps you on the edge until a block is placed", this.k);
         }
      }

      this.W = var1;
   }

   public static boolean q(class_1799 var0, class_1922 var1, class_2338 var2) {
      if (!var0.method_7960() && var0.method_7909() instanceof class_1747 var3) {
         class_2248 var6 = var3.method_7711();
         class_2680 var5 = var6.method_9564();
         return !(var6 instanceof class_2346)
            && !(var6 instanceof class_2530)
            && var6 != class_2246.field_10092
            && var6 != class_2246.field_23152
            && var6 != class_2246.field_9980
            && var6 != class_2246.field_16329
            && var6 != class_2246.field_16331
            && var6 != class_2246.field_10179
            && !var5.method_31709()
            && var5.method_26234(var1, var2);
      } else {
         return false;
      }
   }

   private List<class_1792> J() {
      LinkedHashSet var1 = new LinkedHashSet();
      class_1661 var2 = mc.field_1724.method_31548();
      class_1799 var3 = mc.field_1724.method_6047();
      class_1799 var4 = mc.field_1724.method_6079();
      if (q(var3, mc.field_1687, mc.field_1724.method_24515())) {
         var1.add(var3.method_7909());
      }

      if (q(var4, mc.field_1687, mc.field_1724.method_24515())) {
         var1.add(var4.method_7909());
      }

      if (this.e.getValue()) {
         ArrayList var5 = new ArrayList();

         for (int var6 = 0; var6 < 9; var6++) {
            class_1799 var7 = var2.method_5438(var6);
            if (q(var7, mc.field_1687, mc.field_1724.method_24515())) {
               var5.add(var7);
            }
         }

         var5.sort(Comparator.comparingInt(class_1799::method_7947).reversed());
         var5.forEach(var1x -> var1.add(var1x.method_7909()));
      }

      return List.copyOf(var1);
   }

   private List<List<class_2338>> p() {
      class_243 var1 = com.larpclient.j.h().method_1021(this.y.getValue());
      class_243 var2 = mc.field_1724.method_18798().method_18805(1.0, 0.0, 1.0);
      int var3 = gw.v(mc.field_1724.method_23318());
      return gw.u(
         mc.field_1724.method_73189(),
         mc.field_1724.method_5829(),
         var1.method_1019(var2),
         this.E.getValue() ? List.of(this.U) : gw.W(var3, this.u),
         !mc.field_1724.method_24828(),
         fk::v,
         fk::V
      );
   }

   private boolean P(class_2338 var1) {
      return this.p().stream().anyMatch(var1x -> var1x.contains(var1));
   }

   private void I(String var1) {
      this.p = var1;
      long var2 = System.nanoTime();
      if (!var1.equals(this.M) && var2 - this.G >= 2000000000L) {
         this.M = var1;
         this.G = var2;
         q8.v(this.getName(), "{}; will retry when the current placement conditions permit", var1);
      }
   }

   private void Z() {
      if (this.d) {
         this.d = (boolean)0;
         class_746 var1 = mc.field_1724;
         if (var1 != null
            && var1 == this.r
            && this.f
            && !this.v
            && !var1.method_24828()
            && !(var1.method_18798().field_1351 >= 0.0)
            && this.X()
            && !this.T.equals("Safe Walk is off")) {
            this.l++;
            long var2 = System.nanoTime();
            if (var2 - this.j >= 2000000000L) {
               this.j = var2;
               q8.G(
                  this.getName(),
                  "Walked off a ledge without the Safe Walk sneak ({} times since enable): {}{}; camera={}, status={}",
                  this.l,
                  this.T,
                  this.T.equals("the step looked safe") ? String.format(Locale.ROOT, " (travel bound %.3f blocks)", this.C) : "",
                  wp.o(),
                  this.p
               );
            }
         }
      }
   }

   @Override
   public void onTick() {
      this.Z();
      if (mc.field_1724.method_24828()) {
         this.u = gw.v(mc.field_1724.method_23318());
      }

      if (!Float.isNaN(this.D) && !P()) {
         this.U("the outer camera ended");
      }

      class_2338 var1 = this.x;
      fk.N(this);
      this.x = this.F = null;
      this.q = null;
      if (!this.r()) {
         this.p();
         this.I("Paused: screen, movement, item use or another camera owns control");
      } else {
         if (!this.y()) {
            wp.k(wp.z.e);
            this.U("no block to aim at");
         }

         int var16 = this.n;
         int var15 = mc.field_1724.field_6012;
         if ((Integer.compare(var15, var16) >>> 31 ^ 1) == 0) {
            this.V.x(null);
            this.p = "Placement delay";
         } else {
            List var2 = this.J();
            if (var2.isEmpty()) {
               this.p();
               this.I("No suitable blocks in held hands or hotbar");
            } else {
               List var3 = this.p();
               ArrayList var4 = new ArrayList();

               label108:
               for (List var6 : var3) {
                  for (class_2338 var8 : var6) {
                     if (fk.v(var8)) {
                        for (class_1792 var10 : var2) {
                           class_3965 var11 = fk.c(this, var8, var10, this.H.getValue(), true, this.e.getValue());
                           if (var11 != null) {
                              boolean var12 = fk.N(this, var11.method_17777(), this.H.getValue());
                              var4.add(new ScaffoldModule.v(var8, var10, var11.method_17777(), var11.method_17780(), var12));
                              if (var12) {
                                 break label108;
                              }
                              break;
                           }
                        }
                     }
                  }

                  if (!var4.isEmpty()) {
                     break;
                  }
               }

               int var21 = gw.J(var4.stream().map(ScaffoldModule.v::sentAim).toList(), var4.stream().map(ScaffoldModule.v::pos).toList().indexOf(var1));
               if (var21 >= 0) {
                  ScaffoldModule.v var22 = (ScaffoldModule.v)var4.get(var21);
                  class_2338 var24 = var22.pos();
                  float[] var10000;
                  if (var22.sentAim()) {
                     class_2338 var10001 = var22.support();
                     class_2350 var10002 = var22.face();
                     boolean var14 = false;
                     int var13 = this.g;
                     var10000 = this.M(var10001, var10002, (-Integer.signum(var13) >>> 31 ^ 1) == 0);
                  } else {
                     var10000 = null;
                  }

                  float[] var26 = var10000;
                  int var27 = var26 != null ? 1 : 0;
                  this.x = var24;
                  boolean var28 = fk.m(
                     this,
                     var24,
                     var22.item(),
                     this.H.getValue(),
                     true,
                     this.e.getValue(),
                     () -> this.r() && var24.equals(this.x) && this.P(var24),
                     () -> var27,
                     var2x -> {
                        this.n = mc.field_1724.field_6012 + this.I.getValue().intValue() + 1;
                        if (var2x) {
                           this.N++;
                           if (!mc.field_1724.method_24828()) {
                              this.b++;
                              if (var24.method_10264() < gw.v(mc.field_1724.method_23318())) {
                                 this.Z++;
                              }
                           }
                        }

                        this.p = var2x ? "Bridging" : "Placement rejected; retrying";
                     }
                  );
                  if (var28) {
                     this.V.x(var24);
                     if (var27 != 0) {
                        this.q = var26;
                        this.g = 0;
                     } else {
                        byte var18 = 1;
                        int var17 = this.t;
                        this.t = var17 + var18;
                        this.F = var22.support();
                        if (var22.sentAim()) {
                           byte var20 = 1;
                           int var19 = this.g;
                           this.g = var19 + var20;
                        }
                     }

                     this.p = var27 != 0 ? "Aiming at bridge block" : "Turning before the click";
                     return;
                  }

                  this.x = null;
               }

               this.V.x(null);
               class_2338 var23 = gw.o(
                  var3.stream().flatMap(Collection::stream).toList(),
                  fk::v,
                  fk::V,
                  var1x -> fk.D(this, var1x, class_243.method_24953(var1x).method_1020(mc.field_1724.method_33571()), this.H.getValue()) != null
               );
               if (var23 != null) {
                  this.F = var23;
                  float[] var25 = t9.X(mc.field_1724.method_33571(), class_243.method_24953(var23));
                  this.aimPlacement(var25[0], var25[1]);
                  this.p = "Looking at the next support";
               } else {
                  this.I("Waiting for a reachable support face beneath your path");
               }
            }
         }
      }
   }

   private record v(class_2338 pos, class_1792 item, class_2338 support, class_2350 face, boolean sentAim) {
   }
}
