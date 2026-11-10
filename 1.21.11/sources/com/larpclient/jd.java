package com.larpclient;

import java.util.random.RandomGenerator;

public final class jd {
   private final double Z;

   public jd(RandomGenerator var1) {
      this.Z = var1.nextDouble() * Math.PI * 2.0;
   }

   public boolean a(bz var1, bz[] var2, ic var3, RandomGenerator var4, double var5, boolean var7) {
      double var8 = var3.V * (var3.g == ic.z.y ? 1.2 : 1.0);

      for (int var10 = 0; var10 < 96; var10++) {
         double var11;
         if (var7 && var4.nextDouble() < 0.38) {
            var11 = var5 + var4.nextGaussian() * 0.9;
         } else if (var4.nextDouble() < 0.45) {
            var11 = this.Z + var4.nextInt(3) * 2.1 + var4.nextGaussian() * 0.48;
         } else {
            var11 = var4.nextDouble() * Math.PI * 2.0;
         }

         double var13 = var3.g();
         double var15 = var3.d();
         double var17 = Math.sqrt(var13 * var13 + var4.nextDouble() * (var15 * var15 - var13 * var13));
         double var19 = Math.sin(var11) * var17;
         double var21 = Math.cos(var11) * var17;
         double var23 = var3.I() + var4.nextDouble() * (var3.W() - var3.I());
         byte var25 = 1;

         for (bz var29 : var2) {
            if (var29 != var1 && var29.S) {
               double var30 = var19 - var29.p;
               double var32 = var23 - var29.A;
               double var34 = var21 - var29.K;
               if (var30 * var30 + var32 * var32 + var34 * var34 < var8 * var8) {
                  var25 = 0;
                  break;
               }
            }
         }

         if (var25 != 0) {
            var1.p = var19;
            var1.A = var23;
            var1.K = var21;
            return true;
         }
      }

      return false;
   }
}
