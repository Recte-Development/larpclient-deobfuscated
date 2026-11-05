package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class h7 {
   public static final double m = 1.5;
   public static final double s = 1.2;
   public static final double k = 2.4;
   public static final double H = 1.22;
   public static final double I = 0.25;
   public static final double S = 0.42;
   public static final double D = 0.08;
   public static final double K = 0.98;
   public static final int q = 6;
   public static final double u = 0.3;
   public static final double M = 0.9;

   public static Vec3 z(Vec3 var0, double var1, Vec3 var3) {
      double var4 = var0.distanceTo(var3) / 2.4;
      if (var4 > 1.0) {
         return Vec3.ZERO;
      }

      Vec3 var6 = var0.add(0.0, var1, 0.0).subtract(var3);
      return var6.lengthSqr() < 1.0E-12 ? Vec3.ZERO : var6.normalize().scale((1.0 - var4) * 1.22);
   }

   public static h7.z t(Vec3 var0, Vec3 var1, Vec3 var2, h7.u var3) {
      Vec3 var4 = var1.normalize().scale(1.5).add(var2);
      Vec3 var5 = var0;

      for (int var6 = 1; var6 <= 6; var6++) {
         Vec3 var7 = var5.add(var4);
         h7.y var8 = var3.R(var5, var7);
         if (var8 != null) {
            return new h7.z(0.0F, 0.0F, var6, var8.point().add(var8.normal().scale(0.25)), Vec3.ZERO);
         }

         var5 = var7;
      }

      return null;
   }

   public static Vec3 q(Vec3 var0, Vec3 var1, boolean var2, boolean var3, int var4) {
      double var5 = var0.y;
      double var7 = var3 && var2 ? 0.42 : (var2 ? 0.0 : var1.y);

      for (int var9 = 0; var9 < var4; var9++) {
         var5 += var7;
         var7 = (var7 - 0.08) * 0.98;
         if (var2 && !var3) {
            var5 = var0.y;
            var7 = 0.0;
         }
      }

      return new Vec3(var0.x + var1.x * var4, var5, var0.z + var1.z * var4);
   }

   public static h7.z q(Vec3 var0, double var1, Vec3 var3, boolean var4, boolean var5, float var6, boolean var7, h7.u var8) {
      h7.z var9 = null;
      double var10 = Double.NEGATIVE_INFINITY;
      float var12 = var7 ? var6 + 180.0F : var6;

      for (int var13 = var7 ? 40 : 90; var13 <= 90; var13++) {
         h7.z var14 = L(var0, var1, var3, var4, var5, var12, var13, var8);
         if (var14 != null) {
            double var15 = E(var14, var6, var7);
            if (var14.push().length() >= 0.3 && var15 > var10) {
               var10 = var15;
               var9 = var14;
            }
         }
      }

      return var9;
   }

   public static h7.z L(Vec3 var0, double var1, Vec3 var3, boolean var4, boolean var5, float var6, float var7, h7.u var8) {
      Vec3 var9 = var0.add(0.0, var1, 0.0);
      Vec3 var10 = var4 ? new Vec3(var3.x, 0.0, var3.z) : var3;
      h7.z var11 = t(var9, Vec3.directionFromRotation(var7, var6), var10, var8);
      if (var11 == null) {
         return null;
      }

      Vec3 var12 = q(var0, var3, var4, var5, var11.ticks());
      return new h7.z(var6, var7, var11.ticks(), var11.burst(), z(var12, var1, var11.burst()));
   }

   public static double E(h7.z var0, float var1, boolean var2) {
      Vec3 var3 = var0.push();
      Vec3 var4 = Vec3.directionFromRotation(0.0F, var1);
      double var5 = var3.x * var4.x + var3.z * var4.z;
      return var2 ? (var3.y >= 0.15 ? var5 + 0.25 * var3.y : Double.NEGATIVE_INFINITY) : var3.y;
   }

   public static boolean p(h7.z var0, h7.z var1, float var2, boolean var3) {
      if (var0 != null && var1 != null && !(var1.push().length() < 0.3)) {
         double var4 = E(var0, var2, var3);
         double var6 = E(var1, var2, var3);
         return Double.isFinite(var6) && var6 >= (var4 > 0.0 ? var4 * 0.9 : var4);
      } else {
         return false;
      }
   }

   public static int H(int var0, int var1, int var2) {
      int var3 = var2 > 0 ? var2 : var0 + 1 + Math.max(0, var1);
      return Math.max(0, var3 - 2);
   }

   public static double f(double var0) {
      double var2 = 0.0;

      for (int var4 = 0; var4 < 200 && var0 > 0.0; var4++) {
         var2 += var0;
         var0 = (var0 - 0.08) * 0.98;
      }

      return var2;
   }

   private h7() {
   }

   public interface u {
      h7.y R(Vec3 var1, Vec3 var2);
   }

   public record y(Vec3 point, Vec3 normal) {
   }

   public record z(float yaw, float pitch, int ticks, Vec3 burst, Vec3 push) {
   }
}
