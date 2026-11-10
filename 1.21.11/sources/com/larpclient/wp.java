package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q219e0ba2e314b7b5;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.AutoPearlCatchModule;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.WindChargeHopModule;
import com.larpclient.module.a_;
import net.minecraft.class_243;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4050;
import net.minecraft.class_5498;
import net.minecraft.class_746;

public final class wp {
   private static final wp.p W = new wp.p();
   private static class_5498 j;
   private static class_5498 b;
   private static float m;
   private static float C;
   private static class_243 e = class_243.field_1353;
   private static class_243 D = class_243.field_1353;
   public static final int y = 40;
   private static gn u;
   private static double c;
   private static boolean G;
   private static wp.z r = wp.z.D;
   private static int N;

   public static boolean h(wp.z var0) {
      o();
      return W.G(var0);
   }

   public static void S(wp.z var0) {
      class_310 var1 = class_310.method_1551();
      o();
      if (W.G(var0)) {
         if (W.p == var0) {
            if (!r(var0)) {
               X(var0);
            }
         } else if (W.p == wp.z.u && r(var0)) {
            t("taken over by " + var0);
            h();
            W.I(var0, var1.field_1724, var1.field_1687);
            w("acquired");
         } else {
            if (r(W.p)) {
               k(W.p);
            }

            if (W.p == wp.z.D && !r(var0)) {
               j = var1.field_1690.method_31044();
            }

            if (W.I(var0, var1.field_1724, var1.field_1687)) {
               m = var1.field_1724.method_36454();
               C = var1.field_1724.method_36455();
               b = var1.field_1690.method_31044();
               if (r(var0) && b.method_31035()) {
                  m += 180.0F;
                  C = -C;
               }

               D = e = var0 == wp.z.w
                  ? var1.field_1724.method_73189().method_1031(0.0, var1.field_1724.method_18381(class_4050.field_18076), 0.0)
                  : var1.field_1724.method_33571();
               if (!r(var0)) {
                  X(var0);
               } else {
                  w("acquired");
               }
            }
         }
      }
   }

   public static void k(wp.z var0) {
      if (W.p == var0) {
         class_310 var1 = class_310.method_1551();
         if (r(var0) && W.z(var1.field_1724, var1.field_1687, var1.field_1724 != null && var1.field_1724.method_5805())) {
            F();
            float[] var2 = N(m, C, var1.field_1690.method_31044().method_31035());
            var2[0] = var1.field_1724.method_36454() + class_3532.method_15393(var2[0] - var1.field_1724.method_36454());
            var1.field_1724.method_36456(var2[0]);
            var1.field_1724.method_36457(var2[1]);
            if (var0 == wp.z.e) {
               var1.field_1724.method_5847(var2[0]);
            }

            var1.field_1724.field_5982 = var2[0];
            var1.field_1724.field_6004 = var2[1];
         }

         B();
         if (var0 == wp.z.x || var0 == wp.z.w) {
            ScaffoldModule.G();
         }
      }
   }

   public static void g(wp.z var0, gn var1, double var2, boolean var4) {
      if (W.p == var0 && var0 != wp.z.u) {
         if (var1 != null && !(var2 >= 180.0) && r(var0) && z()) {
            class_310 var5 = class_310.method_1551();
            F();
            float[] var6 = N(m, C, var5.field_1690.method_31044().method_31035());
            if (gn.j(var5.field_1724.method_36454(), var5.field_1724.method_36455(), var6[0], var6[1], a_.i())) {
               B();
            } else {
               W.D(var0);
               u = var1;
               c = var2;
               G = var4;
               r = var0;
               N = 0;
               var1.z();
            }
         } else {
            k(var0);
         }
      }
   }

   public static void s() {
      if (o() == wp.z.u) {
         class_310 var0 = class_310.method_1551();
         class_746 var1 = var0.field_1724;
         float[] var2 = N(m, C, var0.field_1690.method_31044().method_31035());
         float var3 = a_.i();
         if (gn.j(var1.method_36454(), var1.method_36455(), var2[0], var2[1], var3)) {
            t("done, " + u.E());
            B();
         } else if (++N > 40) {
            t("not on your look after 40 ticks, so handed back at once; " + u.E());
            k(wp.z.u);
         } else {
            float[] var4 = u.J(var1.method_36454(), var1.method_36455(), var2[0], var2[1], c, G, var3);
            t9.H(t9.v.r, var4[0], var4[1], 1.0, var1.method_36454(), var1.method_36455());
         }
      }
   }

   public static void W() {
      if (W.p == wp.z.u && t9.I() && !t9.k(t9.v.r)) {
         t("ended by another module's turn; " + u.E());
         B();
      }
   }

   public static void N() {
      if (W.p == wp.z.u) {
         class_310 var0 = class_310.method_1551();
         if (i(var0.field_1690.field_1886) || i(var0.field_1690.field_1904)) {
            B("your click");
         }
      }
   }

   public static void B(String var0) {
      if (W.p == wp.z.u) {
         t("finished at once for " + var0 + "; " + u.E());
         k(wp.z.u);
      }
   }

   private static boolean i(class_304 var0) {
      return var0.method_1434() || ((q219e0ba2e314b7b5)var0).larpClickCount() > 0;
   }

   public static boolean o() {
      return W.p == wp.z.u;
   }

   private static void h() {
      u = null;
      r = wp.z.D;
      N = 0;
   }

   private static void t(String var0) {
      String var1 = switch (r) {
         case M -> "Auto Builder";
         default -> "Camera";
         case m -> "Mace";
         case g -> "WindHop";
         case V -> "PearlCatch";
      };
      q8.G(var1, "Return to your look {}", var0);
   }

   private static boolean z() {
      class_310 var0 = class_310.method_1551();
      return W.z(var0.field_1724, var0.field_1687, var0.field_1724 != null && var0.field_1724.method_5805())
         && var0.field_1755 == null
         && var0.method_1569()
         && !var0.field_1724.method_7325();
   }

   public static void B() {
      if (r(W.p)) {
         w("released");
         l();
      }

      if (j != null) {
         class_310.method_1551().field_1690.method_31043(j);
      }

      j = null;
      b = null;
      W.e(W.p);
      h();
      D = e = class_243.field_1353;
      C = 0.0F;
      m = 0.0F;
   }

   public static wp.z o() {
      if (W.p == wp.z.e) {
         ScaffoldModule var0 = ScaffoldModule.y();
         if (!z() || var0 == null || !var0.y()) {
            k(wp.z.e);
         }
      }

      if (W.p == wp.z.v) {
         com.larpclient.module.g var1 = com.larpclient.module.g.K();
         if (!z() || var1 == null || !var1.x()) {
            k(wp.z.v);
         }
      }

      if (W.p == wp.z.r) {
         com.larpclient.module.b var2 = com.larpclient.module.b.e();
         if (!z() || var2 == null || !var2.F()) {
            k(wp.z.r);
         }
      }

      if (W.p == wp.z.J) {
         AutoAnchorModule var3 = AutoAnchorModule.T();
         if (!z() || var3 == null || !var3.y()) {
            k(wp.z.J);
         }
      }

      if (W.p == wp.z.L) {
         AuraModule var4 = AuraModule.R();
         if (!z() || var4 == null || !var4.G()) {
            k(wp.z.L);
         }
      }

      if (W.p == wp.z.m) {
         MaceModule var5 = MaceModule.c();
         if (!z() || var5 == null) {
            k(wp.z.m);
         } else if (!var5.l()) {
            g(wp.z.m, var5.A(), var5.L(), true);
         }
      }

      if (W.p == wp.z.h) {
         AutoCrystalModule var6 = AutoCrystalModule.l();
         if (!z() || var6 == null || !var6.q()) {
            k(wp.z.h);
         }
      }

      if (W.p == wp.z.V) {
         AutoPearlCatchModule var7 = AutoPearlCatchModule.M();
         if (!z() || var7 == null) {
            k(wp.z.V);
         } else if (!var7.l()) {
            g(wp.z.V, var7.k(), var7.x(), var7.X());
         }
      }

      if (W.p == wp.z.g) {
         WindChargeHopModule var8 = WindChargeHopModule.T();
         if (!z() || var8 == null) {
            k(wp.z.g);
         } else if (!var8.u()) {
            g(wp.z.g, var8.H(), var8.t(), var8.J());
         }
      }

      if (W.p == wp.z.M) {
         s0 var9 = s0.u();
         if (!z() || var9 == null) {
            k(wp.z.M);
         } else if (!var9.m()) {
            g(wp.z.M, var9.F(), var9.M(), true);
         }
      }

      if (W.p == wp.z.u && !z()) {
         k(wp.z.u);
      }

      class_310 var10 = class_310.method_1551();
      if (W.p != wp.z.D && !W.z(var10.field_1724, var10.field_1687, var10.field_1724 != null && var10.field_1724.method_5805())) {
         B();
      }

      if (r(W.p)) {
         F();
      }

      return W.p;
   }

   private static boolean r(wp.z var0) {
      return var0 == wp.z.L
         || var0 == wp.z.m
         || var0 == wp.z.v
         || var0 == wp.z.r
         || var0 == wp.z.J
         || var0 == wp.z.h
         || var0 == wp.z.e
         || var0 == wp.z.V
         || var0 == wp.z.g
         || var0 == wp.z.u
         || var0 == wp.z.M;
   }

   public static boolean Y() {
      return r(o());
   }

   private static void F() {
      class_5498 var0 = class_310.method_1551().field_1690.method_31044();
      if (b != null && b.method_31035() != var0.method_31035()) {
         m = m + (var0.method_31035() ? 180.0F : -180.0F);
         C = -C;
      }

      b = var0;
   }

   public static boolean d() {
      return o() == wp.z.w;
   }

   public static float R(float var0) {
      return !Y() ? Float.NaN : D(N(m, C, class_310.method_1551().field_1690.method_31044().method_31035())[0], var0);
   }

   public static float n() {
      return !Y() ? Float.NaN : N(m, C, class_310.method_1551().field_1690.method_31044().method_31035())[1];
   }

   public static float D(float var0, float var1) {
      return var1 + class_3532.method_15393(var0 - var1);
   }

   public static float n(float var0, float var1) {
      return Math.round((var1 - var0) / 360.0F) * 360.0F;
   }

   private static void l() {
      class_746 var0 = class_310.method_1551().field_1724;
      if (var0 != null) {
         float var1 = n(var0.field_3932, var0.method_36454());
         if (var1 != 0.0F) {
            var0.field_3932 += var1;
            var0.field_3931 += var1;
            q8.v("Camera", "Hand sway moved {} degrees back onto the player's rotation", var1);
         }
      }
   }

   public static void E(double var0, double var2) {
      m += (float)var0 * 0.15F;
      float var4 = d() ? 90.0F : 89.0F;
      C = class_3532.method_15363(C + (float)var2 * 0.15F, -var4, var4);
   }

   public static float g() {
      return m;
   }

   public static float o() {
      return class_310.method_1551().field_1690.method_31044().method_31035() ? m - 180.0F : m;
   }

   public static float J() {
      return C;
   }

   public static class_243 W(float var0) {
      return D.method_35590(e, var0);
   }

   public static void A(double var0, double var2, boolean var4) {
      class_310 var5 = class_310.method_1551();
      D = e;
      if (d() && var5.field_1755 == null && var5.method_1569()) {
         e = e.method_1019(hq.T(m, FreecamModule.c(), var0, var2, var4));
      }
   }

   private static void X(wp.z var0) {
      class_310 var1 = class_310.method_1551();
      class_5498 var2 = j == null ? var1.field_1690.method_31044() : j;
      class_5498 var3 = var0 == wp.z.w ? (FreecamModule.z() && var2.method_31034() ? class_5498.field_26665 : var2) : class_5498.field_26665;
      if (var1.field_1690.method_31044() != var3) {
         var1.field_1690.method_31043(var3);
      }
   }

   public static float[] N(float var0, float var1, boolean var2) {
      return new float[]{var2 ? var0 - 180.0F : var0, var2 ? -var1 : var1};
   }

   private static void w(String var0) {
      if (W.p != wp.z.u) {
         if (W.p == wp.z.e) {
            q8.v("Scaffold", "Independent view {}", var0);
         } else if (W.p == wp.z.v) {
            q8.G("AutoTrap", "Independent view {}", var0);
         } else if (W.p == wp.z.r) {
            q8.G("AutoWeb", "Independent view {}", var0);
         } else if (W.p == wp.z.J) {
            q8.G("AutoAnchor", "Independent view {}", var0);
         } else if (W.p == wp.z.h) {
            q8.v("AutoCrystal", "Independent view {}", var0);
         } else if (W.p == wp.z.V) {
            q8.G("PearlCatch", "Silent view {}", var0);
         } else if (W.p == wp.z.g) {
            q8.G("WindHop", "Silent view {}", var0);
         } else {
            if (W.p == wp.z.L) {
               q8.v("Aura", "Independent view {}", var0);
            }

            if (W.p == wp.z.m) {
               q8.v("Mace", "Independent view {}", var0);
            }
         }
      }
   }

   private wp() {
   }

   public static final class p {
      private wp.z p = wp.z.D;
      private Object i;
      private Object R;

      public wp.z s() {
         return this.p;
      }

      public boolean G(wp.z var1) {
         if (var1 == wp.z.u) {
            return false;
         } else if (this.p == wp.z.u) {
            return true;
         } else if (var1 == wp.z.M) {
            return this.p == wp.z.D || this.p == var1;
         } else if (var1 == wp.z.e) {
            return this.p == wp.z.D || this.p == wp.z.M || this.p == var1;
         } else if (V(var1)) {
            return this.p == wp.z.D || this.p == wp.z.e || this.p == wp.z.M || V(this.p);
         } else if (var1 == wp.z.L) {
            return this.p == wp.z.D || this.p == wp.z.e || this.p == wp.z.M || V(this.p) || this.p == var1;
         } else if (var1 == wp.z.m) {
            return this.p == wp.z.D || this.p == wp.z.e || this.p == wp.z.M || V(this.p) || this.p == wp.z.L || this.p == var1;
         } else if (var1 == wp.z.h) {
            return this.p == wp.z.D || this.p == wp.z.e || this.p == wp.z.M || V(this.p) || this.p == wp.z.L || this.p == var1;
         } else if (var1 == wp.z.g) {
            return this.p != wp.z.x && this.p != wp.z.w && this.p != wp.z.V;
         } else if (var1 != wp.z.V) {
            return var1 == wp.z.x ? this.p != wp.z.w : true;
         } else {
            return this.p != wp.z.x && this.p != wp.z.w;
         }
      }

      private static boolean V(wp.z var0) {
         return var0 == wp.z.v || var0 == wp.z.r || var0 == wp.z.J;
      }

      public boolean I(wp.z var1, Object var2, Object var3) {
         if (!this.G(var1)) {
            return false;
         }

         this.p = var1;
         this.i = var2;
         this.R = var3;
         return true;
      }

      public boolean D(wp.z var1) {
         if (this.p == var1 && var1 != wp.z.D && var1 != wp.z.u) {
            this.p = wp.z.u;
            return true;
         } else {
            return false;
         }
      }

      public boolean z(Object var1, Object var2, boolean var3) {
         return var1 != null && var3 && this.i == var1 && this.R == var2;
      }

      public boolean e(wp.z var1) {
         if (this.p != var1) {
            return false;
         }

         this.p = wp.z.D;
         this.i = this.R = null;
         return true;
      }
   }

   public enum z {
      D,
      u,
      M,
      e,
      v,
      r,
      J,
      L,
      m,
      h,
      g,
      V,
      x,
      w;
   }
}
