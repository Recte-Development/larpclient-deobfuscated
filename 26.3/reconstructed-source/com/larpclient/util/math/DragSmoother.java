package com.larpclient.util.math;

import com.larpclient.hud.HudLayout2;

public final class DragSmoother {
   private double smoothX;
   private double smoothY;
   private double anchorX;
   private double anchorY;
   private int lockedAxis;

   public void reset(double var1, double var3) {
      this.smoothX = this.anchorX = var1;
      this.smoothY = this.anchorY = var3;
      this.lockedAxis = 0;
   }

   public HudLayout2.DragResult step(double var1, double var3, boolean var5, double var6) {
      if (var5) {
         if (this.lockedAxis == 0 && Math.max(Math.abs(var1 - this.anchorX), Math.abs(var3 - this.anchorY)) > 2.0) {
            this.lockedAxis = Math.abs(var1 - this.anchorX) >= Math.abs(var3 - this.anchorY) ? 1 : 2;
         }

         if (this.lockedAxis == 1) {
            var3 = this.anchorY;
         }

         if (this.lockedAxis == 2) {
            var1 = this.anchorX;
         }
      } else {
         this.lockedAxis = 0;
      }

      double var8 = 1.0 - Math.pow(0.85, Math.clamp(var6, 0.0, 0.25) * 60.0);
      this.smoothX = this.smoothX + (var1 - this.smoothX) * var8;
      this.smoothY = this.smoothY + (var3 - this.smoothY) * var8;
      if (Math.abs(var1 - this.smoothX) < 0.25) {
         this.smoothX = var1;
      }

      if (Math.abs(var3 - this.smoothY) < 0.25) {
         this.smoothY = var3;
      }

      return new HudLayout2.DragResult((int)Math.round(this.smoothX), (int)Math.round(this.smoothY), -1, -1);
   }

   public static double step(HudLayout2.ResizeDrag var0, double var1, double var3) {
      double var5 = (var1 - var0.mouseX()) / Math.max(20.0, var0.bounds().width() / var0.initialScale());
      double var7 = (var3 - var0.mouseY()) / Math.max(20.0, var0.bounds().height() / var0.initialScale());
      return Math.clamp(Math.round((var0.initialScale() + (Math.abs(var5) > Math.abs(var7) ? var5 : var7)) / 0.02) * 0.02, 0.2, 4.0);
   }
}
