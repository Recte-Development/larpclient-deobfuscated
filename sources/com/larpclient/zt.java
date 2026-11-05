package com.larpclient;

public final class zt {
   public static final double g = 0.12;
   public static final double C = 0.48;
   private static final double P = 3.8;
   private static final double J = 0.14;
   private static final double t = 0.22;
   private final zt.c r;
   private long y = -1L;
   private boolean q;
   private boolean c;
   private boolean e;
   private double p;
   private double M;
   private double A;
   private double N;
   private double l;
   private ud X;
   private zt.q K;
   private zt.e s = zt.e.g;
   private float L = -16.0F;
   static final double F = 2.101;
   static final double G = 0.15963829787234074;

   public zt() {
      this(new zt.c());
   }

   private zt(zt.c var1) {
      this.r = var1;
   }

   public static zt.q a(o4 var0) {
      return B(var0, 0.0);
   }

   public static zt.q B(o4 var0, double var1) {
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
         return new zt.q(new p5.w(var9, var11, var7, var8), new p5.w(var9, var11 - 24, var7, 18), var12, var16, true);
      } else {
         return new zt.q(new p5.w(var3, 0, 0, 0), new p5.w(var3, 0, 0, 0), 0.0F, 0, false);
      }
   }

   public static int V(zt.q var0, double var1) {
      p5.w var3 = var0.model();
      return Math.clamp(var3.y() + var3.height() - (int)Math.ceil((2.101 + var1) * var0.scale()), 0, var3.y());
   }

   public static double G(zt.q var0, int var1) {
      return var1 + var0.scale() + 8.0F - var0.centerX();
   }

   public static double a(double var0) {
      return var0 / 3.8 + 0.18;
   }

   public zt.l U(long var1, boolean var3, boolean var4, boolean var5, boolean var6, zt.q var7, int var8, int var9, int var10) {
      zt.l var11 = this.v(var1, var3, var4, var5, var6, var7, var8, var9, var10);
      this.s = var11.stage();
      this.K = var7;
      this.e = var4;
      this.L = var11.pose().bodyYaw();
      return var11;
   }

   private zt.l v(long var1, boolean var3, boolean var4, boolean var5, boolean var6, zt.q var7, int var8, int var9, int var10) {
      double var11 = this.y >= 0L && var1 >= this.y && var1 - this.y <= 500000000L ? (var1 - this.y) / 1.0E9 : 0.0;
      this.y = var1;
      if (!var3 || !this.q || !var6) {
         var11 = 0.0;
      }

      this.q = (boolean)(var3 && var6 ? 1 : 0);
      if (!var6 || !var7.visible()) {
         this.X = null;
      }

      if (var6 && var7.visible()) {
         if (!var4) {
            this.X = null;
         }

         if (!var4 || !var5) {
            this.r.l = (boolean)1;
         }

         if (!this.c && var3) {
            this.c = (boolean)1;
            this.A = G(var7, var8) / var7.scale();
            var11 = 0.0;
         }

         if (var4) {
            this.M += var11;
            int var13 = var9 >= 0 && var10 >= 0 ? 1 : 0;
            double var14 = var13 != 0 ? Math.clamp((var9 - var7.centerX()) / Math.max(80.0, var7.model().height() * 0.8), -1.0, 1.0) : 0.0;
            double var16 = var13 != 0
               ? Math.clamp((var10 - var7.model().y() - var7.model().height() * 0.15) / Math.max(60.0, var7.model().height() * 0.7), -1.0, 1.0)
               : 0.0;
            double var18 = -Math.expm1(-var11 * 9.0);
            this.N = this.N + (var14 - this.N) * var18;
            this.l = this.l + (var16 - this.l) * var18;
         } else {
            this.M = this.N = this.l = 0.0;
         }

         float var30 = (float)(-16.0 + this.N * 4.0);
         float var31 = var4 ? (float)(-this.N * 0.3) : 0.0F;
         float var15 = (float)(this.l * 0.2);
         if (this.r.l && this.X != null) {
            zt.l var32 = this.X.V(var11, var7.scale(), var7.floorY(), var8, var7.centerX(), var30, var31, var15, (float)(this.M * 20.0));
            if (var32 != null) {
               return var32;
            }

            this.X = null;
         }

         if (this.r.l) {
            return new zt.l(zt.e.N, 0.0, 1.0, new zt.n(var30, var31, var15, 0.0F, 0.0F, (float)(this.M * 20.0)));
         } else if (!this.c) {
            return new zt.l(zt.e.g, 0.0, 0.0, new zt.n(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
         } else {
            double var33 = 0.12 + a(this.A) + 0.48;
            this.p = Math.min(this.p + var11, var33);
            double var34 = this.p - 0.12;
            if (var34 < 0.0) {
               return new zt.l(zt.e.g, 0.0, 0.0, new zt.n(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float)(this.M * 20.0)));
            } else {
               zt.z var20 = q(var34, this.A);
               float var21 = (float)(var20.distance * 4.0);
               if (var34 < a(this.A)) {
                  return new zt.l(
                     zt.e.V,
                     G(var7, var8) * (1.0 - var20.distance / this.A),
                     0.0,
                     new zt.n(-90.0F, 0.0F, 0.0F, var21, (float)(var20.speed / 5.0), (float)(this.M * 20.0))
                  );
               } else if (this.p >= var33) {
                  this.r.l = (boolean)1;
                  return new zt.l(zt.e.N, 0.0, 1.0, new zt.n(var30, var31, var15, 0.0F, 0.0F, (float)(this.M * 20.0)));
               } else {
                  double var22 = var34 - a(this.A);
                  double var24 = k(var22 / 0.48);
                  double var26 = k(var22 / 0.33599999999999997);
                  float var28 = (float)(-90.0 + (var30 + 90.0F) * var24);
                  float var29 = (float)(Math.clamp(Math.toRadians(var28 - var30) + var31, -0.75, 0.75) * var26);
                  return new zt.l(zt.e.K, 0.0, 0.0, new zt.n(var28, var29, (float)(var15 * var26), var21, 0.0F, (float)(this.M * 20.0)));
               }
            }
         }
      } else {
         return new zt.l(zt.e.g, 0.0, 0.0, new zt.n(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
      }
   }

   private static zt.z q(double var0, double var2) {
      double var4 = Math.max(0.0, var2 / 3.8 - 0.18);
      if (var0 >= a(var2)) {
         return new zt.z(var2, 0.0);
      }

      if (var0 < 0.14) {
         double var8 = Math.max(0.0, var0);
         return new zt.z(3.8 * (var8 / 2.0 - 0.02228169203286535 * Math.sin(Math.PI * var8 / 0.14)), 3.8 * (0.5 - 0.5 * Math.cos(Math.PI * var8 / 0.14)));
      }

      if (var0 < 0.14 + var4) {
         return new zt.z(3.8 * (0.07 + var0 - 0.14), 3.8);
      }

      double var6 = var0 - 0.14 - var4;
      return new zt.z(
         3.8 * (0.07 + var4 + var6 / 2.0 + 0.03501408748021698 * Math.sin(Math.PI * var6 / 0.22)), 3.8 * (0.5 + 0.5 * Math.cos(Math.PI * var6 / 0.22))
      );
   }

   private static double k(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   public boolean H(double var1, double var3) {
      if (this.K == null || !this.K.visible() || !this.e || !this.r.l) {
         return false;
      } else if (this.X == null) {
         return this.s != zt.e.N ? false : ud.f((var1 - this.K.centerX()) / this.K.scale(), (var3 - this.K.floorY()) / this.K.scale() + 1.15, false);
      } else {
         return this.X.M() || this.X.n(var1, var3, this.K.scale());
      }
   }

   public boolean w(double var1, double var3) {
      if (this.H(var1, var3) && (this.X == null || !this.X.M())) {
         if (this.X == null) {
            this.X = new ud(this.K.centerX(), this.K.floorY() - 1.15 * this.K.scale(), this.L);
         }

         this.X.v(var1, var3, this.K.scale());
         return true;
      } else {
         return false;
      }
   }

   public void F(double var1, double var3) {
      if (this.X != null) {
         this.X.r(var1, var3);
      }
   }

   public boolean h() {
      return this.X != null && this.X.M();
   }

   public boolean Y() {
      return this.X != null && this.X.Q();
   }

   public double[][] Q() {
      return this.X != null && this.K != null ? this.X.F(this.K.scale()) : null;
   }

   public void O(boolean var1) {
      if (var1) {
         this.r.l = (boolean)1;
      }

      this.X = null;
      this.o();
   }

   public void n() {
      this.r.l = this.c = (boolean)0;
      this.p = 0.0;
      this.X = null;
      this.o();
   }

   public void o() {
      this.y = -1L;
      this.q = (boolean)0;
   }

   public static final class c {
      private boolean l;

      public zt B() {
         return new zt(this);
      }
   }

   public enum e {
      g,
      V,
      K,
      N,
      y,
      j,
      a,
      s,
      r,
      u,
      p;
   }

   public record i(int left, int top, int side, float shiftX, float shiftY) {
      public static zt.i of(zt.t var0, float var1, double var2) {
         int var4 = (int)Math.ceil(2.0 * var2 * var1) + 2;
         int var5 = (int)Math.floor(var0.x() - var4 / 2.0);
         int var6 = (int)Math.floor(var0.y() - var4 / 2.0);
         float var7 = -1.251F;
         return new zt.i(var5, var6, var4, (float)((var0.x() - var5 - var4 / 2.0) / var1), (float)((var0.y() - var6 - var4) / var1) - var7);
      }
   }

   public record l(zt.e stage, double xOffset, double nameOpacity, zt.n pose, zt.t toy) {
      public l(zt.e var1, double var2, double var4, zt.n var6) {
         this(var1, var2, var4, var6, null);
      }
   }

   public record n(float bodyYaw, float headYaw, float headPitch, float walkPosition, float walkSpeed, float ageInTicks) {
   }

   public record p(
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

   public record q(p5.w model, p5.w name, float scale, int floorY, boolean visible) {
      public float centerX() {
         return this.model.x() + this.model.width() / 2.0F;
      }

      public int renderWidth() {
         return Math.max(this.model.width(), (int)Math.ceil(this.scale * 2.3));
      }
   }

   public record t(double x, double y, float roll, double lift, zt.p limbs, float dizzy, float impact, double vx, double vy, float spin) {
   }

   private record z(double distance, double speed) {
   }
}
