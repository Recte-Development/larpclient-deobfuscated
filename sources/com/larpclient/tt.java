package com.larpclient;

public final class tt {
   private static final double a = 0.5;
   private Object k;
   private z5 f;
   private tt.v u = tt.v.S;
   private double M;
   private double h = Double.NaN;
   private double V;
   private final tt.x c = new tt.x();

   public tt.v f() {
      return this.u;
   }

   public void A() {
      this.k = null;
      this.f = null;
      this.h = Double.NaN;
      this.u = tt.v.S;
   }

   public void x() {
      if (this.k != null) {
         this.u = tt.v.v;
      }
   }

   public boolean S(double var1, double var3) {
      if (this.k != null && this.u == tt.v.S) {
         this.u = tt.v.T;
         this.M = var1;
         this.V = this.f.e / (this.f.s * Math.clamp(Double.isFinite(var3) ? var3 : 2.0, 0.5, 2.0));
         return true;
      } else {
         return false;
      }
   }

   public tt.x F(Object var1, z5 var2, double var3, float var5, boolean var6) {
      int var7 = this.c.U == 2 ? 1 : 0;
      this.c.f();
      if (var1 == null) {
         this.A();
         return this.c;
      }

      if (!Double.isFinite(var3)) {
         var3 = 0.0;
      }

      if (var1 != this.k || var2 != this.f) {
         var7 = 0;
         this.k = var1;
         this.f = var2;
         this.u = tt.v.F;
         this.M = var3;
      } else if (Double.isFinite(this.h) && (var3 < this.h || var3 - this.h > 0.500001)) {
         var7 = 0;
         this.u = tt.v.S;
      }

      this.h = var3;
      var5 = Float.isFinite(var5) ? Math.clamp(var5, 0.0F, 1.0F) : 0.0F;
      this.c.r = (boolean)(var7 == 0 || this.u != tt.v.v && !(var5 > 0.0F) ? 0 : 1);
      if (var5 > 0.0F) {
         this.u = tt.v.v;
      } else if (this.u == tt.v.v) {
         this.u = tt.v.S;
      }

      if (this.u == tt.v.F) {
         float var8 = (float)Math.clamp((var3 - this.M) / this.f.N, 0.0, 1.0);
         this.c.U = 1;
         this.c.K = var8;
         if (var8 >= 1.0F) {
            this.u = tt.v.S;
         }
      } else if (this.u == tt.v.T) {
         float var10 = (float)Math.clamp((var3 - this.M) / this.V, 0.0, 1.0);
         this.c.U = 2;
         this.c.K = var10;
         if (var10 >= 1.0F) {
            this.u = tt.v.S;
            this.c.f();
         }
      } else if (this.u == tt.v.v) {
         this.c.U = 3;
         this.c.K = var5;
      }

      if (var6 && this.u == tt.v.S) {
         this.c.Z = this.c.Z + 0.003F * (float)Math.sin(var3 * 1.8);
      }

      return this.c;
   }

   public enum v {
      F,
      S,
      T,
      v;
   }

   public static final class x {
      public float Z;
      public float K;
      public int U;
      public boolean r;

      private void f() {
         this.Z = this.K = 0.0F;
         this.U = 0;
         this.r = (boolean)0;
      }
   }
}
