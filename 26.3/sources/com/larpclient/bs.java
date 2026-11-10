package com.larpclient;

public final class bs {
   public static double M(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public static void m(fe var0, jd var1, double var2) {
      var0.K += var2;
      var0.S += var2;
      switch (var0.z) {
         case k:
            var0.g = M(var0.S / var1.e);
            if (var0.S >= var1.e) {
               var0.z = fe.q.j;
               var0.S = 0.0;
            }
            break;
         case j:
            var0.g = 1.0;
            if (var0.K >= var0.x) {
               var0.l();
            }
            break;
         case G:
            var0.g = var0.G * (1.0 - M(var0.S / var1.E));
            if (var0.S >= var1.E) {
               var0.g = 0.0;
               var0.z = fe.q.h;
               var0.Q = (boolean)0;
            }
            break;
         case h:
            var0.g = 0.0;
      }
      double var4 = switch (var1.Z) {
         case U -> 0.0;
         case S -> 0.45;
         case y -> 1.0;
         case T -> 0.8;
         case H -> 2.0;
      };
      double var6 = var0.K * var0.s * var1.O * (var1.Z == jd.i.T ? 0.5 : 1.0) + var0.t;
      var0.W = Math.sin(var6) * var1.n * var4;
      var0.l = Math.sin(var6 * 0.63) * var1.n * var4 * 0.35;
      var0.V = Math.cos(var6 * 0.77) * var1.n * var4 * 0.25;
      var0.E = Math.sin(var6 * 0.7) * Math.toRadians(var1.C) * var4;
      double var8 = var1.Z == jd.i.T ? 0.045 : 0.012;
      var0.P = (0.7 + 0.3 * var0.g) * (1.0 + Math.sin(var6 * 0.65) * var8 * var4);
      if (var1.Z != jd.i.U) {
         var0.W = var0.W - (1.0 - var0.g) * 0.12;
      }
   }

   private bs() {
   }
}
