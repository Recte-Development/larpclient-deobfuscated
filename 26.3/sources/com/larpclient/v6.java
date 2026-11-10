package com.larpclient;

public final class v6 {
   public static final double K = 0.14;
   private static final v6.e V = new v6.e(0.0F, 0.0F);
   private float f;
   private float Y;
   private float v;
   private double u = Double.NaN;

   public void O() {
      this.f = this.Y = this.v = 0.0F;
      this.u = Double.NaN;
   }

   public v6.e E(i7.g var1, double var2) {
      if (var1.B == 2) {
         this.u = Double.NaN;
         this.f = var1.v;
         this.Y = dw.i(this.f);
         return new v6.e(this.f, this.Y);
      }

      if (var1.S && this.Y > 0.0F) {
         this.u = var2;
         this.v = this.Y;
         _q.c("SwordModels", "Watch inspect interrupted at phase {}, releasing over {}s", String.format("%.2f", this.f), 0.14);
      }

      this.Y = 0.0F;
      if (Double.isFinite(this.u) && Double.isFinite(var2)) {
         float var4 = (float)Math.clamp((var2 - this.u) / 0.14, 0.0, 1.0);
         if (!(var4 >= 1.0F) && !(var2 < this.u)) {
            float var5 = 1.0F - var4 * var4 * (3.0F - 2.0F * var4);
            return new v6.e(this.f, this.v * var5);
         } else {
            this.u = Double.NaN;
            return V;
         }
      } else {
         return V;
      }
   }

   public record e(float phase, float weight) {
   }
}
