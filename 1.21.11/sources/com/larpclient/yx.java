package com.larpclient;

public final class yx {
   public static final double j = 0.12;
   public static final double d = 0.48;
   private static final double k = 3.8;
   private static final double I = 0.14;
   private static final double e = 0.22;
   private final yx.f v;
   private long E = -1L;
   private boolean Z;
   private boolean A;
   private boolean t;
   private double U;
   private double T;
   private double R;
   private double b;
   private double V;
   private qg M;
   private yx.x N;
   private yx.s G = yx.s.g;
   private float P = -16.0F;
   static final double w = 2.101;
   static final double f = 0.15963829787234074;

   public yx() {
      this(new yx.f());
   }

   private yx(yx.f var1) {
      this.v = var1;
   }

   public static yx.x O(hc var0) {
      return B(var0, 0.0);
   }

   public static yx.x B(hc var0, double var1) {
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
         return new yx.x(new ue.c(var9, var11, var7, var8), new ue.c(var9, var11 - 24, var7, 18), var12, var16, true);
      } else {
         return new yx.x(new ue.c(var3, 0, 0, 0), new ue.c(var3, 0, 0, 0), 0.0F, 0, false);
      }
   }

   public static int o(yx.x var0, double var1) {
      ue.c var3 = var0.model();
      return Math.clamp(var3.y() + var3.height() - (int)Math.ceil((2.101 + var1) * var0.scale()), 0, var3.y());
   }

   public static double u(yx.x var0, int var1) {
      return var1 + var0.scale() + 8.0F - var0.centerX();
   }

   public static double r(double var0) {
      return var0 / 3.8 + 0.18;
   }

   public yx.o K(long var1, boolean var3, boolean var4, boolean var5, boolean var6, yx.x var7, int var8, int var9, int var10) {
      yx.o var11 = this.f(var1, var3, var4, var5, var6, var7, var8, var9, var10);
      this.G = var11.stage();
      this.N = var7;
      this.t = var4;
      this.P = var11.pose().bodyYaw();
      return var11;
   }

   private yx.o f(long var1, boolean var3, boolean var4, boolean var5, boolean var6, yx.x var7, int var8, int var9, int var10) {
      double var11 = this.E >= 0L && var1 >= this.E && var1 - this.E <= 500000000L ? (var1 - this.E) / 1.0E9 : 0.0;
      this.E = var1;
      if (!var3 || !this.Z || !var6) {
         var11 = 0.0;
      }

      this.Z = (boolean)(var3 && var6 ? 1 : 0);
      if (!var6 || !var7.visible()) {
         this.M = null;
      }

      if (var6 && var7.visible()) {
         if (!var4) {
            this.M = null;
         }

         if (!var4 || !var5) {
            this.v.s = (boolean)1;
         }

         if (!this.A && var3) {
            this.A = (boolean)1;
            this.R = u(var7, var8) / var7.scale();
            var11 = 0.0;
         }

         if (var4) {
            this.T += var11;
            int var13 = var9 >= 0 && var10 >= 0 ? 1 : 0;
            double var14 = var13 != 0 ? Math.clamp((var9 - var7.centerX()) / Math.max(80.0, var7.model().height() * 0.8), -1.0, 1.0) : 0.0;
            double var16 = var13 != 0
               ? Math.clamp((var10 - var7.model().y() - var7.model().height() * 0.15) / Math.max(60.0, var7.model().height() * 0.7), -1.0, 1.0)
               : 0.0;
            double var18 = -Math.expm1(-var11 * 9.0);
            this.b = this.b + (var14 - this.b) * var18;
            this.V = this.V + (var16 - this.V) * var18;
         } else {
            this.T = this.b = this.V = 0.0;
         }

         float var30 = (float)(-16.0 + this.b * 4.0);
         float var31 = var4 ? (float)(-this.b * 0.3) : 0.0F;
         float var15 = (float)(this.V * 0.2);
         if (this.v.s && this.M != null) {
            yx.o var32 = this.M.V(var11, var7.scale(), var7.floorY(), var8, var7.centerX(), var30, var31, var15, (float)(this.T * 20.0));
            if (var32 != null) {
               return var32;
            }

            this.M = null;
         }

         if (this.v.s) {
            return new yx.o(yx.s.o, 0.0, 1.0, new yx.k(var30, var31, var15, 0.0F, 0.0F, (float)(this.T * 20.0)));
         } else if (!this.A) {
            return new yx.o(yx.s.g, 0.0, 0.0, new yx.k(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
         } else {
            double var33 = 0.12 + r(this.R) + 0.48;
            this.U = Math.min(this.U + var11, var33);
            double var34 = this.U - 0.12;
            if (var34 < 0.0) {
               return new yx.o(yx.s.g, 0.0, 0.0, new yx.k(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, (float)(this.T * 20.0)));
            } else {
               yx.v var20 = z(var34, this.R);
               float var21 = (float)(var20.distance * 4.0);
               if (var34 < r(this.R)) {
                  return new yx.o(
                     yx.s.j,
                     u(var7, var8) * (1.0 - var20.distance / this.R),
                     0.0,
                     new yx.k(-90.0F, 0.0F, 0.0F, var21, (float)(var20.speed / 5.0), (float)(this.T * 20.0))
                  );
               } else if (this.U >= var33) {
                  this.v.s = (boolean)1;
                  return new yx.o(yx.s.o, 0.0, 1.0, new yx.k(var30, var31, var15, 0.0F, 0.0F, (float)(this.T * 20.0)));
               } else {
                  double var22 = var34 - r(this.R);
                  double var24 = R(var22 / 0.48);
                  double var26 = R(var22 / 0.33599999999999997);
                  float var28 = (float)(-90.0 + (var30 + 90.0F) * var24);
                  float var29 = (float)(Math.clamp(Math.toRadians(var28 - var30) + var31, -0.75, 0.75) * var26);
                  return new yx.o(yx.s.w, 0.0, 0.0, new yx.k(var28, var29, (float)(var15 * var26), var21, 0.0F, (float)(this.T * 20.0)));
               }
            }
         }
      } else {
         return new yx.o(yx.s.g, 0.0, 0.0, new yx.k(-90.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
      }
   }

   private static yx.v z(double var0, double var2) {
      double var4 = Math.max(0.0, var2 / 3.8 - 0.18);
      if (var0 >= r(var2)) {
         return new yx.v(var2, 0.0);
      }

      if (var0 < 0.14) {
         double var8 = Math.max(0.0, var0);
         return new yx.v(3.8 * (var8 / 2.0 - 0.02228169203286535 * Math.sin(Math.PI * var8 / 0.14)), 3.8 * (0.5 - 0.5 * Math.cos(Math.PI * var8 / 0.14)));
      }

      if (var0 < 0.14 + var4) {
         return new yx.v(3.8 * (0.07 + var0 - 0.14), 3.8);
      }

      double var6 = var0 - 0.14 - var4;
      return new yx.v(
         3.8 * (0.07 + var4 + var6 / 2.0 + 0.03501408748021698 * Math.sin(Math.PI * var6 / 0.22)), 3.8 * (0.5 + 0.5 * Math.cos(Math.PI * var6 / 0.22))
      );
   }

   private static double R(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   public boolean I(double var1, double var3) {
      if (this.N == null || !this.N.visible() || !this.t || !this.v.s) {
         return false;
      } else if (this.M == null) {
         return this.G != yx.s.o ? false : qg.G((var1 - this.N.centerX()) / this.N.scale(), (var3 - this.N.floorY()) / this.N.scale() + 1.15, false);
      } else {
         return this.M.D() || this.M.s(var1, var3, this.N.scale());
      }
   }

   public boolean r(double var1, double var3) {
      if (this.I(var1, var3) && (this.M == null || !this.M.D())) {
         if (this.M == null) {
            this.M = new qg(this.N.centerX(), this.N.floorY() - 1.15 * this.N.scale(), this.P);
         }

         this.M.z(var1, var3, this.N.scale());
         return true;
      } else {
         return false;
      }
   }

   public void f(double var1, double var3) {
      if (this.M != null) {
         this.M.W(var1, var3);
      }
   }

   public boolean c() {
      return this.M != null && this.M.D();
   }

   public boolean k() {
      return this.M != null && this.M.p();
   }

   public double[][] p() {
      return this.M != null && this.N != null ? this.M.X(this.N.scale()) : null;
   }

   public void e(boolean var1) {
      if (var1) {
         this.v.s = (boolean)1;
      }

      this.M = null;
      this.w();
   }

   public void U() {
      this.v.s = this.A = (boolean)0;
      this.U = 0.0;
      this.M = null;
      this.w();
   }

   public void w() {
      this.E = -1L;
      this.Z = (boolean)0;
   }

   public record e(int left, int top, int side, float shiftX, float shiftY) {
      public static yx.e of(yx.g var0, float var1, double var2) {
         int var4 = (int)Math.ceil(2.0 * var2 * var1) + 2;
         int var5 = (int)Math.floor(var0.x() - var4 / 2.0);
         int var6 = (int)Math.floor(var0.y() - var4 / 2.0);
         float var7 = -1.251F;
         return new yx.e(var5, var6, var4, (float)((var0.x() - var5 - var4 / 2.0) / var1), (float)((var0.y() - var6 - var4) / var1) - var7);
      }
   }

   public static final class f {
      private boolean s;

      public yx F() {
         return new yx(this);
      }
   }

   public record g(double x, double y, float roll, double lift, yx.h limbs, float dizzy, float impact, double vx, double vy, float spin) {
   }

   public record h(
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

   public record k(float bodyYaw, float headYaw, float headPitch, float walkPosition, float walkSpeed, float ageInTicks) {
   }

   public record o(yx.s stage, double xOffset, double nameOpacity, yx.k pose, yx.g toy) {
      public o(yx.s var1, double var2, double var4, yx.k var6) {
         this(var1, var2, var4, var6, null);
      }
   }

   public enum s {
      g,
      j,
      w,
      o,
      X,
      e,
      E,
      T,
      N,
      h,
      b;
   }

   private record v(double distance, double speed) {
   }

   public record x(ue.c model, ue.c name, float scale, int floorY, boolean visible) {
      public float centerX() {
         return this.model.x() + this.model.width() / 2.0F;
      }

      public int renderWidth() {
         return Math.max(this.model.width(), (int)Math.ceil(this.scale * 2.3));
      }
   }
}
