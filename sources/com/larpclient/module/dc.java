package com.larpclient.module;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class dc {
   private dc() {
   }

   public static Vec3 q(LivingEntity var0, double var1) {
      double var3 = Mth.clamp(var1, 0.0, 4.0);
      Vec3 var5 = var0.position();
      if (var3 <= 1.0E-4F) {
         return var5;
      }

      Vec3 var6 = new Vec3(var0.getX() - var0.xo, var0.getY() - var0.yo, var0.getZ() - var0.zo);
      Vec3 var7 = var6.scale(0.7F).add(var0.getDeltaMovement().scale(0.3F));
      if (Double.isFinite(var7.x) && Double.isFinite(var7.y) && Double.isFinite(var7.z) && !(var7.lengthSqr() < 1.0E-6F)) {
         if (var0.onGround()) {
            var7 = new Vec3(var7.x, 0.0, var7.z);
         }

         Vec3 var8 = var7.scale(var3);
         double var9 = 6.0;
         if (var8.lengthSqr() > var9 * var9) {
            var8 = var8.normalize().scale(var9);
         }

         return var5.add(var8);
      } else {
         return var5;
      }
   }
}
