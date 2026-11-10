package com.larpclient;

public final class fv {
   public static final double d = 0.14;
   private static final fv.i g = new fv.i(0.0F, 0.0F);
   private float n;
   private float U;
   private float X;
   private double l = Double.NaN;

   public void S() {
      this.n = this.U = this.X = 0.0F;
      this.l = Double.NaN;
   }

   public fv.i z(sl.t var1, double var2) {
      if (var1.L == 2) {
         this.l = Double.NaN;
         this.n = var1.J;
         this.U = sn.a(this.n);
         return new fv.i(this.n, this.U);
      }

      if (var1.P && this.U > 0.0F) {
         this.l = var2;
         this.X = this.U;
         q8.v("SwordModels", "Watch inspect interrupted at phase {}, releasing over {}s", String.format("%.2f", this.n), 0.14);
      }

      this.U = 0.0F;
      if (Double.isFinite(this.l) && Double.isFinite(var2)) {
         float var4 = (float)Math.clamp((var2 - this.l) / 0.14, 0.0, 1.0);
         if (!(var4 >= 1.0F) && !(var2 < this.l)) {
            float var5 = 1.0F - var4 * var4 * (3.0F - 2.0F * var4);
            return new fv.i(this.n, this.X * var5);
         } else {
            this.l = Double.NaN;
            return g;
         }
      } else {
         return g;
      }
   }

   public record i(float phase, float weight) {
   }
}
