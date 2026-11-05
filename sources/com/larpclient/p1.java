package com.larpclient;

import java.util.Optional;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class p1 {
   public static Optional<Vec3> S(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      if (!Double.isFinite(var3) || var3 < 0.0) {
         return Optional.empty();
      } else {
         return var2.contains(var0) ? Optional.of(var0) : var2.clip(var0, var0.add(var1.normalize().scale(var3)));
      }
   }

   public static double X(Vec3 var0, AABB var1) {
      return var0.distanceTo(
         new Vec3(Math.clamp(var0.x, var1.minX, var1.maxX), Math.clamp(var0.y, var1.minY, var1.maxY), Math.clamp(var0.z, var1.minZ, var1.maxZ))
      );
   }

   public static boolean H(Vec3 var0, Vec3 var1, AABB var2) {
      double var3 = var0.distanceToSqr(var1);
      if (var2.contains(var0)) {
         return var3 > 1.0E-7;
      }

      Optional var5 = var2.clip(var0, var1);
      return var5.isPresent() && var0.distanceToSqr((Vec3)var5.get()) + 1.0E-7 < var3;
   }

   private p1() {
   }
}
