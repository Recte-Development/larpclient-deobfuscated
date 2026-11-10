package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.q08272dc7904e0a01;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public final class dx {
   public static final ScreenRectangle j = new ScreenRectangle(0, 0, 1, 1);
   private static final double L = -10000.0;
   private static dx.x J;
   private static Object R;
   private static dx.x G;
   private static boolean d;
   private static int a;
   private static int O = -1;
   private static final double[] K = new double[4];
   private static final double[] x = new double[4];
   private static boolean B;
   private static boolean E;
   private static Object t;
   private static double f = 1.0;
   private static double C;
   private static double h;
   private static final dx.v q = new dx.v();

   private dx() {
   }

   private static int y(double var0, int var2) {
      return (int)Math.clamp(var0, 0.0, Math.max(1, var2));
   }

   public static void u(Object var0, dx.x var1) {
      if (B && R != null) {
         System.arraycopy(K, 0, x, 0, 4);
         t = R;
         E = (boolean)1;
      }

      B = (boolean)0;
      if (var0 != t) {
         E = (boolean)0;
      }

      R = var1 == null ? null : var0;
      J = var1;
   }

   public static void X(GuiGraphicsExtractor var0, dx.x var1, boolean var2, Runnable var3) {
      dx.f var4 = (dx.f)((q08272dc7904e0a01)var0).larpGuiRenderState();
      var4.larpWarpBegin(var1, var2);
      a++;

      try {
         var3.run();
      } finally {
         a--;
         var4.larpWarpEnd();
      }
   }

   public static boolean X() {
      return a > 0;
   }

   public static void C(double var0, double var2, double var4) {
      f = Math.max(0.01, var0);
      C = var2;
      h = var4;
   }

   public static double[] C(Object var0) {
      return E && var0 == t ? (double[])x.clone() : null;
   }

   public static dx.x x(Object var0) {
      return var0 != null && var0 == R ? J : null;
   }

   public static void X(int var0, dx.x var1, boolean var2) {
      O = var1 == null ? -1 : var0;
      G = var1;
      d = var2;
   }

   public static dx.x X() {
      return O >= 0 ? G : null;
   }

   private static void I(float var0, float var1) {
      if (d && O >= 0 && O <= 1) {
         double var2 = C + (var0 - C) / f;
         double var4 = h + (var1 - h) / f;
         if (!B) {
            K[0] = K[2] = var2;
            K[1] = K[3] = var4;
            B = (boolean)1;
         } else {
            K[0] = Math.min(K[0], var2);
            K[1] = Math.min(K[1], var4);
            K[2] = Math.max(K[2], var2);
            K[3] = Math.max(K[3], var4);
         }
      }
   }

   public static dx.v o(VertexConsumer var0, dx.x var1, boolean var2) {
      return s(var0, var1, var2, null, dx.r.h);
   }

   public static dx.v s(VertexConsumer var0, dx.x var1, boolean var2, ScreenRectangle var3, dx.r var4) {
      return q.a(var0, var1, var2, var1.clipsFlat() && var4 != dx.r.n ? var3 : null, var4);
   }

   public interface f {
      void larpWarpBegin(dx.x var1, boolean var2);

      void larpWarpEnd();
   }

   public record n(double pivotX, double pivotY, double targetX, double targetY, double scale, double sin, double cos, double focal) implements dx.x {
      public static dx.n of(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
         return new dx.n(var0, var2, var4, var6, var8, Math.sin(var10), Math.cos(var10), var12);
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

   public enum r {
      h,
      n,
      B;
   }

   public static final class v implements VertexConsumer {
      private static final float t = 12.0F;
      private static final int N = 1;
      private static final int P = 2;
      private static final int q = 4;
      private static final int A = 8;
      private static final int S = 16;
      private static final int T = 32;
      private VertexConsumer j;
      private dx.x D;
      private ScreenRectangle I;
      private boolean f;
      private boolean i;
      private boolean J;
      private int v = -1;
      private int B;
      private int X;
      private int a;
      private final float[] L = new float[4];
      private final float[] g = new float[4];
      private final float[] s = new float[4];
      private final float[] H = new float[4];
      private final float[] r = new float[4];
      private final int[] m = new int[4];
      private final int[] y = new int[4];
      private final int[] C = new int[4];
      private final int[] l = new int[4];
      private final int[] Z = new int[4];
      private float p;
      private float R;
      private float e;
      private float K;

      private dx.v a(VertexConsumer var1, dx.x var2, boolean var3, ScreenRectangle var4, dx.r var5) {
         this.j = var1;
         this.D = var2;
         this.f = var3;
         this.I = var4;
         this.i = (boolean)(var5 == dx.r.n ? 1 : 0);
         this.J = (boolean)(var5 == dx.r.B ? 1 : 0);
         this.v = -1;
         this.B = 0;
         return this;
      }

      public void I() {
         if (this.v == 3) {
            this.L();
         } else {
            for (int var1 = 0; var1 <= this.v; var1++) {
               this.f(var1);
            }
         }

         this.v = -1;
         this.B = 0;
         this.j = null;
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         dx.I(var1, var2);
         if (!this.f) {
            double[] var4 = this.D.forward(var1, var2);
            this.j.addVertex((float)var4[0], (float)var4[1], var3);
            return this;
         }

         if (this.v == 3) {
            this.L();
            this.v = -1;
            this.B = 0;
         }

         this.v++;
         this.L[this.v] = var1;
         this.g[this.v] = var2;
         this.s[this.v] = var3;
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor((var4 & 0xFF) << 24 | (var1 & 0xFF) << 16 | (var2 & 0xFF) << 8 | var3 & 0xFF);
      }

      public VertexConsumer setColor(int var1) {
         if (!this.f) {
            this.j.setColor(var1);
            return this;
         } else {
            this.m[this.v] = var1;
            this.B |= 1;
            return this;
         }
      }

      public VertexConsumer setUv(float var1, float var2) {
         if (!this.f) {
            this.j.setUv(var1, var2);
            return this;
         } else {
            this.H[this.v] = var1;
            this.r[this.v] = var2;
            this.B |= 2;
            return this;
         }
      }

      public VertexConsumer setUv1(int var1, int var2) {
         if (!this.f) {
            this.j.setUv1(var1, var2);
            return this;
         } else {
            this.y[this.v] = var1;
            this.C[this.v] = var2;
            this.B |= 4;
            return this;
         }
      }

      public VertexConsumer setUv3(float var1, float var2) {
         if (!this.f) {
            this.j.setUv3(var1, var2);
         }

         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         if (!this.f) {
            this.j.setUv2(var1, var2);
            return this;
         } else {
            this.l[this.v] = var1;
            this.Z[this.v] = var2;
            this.B |= 8;
            return this;
         }
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         if (!this.f) {
            this.j.setNormal(var1, var2, var3);
            return this;
         } else {
            this.p = var1;
            this.R = var2;
            this.e = var3;
            this.B |= 16;
            return this;
         }
      }

      public VertexConsumer setLineWidth(float var1) {
         if (!this.f) {
            this.j.setLineWidth(var1);
            return this;
         } else {
            this.K = var1;
            this.B |= 32;
            return this;
         }
      }

      private void f(int var1) {
         double[] var2 = this.D.forward(this.L[var1], this.g[var1]);
         this.j.addVertex((float)var2[0], (float)var2[1], this.s[var1]);
         if ((this.B & 1) != 0) {
            this.j.setColor(this.m[var1]);
         }

         if ((this.B & 2) != 0) {
            this.j.setUv(this.H[var1], this.r[var1]);
         }

         if ((this.B & 4) != 0) {
            this.j.setUv1(this.y[var1], this.C[var1]);
         }

         if ((this.B & 8) != 0) {
            this.j.setUv2(this.l[var1], this.Z[var1]);
         }

         if ((this.B & 16) != 0) {
            this.j.setNormal(this.p, this.R, this.e);
         }

         if ((this.B & 32) != 0) {
            this.j.setLineWidth(this.K);
         }

         if (this.J) {
            this.j.setUv1(this.X, this.a);
         }
      }

      private void L() {
         if (this.J) {
            this.X = Math.clamp(Math.round((Math.abs(this.H[0]) - this.s[0]) * 64.0F), 0, 32767);
            this.a = Math.clamp(Math.round((Math.abs(this.r[0]) - this.s[0]) * 64.0F), 0, 32767);
         }

         if (this.i) {
            for (int var14 = 0; var14 < 4; var14++) {
               if (!this.D.front(this.L[var14], this.g[var14])) {
                  return;
               }
            }

            for (int var15 = 0; var15 < 4; var15++) {
               this.f(var15);
            }
         } else {
            float var1 = 0.0F;
            float var2 = 1.0F;
            float var3 = 0.0F;
            float var4 = 1.0F;
            if (this.I != null) {
               float[] var5;
               float[] var6;
               if (this.L[0] == this.L[1] && this.L[3] == this.L[2] && this.g[0] == this.g[3] && this.g[1] == this.g[2]) {
                  var5 = t(this.L[0], this.L[3], this.I.left(), this.I.right());
                  var6 = t(this.g[0], this.g[1], this.I.top(), this.I.bottom());
               } else if (this.g[0] == this.g[1] && this.g[3] == this.g[2] && this.L[0] == this.L[3] && this.L[1] == this.L[2]) {
                  var5 = t(this.g[0], this.g[3], this.I.top(), this.I.bottom());
                  var6 = t(this.L[0], this.L[1], this.I.left(), this.I.right());
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
               var17 = Math.max(var17, (float)Math.hypot(this.L[var8] - this.L[var7], this.g[var8] - this.g[var7]));
            }

            var17 *= Math.max(var2 - var1, var4 - var3);
            int var19 = Math.clamp((int)Math.ceil(var17 / 12.0F), 1, 24);
            if (var19 == 1 && var16 != 0) {
               for (int var21 = 0; var21 < 4; var21++) {
                  if (!this.D.front(this.L[var21], this.g[var21])) {
                     return;
                  }
               }

               for (int var22 = 0; var22 < 4; var22++) {
                  this.f(var22);
               }
            } else {
               for (int var20 = 0; var20 < var19; var20++) {
                  float var9 = var1 + (var2 - var1) * var20 / var19;
                  float var10 = var1 + (var2 - var1) * (var20 + 1) / var19;

                  for (int var11 = 0; var11 < var19; var11++) {
                     float var12 = var3 + (var4 - var3) * var11 / var19;
                     float var13 = var3 + (var4 - var3) * (var11 + 1) / var19;
                     if (this.l(var9, var12) && this.l(var9, var13) && this.l(var10, var13) && this.l(var10, var12)) {
                        this.n(var9, var12);
                        this.n(var9, var13);
                        this.n(var10, var13);
                        this.n(var10, var12);
                     }
                  }
               }
            }
         }
      }

      private static float[] t(float var0, float var1, int var2, int var3) {
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

      private boolean l(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         return this.D.front(I(this.L, var3, var4, var5, var6), I(this.g, var3, var4, var5, var6));
      }

      private void n(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         double[] var7 = this.D.forward(I(this.L, var3, var4, var5, var6), I(this.g, var3, var4, var5, var6));
         this.j.addVertex((float)var7[0], (float)var7[1], I(this.s, var3, var4, var5, var6));
         if ((this.B & 1) != 0) {
            this.j.setColor(this.d(var3, var4, var5, var6));
         }

         if ((this.B & 2) != 0) {
            this.j.setUv(I(this.H, var3, var4, var5, var6), I(this.r, var3, var4, var5, var6));
         }

         if ((this.B & 4) != 0) {
            this.j.setUv1(Math.round(r(this.y, var3, var4, var5, var6)), Math.round(r(this.C, var3, var4, var5, var6)));
         }

         if ((this.B & 8) != 0) {
            this.j.setUv2(Math.round(r(this.l, var3, var4, var5, var6)), Math.round(r(this.Z, var3, var4, var5, var6)));
         }

         if ((this.B & 16) != 0) {
            this.j.setNormal(this.p, this.R, this.e);
         }

         if ((this.B & 32) != 0) {
            this.j.setLineWidth(this.K);
         }

         if (this.J) {
            this.j.setUv1(this.X, this.a);
         }
      }

      private static float I(float[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private static float r(int[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private int d(float var1, float var2, float var3, float var4) {
         int var5 = 0;

         for (byte var6 = 0; var6 < 32; var6 += 8) {
            float var7 = (this.m[0] >>> var6 & 0xFF) * var1
               + (this.m[1] >>> var6 & 0xFF) * var2
               + (this.m[2] >>> var6 & 0xFF) * var3
               + (this.m[3] >>> var6 & 0xFF) * var4;
            var5 |= Math.clamp(Math.round(var7), 0, 255) << var6;
         }

         return var5;
      }
   }

   public interface x {
      double[] forward(double var1, double var3);

      double[] inverse(double var1, double var3);

      double[] bounds(double var1, double var3, double var5, double var7);

      default boolean front(double var1, double var3) {
         return true;
      }

      default boolean clipsFlat() {
         return false;
      }

      default ScreenRectangle M(ScreenRectangle var1, int var2, int var3) {
         if (var1 == null) {
            return null;
         }

         double[] var4 = this.bounds(var1.left(), var1.top(), var1.right(), var1.bottom());
         int var5 = dx.y(Math.floor(var4[0]), var2);
         int var6 = dx.y(Math.floor(var4[1]), var3);
         int var7 = dx.y(Math.ceil(var4[2]), var2);
         int var8 = dx.y(Math.ceil(var4[3]), var3);
         return var7 > var5 && var8 > var6 ? new ScreenRectangle(var5, var6, var7 - var5, var8 - var6) : dx.j;
      }
   }

   public static final class z implements dx.x {
      public static final double k = 0.02;
      private final double e;
      private final double v;
      private final double I;
      private final double d;
      private final double X;
      private final double h;
      private final double s;
      private final double Z;
      private final double b;
      private final double T;
      private final double E;
      private final double B;
      private final double p;
      private final double N;
      private final double P;
      private final double M;
      private final double o;
      private final double j;

      public z(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         this.e = var1;
         this.v = var3;
         this.I = var5;
         this.d = var7;
         this.X = var9;
         this.h = var11;
         this.s = var13;
         this.Z = var15;
         this.b = var17;
         this.T = var9 * var17 - var11 * var15;
         this.E = var5 * var15 - var3 * var17;
         this.B = var3 * var11 - var5 * var9;
         this.p = var11 * var13 - var7 * var17;
         this.N = var1 * var17 - var5 * var13;
         this.P = var5 * var7 - var1 * var11;
         this.M = var7 * var15 - var9 * var13;
         this.o = var3 * var13 - var1 * var15;
         this.j = var1 * var9 - var3 * var7;
      }

      public double U(double var1, double var3) {
         return this.s * var1 + this.Z * var3 + this.b;
      }

      @Override
      public boolean front(double var1, double var3) {
         return this.U(var1, var3) >= 0.02;
      }

      @Override
      public boolean clipsFlat() {
         return true;
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = Math.max(0.02, this.U(var1, var3));
         return new double[]{(this.e * var1 + this.v * var3 + this.I) / var5, (this.d * var1 + this.X * var3 + this.h) / var5};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = this.T * var1 + this.E * var3 + this.B;
         double var7 = this.p * var1 + this.N * var3 + this.P;
         double var9 = this.M * var1 + this.o * var3 + this.j;
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
            double var22 = this.U(var20[0], var20[1]) - 0.02;
            double var24 = this.U(var21[0], var21[1]) - 0.02;
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
}
