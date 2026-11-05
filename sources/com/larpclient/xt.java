package com.larpclient;

public final class xt {
   public static final double s = 0.14;
   private static final xt.i M = new xt.i(0.0F, 0.0F);
   private float F;
   private float v;
   private float W;
   private double x = Double.NaN;

   public void N() {
      this.F = this.v = this.W = 0.0F;
      this.x = Double.NaN;
   }

   public xt.i y(xh.n var1, double var2) {
      if (var1.k == 2) {
         this.x = Double.NaN;
         this.F = var1.r;
         this.v = rc.X(this.F);
         return new xt.i(this.F, this.v);
      }

      if (var1.K && this.v > 0.0F) {
         this.x = var2;
         this.W = this.v;
         r4.g("SwordModels", "Watch inspect interrupted at phase {}, releasing over {}s", String.format("%.2f", this.F), 0.14);
      }

      this.v = 0.0F;
      if (Double.isFinite(this.x) && Double.isFinite(var2)) {
         float var4 = (float)Math.clamp((var2 - this.x) / 0.14, 0.0, 1.0);
         if (!(var4 >= 1.0F) && !(var2 < this.x)) {
            float var5 = 1.0F - var4 * var4 * (3.0F - 2.0F * var4);
            return new xt.i(this.F, this.W * var5);
         } else {
            this.x = Double.NaN;
            return M;
         }
      } else {
         return M;
      }
   }

   public record i(float phase, float weight) {
   }
}
