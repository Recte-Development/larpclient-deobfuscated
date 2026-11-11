package com.larpclient.util.math;

import com.larpclient.util.render.Animation;
import com.larpclient.util.render.PressAnimation;

public final class ChargeRamp {
   public static final int mAX_CHARGE_MS = 240;
   private long lastUpdateNanos = -1L;
   private double chargedMs;
   private boolean wasHolding;
   private boolean finished;
   private PressAnimation.Transform pose = PressAnimation.Transform.REST;

   public PressAnimation.Transform getPose() {
      return this.pose;
   }

   public double getChargedMs() {
      return this.chargedMs;
   }

   public boolean isFinished() {
      return this.finished;
   }

   public PressAnimation.Transform update(long var1, boolean var3, boolean var4) {
      if (this.finished) {
         return this.pose;
      }

      if (!var4) {
         this.finish();
         return this.pose;
      }

      if (var3 && this.wasHolding && this.lastUpdateNanos >= 0L && var1 >= this.lastUpdateNanos && var1 - this.lastUpdateNanos <= 500000000L) {
         this.chargedMs = this.chargedMs + (var1 - this.lastUpdateNanos) / 1000000.0;
      }

      this.lastUpdateNanos = var1;
      this.wasHolding = var3;
      if (this.chargedMs >= 240.0) {
         this.finish();
         return this.pose;
      }

      if (var3) {
         double var5 = Animation.Easing.EASE_OUT_BACK.apply(this.chargedMs / 240.0);
         this.pose = new PressAnimation.Transform(0.965 + 0.035 * var5, 7.0 * (1.0 - var5));
      }

      return this.pose;
   }

   public void finish() {
      this.finished = (boolean)1;
      this.chargedMs = 240.0;
      this.pose = PressAnimation.Transform.REST;
   }
}
