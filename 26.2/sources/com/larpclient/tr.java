package com.larpclient;

public final class tr {
   public static final double O = 0.14;
   private static final tr.f I = new tr.f(0.0F, 0.0F);
   private float Z;
   private float G;
   private float j;
   private double a = Double.NaN;

   public void x() {
      this.Z = this.G = this.j = 0.0F;
      this.a = Double.NaN;
   }

   public tr.f J(gx.f var1, double var2) {
      if (var1.C == 2) {
         this.a = Double.NaN;
         this.Z = var1.t;
         this.G = wh.z(this.Z);
         return new tr.f(this.Z, this.G);
      }

      if (var1.H && this.G > 0.0F) {
         this.a = var2;
         this.j = this.G;
         h.U("SwordModels", "Watch inspect interrupted at phase {}, releasing over {}s", String.format("%.2f", this.Z), 0.14);
      }

      this.G = 0.0F;
      if (Double.isFinite(this.a) && Double.isFinite(var2)) {
         float var4 = (float)Math.clamp((var2 - this.a) / 0.14, 0.0, 1.0);
         if (!(var4 >= 1.0F) && !(var2 < this.a)) {
            float var5 = 1.0F - var4 * var4 * (3.0F - 2.0F * var4);
            return new tr.f(this.Z, this.j * var5);
         } else {
            this.a = Double.NaN;
            return I;
         }
      } else {
         return I;
      }
   }

   public record f(float phase, float weight) {
   }
}
