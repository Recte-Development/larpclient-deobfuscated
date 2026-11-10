package com.larpclient;

public final class pr {
   public static double k(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private pr() {
   }

   public enum b {
      A,
      K,
      M,
      Y,
      b;

      public double p(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case A -> 1.0 - var1;
            case K -> pr.k(Math.min(var1 / 0.08, 1.0)) * pr.k(1.0 - var1);
            case M -> pr.k(Math.min(var1 / 0.15, 1.0)) * pr.k(Math.min((1.0 - var1) / 0.55, 1.0));
            case Y -> pr.k(Math.min(var1 / 0.06, 1.0)) * pr.k(Math.min((1.0 - var1) / 0.3, 1.0));
            case b -> pr.k(Math.min(var1 / 0.04, 1.0)) * Math.pow(1.0 - var1, 3.0);
         };
      }
   }

   public enum g {
      Z,
      U,
      B,
      p,
      A;

      public double a(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case Z -> 1.0;
            case U -> 1.0 - var1;
            case B -> 0.15 + 0.85 * pr.k(var1);
            case p -> (var1 < 0.16 ? 1.18 * pr.k(var1 / 0.16) : 1.0 + 0.18 * Math.exp(-(var1 - 0.16) * 15.0)) * Math.min(1.0, (1.0 - var1) / 0.25);
            case A -> (0.8 + 0.2 * Math.sin(var1 * Math.PI * 6.0)) * Math.min(1.0, var1 / 0.08);
         };
      }
   }
}
