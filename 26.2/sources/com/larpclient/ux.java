package com.larpclient;

public final class ux {
   private double d;
   private double n;
   private double G;
   private double x;
   private int f;

   public void o(double var1, double var3) {
      this.d = this.G = var1;
      this.n = this.x = var3;
      this.f = 0;
   }

   public hw.b D(double var1, double var3, boolean var5, double var6) {
      if (var5) {
         if (this.f == 0 && Math.max(Math.abs(var1 - this.G), Math.abs(var3 - this.x)) > 2.0) {
            this.f = Math.abs(var1 - this.G) >= Math.abs(var3 - this.x) ? 1 : 2;
         }

         if (this.f == 1) {
            var3 = this.x;
         }

         if (this.f == 2) {
            var1 = this.G;
         }
      } else {
         this.f = 0;
      }

      double var8 = 1.0 - Math.pow(0.85, Math.clamp(var6, 0.0, 0.25) * 60.0);
      this.d = this.d + (var1 - this.d) * var8;
      this.n = this.n + (var3 - this.n) * var8;
      if (Math.abs(var1 - this.d) < 0.25) {
         this.d = var1;
      }

      if (Math.abs(var3 - this.n) < 0.25) {
         this.n = var3;
      }

      return new hw.b((int)Math.round(this.d), (int)Math.round(this.n), -1, -1);
   }

   public static double S(hw.p var0, double var1, double var3) {
      double var5 = (var1 - var0.mouseX()) / Math.max(20.0, var0.bounds().width() / var0.initialScale());
      double var7 = (var3 - var0.mouseY()) / Math.max(20.0, var0.bounds().height() / var0.initialScale());
      return Math.clamp(Math.round((var0.initialScale() + (Math.abs(var5) > Math.abs(var7) ? var5 : var7)) / 0.02) * 0.02, 0.2, 4.0);
   }
}
