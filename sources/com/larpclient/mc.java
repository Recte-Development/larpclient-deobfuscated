package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.random.RandomGenerator;

public final class mc {
   public static final int l = 118;
   private final List<mc.a> k = new ArrayList<>();

   public List<mc.a> L() {
      return Collections.unmodifiableList(this.k);
   }

   public void N() {
      this.k.clear();
   }

   public void U(double var1, RandomGenerator var3) {
      double var4 = Math.clamp(var1 / 6.0, 0.0, 1.0);
      if (!(var1 <= 0.05)) {
         while (this.k.size() > 80) {
            this.k.removeFirst();
         }

         int var6 = 24 + (int)Math.round(var4 * 14.0);

         for (int var7 = 0; var7 < var6; var7++) {
            double var8 = var3.nextDouble(Math.PI * 2);
            double var10 = 2.0 * (var3.nextDouble(62.0, 118.0) + var4 * 48.0);
            double var12 = var3.nextDouble(0.0, 8.0);
            this.k
               .add(
                  new mc.a(
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

   public void S(double var1) {
      double var3 = Math.clamp(var1, 0.0, 0.25);

      while (var3 > 0.0) {
         double var5 = Math.min(0.008333333333333333, var3);

         for (mc.a var8 : this.k) {
            var8.J += var5;
            var8.H = var8.H + var8.P * var5;
            var8.h = var8.h + var8.j * var5;
            var8.P = var8.P * Math.max(0.0, 1.0 - 3.2 * var5);
            var8.j = var8.j * Math.max(0.0, 1.0 - 3.2 * var5) + 16.0 * var5;
         }

         var3 -= var5;
      }

      this.k.removeIf(var0 -> var0.J >= var0.l);
   }

   public static final class a {
      public double H;
      public double h;
      public double P;
      public double j;
      public double J;
      public final double G;
      public final double l;
      public final boolean U;

      private a(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13) {
         this.H = var1;
         this.h = var3;
         this.P = var5;
         this.j = var7;
         this.G = var9;
         this.l = var11;
         this.U = var13;
      }
   }
}
