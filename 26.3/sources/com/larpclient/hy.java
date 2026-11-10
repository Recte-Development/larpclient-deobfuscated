package com.larpclient;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class hy {
   public static final double g = 4.0;
   public static final double U = 1.0;
   public static final double s = 0.1;

   public static double b(int var0) {
      return BowItem.getPowerForTime(Math.max(0, var0)) * 3.0F;
   }

   public static double N(double var0, double var2) {
      return Double.isFinite(var0) && Double.isFinite(var2) ? 4.0 + 1.0 * Math.clamp(var2, 0.0, 1.0) * 1.5 * Math.min(Math.max(0.0, var0), 5.0) : 4.0;
   }

   public static double[] N(double var0) {
      return new double[]{N(var0, 0.0), N(var0, 0.5), N(var0, 1.0)};
   }

   public static double h(float[] var0) {
      return (var0[0] + 4.0F * var0[1] + var0[2]) / 6.0F;
   }

   public static boolean a(Vec3 var0, Vec3 var1, AABB var2, List<AABB> var3) {
      if (var2.contains(var0)) {
         return false;
      }

      Optional var4 = var2.clip(var0, var1);
      if (var4.isEmpty()) {
         return false;
      }

      double var5 = var0.distanceToSqr((Vec3)var4.get());

      for (AABB var8 : var3) {
         if (var8.contains(var0)) {
            return false;
         }

         Optional var9 = var8.clip(var0, var1);
         if (var9.isPresent() && var0.distanceToSqr((Vec3)var9.get()) <= var5) {
            return false;
         }
      }

      return true;
   }

   private hy() {
   }
}
