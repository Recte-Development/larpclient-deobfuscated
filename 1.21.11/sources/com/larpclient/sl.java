package com.larpclient;

public final class sl {
   private static final double P = 0.5;
   private Object I;
   private co E;
   private sl.a b = sl.a.C;
   private double v;
   private double k = Double.NaN;
   private double x;
   private final sl.t X = new sl.t();

   public sl.a b() {
      return this.b;
   }

   public void e() {
      this.I = null;
      this.E = null;
      this.k = Double.NaN;
      this.b = sl.a.C;
   }

   public void L() {
      if (this.I != null) {
         this.b = sl.a.G;
      }
   }

   public boolean o(double var1, double var3) {
      if (this.I != null && this.b == sl.a.C) {
         this.b = sl.a.l;
         this.v = var1;
         this.x = this.E.f / (this.E.M * Math.clamp(Double.isFinite(var3) ? var3 : 2.0, 0.5, 2.0));
         return true;
      } else {
         return false;
      }
   }

   public sl.t j(Object var1, co var2, double var3, float var5, boolean var6) {
      int var7 = this.X.L == 2 ? 1 : 0;
      this.X.M();
      if (var1 == null) {
         this.e();
         return this.X;
      }

      if (!Double.isFinite(var3)) {
         var3 = 0.0;
      }

      if (var1 != this.I || var2 != this.E) {
         var7 = 0;
         this.I = var1;
         this.E = var2;
         this.b = sl.a.b;
         this.v = var3;
      } else if (Double.isFinite(this.k) && (var3 < this.k || var3 - this.k > 0.500001)) {
         var7 = 0;
         this.b = sl.a.C;
      }

      this.k = var3;
      var5 = Float.isFinite(var5) ? Math.clamp(var5, 0.0F, 1.0F) : 0.0F;
      this.X.P = (boolean)(var7 == 0 || this.b != sl.a.G && !(var5 > 0.0F) ? 0 : 1);
      if (var5 > 0.0F) {
         this.b = sl.a.G;
      } else if (this.b == sl.a.G) {
         this.b = sl.a.C;
      }

      if (this.b == sl.a.b) {
         float var8 = (float)Math.clamp((var3 - this.v) / this.E.n, 0.0, 1.0);
         this.X.L = 1;
         this.X.J = var8;
         if (var8 >= 1.0F) {
            this.b = sl.a.C;
         }
      } else if (this.b == sl.a.l) {
         float var10 = (float)Math.clamp((var3 - this.v) / this.x, 0.0, 1.0);
         this.X.L = 2;
         this.X.J = var10;
         if (var10 >= 1.0F) {
            this.b = sl.a.C;
            this.X.M();
         }
      } else if (this.b == sl.a.G) {
         this.X.L = 3;
         this.X.J = var5;
      }

      if (var6 && this.b == sl.a.C) {
         this.X.t = this.X.t + 0.003F * (float)Math.sin(var3 * 1.8);
      }

      return this.X;
   }

   public enum a {
      b,
      C,
      l,
      G;
   }

   public static final class t {
      public float t;
      public float J;
      public int L;
      public boolean P;

      private void M() {
         this.t = this.J = 0.0F;
         this.L = 0;
         this.P = (boolean)0;
      }
   }
}
