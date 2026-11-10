package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

public final class w8 {
   public static final int h = 118;
   private final List<w8.k> u = new ArrayList<>();

   public List<w8.k> D() {
      return Collections.unmodifiableList(this.u);
   }

   public void z() {
      this.u.clear();
   }

   public void S(double var1, RandomGenerator var3) {
      double var4 = Math.clamp(var1 / 6.0, 0.0, 1.0);
      if (!(var1 <= 0.05)) {
         while (this.u.size() > 80) {
            this.u.removeFirst();
         }

         int var6 = 24 + (int)Math.round(var4 * 14.0);

         for (int var7 = 0; var7 < var6; var7++) {
            double var8 = var3.nextDouble(Math.PI * 2);
            double var10 = 2.0 * (var3.nextDouble(62.0, 118.0) + var4 * 48.0);
            double var12 = var3.nextDouble(0.0, 8.0);
            this.u
               .add(
                  new w8.k(
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

   public void d(double var1) {
      double var3 = Math.clamp(var1, 0.0, 0.25);

      while (var3 > 0.0) {
         double var5 = Math.min(0.008333333333333333, var3);

         for (w8.k var8 : this.u) {
            var8.p += var5;
            var8.y = var8.y + var8.j * var5;
            var8.Z = var8.Z + var8.K * var5;
            var8.j = var8.j * Math.max(0.0, 1.0 - 3.2 * var5);
            var8.K = var8.K * Math.max(0.0, 1.0 - 3.2 * var5) + 16.0 * var5;
         }

         var3 -= var5;
      }

      this.u.removeIf(var0 -> var0.p >= var0.W);
   }

   public static final class k {
      public double y;
      public double Z;
      public double j;
      public double K;
      public double p;
      public final double J;
      public final double W;
      public final boolean I;

      private k(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
         this.y = var1;
         this.Z = var3;
         this.j = var5;
         this.K = var7;
         this.J = var9;
         this.W = var11;
         this.I = var13;
      }
   }
}
