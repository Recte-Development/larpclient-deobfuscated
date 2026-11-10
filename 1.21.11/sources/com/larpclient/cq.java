package com.larpclient;

public final class cq {
   public static final int i = 120;
   private final double[] b = new double[120];
   private int w;
   private int z;
   private double g;
   private double G;
   private double Y;
   private Object Z;
   private Object q;
   private boolean D;

   public void H() {
      this.w = this.z = 0;
      this.g = 0.0;
      this.D = (boolean)0;
      this.Z = this.q = null;
   }

   public void e() {
      this.D = (boolean)0;
   }

   public void n(Object var1, Object var2, double var3, double var5, double var7) {
      if (var1 != null && var2 != null && Double.isFinite(var3) && Double.isFinite(var5) && Double.isFinite(var7) && !(var7 <= 0.0)) {
         if (this.Z != var1 || this.q != var2) {
            this.H();
            this.Z = var1;
            this.q = var2;
         }

         if (!this.D) {
            this.G = var3;
            this.Y = var5;
            this.D = (boolean)1;
         } else {
            double var9 = Math.hypot(var3 - this.G, var5 - this.Y);
            this.G = var3;
            this.Y = var5;
            if (var9 > 16.0) {
               this.w = this.z = 0;
               this.g = 0.0;
            } else {
               double var11 = var9 * var7;
               if (!Double.isFinite(var11)) {
                  this.H();
               } else {
                  if (this.z == 120) {
                     this.g = this.g - this.b[this.w];
                     this.b[this.w] = var11;
                     this.w = (this.w + 1) % 120;
                  } else {
                     this.b[(this.w + this.z++) % 120] = var11;
                  }

                  this.g += var11;
               }
            }
         }
      } else {
         this.H();
      }
   }

   public int X() {
      return this.z;
   }

   public double G(int var1) {
      if (var1 >= 0 && var1 < this.z) {
         return this.b[(this.w + var1) % 120];
      } else {
         throw new IndexOutOfBoundsException(var1);
      }
   }

   public double g() {
      return this.z == 0 ? 0.0 : this.G(this.z - 1);
   }

   public double O() {
      return this.z == 0 ? 0.0 : Math.max(0.0, this.g / this.z);
   }
}
