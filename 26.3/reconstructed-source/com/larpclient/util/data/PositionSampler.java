package com.larpclient.util.data;

import com.larpclient.render.world.FloatingWord;
import com.larpclient.setting.RangeSettings;
import java.util.random.RandomGenerator;

public final class PositionSampler {
   private final double baseAngle;

   public PositionSampler(RandomGenerator var1) {
      this.baseAngle = var1.nextDouble() * Math.PI * 2.0;
   }

   public boolean placeEntity(FloatingWord var1, FloatingWord[] var2, RangeSettings var3, RandomGenerator var4, double var5, boolean var7) {
      double var8 = var3.speed * (var3.filter == RangeSettings.Movement.FLOAT ? 1.2 : 1.0);

      for (int var10 = 0; var10 < 96; var10++) {
         double var11;
         if (var7 && var4.nextDouble() < 0.38) {
            var11 = var5 + var4.nextGaussian() * 0.9;
         } else if (var4.nextDouble() < 0.45) {
            var11 = this.baseAngle + var4.nextInt(3) * 2.1 + var4.nextGaussian() * 0.48;
         } else {
            var11 = var4.nextDouble() * Math.PI * 2.0;
         }

         double var13 = var3.minDistance();
         double var15 = var3.maxDistance();
         double var17 = Math.sqrt(var13 * var13 + var4.nextDouble() * (var15 * var15 - var13 * var13));
         double var19 = Math.sin(var11) * var17;
         double var21 = Math.cos(var11) * var17;
         double var23 = var3.minHeight() + var4.nextDouble() * (var3.maxHeight() - var3.minHeight());
         byte var25 = 1;

         for (FloatingWord var29 : var2) {
            if (var29 != var1 && var29.active) {
               double var30 = var19 - var29.x;
               double var32 = var23 - var29.y;
               double var34 = var21 - var29.z;
               if (var30 * var30 + var32 * var32 + var34 * var34 < var8 * var8) {
                  var25 = 0;
                  break;
               }
            }
         }

         if (var25 != 0) {
            var1.x = var19;
            var1.y = var23;
            var1.z = var21;
            return true;
         }
      }

      return false;
   }
}
