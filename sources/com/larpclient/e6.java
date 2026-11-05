package com.larpclient;

public final class e6 {
   public static double q(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public static void G(lg var0, e0 var1, double var2) {
      var0.T += var2;
      var0.o += var2;
      switch (var0.t) {
         case A:
            var0.v = q(var0.o / var1.L);
            if (var0.o >= var1.L) {
               var0.t = lg.s.h;
               var0.o = 0.0;
            }
            break;
         case h:
            var0.v = 1.0;
            if (var0.T >= var0.L) {
               var0.g();
            }
            break;
         case W:
            var0.v = var0.j * (1.0 - q(var0.o / var1.w));
            if (var0.o >= var1.w) {
               var0.v = 0.0;
               var0.t = lg.s.I;
               var0.h = (boolean)0;
            }
            break;
         case I:
            var0.v = 0.0;
      }
      double var4 = switch (var1.R) {
         case O -> 0.0;
         case V -> 0.45;
         case L -> 1.0;
         case Y -> 0.8;
         case s -> 2.0;
      };
      double var6 = var0.T * var0.l * var1.e * (var1.R == e0.z.Y ? 0.5 : 1.0) + var0.p;
      var0.E = Math.sin(var6) * var1.h * var4;
      var0.a = Math.sin(var6 * 0.63) * var1.h * var4 * 0.35;
      var0.q = Math.cos(var6 * 0.77) * var1.h * var4 * 0.25;
      var0.K = Math.sin(var6 * 0.7) * Math.toRadians(var1.V) * var4;
      double var8 = var1.R == e0.z.Y ? 0.045 : 0.012;
      var0.J = (0.7 + 0.3 * var0.v) * (1.0 + Math.sin(var6 * 0.65) * var8 * var4);
      if (var1.R != e0.z.O) {
         var0.E = var0.E - (1.0 - var0.v) * 0.12;
      }
   }

   private e6() {
   }
}
