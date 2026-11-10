package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

public final class ji {
   public static final int T = 118;
   private final List<ji.t> K = new ArrayList<>();

   public List<ji.t> N() {
      return Collections.unmodifiableList(this.K);
   }

   public void s() {
      this.K.clear();
   }

   public void U(double var1, RandomGenerator var3) {
      double var4 = Math.clamp(var1 / 6.0, 0.0, 1.0);
      if (!(var1 <= 0.05)) {
         while (this.K.size() > 80) {
            this.K.removeFirst();
         }

         int var6 = 24 + (int)Math.round(var4 * 14.0);

         for (int var7 = 0; var7 < var6; var7++) {
            double var8 = var3.nextDouble(Math.PI * 2);
            double var10 = 2.0 * (var3.nextDouble(62.0, 118.0) + var4 * 48.0);
            double var12 = var3.nextDouble(0.0, 8.0);
            this.K
               .add(
                  new ji.t(
                     Math.cos(var8) * var12,
                     Math.sin(var8) * var12,
                     Math.cos(var8) * var10,
                     Math.sin(var8) * var10,
                     var3.nextDouble(8.4, 14.8),
                     var3.nextDouble(0.62, 0.92),
                     var3.nextBoolean()
                  )
               );
         }
      }
   }

   public void T(double var1) {
      double var3 = Math.clamp(var1, 0.0, 0.25);

      while (var3 > 0.0) {
         double var5 = Math.min(0.008333333333333333, var3);

         for (ji.t var8 : this.K) {
            var8.r += var5;
            var8.Q = var8.Q + var8.z * var5;
            var8.G = var8.G + var8.H * var5;
            var8.z = var8.z * Math.max(0.0, 1.0 - 3.2 * var5);
            var8.H = var8.H * Math.max(0.0, 1.0 - 3.2 * var5) + 16.0 * var5;
         }

         var3 -= var5;
      }

      this.K.removeIf(var0 -> var0.r >= var0.t);
   }

   public static final class t {
      public double Q;
      public double G;
      public double z;
      public double H;
      public double r;
      public final double J;
      public final double t;
      public final boolean n;

      private t(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
         this.Q = var1;
         this.G = var3;
         this.z = var5;
         this.H = var7;
         this.J = var9;
         this.t = var11;
         this.n = var13;
      }
   }
}
