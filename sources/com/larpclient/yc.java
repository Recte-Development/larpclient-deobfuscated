package com.larpclient;

public final class yc {
   public static double H(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private yc() {
   }

   public enum d {
      u,
      k,
      c,
      R,
      i;

      public double v(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case u -> 1.0 - var1;
            case k -> yc.H(Math.min(var1 / 0.08, 1.0)) * yc.H(1.0 - var1);
            case c -> yc.H(Math.min(var1 / 0.15, 1.0)) * yc.H(Math.min((1.0 - var1) / 0.55, 1.0));
            case R -> yc.H(Math.min(var1 / 0.06, 1.0)) * yc.H(Math.min((1.0 - var1) / 0.3, 1.0));
            case i -> yc.H(Math.min(var1 / 0.04, 1.0)) * Math.pow(1.0 - var1, 3.0);
         };
      }
   }

   public enum y {
      W,
      J,
      e,
      T,
      a;

      public double E(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case W -> 1.0;
            case J -> 1.0 - var1;
            case e -> 0.15 + 0.85 * yc.H(var1);
            case T -> (var1 < 0.16 ? 1.18 * yc.H(var1 / 0.16) : 1.0 + 0.18 * Math.exp(-(var1 - 0.16) * 15.0)) * Math.min(1.0, (1.0 - var1) / 0.25);
            case a -> (0.8 + 0.2 * Math.sin(var1 * Math.PI * 6.0)) * Math.min(1.0, var1 / 0.08);
         };
      }
   }
}
