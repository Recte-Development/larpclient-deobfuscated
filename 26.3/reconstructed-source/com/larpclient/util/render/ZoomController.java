package com.larpclient.util.render;

public final class ZoomController {
   public static final double mAX_ZOOM = 16.0;
   private boolean suppressed;
   private boolean keyWasDown;
   private boolean toggled;
   private boolean active;
   private double baseZoom = Double.NaN;
   private double zoom = 4.0;
   private double scale = 1.0;
   private long lastFrameNanos;

   public void reset(boolean var1) {
      this.suppressed = var1;
      this.keyWasDown = var1;
      this.toggled = this.active = (boolean)0;
      this.baseZoom = Double.NaN;
      this.scale = 1.0;
      this.lastFrameNanos = 0L;
   }

   public void update(boolean var1, boolean var2, boolean var3, double var4, boolean var6) {
      if (!var1) {
         this.reset(true);
      } else {
         double var7 = clampZoom(var4);
         if (this.baseZoom != var7) {
            this.baseZoom = var7;
            this.zoom = var7;
         }

         if (!var2) {
            this.suppressed = (boolean)0;
         }

         if (this.suppressed) {
            this.keyWasDown = var2;
            this.active = (boolean)0;
         } else {
            int var9 = var2 && !this.keyWasDown ? 1 : 0;
            if (!var3 && var9 != 0) {
               this.toggled = (boolean)(!this.toggled ? 1 : 0);
            }

            boolean var10 = var3 ? var2 : this.toggled;
            if (!var10 && this.active && !var6) {
               this.zoom = this.baseZoom;
            }

            this.active = var10;
            this.keyWasDown = var2;
         }
      }
   }

   public boolean applyScroll(double var1) {
      if (this.active && Double.isFinite(var1) && var1 != 0.0) {
         this.zoom = clampZoom(this.zoom * Math.pow(1.2, Math.clamp(var1, -8.0, 8.0)));
         return true;
      } else {
         return false;
      }
   }

   public double advanceScale(long var1, double var3, boolean var5) {
      double var6 = this.active ? 1.0 / this.zoom : 1.0;
      if (!var5 || var3 <= 0.0 || !Double.isFinite(var3)) {
         this.scale = var6;
      } else if (this.lastFrameNanos != 0L && var1 > this.lastFrameNanos) {
         double var8 = Math.min((var1 - this.lastFrameNanos) * 1.0E-9, 0.05);
         this.scale = this.scale + (var6 - this.scale) * -Math.expm1(-4.605170185988092 * var8 / var3);
         if (Math.abs(var6 - this.scale) < 1.0E-4) {
            this.scale = var6;
         }
      }

      this.lastFrameNanos = var1;
      return this.scale;
   }

   public boolean isActive() {
      return this.active;
   }

   public double getScale() {
      return this.scale;
   }

   public double getZoom() {
      return this.zoom;
   }

   public static double clampZoom(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, 1.0, 16.0) : 4.0;
   }

   public static float scaleFov(float var0, double var1) {
      if (Float.isFinite(var0) && !(var0 <= 0.0F)) {
         double var3 = Math.clamp(var0, 1.0F, 179.0F);
         double var5 = Double.isFinite(var1) ? Math.clamp(var1, 0.0625, 1.0) : 1.0;
         return var5 == 1.0 ? var0 : (float)Math.max(1.0, Math.toDegrees(2.0 * Math.atan(Math.tan(Math.toRadians(var3) * 0.5) * var5)));
      } else {
         return var0;
      }
   }
}
