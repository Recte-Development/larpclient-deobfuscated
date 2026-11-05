package com.larpclient;

public final class hp {
   public static double R(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private hp() {
   }

   public enum u {
      F,
      C,
      r,
      Q,
      J;

      public double T(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case F -> 1.0;
            case C -> 1.0 - var1;
            case r -> 0.15 + 0.85 * hp.R(var1);
            case Q -> (var1 < 0.16 ? 1.18 * hp.R(var1 / 0.16) : 1.0 + 0.18 * Math.exp(-(var1 - 0.16) * 15.0)) * Math.min(1.0, (1.0 - var1) / 0.25);
            case J -> (0.8 + 0.2 * Math.sin(var1 * Math.PI * 6.0)) * Math.min(1.0, var1 / 0.08);
         };
      }
   }

   public enum y {
      B,
      h,
      e,
      H,
      M;

      public double Z(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case B -> 1.0 - var1;
            case h -> hp.R(Math.min(var1 / 0.08, 1.0)) * hp.R(1.0 - var1);
            case e -> hp.R(Math.min(var1 / 0.15, 1.0)) * hp.R(Math.min((1.0 - var1) / 0.55, 1.0));
            case H -> hp.R(Math.min(var1 / 0.06, 1.0)) * hp.R(Math.min((1.0 - var1) / 0.3, 1.0));
            case M -> hp.R(Math.min(var1 / 0.04, 1.0)) * Math.pow(1.0 - var1, 3.0);
         };
      }
   }
}
