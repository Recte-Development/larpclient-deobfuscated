package com.larpclient;

public final class u7 {
   public static double T(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public static void m(bz var0, ic var1, double var2) {
      var0.J += var2;
      var0.e += var2;
      switch (var0.X) {
         case B:
            var0.M = T(var0.e / var1.f);
            if (var0.e >= var1.f) {
               var0.X = bz.o.x;
               var0.e = 0.0;
            }
            break;
         case x:
            var0.M = 1.0;
            if (var0.J >= var0.u) {
               var0.g();
            }
            break;
         case X:
            var0.M = var0.Q * (1.0 - T(var0.e / var1.O));
            if (var0.e >= var1.O) {
               var0.M = 0.0;
               var0.X = bz.o.H;
               var0.S = (boolean)0;
            }
            break;
         case H:
            var0.M = 0.0;
      }
      double var4 = switch (var1.g) {
         case i -> 0.0;
         case H -> 0.45;
         case u -> 1.0;
         case y -> 0.8;
         case h -> 2.0;
      };
      double var6 = var0.J * var0.a * var1.E * (var1.g == ic.z.y ? 0.5 : 1.0) + var0.o;
      var0.l = Math.sin(var6) * var1.o * var4;
      var0.m = Math.sin(var6 * 0.63) * var1.o * var4 * 0.35;
      var0.q = Math.cos(var6 * 0.77) * var1.o * var4 * 0.25;
      var0.c = Math.sin(var6 * 0.7) * Math.toRadians(var1.J) * var4;
      double var8 = var1.g == ic.z.y ? 0.045 : 0.012;
      var0.O = (0.7 + 0.3 * var0.M) * (1.0 + Math.sin(var6 * 0.65) * var8 * var4);
      if (var1.g != ic.z.i) {
         var0.l = var0.l - (1.0 - var0.M) * 0.12;
      }
   }

   private u7() {
   }
}
