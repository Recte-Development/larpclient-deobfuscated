package com.larpclient.util.misc;

import com.larpclient.render.overlay.PostProcessing;

public final class SafetyLevel {
   public static PostProcessing.Quality current = PostProcessing.Quality.BALANCED;
   private static PostProcessing.Quality savedLevel;
   private static PostProcessing.Quality capLevel;

   public static int scaleDelay(int var0) {
      if (var0 <= 0) {
         return 0;
      }

      double var1 = switch (current) {
         case OFF, BALANCED -> 0.7;
         case LOW -> 0.45;
         case HIGH -> 0.9;
         case ULTRA -> 1.0;
      };
      return Math.max(1, (int)Math.ceil(var0 * var1));
   }

   public static boolean isAggressive() {
      return current.ordinal() >= PostProcessing.Quality.BALANCED.ordinal();
   }

   public static void capLevel(PostProcessing.Quality var0) {
      if (savedLevel != null && current != capLevel) {
         savedLevel = current;
      }

      if (var0 == null) {
         if (savedLevel != null) {
            current = savedLevel;
            capLevel = null;
            savedLevel = null;
         }
      } else {
         if (savedLevel == null) {
            savedLevel = current;
         }

         current = var0.ordinal() < savedLevel.ordinal() ? var0 : savedLevel;
         capLevel = current;
      }
   }

   public static PostProcessing.Quality getBaseLevel() {
      return savedLevel != null && current == capLevel ? savedLevel : current;
   }

   private SafetyLevel() {
   }
}
