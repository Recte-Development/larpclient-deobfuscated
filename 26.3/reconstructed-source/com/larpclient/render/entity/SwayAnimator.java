package com.larpclient.render.entity;

import com.larpclient.render.world.FloatingWord;
import com.larpclient.setting.RangeSettings;

public final class SwayAnimator {
   public static double smoothstep(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public static void update(FloatingWord var0, RangeSettings var1, double var2) {
      var0.age += var2;
      var0.stateTime += var2;
      switch (var0.state) {
         case FADE_IN:
            var0.alpha = smoothstep(var0.stateTime / var1.horizontalFactor);
            if (var0.stateTime >= var1.horizontalFactor) {
               var0.state = FloatingWord.State.VISIBLE;
               var0.stateTime = 0.0;
            }
            break;
         case VISIBLE:
            var0.alpha = 1.0;
            if (var0.age >= var0.lifetime) {
               var0.beginFadeOut();
            }
            break;
         case FADE_OUT:
            var0.alpha = var0.fadeStartAlpha * (1.0 - smoothstep(var0.stateTime / var1.verticalFactor));
            if (var0.stateTime >= var1.verticalFactor) {
               var0.alpha = 0.0;
               var0.state = FloatingWord.State.IDLE;
               var0.active = (boolean)0;
            }
            break;
         case IDLE:
            var0.alpha = 0.0;
      }
      double var4 = switch (var1.filter) {
         case UPRIGHT -> 0.0;
         case SWIRL -> 0.45;
         case DRIFT -> 1.0;
         case FLOAT -> 0.8;
         case FALL -> 2.0;
      };
      double var6 = var0.age * var0.speed * var1.scale * (var1.filter == RangeSettings.Movement.FLOAT ? 0.5 : 1.0) + var0.phase;
      var0.offsetX = Math.sin(var6) * var1.minDelay * var4;
      var0.offsetY = Math.sin(var6 * 0.63) * var1.minDelay * var4 * 0.35;
      var0.offsetZ = Math.cos(var6 * 0.77) * var1.minDelay * var4 * 0.25;
      var0.rotation = Math.sin(var6 * 0.7) * Math.toRadians(var1.radius) * var4;
      double var8 = var1.filter == RangeSettings.Movement.FLOAT ? 0.045 : 0.012;
      var0.scale = (0.7 + 0.3 * var0.alpha) * (1.0 + Math.sin(var6 * 0.65) * var8 * var4);
      if (var1.filter != RangeSettings.Movement.UPRIGHT) {
         var0.offsetX = var0.offsetX - (1.0 - var0.alpha) * 0.12;
      }
   }

   private SwayAnimator() {
   }
}
