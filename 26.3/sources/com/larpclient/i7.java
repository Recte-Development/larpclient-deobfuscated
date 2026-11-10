package com.larpclient;

public final class i7 {
   private static final double k = 0.5;
   private Object F;
   private s_ w;
   private i7.r L = i7.r.G;
   private double A;
   private double n = Double.NaN;
   private double R;
   private final i7.g z = new i7.g();

   public i7.r q() {
      return this.L;
   }

   public void i() {
      this.F = null;
      this.w = null;
      this.n = Double.NaN;
      this.L = i7.r.G;
   }

   public void X() {
      if (this.F != null) {
         this.L = i7.r.l;
      }
   }

   public boolean t(double var1, double var3) {
      if (this.F != null && this.L == i7.r.G) {
         this.L = i7.r.A;
         this.A = var1;
         this.R = this.w.c / (this.w.v * Math.clamp(Double.isFinite(var3) ? var3 : 2.0, 0.5, 2.0));
         return true;
      } else {
         return false;
      }
   }

   public i7.g q(Object var1, s_ var2, double var3, float var5, boolean var6) {
      int var7 = this.z.B == 2 ? 1 : 0;
      this.z.y();
      if (var1 == null) {
         this.i();
         return this.z;
      }

      if (!Double.isFinite(var3)) {
         var3 = 0.0;
      }

      if (var1 != this.F || var2 != this.w) {
         var7 = 0;
         this.F = var1;
         this.w = var2;
         this.L = i7.r.n;
         this.A = var3;
      } else if (Double.isFinite(this.n) && (var3 < this.n || var3 - this.n > 0.500001)) {
         var7 = 0;
         this.L = i7.r.G;
      }

      this.n = var3;
      var5 = Float.isFinite(var5) ? Math.clamp(var5, 0.0F, 1.0F) : 0.0F;
      this.z.S = (boolean)(var7 == 0 || this.L != i7.r.l && !(var5 > 0.0F) ? 0 : 1);
      if (var5 > 0.0F) {
         this.L = i7.r.l;
      } else if (this.L == i7.r.l) {
         this.L = i7.r.G;
      }

      if (this.L == i7.r.n) {
         float var8 = (float)Math.clamp((var3 - this.A) / this.w.o, 0.0, 1.0);
         this.z.B = 1;
         this.z.v = var8;
         if (var8 >= 1.0F) {
            this.L = i7.r.G;
         }
      } else if (this.L == i7.r.A) {
         float var10 = (float)Math.clamp((var3 - this.A) / this.R, 0.0, 1.0);
         this.z.B = 2;
         this.z.v = var10;
         if (var10 >= 1.0F) {
            this.L = i7.r.G;
            this.z.y();
         }
      } else if (this.L == i7.r.l) {
         this.z.B = 3;
         this.z.v = var5;
      }

      if (var6 && this.L == i7.r.G) {
         this.z.A = this.z.A + 0.003F * (float)Math.sin(var3 * 1.8);
      }

      return this.z;
   }

   public static final class g {
      public float A;
      public float v;
      public int B;
      public boolean S;

      private void y() {
         this.A = this.v = 0.0F;
         this.B = 0;
         this.S = (boolean)0;
      }
   }

   public enum r {
      n,
      G,
      A,
      l;
   }
}
