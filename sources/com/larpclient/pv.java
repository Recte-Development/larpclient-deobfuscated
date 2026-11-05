package com.larpclient;

public final class pv {
   private double i;
   private double J;
   private double d;
   private double C;
   private int I;

   public void R(double var1, double var3) {
      this.i = this.d = var1;
      this.J = this.C = var3;
      this.I = 0;
   }

   public ez.s s(double var1, double var3, boolean var5, double var6) {
      if (var5) {
         if (this.I == 0 && Math.max(Math.abs(var1 - this.d), Math.abs(var3 - this.C)) > 2.0) {
            this.I = Math.abs(var1 - this.d) >= Math.abs(var3 - this.C) ? 1 : 2;
         }

         if (this.I == 1) {
            var3 = this.C;
         }

         if (this.I == 2) {
            var1 = this.d;
         }
      } else {
         this.I = 0;
      }

      double var8 = 1.0 - Math.pow(0.85, Math.clamp(var6, 0.0, 0.25) * 60.0);
      this.i = this.i + (var1 - this.i) * var8;
      this.J = this.J + (var3 - this.J) * var8;
      if (Math.abs(var1 - this.i) < 0.25) {
         this.i = var1;
      }

      if (Math.abs(var3 - this.J) < 0.25) {
         this.J = var3;
      }

      return new ez.s((int)Math.round(this.i), (int)Math.round(this.J), -1, -1);
   }

   public static double o(ez.i var0, double var1, double var3) {
      double var5 = (var1 - var0.mouseX()) / Math.max(20.0, var0.bounds().width() / var0.initialScale());
      double var7 = (var3 - var0.mouseY()) / Math.max(20.0, var0.bounds().height() / var0.initialScale());
      return Math.clamp(Math.round((var0.initialScale() + (Math.abs(var5) > Math.abs(var7) ? var5 : var7)) / 0.02) * 0.02, 0.5, 2.5);
   }
}
