package com.larpclient.render.world;

import net.minecraft.world.phys.AABB;

public final class BoxAnimator {
   private AABB fromBounds;
   private AABB toBounds;
   private double moveStart;
   private double moveDuration;
   private double fromAlpha;
   private double toAlpha;
   private double alphaStart;

   public void reset() {
      this.fromBounds = this.toBounds = null;
      this.fromAlpha = this.toAlpha = 0.0;
   }

   public BoxAnimator.AnimatedBox current(double var1) {
      if (this.toBounds == null) {
         return new BoxAnimator.AnimatedBox(null, 0.0);
      }

      double var3 = easeOutCubic(this.moveDuration <= 0.0 ? 1.0 : (var1 - this.moveStart) / this.moveDuration);
      double var5 = easeOutCubic((var1 - this.alphaStart) / 0.1);
      return new BoxAnimator.AnimatedBox(
         new AABB(
            reset(this.fromBounds.minX, this.toBounds.minX, var3),
            reset(this.fromBounds.minY, this.toBounds.minY, var3),
            reset(this.fromBounds.minZ, this.toBounds.minZ, var3),
            reset(this.fromBounds.maxX, this.toBounds.maxX, var3),
            reset(this.fromBounds.maxY, this.toBounds.maxY, var3),
            reset(this.fromBounds.maxZ, this.toBounds.maxZ, var3)
         ),
         reset(this.fromAlpha, this.toAlpha, var5)
      );
   }

   public BoxAnimator.AnimatedBox update(AABB var1, double var2, double var4) {
      BoxAnimator.AnimatedBox var6 = this.current(var2);
      if (var1 != null && !var1.equals(this.toBounds)) {
         int var7 = var6.bounds != null && !(var6.alpha < 0.001) && !(var1.getCenter().distanceTo(var6.bounds.getCenter()) > 8.0) ? 0 : 1;
         this.fromBounds = var7 != 0 ? var1 : var6.bounds;
         this.toBounds = var1;
         this.moveStart = var2;
         this.moveDuration = Math.max(0.0, var4);
         if (var7 != 0) {
            this.fromAlpha = 0.0;
            this.toAlpha = 1.0;
            this.alphaStart = var2;
         }
      }

      double var9 = var1 == null ? 0.0 : 1.0;
      if (var9 != this.toAlpha) {
         this.fromAlpha = var6.alpha;
         this.toAlpha = var9;
         this.alphaStart = var2;
      }

      return this.current(var2);
   }

   private static double easeOutCubic(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   private static double reset(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public record AnimatedBox(AABB bounds, double alpha) {
   }
}
