package com.larpclient;

import com.larp.obf.q5682c6a8aca9b6ec.qab72417509509ebd;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public final class rk {
   public static final ScreenRectangle U = new ScreenRectangle(0, 0, 1, 1);
   private static final double Q = -10000.0;
   private static rk.f F;
   private static Object I;
   private static rk.f k;
   private static boolean C;
   private static int M;
   private static int l = -1;
   private static final double[] S = new double[4];
   private static final double[] m = new double[4];
   private static boolean A;
   private static boolean g;
   private static Object e;
   private static double z = 1.0;
   private static double f;
   private static double B;
   private static final rk.h X = new rk.h();

   private rk() {
   }

   private static int C(double var0, int var2) {
      return (int)Math.clamp(var0, 0.0, Math.max(1, var2));
   }

   public static void t(Object var0, rk.f var1) {
      if (A && I != null) {
         System.arraycopy(S, 0, m, 0, 4);
         e = I;
         g = (boolean)1;
      }

      A = (boolean)0;
      if (var0 != e) {
         g = (boolean)0;
      }

      I = var1 == null ? null : var0;
      F = var1;
   }

   public static void H(GuiGraphicsExtractor var0, rk.f var1, boolean var2, Runnable var3) {
      rk.l var4 = (rk.l)((qab72417509509ebd)var0).larpGuiRenderState();
      var4.larpWarpBegin(var1, var2);
      M++;

      try {
         var3.run();
      } finally {
         M--;
         var4.larpWarpEnd();
      }
   }

   public static boolean X() {
      return M > 0;
   }

   public static void R(double var0, double var2, double var4) {
      z = Math.max(0.01, var0);
      f = var2;
      B = var4;
   }

   public static double[] x(Object var0) {
      return g && var0 == e ? (double[])m.clone() : null;
   }

   public static rk.f t(Object var0) {
      return var0 != null && var0 == I ? F : null;
   }

   public static void S(int var0, rk.f var1, boolean var2) {
      l = var1 == null ? -1 : var0;
      k = var1;
      C = var2;
   }

   public static rk.f w() {
      return l >= 0 ? k : null;
   }

   private static void y(float var0, float var1) {
      if (C && l >= 0 && l <= 1) {
         double var2 = f + (var0 - f) / z;
         double var4 = B + (var1 - B) / z;
         if (!A) {
            S[0] = S[2] = var2;
            S[1] = S[3] = var4;
            A = (boolean)1;
         } else {
            S[0] = Math.min(S[0], var2);
            S[1] = Math.min(S[1], var4);
            S[2] = Math.max(S[2], var2);
            S[3] = Math.max(S[3], var4);
         }
      }
   }

   public static rk.h r(VertexConsumer var0, rk.f var1, boolean var2) {
      return E(var0, var1, var2, null, rk.u.G);
   }

   public static rk.h E(VertexConsumer var0, rk.f var1, boolean var2, ScreenRectangle var3, rk.u var4) {
      return X.k(var0, var1, var2, var1.clipsFlat() && var4 != rk.u.b ? var3 : null, var4);
   }

   public interface f {
      double[] forward(double var1, double var3);

      double[] inverse(double var1, double var3);

      double[] bounds(double var1, double var3, double var5, double var7);

      default boolean front(double var1, double var3) {
         return true;
      }

      default boolean clipsFlat() {
         return false;
      }

      default ScreenRectangle p(ScreenRectangle var1, int var2, int var3) {
         if (var1 == null) {
            return null;
         }

         double[] var4 = this.bounds(var1.left(), var1.top(), var1.right(), var1.bottom());
         int var5 = rk.C(Math.floor(var4[0]), var2);
         int var6 = rk.C(Math.floor(var4[1]), var3);
         int var7 = rk.C(Math.ceil(var4[2]), var2);
         int var8 = rk.C(Math.ceil(var4[3]), var3);
         return var7 > var5 && var8 > var6 ? new ScreenRectangle(var5, var6, var7 - var5, var8 - var6) : rk.U;
      }
   }

   public static final class h implements VertexConsumer {
      private static final float H = 12.0F;
      private static final int F = 1;
      private static final int z = 2;
      private static final int u = 4;
      private static final int j = 8;
      private static final int S = 16;
      private static final int Z = 32;
      private VertexConsumer Y;
      private rk.f p;
      private ScreenRectangle T;
      private boolean s;
      private boolean l;
      private boolean o;
      private int M = -1;
      private int Q;
      private int P;
      private int J;
      private final float[] b = new float[4];
      private final float[] h = new float[4];
      private final float[] C = new float[4];
      private final float[] G = new float[4];
      private final float[] R = new float[4];
      private final int[] t = new int[4];
      private final int[] k = new int[4];
      private final int[] a = new int[4];
      private final int[] A = new int[4];
      private final int[] L = new int[4];
      private float y;
      private float D;
      private float d;
      private float W;

      private rk.h k(VertexConsumer var1, rk.f var2, boolean var3, ScreenRectangle var4, rk.u var5) {
         this.Y = var1;
         this.p = var2;
         this.s = var3;
         this.T = var4;
         this.l = (boolean)(var5 == rk.u.b ? 1 : 0);
         this.o = (boolean)(var5 == rk.u.w ? 1 : 0);
         this.M = -1;
         this.Q = 0;
         return this;
      }

      public void k() {
         if (this.M == 3) {
            this.N();
         } else {
            for (int var1 = 0; var1 <= this.M; var1++) {
               this.k(var1);
            }
         }

         this.M = -1;
         this.Q = 0;
         this.Y = null;
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         rk.y(var1, var2);
         if (!this.s) {
            double[] var4 = this.p.forward(var1, var2);
            this.Y.addVertex((float)var4[0], (float)var4[1], var3);
            return this;
         }

         if (this.M == 3) {
            this.N();
            this.M = -1;
            this.Q = 0;
         }

         this.M++;
         this.b[this.M] = var1;
         this.h[this.M] = var2;
         this.C[this.M] = var3;
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor((var4 & 0xFF) << 24 | (var1 & 0xFF) << 16 | (var2 & 0xFF) << 8 | var3 & 0xFF);
      }

      public VertexConsumer setColor(int var1) {
         if (!this.s) {
            this.Y.setColor(var1);
            return this;
         } else {
            this.t[this.M] = var1;
            this.Q |= 1;
            return this;
         }
      }

      public VertexConsumer setUv(float var1, float var2) {
         if (!this.s) {
            this.Y.setUv(var1, var2);
            return this;
         } else {
            this.G[this.M] = var1;
            this.R[this.M] = var2;
            this.Q |= 2;
            return this;
         }
      }

      public VertexConsumer setUv1(int var1, int var2) {
         if (!this.s) {
            this.Y.setUv1(var1, var2);
            return this;
         } else {
            this.k[this.M] = var1;
            this.a[this.M] = var2;
            this.Q |= 4;
            return this;
         }
      }

      public VertexConsumer setUv3(float var1, float var2) {
         if (!this.s) {
            this.Y.setUv3(var1, var2);
         }

         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         if (!this.s) {
            this.Y.setUv2(var1, var2);
            return this;
         } else {
            this.A[this.M] = var1;
            this.L[this.M] = var2;
            this.Q |= 8;
            return this;
         }
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         if (!this.s) {
            this.Y.setNormal(var1, var2, var3);
            return this;
         } else {
            this.y = var1;
            this.D = var2;
            this.d = var3;
            this.Q |= 16;
            return this;
         }
      }

      public VertexConsumer setLineWidth(float var1) {
         if (!this.s) {
            this.Y.setLineWidth(var1);
            return this;
         } else {
            this.W = var1;
            this.Q |= 32;
            return this;
         }
      }

      private void k(int var1) {
         double[] var2 = this.p.forward(this.b[var1], this.h[var1]);
         this.Y.addVertex((float)var2[0], (float)var2[1], this.C[var1]);
         if ((this.Q & 1) != 0) {
            this.Y.setColor(this.t[var1]);
         }

         if ((this.Q & 2) != 0) {
            this.Y.setUv(this.G[var1], this.R[var1]);
         }

         if ((this.Q & 4) != 0) {
            this.Y.setUv1(this.k[var1], this.a[var1]);
         }

         if ((this.Q & 8) != 0) {
            this.Y.setUv2(this.A[var1], this.L[var1]);
         }

         if ((this.Q & 16) != 0) {
            this.Y.setNormal(this.y, this.D, this.d);
         }

         if ((this.Q & 32) != 0) {
            this.Y.setLineWidth(this.W);
         }

         if (this.o) {
            this.Y.setUv1(this.P, this.J);
         }
      }

      private void N() {
         if (this.o) {
            this.P = Math.clamp(Math.round((Math.abs(this.G[0]) - this.C[0]) * 64.0F), 0, 32767);
            this.J = Math.clamp(Math.round((Math.abs(this.R[0]) - this.C[0]) * 64.0F), 0, 32767);
         }

         if (this.l) {
            for (int var14 = 0; var14 < 4; var14++) {
               if (!this.p.front(this.b[var14], this.h[var14])) {
                  return;
               }
            }

            for (int var15 = 0; var15 < 4; var15++) {
               this.k(var15);
            }
         } else {
            float var1 = 0.0F;
            float var2 = 1.0F;
            float var3 = 0.0F;
            float var4 = 1.0F;
            if (this.T != null) {
               float[] var5;
               float[] var6;
               if (this.b[0] == this.b[1] && this.b[3] == this.b[2] && this.h[0] == this.h[3] && this.h[1] == this.h[2]) {
                  var5 = D(this.b[0], this.b[3], this.T.left(), this.T.right());
                  var6 = D(this.h[0], this.h[1], this.T.top(), this.T.bottom());
               } else if (this.h[0] == this.h[1] && this.h[3] == this.h[2] && this.b[0] == this.b[3] && this.b[1] == this.b[2]) {
                  var5 = D(this.h[0], this.h[3], this.T.top(), this.T.bottom());
                  var6 = D(this.b[0], this.b[1], this.T.left(), this.T.right());
               } else {
                  var5 = var6 = new float[]{0.0F, 1.0F};
               }

               if (var5 == null || var6 == null) {
                  return;
               }

               var1 = var5[0];
               var2 = var5[1];
               var3 = var6[0];
               var4 = var6[1];
            }

            int var16 = var1 == 0.0F && var2 == 1.0F && var3 == 0.0F && var4 == 1.0F ? 1 : 0;
            float var17 = 0.0F;

            for (int var7 = 0; var7 < 4; var7++) {
               int var8 = var7 + 1 & 3;
               var17 = Math.max(var17, (float)Math.hypot(this.b[var8] - this.b[var7], this.h[var8] - this.h[var7]));
            }

            var17 *= Math.max(var2 - var1, var4 - var3);
            int var19 = Math.clamp((int)Math.ceil(var17 / 12.0F), 1, 24);
            if (var19 == 1 && var16 != 0) {
               for (int var21 = 0; var21 < 4; var21++) {
                  if (!this.p.front(this.b[var21], this.h[var21])) {
                     return;
                  }
               }

               for (int var22 = 0; var22 < 4; var22++) {
                  this.k(var22);
               }
            } else {
               for (int var20 = 0; var20 < var19; var20++) {
                  float var9 = var1 + (var2 - var1) * var20 / var19;
                  float var10 = var1 + (var2 - var1) * (var20 + 1) / var19;

                  for (int var11 = 0; var11 < var19; var11++) {
                     float var12 = var3 + (var4 - var3) * var11 / var19;
                     float var13 = var3 + (var4 - var3) * (var11 + 1) / var19;
                     if (this.h(var9, var12) && this.h(var9, var13) && this.h(var10, var13) && this.h(var10, var12)) {
                        this.y(var9, var12);
                        this.y(var9, var13);
                        this.y(var10, var13);
                        this.y(var10, var12);
                     }
                  }
               }
            }
         }
      }

      private static float[] D(float var0, float var1, int var2, int var3) {
         if (var0 != var1) {
            float var4 = (var2 - var0) / (var1 - var0);
            float var5 = (var3 - var0) / (var1 - var0);
            float var6 = Math.max(0.0F, Math.min(var4, var5));
            float var7 = Math.min(1.0F, Math.max(var4, var5));
            return var7 > var6 ? new float[]{var6, var7} : null;
         } else {
            return var0 >= var2 && var0 <= var3 ? new float[]{0.0F, 1.0F} : null;
         }
      }

      private boolean h(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         return this.p.front(H(this.b, var3, var4, var5, var6), H(this.h, var3, var4, var5, var6));
      }

      private void y(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         double[] var7 = this.p.forward(H(this.b, var3, var4, var5, var6), H(this.h, var3, var4, var5, var6));
         this.Y.addVertex((float)var7[0], (float)var7[1], H(this.C, var3, var4, var5, var6));
         if ((this.Q & 1) != 0) {
            this.Y.setColor(this.R(var3, var4, var5, var6));
         }

         if ((this.Q & 2) != 0) {
            this.Y.setUv(H(this.G, var3, var4, var5, var6), H(this.R, var3, var4, var5, var6));
         }

         if ((this.Q & 4) != 0) {
            this.Y.setUv1(Math.round(H(this.k, var3, var4, var5, var6)), Math.round(H(this.a, var3, var4, var5, var6)));
         }

         if ((this.Q & 8) != 0) {
            this.Y.setUv2(Math.round(H(this.A, var3, var4, var5, var6)), Math.round(H(this.L, var3, var4, var5, var6)));
         }

         if ((this.Q & 16) != 0) {
            this.Y.setNormal(this.y, this.D, this.d);
         }

         if ((this.Q & 32) != 0) {
            this.Y.setLineWidth(this.W);
         }

         if (this.o) {
            this.Y.setUv1(this.P, this.J);
         }
      }

      private static float H(float[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private static float H(int[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private int R(float var1, float var2, float var3, float var4) {
         int var5 = 0;

         for (byte var6 = 0; var6 < 32; var6 += 8) {
            float var7 = (this.t[0] >>> var6 & 0xFF) * var1
               + (this.t[1] >>> var6 & 0xFF) * var2
               + (this.t[2] >>> var6 & 0xFF) * var3
               + (this.t[3] >>> var6 & 0xFF) * var4;
            var5 |= Math.clamp(Math.round(var7), 0, 255) << var6;
         }

         return var5;
      }
   }

   public interface l {
      void larpWarpBegin(rk.f var1, boolean var2);

      void larpWarpEnd();
   }

   public static final class n implements rk.f {
      public static final double F = 0.02;
      private final double S;
      private final double h;
      private final double i;
      private final double E;
      private final double g;
      private final double z;
      private final double k;
      private final double Q;
      private final double C;
      private final double s;
      private final double Y;
      private final double A;
      private final double L;
      private final double X;
      private final double P;
      private final double x;
      private final double y;
      private final double T;

      public n(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         this.S = var1;
         this.h = var3;
         this.i = var5;
         this.E = var7;
         this.g = var9;
         this.z = var11;
         this.k = var13;
         this.Q = var15;
         this.C = var17;
         this.s = var9 * var17 - var11 * var15;
         this.Y = var5 * var15 - var3 * var17;
         this.A = var3 * var11 - var5 * var9;
         this.L = var11 * var13 - var7 * var17;
         this.X = var1 * var17 - var5 * var13;
         this.P = var5 * var7 - var1 * var11;
         this.x = var7 * var15 - var9 * var13;
         this.y = var3 * var13 - var1 * var15;
         this.T = var1 * var9 - var3 * var7;
      }

      public double e(double var1, double var3) {
         return this.k * var1 + this.Q * var3 + this.C;
      }

      @Override
      public boolean front(double var1, double var3) {
         return this.e(var1, var3) >= 0.02;
      }

      @Override
      public boolean clipsFlat() {
         return true;
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = Math.max(0.02, this.e(var1, var3));
         return new double[]{(this.S * var1 + this.h * var3 + this.i) / var5, (this.E * var1 + this.g * var3 + this.z) / var5};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = this.s * var1 + this.Y * var3 + this.A;
         double var7 = this.L * var1 + this.X * var3 + this.P;
         double var9 = this.x * var1 + this.y * var3 + this.T;
         if (Math.abs(var9) < 1.0E-12) {
            return new double[]{-10000.0, -10000.0};
         }

         double var11 = var5 / var9;
         double var13 = var7 / var9;
         return Double.isFinite(var11) && Double.isFinite(var13) && this.front(var11, var13) ? new double[]{var11, var13} : new double[]{-10000.0, -10000.0};
      }

      @Override
      public double[] bounds(double var1, double var3, double var5, double var7) {
         double[][] var9 = new double[][]{{var1, var3}, {var1, var7}, {var5, var7}, {var5, var3}};
         double var10 = Double.POSITIVE_INFINITY;
         double var12 = var10;
         double var14 = Double.NEGATIVE_INFINITY;
         double var16 = var14;
         byte var18 = 0;

         for (int var19 = 0; var19 < 4; var19++) {
            double[] var20 = var9[var19];
            double[] var21 = var9[var19 + 1 & 3];
            double var22 = this.e(var20[0], var20[1]) - 0.02;
            double var24 = this.e(var21[0], var21[1]) - 0.02;
            if (var22 >= 0.0) {
               double[] var26 = this.forward(var20[0], var20[1]);
               var18 = 1;
               var10 = Math.min(var10, var26[0]);
               var12 = Math.min(var12, var26[1]);
               var14 = Math.max(var14, var26[0]);
               var16 = Math.max(var16, var26[1]);
            }

            if ((var22 >= 0.0 ? 1 : 0) != (var24 >= 0.0 ? 1 : 0)) {
               double var29 = var22 / (var22 - var24);
               double[] var28 = this.forward(var20[0] + (var21[0] - var20[0]) * var29, var20[1] + (var21[1] - var20[1]) * var29);
               var18 = 1;
               var10 = Math.min(var10, var28[0]);
               var12 = Math.min(var12, var28[1]);
               var14 = Math.max(var14, var28[0]);
               var16 = Math.max(var16, var28[1]);
            }
         }

         return var18 != 0 ? new double[]{var10, var12, var14, var16} : new double[]{-10000.0, -10000.0, -10000.0, -10000.0};
      }
   }

   public enum u {
      G,
      b,
      w;
   }

   public record x(double pivotX, double pivotY, double targetX, double targetY, double scale, double sin, double cos, double focal) implements rk.f {
      public static rk.x of(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
         return new rk.x(var0, var2, var4, var6, var8, Math.sin(var10), Math.cos(var10), var12);
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = (var1 - this.pivotX) * this.scale;
         double var7 = (var3 - this.pivotY) * this.scale;
         double var9 = this.focal / Math.max(this.focal * 0.2, this.focal + var5 * this.sin);
         return new double[]{this.targetX + var5 * this.cos * var9, this.targetY + var7 * var9};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = var1 - this.targetX;
         double var7 = var3 - this.targetY;
         double var9 = this.focal * this.cos - var5 * this.sin;
         if (var9 <= this.focal * 0.001) {
            return new double[]{-10000.0, -10000.0};
         }

         double var11 = var5 * this.focal / var9;
         double var13 = this.focal / Math.max(this.focal * 0.2, this.focal + var11 * this.sin);
         return new double[]{this.pivotX + var11 / this.scale, this.pivotY + var7 / var13 / this.scale};
      }

      @Override
      public double[] bounds(double var1, double var3, double var5, double var7) {
         double[] var9 = this.forward(var1, var3);
         double[] var10 = this.forward(var1, var7);
         double[] var11 = this.forward(var5, var7);
         double[] var12 = this.forward(var5, var3);
         return new double[]{
            Math.min(Math.min(var9[0], var10[0]), Math.min(var11[0], var12[0])),
            Math.min(Math.min(var9[1], var10[1]), Math.min(var11[1], var12[1])),
            Math.max(Math.max(var9[0], var10[0]), Math.max(var11[0], var12[0])),
            Math.max(Math.max(var9[1], var10[1]), Math.max(var11[1], var12[1]))
         };
      }
   }
}
