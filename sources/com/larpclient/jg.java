package com.larpclient;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class jg {
   public static final double z = 4.0;
   public static final double N = 1.0;
   public static final double T = 0.1;

   public static double d(int var0) {
      return BowItem.getPowerForTime(Math.max(0, var0)) * 3.0F;
   }

   public static double E(double var0, double var2) {
      return Double.isFinite(var0) && Double.isFinite(var2) ? 4.0 + 1.0 * Math.clamp(var2, 0.0, 1.0) * 1.5 * Math.min(Math.max(0.0, var0), 5.0) : 4.0;
   }

   public static double[] q(double var0) {
      return new double[]{E(var0, 0.0), E(var0, 0.5), E(var0, 1.0)};
   }

   public static double N(float[] var0) {
      return (var0[0] + 4.0F * var0[1] + var0[2]) / 6.0F;
   }

   public static boolean L(Vec3 var0, Vec3 var1, AABB var2, List<AABB> var3) {
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

   private jg() {
   }
}
