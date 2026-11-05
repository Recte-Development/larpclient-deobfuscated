package com.larpclient;

public final class p4 {
   public static final double Y = 0.14;
   private static final p4.q R = new p4.q(0.0F, 0.0F);
   private float h;
   private float G;
   private float d;
   private double u = Double.NaN;

   public void o() {
      this.h = this.G = this.d = 0.0F;
      this.u = Double.NaN;
   }

   public p4.q P(tt.x var1, double var2) {
      if (var1.U == 2) {
         this.u = Double.NaN;
         this.h = var1.K;
         this.G = a.C(this.h);
         return new p4.q(this.h, this.G);
      }

      if (var1.r && this.G > 0.0F) {
         this.u = var2;
         this.d = this.G;
         mt.U("SwordModels", "Watch inspect interrupted at phase {}, releasing over {}s", String.format("%.2f", this.h), 0.14);
      }

      this.G = 0.0F;
      if (Double.isFinite(this.u) && Double.isFinite(var2)) {
         float var4 = (float)Math.clamp((var2 - this.u) / 0.14, 0.0, 1.0);
         if (!(var4 >= 1.0F) && !(var2 < this.u)) {
            float var5 = 1.0F - var4 * var4 * (3.0F - 2.0F * var4);
            return new p4.q(this.h, this.d * var5);
         } else {
            this.u = Double.NaN;
            return R;
         }
      } else {
         return R;
      }
   }

   public record q(float phase, float weight) {
   }
}
