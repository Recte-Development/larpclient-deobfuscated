package com.larpclient;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class mk {
   public static double W(double var0) {
      double var2 = Math.clamp(var0 - 0.5, 0.0, 1.0);
      return var2 * var2 * (3.0 - 2.0 * var2);
   }

   public static float[] G(AABB var0, Vec3 var1, Vec3 var2, float var3, float var4) {
      if (var0.contains(var1)) {
         return new float[]{var3, var4};
      }

      float[] var5 = j6.s(var1, var2);
      if (Math.hypot(var2.x - var1.x, var2.z - var1.z) < 1.0E-6) {
         var5[0] = var3;
      }

      return var5;
   }

   public static float[] h(AABB var0, Vec3 var1, Vec3 var2, AABB var3, Vec3 var4, float var5, float var6, double var7) {
      float[] var9 = G(var0, var1, var2, var5, var6);
      if (var0.contains(var1)) {
         return var9;
      }

      float var10 = var5;
      float var11 = var6;
      if (var3 != null && var4 != null && !var3.contains(var4) && var1.distanceToSqr(var4) <= 1.0 && var0.getCenter().distanceToSqr(var3.getCenter()) <= 1.0) {
         Vec3 var12 = c(var3, B(var0, var2));
         float[] var13 = j6.s(var4, var12);
         float var14 = Mth.wrapDegrees(var9[0] - var13[0]);
         float var15 = var9[1] - var13[1];
         if (Math.abs(var14) <= 60.0F && Math.abs(var15) <= 60.0F) {
            var10 += var14;
            var11 += var15;
         }
      }

      return j6.s(var10, var11, var9[0], var9[1], var7);
   }

   public static Vec3 k(AABB var0, Vec3 var1) {
      double var2 = Math.min(0.001, Math.min(var0.getXsize(), Math.min(var0.getYsize(), var0.getZsize())) / 4.0);
      return new Vec3(
         Math.clamp(var1.x, var0.minX + var2, var0.maxX - var2),
         Math.clamp(var1.y, var0.minY + var2, var0.maxY - var2),
         Math.clamp(var1.z, var0.minZ + var2, var0.maxZ - var2)
      );
   }

   public static Vec3 B(AABB var0, Vec3 var1) {
      return new Vec3(
         (var1.x - var0.minX) / Math.max(0.001, var0.getXsize()),
         (var1.y - var0.minY) / Math.max(0.001, var0.getYsize()),
         (var1.z - var0.minZ) / Math.max(0.001, var0.getZsize())
      );
   }

   public static Vec3 c(AABB var0, Vec3 var1) {
      return k(var0, new Vec3(var0.minX + var1.x * var0.getXsize(), var0.minY + var1.y * var0.getYsize(), var0.minZ + var1.z * var0.getZsize()));
   }

   public static Optional<Vec3> L(AABB var0, Vec3 var1, Vec3 var2, Vec3 var3, Predicate<Vec3> var4) {
      Vec3 var5 = k(var0, var2);
      if (var4.test(var5)) {
         return Optional.of(var5);
      }

      if (var3 != null) {
         var5 = c(var0, var3);
         if (var4.test(var5)) {
            return Optional.of(var5);
         }
      }

      var5 = k(var0, var1);
      if (var4.test(var5)) {
         return Optional.of(var5);
      }

      for (double var9 : new double[]{0.5, 0.9, 0.1}) {
         for (double[] var14 : new double[][]{{0.5, 0.5}, {0.1, 0.1}, {0.1, 0.9}, {0.9, 0.1}, {0.9, 0.9}}) {
            var5 = c(var0, new Vec3(var14[0], var9, var14[1]));
            if (var4.test(var5)) {
               return Optional.of(var5);
            }
         }
      }

      return Optional.empty();
   }

   public static Vec3 n(AABB var0, Vec3 var1, Vec3 var2, double var3, Predicate<Vec3> var5) {
      Vec3 var6 = new Vec3(var2.x, 0.0, var2.z).scale(var3);
      if (var6.lengthSqr() > 0.25) {
         var6 = var6.normalize().scale(0.5);
      }

      Vec3 var7 = k(var0, var1.add(var6));
      return var5.test(var7) ? var7 : var1;
   }

   private mk() {
   }
}
