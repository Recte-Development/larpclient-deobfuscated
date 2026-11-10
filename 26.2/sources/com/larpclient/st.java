package com.larpclient;

public final class st {
   public static double h(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private st() {
   }

   public enum c {
      X,
      Q,
      z,
      u,
      T;

      public double q(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case X -> 1.0;
            case Q -> 1.0 - var1;
            case z -> 0.15 + 0.85 * st.h(var1);
            case u -> (var1 < 0.16 ? 1.18 * st.h(var1 / 0.16) : 1.0 + 0.18 * Math.exp(-(var1 - 0.16) * 15.0)) * Math.min(1.0, (1.0 - var1) / 0.25);
            case T -> (0.8 + 0.2 * Math.sin(var1 * Math.PI * 6.0)) * Math.min(1.0, var1 / 0.08);
         };
      }
   }

   public enum z {
      f,
      u,
      C,
      J,
      z;

      public double x(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case f -> 1.0 - var1;
            case u -> st.h(Math.min(var1 / 0.08, 1.0)) * st.h(1.0 - var1);
            case C -> st.h(Math.min(var1 / 0.15, 1.0)) * st.h(Math.min((1.0 - var1) / 0.55, 1.0));
            case J -> st.h(Math.min(var1 / 0.06, 1.0)) * st.h(Math.min((1.0 - var1) / 0.3, 1.0));
            case z -> st.h(Math.min(var1 / 0.04, 1.0)) * Math.pow(1.0 - var1, 3.0);
         };
      }
   }
}
