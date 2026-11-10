package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

public final class hq {
   public static final int k = 118;
   private final List<hq.n> S = new ArrayList<>();

   public List<hq.n> n() {
      return Collections.unmodifiableList(this.S);
   }

   public void o() {
      this.S.clear();
   }

   public void h(double var1, RandomGenerator var3) {
      double var4 = Math.clamp(var1 / 6.0, 0.0, 1.0);
      if (!(var1 <= 0.05)) {
         while (this.S.size() > 80) {
            this.S.removeFirst();
         }

         int var6 = 24 + (int)Math.round(var4 * 14.0);

         for (int var7 = 0; var7 < var6; var7++) {
            double var8 = var3.nextDouble(Math.PI * 2);
            double var10 = 2.0 * (var3.nextDouble(62.0, 118.0) + var4 * 48.0);
            double var12 = var3.nextDouble(0.0, 8.0);
            this.S
               .add(
                  new hq.n(
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

   public void N(double var1) {
      double var3 = Math.clamp(var1, 0.0, 0.25);

      while (var3 > 0.0) {
         double var5 = Math.min(0.008333333333333333, var3);

         for (hq.n var8 : this.S) {
            var8.d += var5;
            var8.v = var8.v + var8.o * var5;
            var8.I = var8.I + var8.J * var5;
            var8.o = var8.o * Math.max(0.0, 1.0 - 3.2 * var5);
            var8.J = var8.J * Math.max(0.0, 1.0 - 3.2 * var5) + 16.0 * var5;
         }

         var3 -= var5;
      }

      this.S.removeIf(var0 -> var0.d >= var0.P);
   }

   public static final class n {
      public double v;
      public double I;
      public double o;
      public double J;
      public double d;
      public final double M;
      public final double P;
      public final boolean Y;

      private n(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
         this.v = var1;
         this.I = var3;
         this.o = var5;
         this.J = var7;
         this.M = var9;
         this.P = var11;
         this.Y = var13;
      }
   }
}
