package com.larpclient;

import net.minecraft.class_243;

public final class ep {
   public static final double f = 1.5;
   public static final double H = 1.2;
   public static final double l = 2.4;
   public static final double X = 1.22;
   public static final double Y = 0.25;
   public static final double z = 0.42;
   public static final double u = 0.08;
   public static final double s = 0.98;
   public static final int B = 6;
   public static final double g = 0.3;
   public static final double J = 0.9;

   public static class_243 P(class_243 var0, double var1, class_243 var3) {
      double var4 = var0.method_1022(var3) / 2.4;
      if (var4 > 1.0) {
         return class_243.field_1353;
      }

      class_243 var6 = var0.method_1031(0.0, var1, 0.0).method_1020(var3);
      return var6.method_1027() < 1.0E-12 ? class_243.field_1353 : var6.method_1029().method_1021((1.0 - var4) * 1.22);
   }

   public static ep.o a(class_243 var0, class_243 var1, class_243 var2, ep.d var3) {
      class_243 var4 = var1.method_1029().method_1021(1.5).method_1019(var2);
      class_243 var5 = var0;

      for (int var6 = 1; var6 <= 6; var6++) {
         class_243 var7 = var5.method_1019(var4);
         ep.i var8 = var3.z(var5, var7);
         if (var8 != null) {
            return new ep.o(0.0F, 0.0F, var6, var8.point().method_1019(var8.normal().method_1021(0.25)), class_243.field_1353);
         }

         var5 = var7;
      }

      return null;
   }

   public static class_243 m(class_243 var0, class_243 var1, boolean var2, boolean var3, int var4) {
      double var5 = var0.field_1351;
      double var7 = var3 && var2 ? 0.42 : (var2 ? 0.0 : var1.field_1351);

      for (int var9 = 0; var9 < var4; var9++) {
         var5 += var7;
         var7 = (var7 - 0.08) * 0.98;
         if (var2 && !var3) {
            var5 = var0.field_1351;
            var7 = 0.0;
         }
      }

      return new class_243(var0.field_1352 + var1.field_1352 * var4, var5, var0.field_1350 + var1.field_1350 * var4);
   }

   public static ep.o e(class_243 var0, double var1, class_243 var3, boolean var4, boolean var5, float var6, boolean var7, ep.d var8) {
      ep.o var9 = null;
      double var10 = Double.NEGATIVE_INFINITY;
      float var12 = var7 ? var6 + 180.0F : var6;

      for (int var13 = var7 ? 40 : 90; var13 <= 90; var13++) {
         ep.o var14 = O(var0, var1, var3, var4, var5, var12, var13, var8);
         if (var14 != null) {
            double var15 = T(var14, var6, var7);
            if (var14.push().method_1033() >= 0.3 && var15 > var10) {
               var10 = var15;
               var9 = var14;
            }
         }
      }

      return var9;
   }

   public static ep.o O(class_243 var0, double var1, class_243 var3, boolean var4, boolean var5, float var6, float var7, ep.d var8) {
      class_243 var9 = var0.method_1031(0.0, var1, 0.0);
      class_243 var10 = var4 ? new class_243(var3.field_1352, 0.0, var3.field_1350) : var3;
      ep.o var11 = a(var9, class_243.method_1030(var7, var6), var10, var8);
      if (var11 == null) {
         return null;
      }

      class_243 var12 = m(var0, var3, var4, var5, var11.ticks());
      return new ep.o(var6, var7, var11.ticks(), var11.burst(), P(var12, var1, var11.burst()));
   }

   public static double T(ep.o var0, float var1, boolean var2) {
      class_243 var3 = var0.push();
      class_243 var4 = class_243.method_1030(0.0F, var1);
      double var5 = var3.field_1352 * var4.field_1352 + var3.field_1350 * var4.field_1350;
      return var2 ? (var3.field_1351 >= 0.15 ? var5 + 0.25 * var3.field_1351 : Double.NEGATIVE_INFINITY) : var3.field_1351;
   }

   public static boolean R(ep.o var0, ep.o var1, float var2, boolean var3) {
      if (var0 != null && var1 != null && !(var1.push().method_1033() < 0.3)) {
         double var4 = T(var0, var2, var3);
         double var6 = T(var1, var2, var3);
         return Double.isFinite(var6) && var6 >= (var4 > 0.0 ? var4 * 0.9 : var4);
      } else {
         return false;
      }
   }

   public static int s(int var0, int var1, int var2) {
      int var3 = var2 > 0 ? var2 : var0 + 1 + Math.max(0, var1);
      return Math.max(0, var3 - 2);
   }

   public static double p(double var0) {
      double var2 = 0.0;

      for (int var4 = 0; var4 < 200 && var0 > 0.0; var4++) {
         var2 += var0;
         var0 = (var0 - 0.08) * 0.98;
      }

      return var2;
   }

   private ep() {
   }

   public interface d {
      ep.i z(class_243 var1, class_243 var2);
   }

   public record i(class_243 point, class_243 normal) {
   }

   public record o(float yaw, float pitch, int ticks, class_243 burst, class_243 push) {
   }
}
