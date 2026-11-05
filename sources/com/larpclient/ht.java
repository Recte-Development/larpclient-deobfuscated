package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

public final class ht {
   public static final int r = 118;
   private final List<ht.t> s = new ArrayList<>();

   public List<ht.t> j() {
      return Collections.unmodifiableList(this.s);
   }

   public void j() {
      this.s.clear();
   }

   public void q(double var1, RandomGenerator var3) {
      double var4 = Math.clamp(var1 / 6.0, 0.0, 1.0);
      if (!(var1 <= 0.05)) {
         while (this.s.size() > 80) {
            this.s.removeFirst();
         }

         int var6 = 24 + (int)Math.round(var4 * 14.0);

         for (int var7 = 0; var7 < var6; var7++) {
            double var8 = var3.nextDouble(Math.PI * 2);
            double var10 = 2.0 * (var3.nextDouble(62.0, 118.0) + var4 * 48.0);
            double var12 = var3.nextDouble(0.0, 8.0);
            this.s
               .add(
                  new ht.t(
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

   public void F(double var1) {
      double var3 = Math.clamp(var1, 0.0, 0.25);

      while (var3 > 0.0) {
         double var5 = Math.min(0.008333333333333333, var3);

         for (ht.t var8 : this.s) {
            var8.g += var5;
            var8.u = var8.u + var8.I * var5;
            var8.e = var8.e + var8.p * var5;
            var8.I = var8.I * Math.max(0.0, 1.0 - 3.2 * var5);
            var8.p = var8.p * Math.max(0.0, 1.0 - 3.2 * var5) + 16.0 * var5;
         }

         var3 -= var5;
      }

      this.s.removeIf(var0 -> var0.g >= var0.L);
   }

   public static final class t {
      public double u;
      public double e;
      public double I;
      public double p;
      public double g;
      public final double n;
      public final double L;
      public final boolean V;

      private t(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
         this.u = var1;
         this.e = var3;
         this.I = var5;
         this.p = var7;
         this.n = var9;
         this.L = var11;
         this.V = var13;
      }
   }
}
