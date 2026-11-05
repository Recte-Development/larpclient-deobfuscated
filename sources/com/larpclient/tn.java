package com.larpclient;

import java.util.random.RandomGenerator;

public final class tn {
   private final double T;

   public tn(RandomGenerator var1) {
      this.T = var1.nextDouble() * Math.PI * 2.0;
   }

   public boolean G(t6 var1, t6[] var2, mf var3, RandomGenerator var4, double var5, boolean var7) {
      double var8 = var3.b * (var3.z == mf.k.Y ? 1.2 : 1.0);

      for (int var10 = 0; var10 < 96; var10++) {
         double var11;
         if (var7 && var4.nextDouble() < 0.38) {
            var11 = var5 + var4.nextGaussian() * 0.9;
         } else if (var4.nextDouble() < 0.45) {
            var11 = this.T + var4.nextInt(3) * 2.1 + var4.nextGaussian() * 0.48;
         } else {
            var11 = var4.nextDouble() * Math.PI * 2.0;
         }

         double var13 = var3.x();
         double var15 = var3.V();
         double var17 = Math.sqrt(var13 * var13 + var4.nextDouble() * (var15 * var15 - var13 * var13));
         double var19 = Math.sin(var11) * var17;
         double var21 = Math.cos(var11) * var17;
         double var23 = var3.A() + var4.nextDouble() * (var3.y() - var3.A());
         byte var25 = 1;

         for (t6 var29 : var2) {
            if (var29 != var1 && var29.g) {
               double var30 = var19 - var29.r;
               double var32 = var23 - var29.N;
               double var34 = var21 - var29.H;
               if (var30 * var30 + var32 * var32 + var34 * var34 < var8 * var8) {
                  var25 = 0;
                  break;
               }
            }
         }

         if (var25 != 0) {
            var1.r = var19;
            var1.N = var23;
            var1.H = var21;
            return true;
         }
      }

      return false;
   }
}
