package com.larpclient;

public final class l {
   public static double F(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public static void n(wq var0, mo var1, double var2) {
      var0.u += var2;
      var0.I += var2;
      switch (var0.f) {
         case d:
            var0.O = F(var0.I / var1.y);
            if (var0.I >= var1.y) {
               var0.f = wq.y.l;
               var0.I = 0.0;
            }
            break;
         case l:
            var0.O = 1.0;
            if (var0.u >= var0.R) {
               var0.r();
            }
            break;
         case A:
            var0.O = var0.X * (1.0 - F(var0.I / var1.p));
            if (var0.I >= var1.p) {
               var0.O = 0.0;
               var0.f = wq.y.v;
               var0.l = (boolean)0;
            }
            break;
         case v:
            var0.O = 0.0;
      }
      double var4 = switch (var1.w) {
         case k -> 0.0;
         case l -> 0.45;
         case d -> 1.0;
         case w -> 0.8;
         case A -> 2.0;
      };
      double var6 = var0.u * var0.V * var1.j * (var1.w == mo.d.w ? 0.5 : 1.0) + var0.q;
      var0.L = Math.sin(var6) * var1.M * var4;
      var0.C = Math.sin(var6 * 0.63) * var1.M * var4 * 0.35;
      var0.a = Math.cos(var6 * 0.77) * var1.M * var4 * 0.25;
      var0.A = Math.sin(var6 * 0.7) * Math.toRadians(var1.F) * var4;
      double var8 = var1.w == mo.d.w ? 0.045 : 0.012;
      var0.Q = (0.7 + 0.3 * var0.O) * (1.0 + Math.sin(var6 * 0.65) * var8 * var4);
      if (var1.w != mo.d.k) {
         var0.L = var0.L - (1.0 - var0.O) * 0.12;
      }
   }

   private l() {
   }
}
