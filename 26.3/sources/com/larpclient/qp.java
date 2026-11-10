package com.larpclient;

public final class qp {
   private double j;
   private double k;
   private double X;
   private double U;
   private int Z;

   public void O(double var1, double var3) {
      this.j = this.X = var1;
      this.k = this.U = var3;
      this.Z = 0;
   }

   public qy.o D(double var1, double var3, boolean var5, double var6) {
      if (var5) {
         if (this.Z == 0 && Math.max(Math.abs(var1 - this.X), Math.abs(var3 - this.U)) > 2.0) {
            this.Z = Math.abs(var1 - this.X) >= Math.abs(var3 - this.U) ? 1 : 2;
         }

         if (this.Z == 1) {
            var3 = this.U;
         }

         if (this.Z == 2) {
            var1 = this.X;
         }
      } else {
         this.Z = 0;
      }

      double var8 = 1.0 - Math.pow(0.85, Math.clamp(var6, 0.0, 0.25) * 60.0);
      this.j = this.j + (var1 - this.j) * var8;
      this.k = this.k + (var3 - this.k) * var8;
      if (Math.abs(var1 - this.j) < 0.25) {
         this.j = var1;
      }

      if (Math.abs(var3 - this.k) < 0.25) {
         this.k = var3;
      }

      return new qy.o((int)Math.round(this.j), (int)Math.round(this.k), -1, -1);
   }

   public static double D(qy.w var0, double var1, double var3) {
      double var5 = (var1 - var0.mouseX()) / Math.max(20.0, var0.bounds().width() / var0.initialScale());
      double var7 = (var3 - var0.mouseY()) / Math.max(20.0, var0.bounds().height() / var0.initialScale());
      return Math.clamp(Math.round((var0.initialScale() + (Math.abs(var5) > Math.abs(var7) ? var5 : var7)) / 0.02) * 0.02, 0.2, 4.0);
   }
}
