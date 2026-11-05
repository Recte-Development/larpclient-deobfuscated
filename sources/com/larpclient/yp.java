package com.larpclient;

import java.util.random.RandomGenerator;

public final class yp {
   private final double r;

   public yp(RandomGenerator var1) {
      this.r = var1.nextDouble() * Math.PI * 2.0;
   }

   public boolean T(lg var1, lg[] var2, e0 var3, RandomGenerator var4, double var5, boolean var7) {
      double var8 = var3.H * (var3.R == e0.z.Y ? 1.2 : 1.0);

      for (int var10 = 0; var10 < 96; var10++) {
         double var11;
         if (var7 && var4.nextDouble() < 0.38) {
            var11 = var5 + var4.nextGaussian() * 0.9;
         } else if (var4.nextDouble() < 0.45) {
            var11 = this.r + var4.nextInt(3) * 2.1 + var4.nextGaussian() * 0.48;
         } else {
            var11 = var4.nextDouble() * Math.PI * 2.0;
         }

         double var13 = var3.Z();
         double var15 = var3.B();
         double var17 = Math.sqrt(var13 * var13 + var4.nextDouble() * (var15 * var15 - var13 * var13));
         double var19 = Math.sin(var11) * var17;
         double var21 = Math.cos(var11) * var17;
         double var23 = var3.X() + var4.nextDouble() * (var3.F() - var3.X());
         byte var25 = 1;

         for (lg var29 : var2) {
            if (var29 != var1 && var29.h) {
               double var30 = var19 - var29.m;
               double var32 = var23 - var29.n;
               double var34 = var21 - var29.k;
               if (var30 * var30 + var32 * var32 + var34 * var34 < var8 * var8) {
                  var25 = 0;
                  break;
               }
            }
         }

         if (var25 != 0) {
            var1.m = var19;
            var1.n = var23;
            var1.k = var21;
            return true;
         }
      }

      return false;
   }
}
