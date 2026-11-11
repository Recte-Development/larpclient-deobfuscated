package com.larpclient.util.render;

public final class SpinAnimator {
   static final double fADE_THRESHOLD = 0.5;
   static final double sPIN_DURATION = 0.5;
   private Object lastKey;
   private float fadeValue;
   private boolean fadeActive;
   private boolean seen;
   private boolean lastTriggered;
   private boolean spinning;
   private int lastIndex;
   private double startAngle;
   private double targetAngle;
   private double startTime;

   public void reset() {
      this.lastKey = null;
      this.fadeActive = this.seen = this.lastTriggered = this.spinning = (boolean)0;
   }

   public float latchFade(Object var1, float var2, boolean var3) {
      float var4 = Float.isFinite(var2) ? Math.clamp(var2, 0.0F, 1.0F) : 0.0F;
      if (var3) {
         return var4;
      }

      if (var1 != this.lastKey) {
         this.lastKey = var1;
         this.fadeValue = 1.0F;
         this.fadeActive = (boolean)1;
      }

      if (!this.fadeActive) {
         return 0.0F;
      }

      this.fadeValue = Math.min(this.fadeValue, var4);
      if (this.fadeValue <= 0.01F) {
         this.fadeActive = (boolean)0;
      }

      return this.fadeActive ? this.fadeValue : 0.0F;
   }

   public void update(boolean var1, int var2, double var3) {
      int var5 = !this.seen || !var1 || this.lastTriggered && var2 >= this.lastIndex ? 0 : 1;
      this.seen = (boolean)1;
      this.lastTriggered = var1;
      this.lastIndex = var2;
      if (var5 != 0) {
         if (this.spinning) {
            if (this.progress(var3) < 0.5) {
               return;
            }

            this.startAngle = this.easedAngle(var3);
            this.targetAngle += 360.0;
         } else {
            this.startAngle = 0.0;
            this.targetAngle = 360.0;
         }

         this.startTime = var3;
         this.spinning = (boolean)1;
      }
   }

   public float getAngle(double var1) {
      if (!this.spinning) {
         return 0.0F;
      } else if (this.progress(var1) >= 1.0) {
         this.spinning = (boolean)0;
         return 0.0F;
      } else {
         return (float)(this.easedAngle(var1) % 360.0);
      }
   }

   private double progress(double var1) {
      return Math.clamp((var1 - this.startTime) / 0.5, 0.0, 1.0);
   }

   private double easedAngle(double var1) {
      double var3 = 1.0 - this.progress(var1);
      return this.startAngle + (this.targetAngle - this.startAngle) * (1.0 - var3 * var3 * var3);
   }
}
