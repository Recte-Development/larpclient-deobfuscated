package com.larpclient;

import java.util.List;
import net.minecraft.class_243;

public final class j3 {
   public static j3.m v(List<fb.d> var0, double var1, j3.b var3) {
      if (var0.size() >= 2 && !(var1 - ((fb.d)var0.getLast()).time() >= var3.duration)) {
         int var4 = var3.smoothing ? Math.clamp(var3.subdivisions, 1, 4) : 1;
         j3.p var5 = new j3.p(var0.size() * var4 * 12);
         j3.p var6 = new j3.p(var0.size() * var4 * 20 + var0.size() * 4);

         for (int var7 = 1; var7 < var0.size(); var7++) {
            fb.d var8 = (fb.d)var0.get(var7 - 1);
            fb.d var9 = (fb.d)var0.get(var7);
            if (var8.stroke() == var9.stroke() && !(var8.tip().method_1025(var9.tip()) < 1.0E-9)) {
               fb.d var10 = var7 > 1 && ((fb.d)var0.get(var7 - 2)).stroke() == var8.stroke() ? (fb.d)var0.get(var7 - 2) : var8;
               fb.d var11 = var7 + 1 < var0.size() && ((fb.d)var0.get(var7 + 1)).stroke() == var8.stroke() ? (fb.d)var0.get(var7 + 1) : var9;
               int var12 = var7 - 1;

               while (var12 > 0 && ((fb.d)var0.get(var12 - 1)).stroke() == var8.stroke()) {
                  var12--;
               }

               int var13 = var7;

               while (var13 + 1 < var0.size() && ((fb.d)var0.get(var13 + 1)).stroke() == var8.stroke()) {
                  var13++;
               }

               j3.d var14 = k(var10, var8, var9, var11, 0.0, var7 - 1 - var12, var13 - var12, var1, var3);

               for (int var15 = 1; var15 <= var4; var15++) {
                  double var16 = (double)var15 / var4;
                  j3.d var18 = k(var10, var8, var9, var11, var16, var7 - 1 - var12 + var16, var13 - var12, var1, var3);
                  i(var5, var6, var14, var18, var3, var8.serial() + (var9.serial() - var8.serial()) * var16);
                  var14 = var18;
               }

               if (var3.sparks && var9.serial() % 7L == 0L && var7 > 1) {
                  Z(var6, var8, var9, var1, var3);
               }
            }
         }

         return new j3.m(var5, var6);
      } else {
         return j3.m.EMPTY;
      }
   }

   private static j3.d k(fb.d var0, fb.d var1, fb.d var2, fb.d var3, double var4, double var6, int var8, double var9, j3.b var11) {
      class_243 var12 = var11.smoothing ? qd.y(var0.tip(), var1.tip(), var2.tip(), var3.tip(), var4) : var1.tip().method_35590(var2.tip(), var4);
      class_243 var13 = var11.smoothing ? qd.y(var0.root(), var1.root(), var2.root(), var3.root(), var4) : var1.root().method_35590(var2.root(), var4);
      class_243 var14 = var13.method_1020(var12);
      double var15 = var14.method_1033();
      if (var15 < 1.0E-8) {
         var14 = new class_243(0.0, -1.0, 0.0);
      } else {
         var14 = var14.method_1021(1.0 / var15);
      }

      double var17 = Math.clamp((var9 - (var1.time() + (var2.time() - var1.time()) * var4)) / var11.duration, 0.0, 1.0);
      double var19 = Math.clamp(var6 / Math.max(1.0, var8 * 0.2), 0.0, 1.0);
      var19 = var19 * var19 * (3.0 - 2.0 * var19);
      double var21 = Math.pow(1.0 - var17, 1.65) * var19;
      double var23 = var15 * var11.width * (0.78 + 0.22 * Math.cos(var17 * 3.0)) * Math.sqrt(var21);
      if (var11.style.equals("Void")) {
         var23 *= 0.7 + 0.3 * (1.0 - var17);
      }

      int var25 = ei.W(var11.secondary, var11.primary, 1.0 - var17 * 0.8);
      if (var11.style.equals("Prismatic")) {
         int var26 = ei.X((float)((0.53 + var17 * 0.35 + var9 * 0.08) % 1.0), 0.5F, 1.0F);
         var25 = ei.W(var25, var26, 0.45);
      }

      return new j3.d(var12, var14, var23, var25, var21 * var11.intensity);
   }

   private static void i(j3.p var0, j3.p var1, j3.d var2, j3.d var3, j3.b var4, double var5) {
      if (!(var2.width < 1.0E-7) || !(var3.width < 1.0E-7)) {
         boolean var7 = var4.style.equals("Void");
         int var8 = var7 ? ei.W(var2.color, -16317420, 0.95) : var2.color;
         int var9 = var7 ? ei.W(var3.color, -16317420, 0.95) : var3.color;
         w(var0, var2, var3, 1.0, 0.8, var8, var9, 0.0, var7 ? 0.68 : 0.09);
         w(var0, var2, var3, 0.8, 0.12, var8, var9, var7 ? 0.68 : 0.09, var7 ? 0.85 : 0.32);
         w(var0, var2, var3, 0.12, 0.0, var8, var9, var7 ? 0.85 : 0.32, 0.05);
         double var10 = var4.style.equals("Prismatic") ? 0.009 : 0.014;
         J(var1, var2, var3, 0.0, var10, ei.W(var2.color, -1, 0.72), ei.W(var3.color, -1, 0.72), 0.92);
         J(var1, var2, var3, 0.56, var10 * 0.46, var2.color, var3.color, 0.34);
         if (var7) {
            J(var1, var2, var3, 0.95, var10 * 0.65, var2.color, var3.color, 0.7);
         }

         if (var4.style.equals("Prismatic")) {
            double var12 = Math.sin(var5 * 1.3);
            double var14 = Math.max(0.0, var12) * Math.max(0.0, var12);
            J(var1, var2, var3, 0.84, var10 * 0.5, ei.W(var2.color, var4.secondary, 0.7), ei.W(var3.color, var4.secondary, 0.7), 0.48 * var14);
         }

         if (var4.halo) {
            J(var1, var2, var3, 0.0, var10 * 4.5, var2.color, var3.color, 0.085);
            J(var1, var2, var3, 0.0, var10 * 2.5, var2.color, var3.color, 0.13);
         }
      }
   }

   private static void w(j3.p var0, j3.d var1, j3.d var2, double var3, double var5, int var7, int var8, double var9, double var11) {
      Q(
         var0,
         var1.at(var3),
         var2.at(var3),
         var2.at(var5),
         var1.at(var5),
         ei.R(var7, var1.opacity * var9),
         ei.R(var8, var2.opacity * var9),
         ei.R(var8, var2.opacity * var11),
         ei.R(var7, var1.opacity * var11)
      );
   }

   private static void J(j3.p var0, j3.d var1, j3.d var2, double var3, double var5, int var7, int var8, double var9) {
      class_243 var11 = var1.at(var3);
      class_243 var12 = var2.at(var3);
      class_243 var13 = var1.axis.method_1021(var5 * Math.sqrt(Math.max(0.0, var1.opacity)) * 0.5);
      class_243 var14 = var2.axis.method_1021(var5 * Math.sqrt(Math.max(0.0, var2.opacity)) * 0.5);
      int var15 = ei.R(var7, var1.opacity * var9);
      int var16 = ei.R(var8, var2.opacity * var9);
      Q(var0, var11.method_1019(var13), var12.method_1019(var14), var12.method_1020(var14), var11.method_1020(var13), var15, var16, var16, var15);
   }

   private static void Z(j3.p var0, fb.d var1, fb.d var2, double var3, j3.b var5) {
      double var6 = var3 - var2.time();
      double var8 = Math.min(0.3, var5.duration * 1.2);
      if (!(var6 < 0.0) && !(var6 >= var8)) {
         class_243 var10 = var2.tip().method_1020(var1.tip());
         if (!(var10.method_1027() < 1.0E-4)) {
            class_243 var11 = var2.root().method_1020(var2.tip()).method_1029();
            double var12 = (var2.serial() & 1L) == 0L ? -1.0 : 1.0;
            class_243 var14 = var10.method_1029().method_1021(0.3).method_1019(var11.method_1036(new class_243(0.0, 0.0, 1.0)).method_1021(var12 * 0.25));
            class_243 var15 = var2.tip().method_1019(var14.method_1021(var6)).method_1031(0.0, -0.18 * var6 * var6, 0.0);
            class_243 var16 = var15.method_1020(var14.method_1021(0.035));
            class_243 var17 = new class_243(-var14.field_1351, var14.field_1352, 0.0).method_1029().method_1021(0.0025 * (1.0 - var6 / var8));
            int var18 = ei.R(ei.W(var5.primary, -1, 0.55), (1.0 - var6 / var8) * var5.intensity * 0.8);
            Q(var0, var15.method_1019(var17), var16.method_1019(var17), var16.method_1020(var17), var15.method_1020(var17), var18, 0, 0, var18);
         }
      }
   }

   private static void Q(j3.p var0, class_243 var1, class_243 var2, class_243 var3, class_243 var4, int var5, int var6, int var7, int var8) {
      var0.j(var1, var5);
      var0.j(var2, var6);
      var0.j(var3, var7);
      var0.j(var4, var8);
   }

   private j3() {
   }

   public record b(
      String style,
      double duration,
      double width,
      double intensity,
      int primary,
      int secondary,
      boolean halo,
      boolean sparks,
      boolean smoothing,
      int subdivisions
   ) {
   }

   private record d(class_243 tip, class_243 axis, double width, int color, double opacity) {
      class_243 at(double var1) {
         return this.tip.method_1019(this.axis.method_1021(this.width * var1));
      }
   }

   public record m(j3.p body, j3.p light) {
      public static final j3.m EMPTY = new j3.m(new j3.p(0), new j3.p(0));

      public int vertices() {
         return this.body.C() + this.light.C();
      }
   }

   public static final class p {
      private final float[] n;
      private final int[] H;
      private int J;

      private p(int var1) {
         this.n = new float[var1 * 3];
         this.H = new int[var1];
      }

      public int C() {
         return this.J;
      }

      public boolean Q() {
         return this.J == 0;
      }

      public float z(int var1) {
         return this.n[var1 * 3];
      }

      public float h(int var1) {
         return this.n[var1 * 3 + 1];
      }

      public float l(int var1) {
         return this.n[var1 * 3 + 2];
      }

      public int p(int var1) {
         return this.H[var1];
      }

      private void j(class_243 var1, int var2) {
         this.n[this.J * 3] = (float)var1.field_1352;
         this.n[this.J * 3 + 1] = (float)var1.field_1351;
         this.n[this.J * 3 + 2] = (float)var1.field_1350;
         this.H[this.J++] = var2;
      }
   }
}
