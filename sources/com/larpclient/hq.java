package com.larpclient;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record hq(Vec3 point, Vec3 normal, Vec3 ring, Vec3 ringNormal, double groundY) {
   public hq(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4) {
      this(var1, var2, var3, var4, Double.NaN);
   }

   public static hq sample(AABB var0, Vec3 var1, Vec3 var2, et.l var3) {
      Vec3 var4 = var0.getCenter();
      Vec3 var5 = var2.lengthSqr() < 1.0E-8 ? var4.subtract(var1).normalize() : var2.normalize();
      Vec3 var6 = var0.clip(var1, var1.add(var5.scale(64.0)))
         .orElseGet(
            () -> new Vec3(Math.clamp(var1.x, var0.minX, var0.maxX), Math.clamp(var1.y, var0.minY, var0.maxY), Math.clamp(var1.z, var0.minZ, var0.maxZ))
         );
      Vec3 var7 = var6.subtract(var4).normalize();
      if (var7.lengthSqr() < 1.0E-8) {
         var7 = new Vec3(0.0, 1.0, 0.0);
      }

      Vec3 var8 = new Vec3(var4.x, var0.minY, var4.z);
      et.d var9 = var3.J(var8.add(0.0, 0.25, 0.0), var8.add(0.0, -4.0, 0.0));
      return var9 != null && var9.normal().y > 0.5
         ? new hq(var6.add(var7.scale(0.025)), var7, var9.point().add(var9.normal().scale(0.035)), var9.normal(), var9.point().y)
         : new hq(var6.add(var7.scale(0.025)), var7, var6.add(var7.scale(0.04)), var7);
   }
}
