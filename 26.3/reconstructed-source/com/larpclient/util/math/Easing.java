package com.larpclient.util.math;

public final class Easing {
   public static double smoothstep(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private Easing() {
   }

   public enum FadeCurve {
      LINEAR,
      QUICK_ATTACK_SMOOTH,
      SOFT_ATTACK_SOFT_RELEASE,
      SHARP_ATTACK_SMOOTH_RELEASE,
      FLASH_CUBIC_DECAY;

      public double alpha(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case LINEAR -> 1.0 - var1;
            case QUICK_ATTACK_SMOOTH -> Easing.smoothstep(Math.min(var1 / 0.08, 1.0)) * Easing.smoothstep(1.0 - var1);
            case SOFT_ATTACK_SOFT_RELEASE -> Easing.smoothstep(Math.min(var1 / 0.15, 1.0)) * Easing.smoothstep(Math.min((1.0 - var1) / 0.55, 1.0));
            case SHARP_ATTACK_SMOOTH_RELEASE -> Easing.smoothstep(Math.min(var1 / 0.06, 1.0)) * Easing.smoothstep(Math.min((1.0 - var1) / 0.3, 1.0));
            case FLASH_CUBIC_DECAY -> Easing.smoothstep(Math.min(var1 / 0.04, 1.0)) * Math.pow(1.0 - var1, 3.0);
         };
      }
   }

   public enum SizeCurve {
      CONSTANT,
      SHRINK,
      GROW,
      POP,
      PULSE;

      public double scale(double var1) {
         var1 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case CONSTANT -> 1.0;
            case SHRINK -> 1.0 - var1;
            case GROW -> 0.15 + 0.85 * Easing.smoothstep(var1);
            case POP -> (var1 < 0.16 ? 1.18 * Easing.smoothstep(var1 / 0.16) : 1.0 + 0.18 * Math.exp(-(var1 - 0.16) * 15.0))
               * Math.min(1.0, (1.0 - var1) / 0.25);
            case PULSE -> (0.8 + 0.2 * Math.sin(var1 * Math.PI * 6.0)) * Math.min(1.0, var1 / 0.08);
         };
      }
   }
}
