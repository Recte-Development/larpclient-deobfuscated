package com.larpclient.util.render;

import java.util.Objects;
import java.util.function.LongSupplier;

public final class Animation {
   private final LongSupplier clock;
   private double startValue;
   private double targetValue;
   private long startTime;
   private double durationNanos = 1.0;
   private Animation.Easing easing = Animation.Easing.EASE_OUT_CUBIC;

   public Animation(double var1) {
      this(var1, System::nanoTime);
   }

   public Animation(double var1, LongSupplier var3) {
      this.clock = Objects.requireNonNull(var3);
      this.startValue = this.targetValue = var1;
      this.startTime = var3.getAsLong();
   }

   public void snapTo(double var1) {
      this.startValue = this.targetValue = var1;
      this.startTime = this.clock.getAsLong();
   }

   public double getValue() {
      return this.startValue + (this.targetValue - this.startValue) * this.easing.apply((this.clock.getAsLong() - this.startTime) / this.durationNanos);
   }

   public void restart(int var1) {
      if (this.getValue() >= 1.0) {
         this.snapTo(0.0);
      }

      this.animateToWithEasing(1.0, var1, Animation.Easing.EASE_OUT_CUBIC);
   }

   public double animateTo(double var1, int var3) {
      return this.animateToWithEasing(var1, var3, Animation.Easing.EASE_OUT_CUBIC);
   }

   public double animateToWithEasing(double var1, int var3, Animation.Easing var4) {
      if (this.targetValue != var1) {
         this.startValue = this.getValue();
         this.targetValue = var1;
         this.startTime = this.clock.getAsLong();
         this.durationNanos = Math.max(1, var3) * 1000000.0;
         this.easing = var4;
      }

      return this.getValue();
   }

   public enum Easing {
      LINEAR,
      EASE_IN_QUAD,
      EASE_OUT_QUAD,
      EASE_IN_OUT_QUAD,
      EASE_IN_CUBIC,
      EASE_OUT_CUBIC,
      EASE_IN_OUT_CUBIC,
      EASE_OUT_EXPO,
      EASE_OUT_SINE,
      EASE_OUT_BACK;

      public double apply(double var1) {
         double var3 = Math.clamp(var1, 0.0, 1.0);

         return switch (this) {
            case LINEAR -> var3;
            case EASE_IN_QUAD -> var3 * var3;
            case EASE_OUT_QUAD -> 1.0 - (1.0 - var3) * (1.0 - var3);
            case EASE_IN_OUT_QUAD -> var3 < 0.5 ? 2.0 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 2.0) / 2.0;
            case EASE_IN_CUBIC -> var3 * var3 * var3;
            case EASE_OUT_CUBIC -> 1.0 - Math.pow(1.0 - var3, 3.0);
            case EASE_IN_OUT_CUBIC -> var3 < 0.5 ? 4.0 * var3 * var3 * var3 : 1.0 - Math.pow(-2.0 * var3 + 2.0, 3.0) / 2.0;
            case EASE_OUT_EXPO -> var3 == 1.0 ? 1.0 : 1.0 - Math.pow(2.0, -10.0 * var3);
            case EASE_OUT_SINE -> Math.sin(var3 * Math.PI / 2.0);
            case EASE_OUT_BACK -> 1.0 + 2.70158 * Math.pow(var3 - 1.0, 3.0) + 1.70158 * Math.pow(var3 - 1.0, 2.0);
         };
      }
   }
}
