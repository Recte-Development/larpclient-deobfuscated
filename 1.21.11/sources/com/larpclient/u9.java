package com.larpclient;

public final class u9 {
   public static double L(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private u9() {
   }

   public enum o {
      d,
      D,
      q,
      I,
      G;

      public double j(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case d -> 1.0;
            case D -> 1.0 - var1;
            case q -> 0.15 + 0.85 * u9.L(var1);
            case I -> (var1 < 0.16 ? 1.18 * u9.L(var1 / 0.16) : 1.0 + 0.18 * Math.exp(-(var1 - 0.16) * 15.0)) * Math.min(1.0, (1.0 - var1) / 0.25);
            case G -> (0.8 + 0.2 * Math.sin(var1 * Math.PI * 6.0)) * Math.min(1.0, var1 / 0.08);
         };
      }
   }

   public enum u {
      W,
      r,
      M,
      b,
      i;

      public double E(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case W -> 1.0 - var1;
            case r -> u9.L(Math.min(var1 / 0.08, 1.0)) * u9.L(1.0 - var1);
            case M -> u9.L(Math.min(var1 / 0.15, 1.0)) * u9.L(Math.min((1.0 - var1) / 0.55, 1.0));
            case b -> u9.L(Math.min(var1 / 0.06, 1.0)) * u9.L(Math.min((1.0 - var1) / 0.3, 1.0));
            case i -> u9.L(Math.min(var1 / 0.04, 1.0)) * Math.pow(1.0 - var1, 3.0);
         };
      }
   }
}
