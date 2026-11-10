package com.larpclient;

public final class gx {
   private static final double S = 0.5;
   private Object b;
   private tc a;
   private gx.l z = gx.l.g;
   private double H;
   private double w = Double.NaN;
   private double Z;
   private final gx.f N = new gx.f();

   public gx.l Z() {
      return this.z;
   }

   public void h() {
      this.b = null;
      this.a = null;
      this.w = Double.NaN;
      this.z = gx.l.g;
   }

   public void l() {
      if (this.b != null) {
         this.z = gx.l.B;
      }
   }

   public boolean r(double var1, double var3) {
      if (this.b != null && this.z == gx.l.g) {
         this.z = gx.l.n;
         this.H = var1;
         this.Z = this.a.v / (this.a.R * Math.clamp(Double.isFinite(var3) ? var3 : 2.0, 0.5, 2.0));
         return true;
      } else {
         return false;
      }
   }

   public gx.f u(Object var1, tc var2, double var3, float var5, boolean var6) {
      int var7 = this.N.C == 2 ? 1 : 0;
      this.N.x();
      if (var1 == null) {
         this.h();
         return this.N;
      }

      if (!Double.isFinite(var3)) {
         var3 = 0.0;
      }

      if (var1 != this.b || var2 != this.a) {
         var7 = 0;
         this.b = var1;
         this.a = var2;
         this.z = gx.l.k;
         this.H = var3;
      } else if (Double.isFinite(this.w) && (var3 < this.w || var3 - this.w > 0.500001)) {
         var7 = 0;
         this.z = gx.l.g;
      }

      this.w = var3;
      var5 = Float.isFinite(var5) ? Math.clamp(var5, 0.0F, 1.0F) : 0.0F;
      this.N.H = (boolean)(var7 == 0 || this.z != gx.l.B && !(var5 > 0.0F) ? 0 : 1);
      if (var5 > 0.0F) {
         this.z = gx.l.B;
      } else if (this.z == gx.l.B) {
         this.z = gx.l.g;
      }

      if (this.z == gx.l.k) {
         float var8 = (float)Math.clamp((var3 - this.H) / this.a.z, 0.0, 1.0);
         this.N.C = 1;
         this.N.t = var8;
         if (var8 >= 1.0F) {
            this.z = gx.l.g;
         }
      } else if (this.z == gx.l.n) {
         float var10 = (float)Math.clamp((var3 - this.H) / this.Z, 0.0, 1.0);
         this.N.C = 2;
         this.N.t = var10;
         if (var10 >= 1.0F) {
            this.z = gx.l.g;
            this.N.x();
         }
      } else if (this.z == gx.l.B) {
         this.N.C = 3;
         this.N.t = var5;
      }

      if (var6 && this.z == gx.l.g) {
         this.N.S = this.N.S + 0.003F * (float)Math.sin(var3 * 1.8);
      }

      return this.N;
   }

   public static final class f {
      public float S;
      public float t;
      public int C;
      public boolean H;

      private void x() {
         this.S = this.t = 0.0F;
         this.C = 0;
         this.H = (boolean)0;
      }
   }

   public enum l {
      k,
      g,
      n,
      B;
   }
}
