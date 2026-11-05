package com.larpclient;

public final class lz {
   private double S;
   private double T;
   private double k;
   private double N;
   private int R;

   public void n(double var1, double var3) {
      this.S = this.k = var1;
      this.T = this.N = var3;
      this.R = 0;
   }

   public _h.n X(double var1, double var3, boolean var5, double var6) {
      if (var5) {
         if (this.R == 0 && Math.max(Math.abs(var1 - this.k), Math.abs(var3 - this.N)) > 2.0) {
            this.R = Math.abs(var1 - this.k) >= Math.abs(var3 - this.N) ? 1 : 2;
         }

         if (this.R == 1) {
            var3 = this.N;
         }

         if (this.R == 2) {
            var1 = this.k;
         }
      } else {
         this.R = 0;
      }

      double var8 = 1.0 - Math.pow(0.85, Math.clamp(var6, 0.0, 0.25) * 60.0);
      this.S = this.S + (var1 - this.S) * var8;
      this.T = this.T + (var3 - this.T) * var8;
      if (Math.abs(var1 - this.S) < 0.25) {
         this.S = var1;
      }

      if (Math.abs(var3 - this.T) < 0.25) {
         this.T = var3;
      }

      return new _h.n((int)Math.round(this.S), (int)Math.round(this.T), -1, -1);
   }

   public static double w(_h.z var0, double var1, double var3) {
      double var5 = (var1 - var0.mouseX()) / Math.max(20.0, var0.bounds().width() / var0.initialScale());
      double var7 = (var3 - var0.mouseY()) / Math.max(20.0, var0.bounds().height() / var0.initialScale());
      return Math.clamp(Math.round((var0.initialScale() + (Math.abs(var5) > Math.abs(var7) ? var5 : var7)) / 0.02) * 0.02, 0.5, 2.5);
   }
}
