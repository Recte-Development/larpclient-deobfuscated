package com.larpclient;

public final class o5 {
   public static final double h = 0.12;
   public static final double D = 0.48;
   private static final double p = 3.8;
   private static final double Q = 0.14;
   private static final double W = 0.22;
   private final o5.b V;
   private long y = -1L;
   private boolean l;
   private boolean S;
   private boolean m;
   private double f;
   private double k;
   private double a;
   private double B;
   private double u;
   private pe C;
   private o5.t H;
   private o5.o G = o5.o.f;
   private float v = -16.0F;
   static final double s = 2.101;
   static final double b = 0.15963829787234074;

   public o5() {
      this(new o5.b());
   }

   private o5(o5.b var1) {
      this.V = var1;
   }

   public static o5.t n(qa var0) {
      return m(var0, 0.0);
   }

   public static o5.t m(qa var0, double var1) {
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
         return new o5.t(new _c.f(var9, var11, var7, var8), new _c.f(var9, var11 - 24, var7, 18), var12, var16, true);
      } else {
         return new o5.t(new _c.f(var3, 0, 0, 0), new _c.f(var3, 0, 0, 0), 0.0F, 0, false);
      }
   }

   public static int S(o5.t var0, double var1) {
      _c.f var3 = var0.model();
      return Math.clamp(var3.y() + var3.height() - (int)Math.ceil((2.101 + var1) * var0.scale()), 0, var3.y());
   }

   public static double U(o5.t var0, int var1) {
      return var1 + var0.scale() + 8.0F - var0.centerX();
   }

   public static double v(double var0) {
      return var0 / 3.8 + 0.18;
   }

   public o5.z a(long var1, boolean var3, boolean var4, boolean var5, boolean var6, o5.t var7, int var8, int var9, int var10) {
      o5.z var11 = this.z(var1, var3, var4, var5, var6, var7, var8, var9, var10);
      this.G = var11.stage();
      this.H = var7;
      this.m = var4;
      this.v = var11.pose().bodyYaw();
      return var11;
   }

   private o5.z z(long var1, boolean var3, boolean var4, boolean var5, boolean var6, o5.t var7, int var8, int var9, int var10) {
      double var11 = this.y >= 0L && var1 >= this.y && var1 - this.y <= 500000000L ? (var1 - this.y) / 1.0E9 : 0.0;
      this.y = var1;
      if (!var3 || !this.l || !var6) {
         var11 = 0.0;
      }

      this.l = (boolean)(var3 && var6 ? 1 : 0);
      if (!var6 || !var7.visible()) {
         this.C = null;
      }

      if (var6 && var7.visible()) {
         if (!var4) {
            this.C = null;
         }

         if (!var4 || !var5) {
            this.V.d = (boolean)1;
         }

         if (!this.S && var3) {
            this.S = (boolean)1;
            this.a = U(var7, var8) / var7.scale();
            var11 = 0.0;
         }

         if (var4) {
            this.k += var11;
            int var13 = var9 >= 0 && var10 >= 0 ? 1 : 0;
            double var14 = var13 != 0 ? Math.clamp((var9 - var7.centerX()) / Math.max(80.0, var7.model().height() * 0.8), -1.0, 1.0) : 0.0;
            double var16 = var13 != 0
               ? Math.clamp((var10 - var7.model().y() - var7.model().height() * 0.15) / Math.max(60.0, var7.model().height() * 0.7), -1.0, 1.0)
               : 0.0;
            double var18 = -Math.expm1(-var11 * 9.0);
            this.B = this.B + (var14 - this.B) * var18;
            this.u = this.u + (var16 - this.u) * var18;
         } else {
            this.k = this.B = this.u = 0.0;
         }

         float var30 = (float)(-16.0 + this.B * 4.0);
         float var31 = var4 ? (float)(-this.B * 0.3) : 0.0F;
         float var15 = (float)(this.u * 0.2);
         if (this.V.d && this.C != null) {
            o5.z var32 = this.C.p(var11, var7.scale(), var7.floorY(), var8, var7.centerX(), var30, var31, var15, (float)(this.k * 20.0));
            if (var32 != null) {
               return var32;
            }

            this.C = null;
         }

         if (this.V.d) {
            return new o5.z(o5.o.H, 0.0, 1.0, new o5.j(var30, var31, var15, 0.0F, 0.0F, (float)(this.k * 20.0)));
         } else if (!this.S) {
            return new o5.z(o5.o.f, 0.0, 0.0, new o5.j(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
         } else {
            double var33 = 0.12 + v(this.a) + 0.48;
            this.f = Math.min(this.f + var11, var33);
            double var34 = this.f - 0.12;
            if (var34 < 0.0) {
               return new o5.z(o5.o.f, 0.0, 0.0, new o5.j(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float)(this.k * 20.0)));
            } else {
               o5.q var20 = H(var34, this.a);
               float var21 = (float)(var20.distance * 4.0);
               if (var34 < v(this.a)) {
                  return new o5.z(
                     o5.o.m,
                     U(var7, var8) * (1.0 - var20.distance / this.a),
                     0.0,
                     new o5.j(-90.0F, 0.0F, 0.0F, var21, (float)(var20.speed / 5.0), (float)(this.k * 20.0))
                  );
               } else if (this.f >= var33) {
                  this.V.d = (boolean)1;
                  return new o5.z(o5.o.H, 0.0, 1.0, new o5.j(var30, var31, var15, 0.0F, 0.0F, (float)(this.k * 20.0)));
               } else {
                  double var22 = var34 - v(this.a);
                  double var24 = M(var22 / 0.48);
                  double var26 = M(var22 / 0.33599999999999997);
                  float var28 = (float)(-90.0 + (var30 + 90.0F) * var24);
                  float var29 = (float)(Math.clamp(Math.toRadians(var28 - var30) + var31, -0.75, 0.75) * var26);
                  return new o5.z(o5.o.a, 0.0, 0.0, new o5.j(var28, var29, (float)(var15 * var26), var21, 0.0F, (float)(this.k * 20.0)));
               }
            }
         }
      } else {
         return new o5.z(o5.o.f, 0.0, 0.0, new o5.j(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
      }
   }

   private static o5.q H(double var0, double var2) {
      double var4 = Math.max(0.0, var2 / 3.8 - 0.18);
      if (var0 >= v(var2)) {
         return new o5.q(var2, 0.0);
      }

      if (var0 < 0.14) {
         double var8 = Math.max(0.0, var0);
         return new o5.q(3.8 * (var8 / 2.0 - 0.02228169203286535 * Math.sin(Math.PI * var8 / 0.14)), 3.8 * (0.5 - 0.5 * Math.cos(Math.PI * var8 / 0.14)));
      }

      if (var0 < 0.14 + var4) {
         return new o5.q(3.8 * (0.07 + var0 - 0.14), 3.8);
      }

      double var6 = var0 - 0.14 - var4;
      return new o5.q(
         3.8 * (0.07 + var4 + var6 / 2.0 + 0.03501408748021698 * Math.sin(Math.PI * var6 / 0.22)), 3.8 * (0.5 + 0.5 * Math.cos(Math.PI * var6 / 0.22))
      );
   }

   private static double M(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   public boolean o(double var1, double var3) {
      if (this.H == null || !this.H.visible() || !this.m || !this.V.d) {
         return false;
      } else if (this.C == null) {
         return this.G != o5.o.H ? false : pe.W((var1 - this.H.centerX()) / this.H.scale(), (var3 - this.H.floorY()) / this.H.scale() + 1.15, false);
      } else {
         return this.C.q() || this.C.o(var1, var3, this.H.scale());
      }
   }

   public boolean K(double var1, double var3) {
      if (this.o(var1, var3) && (this.C == null || !this.C.q())) {
         if (this.C == null) {
            this.C = new pe(this.H.centerX(), this.H.floorY() - 1.15 * this.H.scale(), this.v);
         }

         this.C.d(var1, var3, this.H.scale());
         return true;
      } else {
         return false;
      }
   }

   public void n(double var1, double var3) {
      if (this.C != null) {
         this.C.i(var1, var3);
      }
   }

   public boolean C() {
      return this.C != null && this.C.q();
   }

   public boolean R() {
      return this.C != null && this.C.m();
   }

   public double[][] W() {
      return this.C != null && this.H != null ? this.C.n(this.H.scale()) : null;
   }

   public void R(boolean var1) {
      if (var1) {
         this.V.d = (boolean)1;
      }

      this.C = null;
      this.g();
   }

   public void U() {
      this.V.d = this.S = (boolean)0;
      this.f = 0.0;
      this.C = null;
      this.g();
   }

   public void g() {
      this.y = -1L;
      this.l = (boolean)0;
   }

   public static final class b {
      private boolean d;

      public o5 g() {
         return new o5(this);
      }
   }

   public record j(float bodyYaw, float headYaw, float headPitch, float walkPosition, float walkSpeed, float ageInTicks) {
   }

   public record m(double x, double y, float roll, double lift, o5.n limbs, float dizzy, float impact, double vx, double vy, float spin) {
   }

   public record n(
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

   public enum o {
      f,
      m,
      a,
      H,
      A,
      x,
      j,
      O,
      h,
      T,
      l;
   }

   public record p(int left, int top, int side, float shiftX, float shiftY) {
      public static o5.p of(o5.m var0, float var1, double var2) {
         int var4 = (int)Math.ceil(2.0 * var2 * var1) + 2;
         int var5 = (int)Math.floor(var0.x() - var4 / 2.0);
         int var6 = (int)Math.floor(var0.y() - var4 / 2.0);
         float var7 = -1.251F;
         return new o5.p(var5, var6, var4, (float)((var0.x() - var5 - var4 / 2.0) / var1), (float)((var0.y() - var6 - var4) / var1) - var7);
      }
   }

   private record q(double distance, double speed) {
   }

   public record t(_c.f model, _c.f name, float scale, int floorY, boolean visible) {
      public float centerX() {
         return this.model.x() + this.model.width() / 2.0F;
      }

      public int renderWidth() {
         return Math.max(this.model.width(), (int)Math.ceil(this.scale * 2.3));
      }
   }

   public record z(o5.o stage, double xOffset, double nameOpacity, o5.j pose, o5.m toy) {
      public z(o5.o var1, double var2, double var4, o5.j var6) {
         this(var1, var2, var4, var6, null);
      }
   }
}
