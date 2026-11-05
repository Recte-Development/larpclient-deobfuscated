package com.larpclient;

public final class og {
   public static final double U = 0.12;
   public static final double M = 0.48;
   private static final double B = 3.8;
   private static final double Q = 0.14;
   private static final double j = 0.22;
   private final og.j R;
   private long n = -1L;
   private boolean V;
   private boolean H;
   private boolean A;
   private double i;
   private double Z;
   private double m;
   private double l;
   private double r;
   private _t y;
   private og.u W;
   private og.e S = og.e.H;
   private float q = -16.0F;
   static final double u = 2.101;
   static final double K = 0.15963829787234074;

   public og() {
      this(new og.j());
   }

   private og(og.j var1) {
      this.R = var1;
   }

   public static og.u m(_q var0) {
      return p(var0, 0.0);
   }

   public static og.u p(_q var0, double var1) {
      int var3 = var0.width();
      int var4 = var0.height();
      int var5 = var0.navigation(0).x() + var0.menuWidth() + 18;
      int var6 = Math.max(0, var3 - 16 - var5);
      if (var6 >= 64 && var4 >= 180) {
         int var7 = Math.min(240, var6);
         int var8 = (int)Math.min(360.0, Math.min(var4 * 0.59, var7 * 1.5));
         int var9 = Math.clamp((int)(var3 * 0.738 - var7 / 2.0), var5, var3 - 16 - var7);
         int var10 = Math.min(var4 - 18, (var4 + var8) / 2);
         int var11 = Math.max(58, var10 - var8);
         var8 = var10 - var11;
         float var12 = 0.94F * var8 / 2.125F;
         if (var1 > 0.15963829787234074) {
            int var13 = (int)Math.ceil((2.101 + var1) * var12 - var8);
            int var14 = Math.min(var13, Math.max(0, var4 - 18 - var10));
            var10 += var14;
            var11 -= Math.min(var13 - var14, Math.max(0, var11 - 58));
            var8 = var10 - var11;
         }

         int var16 = Math.round(var10 - 0.101F * var12);
         return new og.u(new o6.b(var9, var11, var7, var8), new o6.b(var9, var11 - 24, var7, 18), var12, var16, true);
      } else {
         return new og.u(new o6.b(var3, 0, 0, 0), new o6.b(var3, 0, 0, 0), 0.0F, 0, false);
      }
   }

   public static int T(og.u var0, double var1) {
      o6.b var3 = var0.model();
      return Math.clamp(var3.y() + var3.height() - (int)Math.ceil((2.101 + var1) * var0.scale()), 0, var3.y());
   }

   public static double r(og.u var0, int var1) {
      return var1 + var0.scale() + 8.0F - var0.centerX();
   }

   public static double r(double var0) {
      return var0 / 3.8 + 0.18;
   }

   public og.s D(long var1, boolean var3, boolean var4, boolean var5, boolean var6, og.u var7, int var8, int var9, int var10) {
      og.s var11 = this.q(var1, var3, var4, var5, var6, var7, var8, var9, var10);
      this.S = var11.stage();
      this.W = var7;
      this.A = var4;
      this.q = var11.pose().bodyYaw();
      return var11;
   }

   private og.s q(long var1, boolean var3, boolean var4, boolean var5, boolean var6, og.u var7, int var8, int var9, int var10) {
      double var11 = this.n >= 0L && var1 >= this.n && var1 - this.n <= 500000000L ? (var1 - this.n) / 1.0E9 : 0.0;
      this.n = var1;
      if (!var3 || !this.V || !var6) {
         var11 = 0.0;
      }

      this.V = (boolean)(var3 && var6 ? 1 : 0);
      if (!var6 || !var7.visible()) {
         this.y = null;
      }

      if (var6 && var7.visible()) {
         if (!var4) {
            this.y = null;
         }

         if (!var4 || !var5) {
            this.R.E = (boolean)1;
         }

         if (!this.H && var3) {
            this.H = (boolean)1;
            this.m = r(var7, var8) / var7.scale();
            var11 = 0.0;
         }

         if (var4) {
            this.Z += var11;
            int var13 = var9 >= 0 && var10 >= 0 ? 1 : 0;
            double var14 = var13 != 0 ? Math.clamp((var9 - var7.centerX()) / Math.max(80.0, var7.model().height() * 0.8), -1.0, 1.0) : 0.0;
            double var16 = var13 != 0
               ? Math.clamp((var10 - var7.model().y() - var7.model().height() * 0.15) / Math.max(60.0, var7.model().height() * 0.7), -1.0, 1.0)
               : 0.0;
            double var18 = -Math.expm1(-var11 * 9.0);
            this.l = this.l + (var14 - this.l) * var18;
            this.r = this.r + (var16 - this.r) * var18;
         } else {
            this.Z = this.l = this.r = 0.0;
         }

         float var30 = (float)(-16.0 + this.l * 4.0);
         float var31 = var4 ? (float)(-this.l * 0.3) : 0.0F;
         float var15 = (float)(this.r * 0.2);
         if (this.R.E && this.y != null) {
            og.s var32 = this.y.u(var11, var7.scale(), var7.floorY(), var8, var7.centerX(), var30, var31, var15, (float)(this.Z * 20.0));
            if (var32 != null) {
               return var32;
            }

            this.y = null;
         }

         if (this.R.E) {
            return new og.s(og.e.n, 0.0, 1.0, new og.i(var30, var31, var15, 0.0F, 0.0F, (float)(this.Z * 20.0)));
         } else if (!this.H) {
            return new og.s(og.e.H, 0.0, 0.0, new og.i(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
         } else {
            double var33 = 0.12 + r(this.m) + 0.48;
            this.i = Math.min(this.i + var11, var33);
            double var34 = this.i - 0.12;
            if (var34 < 0.0) {
               return new og.s(og.e.H, 0.0, 0.0, new og.i(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float)(this.Z * 20.0)));
            } else {
               og.t var20 = W(var34, this.m);
               float var21 = (float)(var20.distance * 4.0);
               if (var34 < r(this.m)) {
                  return new og.s(
                     og.e.e,
                     r(var7, var8) * (1.0 - var20.distance / this.m),
                     0.0,
                     new og.i(-90.0F, 0.0F, 0.0F, var21, (float)(var20.speed / 5.0), (float)(this.Z * 20.0))
                  );
               } else if (this.i >= var33) {
                  this.R.E = (boolean)1;
                  return new og.s(og.e.n, 0.0, 1.0, new og.i(var30, var31, var15, 0.0F, 0.0F, (float)(this.Z * 20.0)));
               } else {
                  double var22 = var34 - r(this.m);
                  double var24 = K(var22 / 0.48);
                  double var26 = K(var22 / 0.33599999999999997);
                  float var28 = (float)(-90.0 + (var30 + 90.0F) * var24);
                  float var29 = (float)(Math.clamp(Math.toRadians(var28 - var30) + var31, -0.75, 0.75) * var26);
                  return new og.s(og.e.y, 0.0, 0.0, new og.i(var28, var29, (float)(var15 * var26), var21, 0.0F, (float)(this.Z * 20.0)));
               }
            }
         }
      } else {
         return new og.s(og.e.H, 0.0, 0.0, new og.i(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
      }
   }

   private static og.t W(double var0, double var2) {
      double var4 = Math.max(0.0, var2 / 3.8 - 0.18);
      if (var0 >= r(var2)) {
         return new og.t(var2, 0.0);
      }

      if (var0 < 0.14) {
         double var8 = Math.max(0.0, var0);
         return new og.t(3.8 * (var8 / 2.0 - 0.02228169203286535 * Math.sin(Math.PI * var8 / 0.14)), 3.8 * (0.5 - 0.5 * Math.cos(Math.PI * var8 / 0.14)));
      }

      if (var0 < 0.14 + var4) {
         return new og.t(3.8 * (0.07 + var0 - 0.14), 3.8);
      }

      double var6 = var0 - 0.14 - var4;
      return new og.t(
         3.8 * (0.07 + var4 + var6 / 2.0 + 0.03501408748021698 * Math.sin(Math.PI * var6 / 0.22)), 3.8 * (0.5 + 0.5 * Math.cos(Math.PI * var6 / 0.22))
      );
   }

   private static double K(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   public boolean m(double var1, double var3) {
      if (this.W == null || !this.W.visible() || !this.A || !this.R.E) {
         return false;
      } else if (this.y == null) {
         return this.S != og.e.n ? false : _t.l((var1 - this.W.centerX()) / this.W.scale(), (var3 - this.W.floorY()) / this.W.scale() + 1.15, false);
      } else {
         return this.y.p() || this.y.E(var1, var3, this.W.scale());
      }
   }

   public boolean c(double var1, double var3) {
      if (this.m(var1, var3) && (this.y == null || !this.y.p())) {
         if (this.y == null) {
            this.y = new _t(this.W.centerX(), this.W.floorY() - 1.15 * this.W.scale(), this.q);
         }

         this.y.t(var1, var3, this.W.scale());
         return true;
      } else {
         return false;
      }
   }

   public void x(double var1, double var3) {
      if (this.y != null) {
         this.y.K(var1, var3);
      }
   }

   public boolean L() {
      return this.y != null && this.y.p();
   }

   public boolean g() {
      return this.y != null && this.y.l();
   }

   public double[][] O() {
      return this.y != null && this.W != null ? this.y.Y(this.W.scale()) : null;
   }

   public void O(boolean var1) {
      if (var1) {
         this.R.E = (boolean)1;
      }

      this.y = null;
      this.l();
   }

   public void j() {
      this.R.E = this.H = (boolean)0;
      this.i = 0.0;
      this.y = null;
      this.l();
   }

   public void l() {
      this.n = -1L;
      this.V = (boolean)0;
   }

   public record a(
      float rightArmX,
      float rightArmZ,
      float leftArmX,
      float leftArmZ,
      float rightLegX,
      float rightLegZ,
      float leftLegX,
      float leftLegZ,
      float headRoll,
      float weight,
      boolean crouch
   ) {
   }

   public enum e {
      H,
      e,
      y,
      n,
      d,
      a,
      I,
      b,
      B,
      V,
      E;
   }

   public record h(int left, int top, int side, float shiftX, float shiftY) {
      public static og.h of(og.q var0, float var1, double var2) {
         int var4 = (int)Math.ceil(2.0 * var2 * var1) + 2;
         int var5 = (int)Math.floor(var0.x() - var4 / 2.0);
         int var6 = (int)Math.floor(var0.y() - var4 / 2.0);
         float var7 = -1.251F;
         return new og.h(var5, var6, var4, (float)((var0.x() - var5 - var4 / 2.0) / var1), (float)((var0.y() - var6 - var4) / var1) - var7);
      }
   }

   public record i(float bodyYaw, float headYaw, float headPitch, float walkPosition, float walkSpeed, float ageInTicks) {
   }

   public static final class j {
      private boolean E;

      public og K() {
         return new og(this);
      }
   }

   public record q(double x, double y, float roll, double lift, og.a limbs, float dizzy, float impact, double vx, double vy, float spin) {
   }

   public record s(og.e stage, double xOffset, double nameOpacity, og.i pose, og.q toy) {
      public s(og.e var1, double var2, double var4, og.i var6) {
         this(var1, var2, var4, var6, null);
      }
   }

   private record t(double distance, double speed) {
   }

   public record u(o6.b model, o6.b name, float scale, int floorY, boolean visible) {
      public float centerX() {
         return this.model.x() + this.model.width() / 2.0F;
      }

      public int renderWidth() {
         return Math.max(this.model.width(), (int)Math.ceil(this.scale * 2.3));
      }
   }
}
