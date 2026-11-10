package com.larpclient;

public final class u1 {
   private double d;
   private double o;
   private double M;
   private double X;
   private int Q;

   public void z(double var1, double var3) {
      this.d = this.M = var1;
      this.o = this.X = var3;
      this.Q = 0;
   }

   public cv.s a(double var1, double var3, boolean var5, double var6) {
      if (var5) {
         if (this.Q == 0 && Math.max(Math.abs(var1 - this.M), Math.abs(var3 - this.X)) > 2.0) {
            this.Q = Math.abs(var1 - this.M) >= Math.abs(var3 - this.X) ? 1 : 2;
         }

         if (this.Q == 1) {
            var3 = this.X;
         }

         if (this.Q == 2) {
            var1 = this.M;
         }
      } else {
         this.Q = 0;
      }

      double var8 = 1.0 - Math.pow(0.85, Math.clamp(var6, 0.0, 0.25) * 60.0);
      this.d = this.d + (var1 - this.d) * var8;
      this.o = this.o + (var3 - this.o) * var8;
      if (Math.abs(var1 - this.d) < 0.25) {
         this.d = var1;
      }

      if (Math.abs(var3 - this.o) < 0.25) {
         this.o = var3;
      }

      return new cv.s((int)Math.round(this.d), (int)Math.round(this.o), -1, -1);
   }

   public static double l(cv.b var0, double var1, double var3) {
      double var5 = (var1 - var0.mouseX()) / Math.max(20.0, var0.bounds().width() / var0.initialScale());
      double var7 = (var3 - var0.mouseY()) / Math.max(20.0, var0.bounds().height() / var0.initialScale());
      return Math.clamp(Math.round((var0.initialScale() + (Math.abs(var5) > Math.abs(var7) ? var5 : var7)) / 0.02) * 0.02, 0.2, 4.0);
   }
}
