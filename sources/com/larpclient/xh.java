package com.larpclient;

public final class xh {
   private static final double o = 0.5;
   private Object l;
   private yi M;
   private xh.m O = xh.m.Q;
   private double i;
   private double S = Double.NaN;
   private double m;
   private final xh.n R = new xh.n();

   public xh.m v() {
      return this.O;
   }

   public void c() {
      this.l = null;
      this.M = null;
      this.S = Double.NaN;
      this.O = xh.m.Q;
   }

   public void P() {
      if (this.l != null) {
         this.O = xh.m.z;
      }
   }

   public boolean Y(double var1, double var3) {
      if (this.l != null && this.O == xh.m.Q) {
         this.O = xh.m.t;
         this.i = var1;
         this.m = this.M.p / (this.M.b * Math.clamp(Double.isFinite(var3) ? var3 : 2.0, 0.5, 2.0));
         return true;
      } else {
         return false;
      }
   }

   public xh.n X(Object var1, yi var2, double var3, float var5, boolean var6) {
      int var7 = this.R.k == 2 ? 1 : 0;
      this.R.J();
      if (var1 == null) {
         this.c();
         return this.R;
      }

      if (!Double.isFinite(var3)) {
         var3 = 0.0;
      }

      if (var1 != this.l || var2 != this.M) {
         var7 = 0;
         this.l = var1;
         this.M = var2;
         this.O = xh.m.k;
         this.i = var3;
      } else if (Double.isFinite(this.S) && (var3 < this.S || var3 - this.S > 0.500001)) {
         var7 = 0;
         this.O = xh.m.Q;
      }

      this.S = var3;
      var5 = Float.isFinite(var5) ? Math.clamp(var5, 0.0F, 1.0F) : 0.0F;
      this.R.K = (boolean)(var7 == 0 || this.O != xh.m.z && !(var5 > 0.0F) ? 0 : 1);
      if (var5 > 0.0F) {
         this.O = xh.m.z;
      } else if (this.O == xh.m.z) {
         this.O = xh.m.Q;
      }

      if (this.O == xh.m.k) {
         float var8 = (float)Math.clamp((var3 - this.i) / this.M.H, 0.0, 1.0);
         this.R.k = 1;
         this.R.r = var8;
         if (var8 >= 1.0F) {
            this.O = xh.m.Q;
         }
      } else if (this.O == xh.m.t) {
         float var10 = (float)Math.clamp((var3 - this.i) / this.m, 0.0, 1.0);
         this.R.k = 2;
         this.R.r = var10;
         if (var10 >= 1.0F) {
            this.O = xh.m.Q;
            this.R.J();
         }
      } else if (this.O == xh.m.z) {
         this.R.k = 3;
         this.R.r = var5;
      }

      if (var6 && this.O == xh.m.Q) {
         this.R.I = this.R.I + 0.003F * (float)Math.sin(var3 * 1.8);
      }

      return this.R;
   }

   public enum m {
      k,
      Q,
      t,
      z;
   }

   public static final class n {
      public float I;
      public float r;
      public int k;
      public boolean K;

      private void J() {
         this.I = this.r = 0.0F;
         this.k = 0;
         this.K = (boolean)0;
      }
   }
}
