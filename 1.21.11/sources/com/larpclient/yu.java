package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1109;
import net.minecraft.class_1299;
import net.minecraft.class_1533;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1667;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1937;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2398;
import net.minecraft.class_2401;
import net.minecraft.class_2426;
import net.minecraft.class_2428;
import net.minecraft.class_243;
import net.minecraft.class_2453;
import net.minecraft.class_2457;
import net.minecraft.class_2462;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2766;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_3730;
import net.minecraft.class_5819;
import net.minecraft.class_638;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_9209;
import net.minecraft.class_9334;
import net.minecraft.class_1113.class_1114;

@Environment(EnvType.CLIENT)
public final class yu {
   private static final yu M = new yu();
   private static final double v = 0.5;
   private static final double y = 1.1;
   private static final double W = 0.85;
   private static final double Y = 0.05;
   private static final double S = 0.99;
   private static final double K = 11.7;
   private static final double V = 12.5;
   private static final int R = 120;
   private static final double x = 10.0;
   private static final double B = 4096.0;
   private static final double w = 64.0;
   private static final double o = 4.0;
   public boolean C;
   private yu.k F = yu.k.p;
   private final Random a = new Random();
   private final List<yu.u> U = new ArrayList<>();
   private class_2338 i;
   private class_2680 H;
   private final List<yu.m> q = new ArrayList<>();
   private class_638 N;
   private long I = -1L;
   private long g = -1L;
   private final Map<class_2338, class_2680> P = new LinkedHashMap<>();
   private class_638 s;
   private int D = -2;
   private long j = -1L;
   private boolean Z;
   private final Map<String, int[]> J = new LinkedHashMap<>();
   private final Map<String, int[]> l = new LinkedHashMap<>();
   private final Map<String, Integer> k = new LinkedHashMap<>();
   private final int[] m = new int[36];
   private final int[] E = new int[36];
   private int A = 0;
   private int t = 0;
   private class_1799 h = null;
   private class_1799 e = null;

   private static boolean A(class_1533 var0) {
      return var0.method_5735() == class_2350.field_11036;
   }

   private static class_243 H(class_1937 var0, class_2338 var1) {
      class_243 var2 = new class_243(var1.method_10263() + 0.5, var1.method_10264() + 0.5, var1.method_10260() + 0.5);
      class_2680 var3 = var0.method_8320(var1);
      if (!(var3.method_26204() instanceof class_2315)) {
         return var2.method_1031(0.0, 0.25, 0.0);
      }

      class_2350 var4 = (class_2350)var3.method_11654(class_2315.field_10918);
      return var2.method_1031(var4.method_10148() * 0.6, var4.method_10164() * 0.6, var4.method_10165() * 0.6);
   }

   private yu() {
   }

   public static yu o() {
      return M;
   }

   public void q(String var1) {
      if (var1 == null) {
         this.F = yu.k.p;
      } else if ("win".equalsIgnoreCase(var1)) {
         this.F = yu.k.O;
      } else if ("lose".equalsIgnoreCase(var1)) {
         this.F = yu.k.w;
      } else {
         this.F = yu.k.p;
      }

      if (this.F == yu.k.p) {
         this.Z = (boolean)0;
      }
   }

   public void d() {
      this.F = switch (this.F) {
         case p -> yu.k.O;
         case O -> yu.k.w;
         case w -> yu.k.p;
      };
      if (this.F == yu.k.p) {
         yq.E("Arrow Game", "Target off");
      } else {
         yq.E("Arrow Game", "Target " + this.F.U);
      }
   }

   public String B() {
      return this.F == null ? "off" : this.F.U;
   }

   public String A() {
      return this.F == null ? "Off" : this.F.m;
   }

   public void T() {
      this.Z = (boolean)1;
   }

   public boolean i() {
      return this.Z;
   }

   public void h() {
      this.Z = (boolean)0;
   }

   public boolean H() {
      return yh.U().A();
   }

   public boolean x(class_1657 var1) {
      if (var1 == null) {
         return false;
      }

      class_1799 var2 = var1.method_6047();
      return var2 != null && !var2.method_7960() && var2.method_31574(class_1802.field_8204)
         ? var2.method_57353().method_58694(class_9334.field_49646) != null
         : false;
   }

   public void M() {
      class_310 var1 = class_310.method_1551();
      class_746 var2 = var1 != null ? var1.field_1724 : null;
      if (!this.x(var2)) {
         yq.E("Arrow Game", "Hold a filled map in your hand to set the X map");
      } else {
         class_9209 var3 = (class_9209)var2.method_6047().method_57353().method_58694(class_9334.field_49646);
         yh.U().W(var3.comp_2315());
         this.Z = (boolean)0;
         yq.E(
            "Arrow Game",
            "X map set to map #" + var3.comp_2315() + " - frames holding this map are the LOSE target, any other item (elytra, block, ...) is the WIN target"
         );
      }
   }

   public void r() {
      yh.U().W(-1);
      this.Z = (boolean)0;
      yq.E("Arrow Game", "X map cleared - falling back to filled=win, empty=lose");
   }

   private static boolean W(class_1533 var0) {
      return var0 != null && var0.method_6940() != null && !var0.method_6940().method_7960();
   }

   private static boolean Y(class_1533 var0) {
      return W(var0) && var0.method_6940().method_31574(class_1802.field_8204)
         ? var0.method_6940().method_57353().method_58694(class_9334.field_49646) != null
         : false;
   }

   private static boolean c(class_1533 var0) {
      if (!Y(var0)) {
         return false;
      }

      int var1 = yh.U().H();
      return var1 < 0 ? false : ((class_9209)var0.method_6940().method_57353().method_58694(class_9334.field_49646)).comp_2315() == var1;
   }

   private static boolean z(class_1533 var0) {
      return !W(var0) ? false : !c(var0);
   }

   public boolean o(class_310 var1, class_2338 var2) {
      if (!this.C || this.F == yu.k.p) {
         return false;
      }

      if (var1 != null && var1.field_1724 != null && var1.field_1687 != null && var2 != null) {
         class_2680 var3 = var1.field_1687.method_8320(var2);
         if (!(var3.method_26204() instanceof class_2401)) {
            return false;
         }

         class_2338 var4 = hu.f().F(hu.q.Y);
         if (var4 == null) {
            return false;
         }

         if (Y(var2, var4) > 4.0) {
            return false;
         }

         int var5 = !var3.method_11654(class_2741.field_12484) ? 1 : 0;
         this.s(var1, var2, var3);
         if (var5 != 0) {
            this.U(var1, var4);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean a(class_2338 var1) {
      return yh.U().m(var1);
   }

   public void f(class_310 var1, class_2338 var2) {
      if (var1 != null && var1.field_1687 != null && var2 != null) {
         class_2680 var3 = var1.field_1687.method_8320(var2);
         if (var3.method_26204() instanceof class_2428) {
            yh.U().O(var2);
            yq.E("Arrow Game", "Bound noteblock at " + var2.method_23854());
         }
      }
   }

   public void j() {
      yh.U().O(null);
   }

   public boolean K(class_310 var1, class_2338 var2) {
      if (!this.C || this.F == yu.k.p) {
         return false;
      }

      if (var1 == null || var1.field_1724 == null || var1.field_1687 == null || var2 == null) {
         return false;
      }

      if (!this.a(var2)) {
         return false;
      }

      class_2680 var3 = var1.field_1687.method_8320(var2);
      if (!(var3.method_26204() instanceof class_2428)) {
         return false;
      }

      class_2338 var4 = hu.f().F(hu.q.Y);
      if (var4 == null) {
         return false;
      }

      if (this.N != var1.field_1687) {
         this.o();
      }

      long var5 = var1.field_1687.method_75260();
      if (var5 < this.I) {
         return true;
      }

      this.I = var5 + 4L;
      this.q(var1, var3, var2);
      this.s(var1, var2);
      this.N = var1.field_1687;
      this.q.add(new yu.m(var4.method_10062(), this.F, var1.field_1687.method_75260() + 6L));
      return true;
   }

   private void q(class_310 var1, class_2680 var2, class_2338 var3) {
      if (var1.field_1687 != null && var1.field_1724 != null) {
         class_2766 var4 = (class_2766)var2.method_11654(class_2428.field_11325);
         int var5 = (Integer)var2.method_11654(class_2428.field_11324);
         float var6;
         if (var4.method_47889()) {
            var1.field_1687
               .method_8406(class_2398.field_11224, var3.method_10263() + 0.5, var3.method_10264() + 1.2, var3.method_10260() + 0.5, var5 / 24.0, 0.0, 0.0);
            var6 = class_2428.method_49818(var5);
         } else {
            var6 = 1.0F;
         }

         if (!var4.method_47890()) {
            class_6880 var7 = var4.method_11886();
            var1.field_1687.method_8396(var1.field_1724, var3, (class_3414)var7.comp_349(), class_3419.field_15247, 3.0F, var6);
         }
      }
   }

   private void s(class_310 var1, class_2338 var2) {
      this.h(var1);
      class_2338 var3 = var2.method_10074();
      class_2680 var4 = var1.field_1687.method_8320(var3);
      if (var4.method_26204() instanceof class_2426 && var4.method_11654(class_2426.field_10927) == class_2350.field_11036) {
         this.s = var1.field_1687;
         this.P.put(var3, var4);
         ArrayDeque var5 = new ArrayDeque();
         class_2338 var6 = var3.method_10074();
         var5.add(var6);

         for (class_2350 var10 : class_2350.values()) {
            var5.add(var6.method_10093(var10));
         }

         while (!var5.isEmpty() && this.P.size() < 32) {
            class_2338 var14 = (class_2338)var5.removeFirst();
            if (!this.P.containsKey(var14) && !(Y(var14, var3) > 256.0)) {
               class_2680 var16 = var1.field_1687.method_8320(var14);
               class_2680 var18 = C(var16);
               if (var18 != null) {
                  this.P.put(var14, var16);

                  for (class_2350 var13 : class_2350.values()) {
                     var5.add(var14.method_10093(var13));
                  }
               }
            }
         }

         for (Entry var17 : this.P.entrySet()) {
            class_2680 var19 = ((class_2680)var17.getValue()).method_26204() instanceof class_2426
               ? (class_2680)((class_2680)var17.getValue()).method_11657(class_2741.field_12484, true)
               : C((class_2680)var17.getValue());
            var1.field_1687.method_8652((class_2338)var17.getKey(), var19, 2);
         }

         this.g = var1.field_1687.method_75260() + 4L;
      }
   }

   static class_2680 C(class_2680 var0) {
      if (var0.method_26204() instanceof class_2457) {
         return (class_2680)var0.method_11657(class_2457.field_11432, 15);
      } else if (var0.method_26204() instanceof class_2462) {
         return (class_2680)var0.method_11657(class_2741.field_12484, true);
      } else {
         return var0.method_26204() instanceof class_2453 ? (class_2680)var0.method_11657(class_2741.field_12548, true) : null;
      }
   }

   private void h(class_310 var1) {
      if (var1.field_1687 != null && var1.field_1687 == this.s) {
         for (Entry var3 : this.P.entrySet()) {
            class_2680 var4 = ((class_2680)var3.getValue()).method_26204() instanceof class_2426
               ? (class_2680)((class_2680)var3.getValue()).method_11657(class_2741.field_12484, true)
               : C((class_2680)var3.getValue());
            if (var4 != null && var1.field_1687.method_8320((class_2338)var3.getKey()).equals(var4)) {
               var1.field_1687.method_8652((class_2338)var3.getKey(), (class_2680)var3.getValue(), 2);
            }
         }
      }

      this.P.clear();
      this.s = null;
      this.g = -1L;
   }

   private static class_238 N(class_243 var0) {
      return new class_238(
         var0.field_1352 - 64.0, var0.field_1351 - 64.0, var0.field_1350 - 64.0, var0.field_1352 + 64.0, var0.field_1351 + 64.0, var0.field_1350 + 64.0
      );
   }

   private class_243 d(class_1533 var1) {
      class_238 var2 = var1.method_5829();
      double var3 = 0.2;
      double var5 = Math.max(0.0, var2.method_17939() - 2.0 * var3);
      double var7 = Math.max(0.0, var2.method_17940() - 2.0 * var3);
      double var9 = Math.max(0.0, var2.method_17941() - 2.0 * var3);
      return new class_243(
         var2.field_1323 + var3 + this.a.nextDouble() * var5,
         var2.field_1322 + var3 + this.a.nextDouble() * var7,
         var2.field_1321 + var3 + this.a.nextDouble() * var9
      );
   }

   public void L(class_310 var1) {
      com.larpclient.i.r(var1);
      if (this.N != var1.field_1687) {
         this.o();
         this.N = var1.field_1687;
      }

      if (var1.field_1687 != null) {
         if (this.g >= 0L && var1.field_1687.method_75260() >= this.g) {
            this.h(var1);
         }

         if (!this.C) {
            this.Z = (boolean)0;
            this.d(var1);
         }

         Iterator var2 = this.q.iterator();

         while (var2.hasNext()) {
            yu.m var3 = (yu.m)var2.next();
            if (var1.field_1687.method_75260() >= var3.due()) {
               var2.remove();
               this.Q(var1, var3.dispenser(), var3.target());
            }
         }

         if (!this.U.isEmpty()) {
            long var8 = var1.field_1687.method_75260();
            long var4 = var8 - this.j;
            if (var4 > 0L) {
               this.j = var8;

               for (long var6 = 0L; var6 < var4 && !this.U.isEmpty(); var6++) {
                  this.o(var1);
               }
            }
         }
      }
   }

   private void o(class_310 var1) {
      Iterator var2 = this.U.iterator();

      while (var2.hasNext()) {
         yu.u var3 = (yu.u)var2.next();
         if (var3.B == null) {
            this.j(var1, var3);
            var2.remove();
         } else if (var3.F <= 0) {
            var3.n++;
            var3.P *= 0.99;
            var3.J = var3.J * 0.99 - 0.05;
            var3.C *= 0.99;
            var3.j = var3.j.method_1031(var3.P, var3.J, var3.C);
            double var15 = Math.sqrt(var3.P * var3.P + var3.C * var3.C);
            var3.B.method_36456((float)Math.toDegrees(Math.atan2(var3.P, var3.C)));
            var3.B.method_36457((float)Math.toDegrees(Math.atan2(var3.J, Math.max(var15, 1.0E-4))));
            var3.B.method_5814(var3.j.field_1352, var3.j.field_1351, var3.j.field_1350);
            if (var3.n >= 120 || var3.M != null && r(var3.j.method_1023(var3.P, var3.J, var3.C), var3.j, var3.M) <= 0.5) {
               var3.H = new class_243(var3.j.field_1352, var3.j.field_1351, var3.j.field_1350);
               var3.B.method_31472();
               var3.B = null;
               var2.remove();
               this.j(var1, var3);
            }
         } else if (var3.d >= var3.F) {
            var3.H = var3.j;
            var3.B.method_31472();
            var3.B = null;
            var2.remove();
            this.j(var1, var3);
         } else {
            class_243 var4 = var3.j;
            var3.d = b(var3.d, var3.F);
            int var5 = Math.min((int)var3.d, var3.F - 1);
            double var6 = var3.d - var5;
            double[] var8 = var5 == 0 ? new double[3] : var3.u[var5 - 1];
            double[] var9 = var3.u[var5];
            var3.j = new class_243(
               var3.t.field_1352 + var8[0] + (var9[0] - var8[0]) * var6,
               var3.t.field_1351 + var8[1] + (var9[1] - var8[1]) * var6,
               var3.t.field_1350 + var8[2] + (var9[2] - var8[2]) * var6
            );
            var3.P = var3.j.field_1352 - var4.field_1352;
            var3.J = var3.j.field_1351 - var4.field_1351;
            var3.C = var3.j.field_1350 - var4.field_1350;
            double var10 = Math.sqrt(var3.P * var3.P + var3.C * var3.C);
            float var12 = (float)Math.toDegrees(Math.atan2(var3.P, var3.C));
            float var13 = (float)Math.toDegrees(Math.atan2(var3.J, Math.max(var10, 1.0E-4)));
            var3.Z = t(var3.Z, var12);
            var3.I = t(var3.I, var13);
            var3.B.method_36456(var3.Z);
            var3.B.method_36457(var3.I);
            var3.B.method_5814(var3.j.field_1352, var3.j.field_1351, var3.j.field_1350);
            var3.n++;
            int var14 = !(var3.d >= var3.F) && (var3.M == null || !(r(var4, var3.j, var3.M) <= 0.5)) ? 0 : 1;
            if (var14 != 0) {
               var3.H = var3.j;
               var3.B.method_31472();
               var3.B = null;
               var2.remove();
               this.j(var1, var3);
            }
         }
      }
   }

   static double b(double var0, int var2) {
      return Math.min(var0 + 0.85, var2);
   }

   private void U(class_310 var1, class_2338 var2) {
      this.Q(var1, var2, this.F);
   }

   private void Q(class_310 var1, class_2338 var2, yu.k var3) {
      this.N = var1.field_1687;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      class_243 var6 = new class_243(var2.method_10263() + 0.5, var2.method_10264() + 0.5, var2.method_10260() + 0.5);
      class_238 var7 = N(var6);
      yh var8 = yh.U();
      boolean var9 = var8.A();
      yq.R("Arrow beginShot: dispenser " + var2 + ", target " + var3.U + ", xMapSet=" + var9 + ", xMapId=" + var8.H() + ", serverKey=" + var8.E());
      if (var1.field_1687 != null) {
         for (class_1533 var11 : var1.field_1687.method_8390(class_1533.class, var7, var0 -> !var0.method_31481() && A(var0))) {
            if (!(var11.method_5707(var6) > 4096.0)) {
               byte var12;
               String var13;
               if (var9) {
                  if (c(var11)) {
                     var12 = 0;
                     var13 = "xMap";
                  } else {
                     if (!W(var11)) {
                        yq.R("Arrow frame " + var11.method_24515() + " skipped (empty)");
                        continue;
                     }

                     var12 = 1;
                     var13 = "item";
                  }
               } else {
                  byte var14 = W(var11);
                  var12 = var14;
                  var13 = "filled=" + var14;
               }

               yq.R(
                  "Arrow frame "
                     + var11.method_24515()
                     + " id="
                     + var11.method_5628()
                     + " dist="
                     + String.format("%.1f", Math.sqrt(var11.method_5707(var6)))
                     + " "
                     + var13
                     + " -> "
                     + (var12 != 0 ? "WIN" : "LOSE")
               );
               if (var12 != 0) {
                  var4.add(var11);
               } else {
                  var5.add(var11);
               }
            }
         }
      }

      ArrayList var17 = var3 == yu.k.O ? var4 : var5;
      if (var17.isEmpty()) {
         yq.E(
            "Arrow Game",
            "No "
               + var3.U
               + " frames found near the dispenser ("
               + var4.size()
               + (var9 ? " item" : " filled")
               + ", "
               + var5.size()
               + (var9 ? " x-map" : " empty")
               + ")"
         );
      } else {
         ArrayList var19 = new ArrayList();

         for (yu.u var22 : this.U) {
            var19.add(var22.b);
         }

         ArrayList var21 = new ArrayList();

         for (class_1533 var25 : var17) {
            if (var19.contains(var25.method_5628())) {
               yq.R("Arrow frame " + var25.method_5628() + " already targeted, not re-firing");
            } else {
               var21.add(var25);
            }
         }

         if (var21.isEmpty()) {
            yq.E("Arrow Game", "Every reachable " + var3.U + " frame already has an arrow in flight; skipping this flick");
         } else {
            var17 = var21;
            int var24 = 0;
            if (var1.field_1687 != null) {
               double var26 = 64.0;
               class_238 var16 = new class_238(
                  var6.field_1352 - var26,
                  var6.field_1351 - var26,
                  var6.field_1350 - var26,
                  var6.field_1352 + var26,
                  var6.field_1351 + var26,
                  var6.field_1350 + var26
               );
               var24 = var1.field_1687.method_8390(class_1533.class, var16, var0 -> !var0.method_31481() && A(var0)).size();
            }

            yq.E(
               "Arrow Game",
               "Scan found "
                  + var4.size()
                  + (var9 ? " item" : " filled")
                  + " and "
                  + var5.size()
                  + (var9 ? " x-map" : " empty")
                  + " frames in range ("
                  + (var4.size() + var5.size())
                  + " total; "
                  + var24
                  + " item frames within 64 blocks); target is "
                  + var3.U
            );
            class_243 var27 = H(var1.field_1687, var2);
            class_1533 var15 = (class_1533)var17.get(this.a.nextInt(var17.size()));
            yu.u var28 = new yu.u();
            var28.b = var15.method_5628();
            var28.j = var27;
            var28.M = this.d(var15);
            var28.t = var28.j;
            if (var1.field_1687 != null) {
               var28.B = (class_1667)class_1299.field_6122.method_5883(var1.field_1687, class_3730.field_16462);
               if (var28.B != null) {
                  var28.B.method_5838(this.D--);
                  var28.B.method_5875(true);
                  var28.B.method_7433(true);
                  var28.B.method_5814(var28.j.field_1352, var28.j.field_1351, var28.j.field_1350);
                  this.z(var28);
                  var1.field_1687.method_53875(var28.B);
               }

               var1.field_1687
                  .method_43128(
                     null,
                     var28.j.field_1352,
                     var28.j.field_1351,
                     var28.j.field_1350,
                     class_3417.field_14600,
                     class_3419.field_15254,
                     1.0F,
                     0.4F + this.a.nextFloat() * 1.2F
                  );
            }

            this.U.add(var28);
            this.O(var1, var2);
            if (var1.field_1687 != null) {
               this.j = var1.field_1687.method_75260();
            }

            yq.E("Arrow Game", "Firing at " + var3.U + " frame " + var28.b);
         }
      }
   }

   private void z(yu.u var1) {
      if (var1.M != null) {
         var1.t = var1.j;
         double var2 = 11.7 + this.a.nextDouble() * 0.8000000000000007;
         var1.W = var2;
         if (this.d(var1)) {
            var1.d = 0.0;
            this.m(var1, var1.Q[0][0], var1.Q[0][1], var1.Q[0][2]);
            yq.R(
               "Arrow trajectory targetCrest="
                  + String.format("%.1f", var1.W)
                  + " speed="
                  + String.format("%.2f", var1.m)
                  + " pitch="
                  + String.format("%.1f", var1.K)
                  + "deg crest="
                  + String.format("%.1f", var1.Y)
                  + " ticks="
                  + var1.F
                  + " to "
                  + var1.M
            );
         } else {
            class_243 var4 = var1.M.method_1020(var1.j).method_1029();
            var1.P = var4.field_1352 * 1.1;
            var1.J = var4.field_1351 * 1.1;
            var1.C = var4.field_1350 * 1.1;
            this.m(var1, var1.P, var1.J, var1.C);
            yq.R("Arrow trajectory FAILED -> straight shot from " + var1.j + " to " + var1.M);
         }
      }
   }

   private void m(yu.u var1, double var2, double var4, double var6) {
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var2, var6));
      float var11 = (float)Math.toDegrees(Math.atan2(var4, var8));
      var1.Z = var10;
      var1.I = var11;
      if (var1.B != null) {
         var1.B.method_36456(var10);
         var1.B.method_36457(var11);
         var1.B.field_5982 = var10;
         var1.B.field_6004 = var11;
      }
   }

   private boolean d(yu.u var1) {
      double var2 = var1.M.field_1352 - var1.j.field_1352;
      double var4 = var1.M.field_1351 - var1.j.field_1351;
      double var6 = var1.M.field_1350 - var1.j.field_1350;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      if (var8 < 0.001) {
         return false;
      }

      yu.n.i var10 = yu.n.I(var2, var4, var6, var1.W);
      if (var10 == null) {
         return false;
      }

      var1.u = var10.m;
      var1.Q = var10.b;
      var1.F = var10.r;
      var1.K = var10.J;
      var1.m = var10.F;
      var1.N = 0.0;
      var1.Y = var10.V;
      var1.T = var10.r;
      var1.P = var10.d;
      var1.J = var10.u;
      var1.C = var10.p;
      return true;
   }

   private static double r(class_243 var0, class_243 var1, class_243 var2) {
      class_243 var3 = var1.method_1020(var0);
      class_243 var4 = var2.method_1020(var0);
      double var5 = var3.method_1026(var3);
      double var7 = var5 > 1.0E-9 ? var4.method_1026(var3) / var5 : 0.0;
      var7 = Math.max(0.0, Math.min(1.0, var7));
      return var2.method_1022(var0.method_1019(var3.method_1021(var7)));
   }

   private static float t(float var0, float var1) {
      float var2 = ((var1 - var0) % 360.0F + 540.0F) % 360.0F - 180.0F;
      return var0 + var2 * 0.2F;
   }

   private void j(class_310 var1, yu.u var2) {
      if (var1 != null && var1.field_1687 != null) {
         class_243 var3 = var2.H != null ? var2.H : var2.j;
         class_1533 var5 = var1.field_1687.method_8469(var2.b) instanceof class_1533 var6 && !var6.method_31481() ? var6 : null;
         if (var5 == null) {
            yq.E("Arrow Game", "Arrow target unloaded before impact; no visual item dropped");
         } else {
            yq.R(
               "Arrow landing target: id="
                  + var5.method_5628()
                  + " at "
                  + var5.method_24515()
                  + " (arrow landed at "
                  + String.format("%.1f,%.1f,%.1f", var3.field_1352, var3.field_1351, var3.field_1350)
                  + ")"
            );
            this.g(var1, var5, var3);
            com.larpclient.i.S(var1, var5);
            yq.E("Arrow Game", "Visual hit: item popped from frame " + var5.method_5628());
         }
      }
   }

   private void g(class_310 var1, class_1533 var2, class_243 var3) {
      if (var1 != null && var1.field_1687 != null && var2 != null) {
         class_243 var4 = var3 != null ? var3 : var2.method_5829().method_1005();
         if (var1.method_1483() != null) {
            var1.method_1483()
               .method_4873(
                  new class_1109(
                     class_3417.field_15151.comp_3319(),
                     class_3419.field_15254,
                     2.0F,
                     0.8F + this.a.nextFloat() * 0.4F,
                     class_5819.method_43047(),
                     false,
                     0,
                     class_1114.field_5478,
                     var4.field_1352,
                     var4.field_1351,
                     var4.field_1350,
                     false
                  )
               );
         }
      }
   }

   private boolean e(class_1533 var1) {
      if (var1 == null) {
         return false;
      }

      if (yh.U().A()) {
         return this.F == yu.k.O ? z(var1) : c(var1);
      }

      byte var2 = W(var1);
      return (boolean)(this.F == yu.k.O ? var2 : var2 == 0);
   }

   private void s(class_310 var1, class_2338 var2, class_2680 var3) {
      this.i = var2.method_10062();
      if (this.H == null) {
         this.H = var3;
      }

      int var4 = !var3.method_11654(class_2741.field_12484) ? 1 : 0;
      if (var1.field_1687 != null) {
         var1.field_1687.method_8501(var2, (class_2680)var3.method_11657(class_2741.field_12484, Boolean.valueOf((boolean)var4)));
      }

      if (var1.field_1724 != null) {
         var1.field_1724.method_5783(class_3417.field_14962, 0.3F, var4 != 0 ? 0.6F : 0.5F);
      }
   }

   private void d(class_310 var1) {
      if (var1 != null && var1.field_1687 != null && this.i != null) {
         if (this.H != null) {
            var1.field_1687.method_8501(this.i, this.H);
         }

         this.i = null;
         this.H = null;
      }
   }

   public void o() {
      this.Z = (boolean)0;
      this.h(class_310.method_1551());

      for (yu.u var2 : this.U) {
         if (var2.B != null) {
            var2.B.method_31472();
         }
      }

      this.U.clear();
      this.q.clear();
      this.I = -1L;
      this.j = -1L;
      class_310 var3 = class_310.method_1551();
      this.d(var3);
   }

   public void O(class_310 var1, class_2338 var2) {
      if (var1 != null && var1.field_1687 != null && var2 != null) {
         String var3 = hu.f().N(var1, var2);
         int[] var4 = this.l.get(var3);
         if (var4 == null) {
            this.k.merge(var3, 1, Integer::sum);
         } else {
            ArrayList var5 = new ArrayList();

            for (int var6 = 0; var6 < var4.length; var6++) {
               if (var4[var6] > 0) {
                  var5.add(var6);
               }
            }

            if (!var5.isEmpty()) {
               int var7 = (Integer)var5.get(this.a.nextInt(var5.size()));
               var4[var7] = Math.max(0, var4[var7] - 1);
            }
         }
      }
   }

   public void C(class_310 var1, class_1716 var2) {
      if (var1 != null && var1.field_1687 != null && var2 != null) {
         this.A = 0;
         this.t = 0;
         int[] var3 = new int[9];
         byte var4 = 0;

         for (class_1735 var6 : var2.field_7761) {
            if (!(var6.field_7871 instanceof class_1661) && var6.method_34266() >= 0 && var6.method_34266() <= 8 && var6.method_7681()) {
               class_1799 var7 = var6.method_7677();
               if (S(var7)) {
                  var3[var6.method_34266()] = var7.method_7947();
                  var4 = 1;
               }
            }
         }

         if (var4 != 0) {
            String var12 = hu.f().W();
            if (var12 != null) {
               int[] var13 = this.J.get(var12);
               this.J.put(var12, var3);
               int[] var14 = this.l.get(var12);
               if (var14 == null) {
                  var14 = (int[])var3.clone();
                  this.l.put(var12, var14);
               }

               for (int var8 = 0; var8 < var14.length; var8++) {
                  if (var3[var8] == 0) {
                     var14[var8] = 0;
                  } else if (var13 != null && var13[var8] != 0) {
                     var14[var8] = Math.min(var14[var8], var3[var8]);
                  } else {
                     var14[var8] = var3[var8];
                  }
               }

               Integer var15 = this.k.remove(var12);

               for (int var9 = var15 == null ? 0 : var15; var9 > 0; var9--) {
                  ArrayList var10 = new ArrayList();

                  for (int var11 = 0; var11 < var14.length; var11++) {
                     if (var14[var11] > 0) {
                        var10.add(var11);
                     }
                  }

                  if (var10.isEmpty()) {
                     break;
                  }

                  int var16 = (Integer)var10.get(this.a.nextInt(var10.size()));
                  var14[var16] = Math.max(0, var14[var16] - 1);
               }
            }
         }
      }
   }

   public void r(String var1, int var2, class_1799 var3) {
      if (var1 != null) {
         this.h = null;
         this.e = null;
         int var4 = S(var3) ? var3.method_7947() : 0;
         if (var2 >= 0 && var2 <= 8) {
            int[] var10 = this.J.get(var1);
            int[] var11 = this.l.get(var1);
            if (var10 == null || var11 == null) {
               return;
            }

            int var12 = var10[var2];
            int var13 = var11[var2];
            int var9 = this.o(var12, var13, var4);
            var10[var2] = var4;
            var11[var2] = var9;
         } else if (var2 >= 9 && var2 <= 44) {
            int var5 = var2 >= 36 ? var2 - 36 : var2;
            if (var5 < 0 || var5 >= 36) {
               return;
            }

            int var6 = this.E[var5];
            int var7 = this.m[var5];
            int var8 = this.o(var6, var7, var4);
            this.E[var5] = var4;
            this.m[var5] = var8;
         }
      }
   }

   public void G(int var1, class_1799 var2) {
      if (var1 >= 0 && var1 < 36) {
         this.h = null;
         this.e = null;
         if (this.m[var1] != 0 || this.E[var1] != 0 || this.A != 0) {
            int var3 = S(var2) ? var2.method_7947() : 0;
            int var4 = this.E[var1];
            int var5 = this.m[var1];
            this.E[var1] = var3;
            this.m[var1] = this.o(var4, var5, var3);
         }
      }
   }

   private int o(int var1, int var2, int var3) {
      if (var3 <= 0) {
         if (var1 > 0 && var2 > 0) {
            this.A += var1;
            this.t = this.t + Math.max(0, var1 - var2);
         }

         return 0;
      } else {
         if (var1 == var3) {
            return Math.min(var2, var3);
         }

         if (var3 > var1) {
            int var8 = var3 - var1;
            int var9 = var8;
            if (this.A > 0) {
               int var6 = Math.min(var8, this.A);
               int var7 = Math.round((float)((long)this.t * var6 / Math.max(1, this.A)));
               var9 = var8 - var7;
               this.A -= var6;
               this.t = Math.max(0, this.t - var7);
            }

            return Math.min(var2 + Math.max(0, var9), var3);
         } else {
            int var4 = var1 - var3;
            int var5 = Math.round((float)((long)var2 * var4 / Math.max(1, var1)));
            this.A += var4;
            this.t = this.t + Math.max(0, var4 - Math.min(var5, var4));
            return Math.max(0, var2 - var5);
         }
      }
   }

   public class_1799 t(class_310 var1, int var2, class_1799 var3) {
      if (var1 == null || var1.field_1687 == null || var3 == null || var3.method_7960()) {
         return null;
      }

      if (var2 >= 0 && var2 <= 8) {
         hu var4 = hu.f();
         String var5 = var4.W();
         if (var5 == null) {
            return null;
         } else {
            int[] var6 = this.l.get(var5);
            if (var6 == null) {
               return null;
            } else {
               int var7 = Math.min(var6[var2], var3.method_7947());
               if (var7 == var3.method_7947()) {
                  return null;
               } else {
                  return var7 <= 0 ? class_1799.field_8037 : var3.method_46651(var7);
               }
            }
         }
      } else {
         return null;
      }
   }

   public class_1799 H(int var1, class_1799 var2) {
      if (var2 == null || var2.method_7960() || !S(var2)) {
         return null;
      } else if (var1 >= 0 && var1 < 36) {
         int var3 = this.m[var1];
         return var3 > 0 && var3 < var2.method_7947() ? var2.method_46651(var3) : null;
      } else {
         return null;
      }
   }

   public class_1799 z(class_1799 var1) {
      if (var1 != null && !var1.method_7960() && S(var1)) {
         if (this.e != null && !this.e.method_7960() && this.h != null && var1.method_31574(this.h.method_7909()) && var1.method_7947() == this.h.method_7947()
            )
          {
            return this.e.method_7972();
         }

         if (this.A > 0 && this.t > 0) {
            int var2 = var1.method_7947() - this.t;
            if (var2 >= var1.method_7947()) {
               return var1;
            } else {
               return var2 <= 0 ? class_1799.field_8037 : var1.method_46651(var2);
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public void c(class_1799 var1, class_1799 var2) {
      if (var1 != null && !var1.method_7960()) {
         this.h = var1;
         this.e = var2 != null && !var2.method_7960() ? var2 : var1;
      } else {
         this.h = null;
         this.e = null;
      }
   }

   public boolean p() {
      return this.C && this.F != yu.k.p;
   }

   private static boolean S(class_1799 var0) {
      return var0.method_31574(class_1802.field_8107) || var0.method_31574(class_1802.field_8236) || var0.method_31574(class_1802.field_8087);
   }

   private static double Y(class_2338 var0, class_2338 var1) {
      int var2 = var0.method_10263() - var1.method_10263();
      int var3 = var0.method_10264() - var1.method_10264();
      int var4 = var0.method_10260() - var1.method_10260();
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   public enum k {
      p("off", "Off"),
      O("win", "Win (filled frames)"),
      w("lose", "Lose (empty frames)");

      public final String U;
      public final String m;

      k(String var3, String var4) {
         this.U = var3;
         this.m = var4;
      }
   }

   private record m(class_2338 dispenser, yu.k target, long due) {
   }

   private static final class n {
      static final int J = 24;
      static final int A = 80;

      private static double G(int var0) {
         return 0.99 * (1.0 - Math.pow(0.99, var0)) / 0.010000000000000009;
      }

      static yu.n.i I(double var0, double var2, double var4, double var6) {
         double var8 = Math.sqrt(var0 * var0 + var4 * var4);
         if (var8 < 0.001) {
            return null;
         }

         int var10 = 24;
         double var11 = Double.MAX_VALUE;
         double var13 = 0.0;

         for (int var15 = 24; var15 <= 80; var15++) {
            double var16 = G(var15);
            double var18 = (var2 + 0.05 * (var15 - var16) / 0.010000000000000009) / var16;
            double var20 = 0.0;
            double var22 = var18;
            double var24 = 0.0;

            for (int var26 = 0; var26 < var15; var26++) {
               var22 = var22 * 0.99 - 0.05;
               var20 += var22;
               if (var20 > var24) {
                  var24 = var20;
               }
            }

            double var48 = Math.abs(var24 - var6);
            if (var48 < var11 - 1.0E-9 || Math.abs(var48 - var11) <= 1.0E-9 && var15 > var10) {
               var11 = var48;
               var10 = var15;
               var13 = var24;
            }
         }

         int var42 = var10;
         double var43 = G(var42);
         double var44 = var0 / var43;
         double var45 = var4 / var43;
         double var46 = (var2 + 0.05 * (var42 - var43) / 0.010000000000000009) / var43;
         double[][] var47 = new double[var42][];
         double[][] var25 = new double[var42][];
         double var49 = 0.0;
         double var28 = 0.0;
         double var30 = 0.0;
         double var32 = var44;
         double var34 = var46;
         double var36 = var45;

         for (int var38 = 0; var38 < var42; var38++) {
            var32 *= 0.99;
            var34 = var34 * 0.99 - 0.05;
            var36 *= 0.99;
            var49 += var32;
            var28 += var34;
            var30 += var36;
            var47[var38] = new double[]{var49, var28, var30};
            var25[var38] = new double[]{var32, var34, var36};
         }

         var47[var42 - 1][0] = var0;
         var47[var42 - 1][1] = var2;
         var47[var42 - 1][2] = var4;
         double var50 = Math.toDegrees(Math.atan2(var46, Math.hypot(var44, var45)));
         double var40 = Math.sqrt(var44 * var44 + var46 * var46 + var45 * var45);
         return new yu.n.i(var42, var47, var25, var44, var46, var45, var13, var50, var40);
      }

      static final class i {
         final int r;
         final double[][] m;
         final double[][] b;
         final double d;
         final double u;
         final double p;
         final double V;
         final double J;
         final double F;

         i(int var1, double[][] var2, double[][] var3, double var4, double var6, double var8, double var10, double var12, double var14) {
            this.r = var1;
            this.m = var2;
            this.b = var3;
            this.d = var4;
            this.u = var6;
            this.p = var8;
            this.V = var10;
            this.J = var12;
            this.F = var14;
         }
      }
   }

   private static final class u {
      class_243 j;
      class_243 M;
      class_243 H;
      int b;
      int n;
      float Z;
      float I;
      double P;
      double J;
      double C;
      class_1667 B;
      double[][] u;
      double[][] Q;
      int F;
      double d;
      class_243 t;
      double W;
      double K;
      double m;
      double N;
      double Y;
      int T;
   }
}
