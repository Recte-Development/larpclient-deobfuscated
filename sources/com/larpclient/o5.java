package com.larpclient;

public final class o5 {
   public static double v(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public static void k(t6 var0, mf var1, double var2) {
      var0.b += var2;
      var0.j += var2;
      switch (var0.P) {
         case p:
            var0.G = v(var0.j / var1.c);
            if (var0.j >= var1.c) {
               var0.P = t6.l.B;
               var0.j = 0.0;
            }
            break;
         case B:
            var0.G = 1.0;
            if (var0.b >= var0.q) {
               var0.y();
            }
            break;
         case J:
            var0.G = var0.M * (1.0 - v(var0.j / var1.l));
            if (var0.j >= var1.l) {
               var0.G = 0.0;
               var0.P = t6.l.X;
               var0.g = (boolean)0;
            }
            break;
         case X:
            var0.G = 0.0;
      }
      double var4 = switch (var1.z) {
         case X -> 0.0;
         case K -> 0.45;
         case E -> 1.0;
         case Y -> 0.8;
         case i -> 2.0;
      };
      double var6 = var0.b * var0.T * var1.x * (var1.z == mf.k.Y ? 0.5 : 1.0) + var0.R;
      var0.L = Math.sin(var6) * var1.n * var4;
      var0.l = Math.sin(var6 * 0.63) * var1.n * var4 * 0.35;
      var0.z = Math.cos(var6 * 0.77) * var1.n * var4 * 0.25;
      var0.I = Math.sin(var6 * 0.7) * Math.toRadians(var1.J) * var4;
      double var8 = var1.z == mf.k.Y ? 0.045 : 0.012;
      var0.F = (0.7 + 0.3 * var0.G) * (1.0 + Math.sin(var6 * 0.65) * var8 * var4);
      if (var1.z != mf.k.X) {
         var0.L = var0.L - (1.0 - var0.G) * 0.12;
      }
   }

   private o5() {
   }
}
