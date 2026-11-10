package com.larpclient;

public final class tx {
   public static final double G = 0.12;
   public static final double m = 0.48;
   private static final double i = 3.8;
   private static final double u = 0.14;
   private static final double C = 0.22;
   private final tx.q y;
   private long a = -1L;
   private boolean J;
   private boolean W;
   private boolean p;
   private double h;
   private double E;
   private double t;
   private double Z;
   private double U;
   private ps z;
   private tx.m V;
   private tx.k n = tx.k.d;
   private float o = -16.0F;
   static final double R = 2.101;
   static final double A = 0.15963829787234074;

   public tx() {
      this(new tx.q());
   }

   private tx(tx.q var1) {
      this.y = var1;
   }

   public static tx.m a(uz var0) {
      return J(var0, 0.0);
   }

   public static tx.m J(uz var0, double var1) {
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
         return new tx.m(new p1.e(var9, var11, var7, var8), new p1.e(var9, var11 - 24, var7, 18), var12, var16, true);
      } else {
         return new tx.m(new p1.e(var3, 0, 0, 0), new p1.e(var3, 0, 0, 0), 0.0F, 0, false);
      }
   }

   public static int i(tx.m var0, double var1) {
      p1.e var3 = var0.model();
      return Math.clamp(var3.y() + var3.height() - (int)Math.ceil((2.101 + var1) * var0.scale()), 0, var3.y());
   }

   public static double X(tx.m var0, int var1) {
      return var1 + var0.scale() + 8.0F - var0.centerX();
   }

   public static double O(double var0) {
      return var0 / 3.8 + 0.18;
   }

   public tx.a d(long var1, boolean var3, boolean var4, boolean var5, boolean var6, tx.m var7, int var8, int var9, int var10) {
      tx.a var11 = this.A(var1, var3, var4, var5, var6, var7, var8, var9, var10);
      this.n = var11.stage();
      this.V = var7;
      this.p = var4;
      this.o = var11.pose().bodyYaw();
      return var11;
   }

   private tx.a A(long var1, boolean var3, boolean var4, boolean var5, boolean var6, tx.m var7, int var8, int var9, int var10) {
      double var11 = this.a >= 0L && var1 >= this.a && var1 - this.a <= 500000000L ? (var1 - this.a) / 1.0E9 : 0.0;
      this.a = var1;
      if (!var3 || !this.J || !var6) {
         var11 = 0.0;
      }

      this.J = (boolean)(var3 && var6 ? 1 : 0);
      if (!var6 || !var7.visible()) {
         this.z = null;
      }

      if (var6 && var7.visible()) {
         if (!var4) {
            this.z = null;
         }

         if (!var4 || !var5) {
            this.y.H = (boolean)1;
         }

         if (!this.W && var3) {
            this.W = (boolean)1;
            this.t = X(var7, var8) / var7.scale();
            var11 = 0.0;
         }

         if (var4) {
            this.E += var11;
            int var13 = var9 >= 0 && var10 >= 0 ? 1 : 0;
            double var14 = var13 != 0 ? Math.clamp((var9 - var7.centerX()) / Math.max(80.0, var7.model().height() * 0.8), -1.0, 1.0) : 0.0;
            double var16 = var13 != 0
               ? Math.clamp((var10 - var7.model().y() - var7.model().height() * 0.15) / Math.max(60.0, var7.model().height() * 0.7), -1.0, 1.0)
               : 0.0;
            double var18 = -Math.expm1(-var11 * 9.0);
            this.Z = this.Z + (var14 - this.Z) * var18;
            this.U = this.U + (var16 - this.U) * var18;
         } else {
            this.E = this.Z = this.U = 0.0;
         }

         float var30 = (float)(-16.0 + this.Z * 4.0);
         float var31 = var4 ? (float)(-this.Z * 0.3) : 0.0F;
         float var15 = (float)(this.U * 0.2);
         if (this.y.H && this.z != null) {
            tx.a var32 = this.z.T(var11, var7.scale(), var7.floorY(), var8, var7.centerX(), var30, var31, var15, (float)(this.E * 20.0));
            if (var32 != null) {
               return var32;
            }

            this.z = null;
         }

         if (this.y.H) {
            return new tx.a(tx.k.O, 0.0, 1.0, new tx.u(var30, var31, var15, 0.0F, 0.0F, (float)(this.E * 20.0)));
         } else if (!this.W) {
            return new tx.a(tx.k.d, 0.0, 0.0, new tx.u(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
         } else {
            double var33 = 0.12 + O(this.t) + 0.48;
            this.h = Math.min(this.h + var11, var33);
            double var34 = this.h - 0.12;
            if (var34 < 0.0) {
               return new tx.a(tx.k.d, 0.0, 0.0, new tx.u(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float)(this.E * 20.0)));
            } else {
               tx.v var20 = y(var34, this.t);
               float var21 = (float)(var20.distance * 4.0);
               if (var34 < O(this.t)) {
                  return new tx.a(
                     tx.k.m,
                     X(var7, var8) * (1.0 - var20.distance / this.t),
                     0.0,
                     new tx.u(-90.0F, 0.0F, 0.0F, var21, (float)(var20.speed / 5.0), (float)(this.E * 20.0))
                  );
               } else if (this.h >= var33) {
                  this.y.H = (boolean)1;
                  return new tx.a(tx.k.O, 0.0, 1.0, new tx.u(var30, var31, var15, 0.0F, 0.0F, (float)(this.E * 20.0)));
               } else {
                  double var22 = var34 - O(this.t);
                  double var24 = H(var22 / 0.48);
                  double var26 = H(var22 / 0.33599999999999997);
                  float var28 = (float)(-90.0 + (var30 + 90.0F) * var24);
                  float var29 = (float)(Math.clamp(Math.toRadians(var28 - var30) + var31, -0.75, 0.75) * var26);
                  return new tx.a(tx.k.T, 0.0, 0.0, new tx.u(var28, var29, (float)(var15 * var26), var21, 0.0F, (float)(this.E * 20.0)));
               }
            }
         }
      } else {
         return new tx.a(tx.k.d, 0.0, 0.0, new tx.u(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
      }
   }

   private static tx.v y(double var0, double var2) {
      double var4 = Math.max(0.0, var2 / 3.8 - 0.18);
      if (var0 >= O(var2)) {
         return new tx.v(var2, 0.0);
      }

      if (var0 < 0.14) {
         double var8 = Math.max(0.0, var0);
         return new tx.v(3.8 * (var8 / 2.0 - 0.02228169203286535 * Math.sin(Math.PI * var8 / 0.14)), 3.8 * (0.5 - 0.5 * Math.cos(Math.PI * var8 / 0.14)));
      }

      if (var0 < 0.14 + var4) {
         return new tx.v(3.8 * (0.07 + var0 - 0.14), 3.8);
      }

      double var6 = var0 - 0.14 - var4;
      return new tx.v(
         3.8 * (0.07 + var4 + var6 / 2.0 + 0.03501408748021698 * Math.sin(Math.PI * var6 / 0.22)), 3.8 * (0.5 + 0.5 * Math.cos(Math.PI * var6 / 0.22))
      );
   }

   private static double H(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   public boolean C(double var1, double var3) {
      if (this.V == null || !this.V.visible() || !this.p || !this.y.H) {
         return false;
      } else if (this.z == null) {
         return this.n != tx.k.O ? false : ps.n((var1 - this.V.centerX()) / this.V.scale(), (var3 - this.V.floorY()) / this.V.scale() + 1.15, false);
      } else {
         return this.z.B() || this.z.w(var1, var3, this.V.scale());
      }
   }

   public boolean p(double var1, double var3) {
      if (this.C(var1, var3) && (this.z == null || !this.z.B())) {
         if (this.z == null) {
            this.z = new ps(this.V.centerX(), this.V.floorY() - 1.15 * this.V.scale(), this.o);
         }

         this.z.C(var1, var3, this.V.scale());
         return true;
      } else {
         return false;
      }
   }

   public void n(double var1, double var3) {
      if (this.z != null) {
         this.z.k(var1, var3);
      }
   }

   public boolean O() {
      return this.z != null && this.z.B();
   }

   public boolean w() {
      return this.z != null && this.z.d();
   }

   public double[][] E() {
      return this.z != null && this.V != null ? this.z.F(this.V.scale()) : null;
   }

   public void A(boolean var1) {
      if (var1) {
         this.y.H = (boolean)1;
      }

      this.z = null;
      this.R();
   }

   public void Z() {
      this.y.H = this.W = (boolean)0;
      this.h = 0.0;
      this.z = null;
      this.R();
   }

   public void R() {
      this.a = -1L;
      this.J = (boolean)0;
   }

   public record a(tx.k stage, double xOffset, double nameOpacity, tx.u pose, tx.e toy) {
      public a(tx.k var1, double var2, double var4, tx.u var6) {
         this(var1, var2, var4, var6, null);
      }
   }

   public record e(double x, double y, float roll, double lift, tx.g limbs, float dizzy, float impact, double vx, double vy, float spin) {
   }

   public record g(
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

   public enum k {
      d,
      m,
      T,
      O,
      P,
      Y,
      K,
      Z,
      R,
      Q,
      F;
   }

   public record m(p1.e model, p1.e name, float scale, int floorY, boolean visible) {
      public float centerX() {
         return this.model.x() + this.model.width() / 2.0F;
      }

      public int renderWidth() {
         return Math.max(this.model.width(), (int)Math.ceil(this.scale * 2.3));
      }
   }

   public static final class q {
      private boolean H;

      public tx P() {
         return new tx(this);
      }
   }

   public record s(int left, int top, int side, float shiftX, float shiftY) {
      public static tx.s of(tx.e var0, float var1, double var2) {
         int var4 = (int)Math.ceil(2.0 * var2 * var1) + 2;
         int var5 = (int)Math.floor(var0.x() - var4 / 2.0);
         int var6 = (int)Math.floor(var0.y() - var4 / 2.0);
         float var7 = -1.251F;
         return new tx.s(var5, var6, var4, (float)((var0.x() - var5 - var4 / 2.0) / var1), (float)((var0.y() - var6 - var4) / var1) - var7);
      }
   }

   public record u(float bodyYaw, float headYaw, float headPitch, float walkPosition, float walkSpeed, float ageInTicks) {
   }

   private record v(double distance, double speed) {
   }
}
