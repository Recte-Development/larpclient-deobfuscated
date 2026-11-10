package com.larpclient;

import com.larp.obf.qa300483f54d804a3.q06414012fb28c0cc;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public final class m_ {
   public static final ScreenRectangle d = new ScreenRectangle(0, 0, 1, 1);
   private static final double N = -10000.0;
   private static m_.g A;
   private static Object C;
   private static m_.g v;
   private static boolean D;
   private static int b;
   private static int T = -1;
   private static final double[] P = new double[4];
   private static final double[] w = new double[4];
   private static boolean r;
   private static boolean H;
   private static Object c;
   private static double p = 1.0;
   private static double V;
   private static double a;
   private static final m_.x x = new m_.x();

   private m_() {
   }

   private static int z(double var0, int var2) {
      return (int)Math.clamp(var0, 0.0, Math.max(1, var2));
   }

   public static void v(Object var0, m_.g var1) {
      if (r && C != null) {
         System.arraycopy(P, 0, w, 0, 4);
         c = C;
         H = (boolean)1;
      }

      r = (boolean)0;
      if (var0 != c) {
         H = (boolean)0;
      }

      C = var1 == null ? null : var0;
      A = var1;
   }

   public static void T(GuiGraphicsExtractor var0, m_.g var1, boolean var2, Runnable var3) {
      m_.i var4 = (m_.i)((q06414012fb28c0cc)var0).larpGuiRenderState();
      var4.larpWarpBegin(var1, var2);
      b++;

      try {
         var3.run();
      } finally {
         b--;
         var4.larpWarpEnd();
      }
   }

   public static boolean T() {
      return b > 0;
   }

   public static void B(double var0, double var2, double var4) {
      p = Math.max(0.01, var0);
      V = var2;
      a = var4;
   }

   public static double[] V(Object var0) {
      return H && var0 == c ? (double[])w.clone() : null;
   }

   public static m_.g U(Object var0) {
      return var0 != null && var0 == C ? A : null;
   }

   public static void F(int var0, m_.g var1, boolean var2) {
      T = var1 == null ? -1 : var0;
      v = var1;
      D = var2;
   }

   public static m_.g j() {
      return T >= 0 ? v : null;
   }

   private static void l(float var0, float var1) {
      if (D && T >= 0 && T <= 1) {
         double var2 = V + (var0 - V) / p;
         double var4 = a + (var1 - a) / p;
         if (!r) {
            P[0] = P[2] = var2;
            P[1] = P[3] = var4;
            r = (boolean)1;
         } else {
            P[0] = Math.min(P[0], var2);
            P[1] = Math.min(P[1], var4);
            P[2] = Math.max(P[2], var2);
            P[3] = Math.max(P[3], var4);
         }
      }
   }

   public static m_.x k(VertexConsumer var0, m_.g var1, boolean var2) {
      return o(var0, var1, var2, null, m_.k.N);
   }

   public static m_.x o(VertexConsumer var0, m_.g var1, boolean var2, ScreenRectangle var3, m_.k var4) {
      return x.n(var0, var1, var2, var1.clipsFlat() && var4 != m_.k.P ? var3 : null, var4);
   }

   public interface g {
      double[] forward(double var1, double var3);

      double[] inverse(double var1, double var3);

      double[] bounds(double var1, double var3, double var5, double var7);

      default boolean front(double var1, double var3) {
         return true;
      }

      default boolean clipsFlat() {
         return false;
      }

      default ScreenRectangle z(ScreenRectangle var1, int var2, int var3) {
         if (var1 == null) {
            return null;
         }

         double[] var4 = this.bounds(var1.left(), var1.top(), var1.right(), var1.bottom());
         int var5 = m_.z(Math.floor(var4[0]), var2);
         int var6 = m_.z(Math.floor(var4[1]), var3);
         int var7 = m_.z(Math.ceil(var4[2]), var2);
         int var8 = m_.z(Math.ceil(var4[3]), var3);
         return var7 > var5 && var8 > var6 ? new ScreenRectangle(var5, var6, var7 - var5, var8 - var6) : m_.d;
      }
   }

   public interface i {
      void larpWarpBegin(m_.g var1, boolean var2);

      void larpWarpEnd();
   }

   public enum k {
      N,
      P,
      k;
   }

   public static final class r implements m_.g {
      public static final double X = 0.02;
      private final double i;
      private final double n;
      private final double M;
      private final double a;
      private final double m;
      private final double l;
      private final double Z;
      private final double V;
      private final double T;
      private final double A;
      private final double s;
      private final double E;
      private final double r;
      private final double Y;
      private final double I;
      private final double p;
      private final double u;
      private final double G;

      public r(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         this.i = var1;
         this.n = var3;
         this.M = var5;
         this.a = var7;
         this.m = var9;
         this.l = var11;
         this.Z = var13;
         this.V = var15;
         this.T = var17;
         this.A = var9 * var17 - var11 * var15;
         this.s = var5 * var15 - var3 * var17;
         this.E = var3 * var11 - var5 * var9;
         this.r = var11 * var13 - var7 * var17;
         this.Y = var1 * var17 - var5 * var13;
         this.I = var5 * var7 - var1 * var11;
         this.p = var7 * var15 - var9 * var13;
         this.u = var3 * var13 - var1 * var15;
         this.G = var1 * var9 - var3 * var7;
      }

      public double U(double var1, double var3) {
         return this.Z * var1 + this.V * var3 + this.T;
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
         return new double[]{(this.i * var1 + this.n * var3 + this.M) / var5, (this.a * var1 + this.m * var3 + this.l) / var5};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = this.A * var1 + this.s * var3 + this.E;
         double var7 = this.r * var1 + this.Y * var3 + this.I;
         double var9 = this.p * var1 + this.u * var3 + this.G;
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

   public record s(double pivotX, double pivotY, double targetX, double targetY, double scale, double sin, double cos, double focal) implements m_.g {
      public static m_.s of(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
         return new m_.s(var0, var2, var4, var6, var8, Math.sin(var10), Math.cos(var10), var12);
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

   public static final class x implements VertexConsumer {
      private static final float f = 12.0F;
      private static final int M = 1;
      private static final int g = 2;
      private static final int I = 4;
      private static final int c = 8;
      private static final int k = 16;
      private static final int K = 32;
      private VertexConsumer T;
      private m_.g u;
      private ScreenRectangle t;
      private boolean J;
      private boolean X;
      private boolean V;
      private int W = -1;
      private int U;
      private int n;
      private int y;
      private final float[] R = new float[4];
      private final float[] C = new float[4];
      private final float[] j = new float[4];
      private final float[] h = new float[4];
      private final float[] z = new float[4];
      private final int[] m = new int[4];
      private final int[] o = new int[4];
      private final int[] Z = new int[4];
      private final int[] x = new int[4];
      private final int[] P = new int[4];
      private float L;
      private float E;
      private float r;
      private float p;

      private m_.x n(VertexConsumer var1, m_.g var2, boolean var3, ScreenRectangle var4, m_.k var5) {
         this.T = var1;
         this.u = var2;
         this.J = var3;
         this.t = var4;
         this.X = (boolean)(var5 == m_.k.P ? 1 : 0);
         this.V = (boolean)(var5 == m_.k.k ? 1 : 0);
         this.W = -1;
         this.U = 0;
         return this;
      }

      public void u() {
         if (this.W == 3) {
            this.h();
         } else {
            for (int var1 = 0; var1 <= this.W; var1++) {
               this.g(var1);
            }
         }

         this.W = -1;
         this.U = 0;
         this.T = null;
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         m_.l(var1, var2);
         if (!this.J) {
            double[] var4 = this.u.forward(var1, var2);
            this.T.addVertex((float)var4[0], (float)var4[1], var3);
            return this;
         }

         if (this.W == 3) {
            this.h();
            this.W = -1;
            this.U = 0;
         }

         this.W++;
         this.R[this.W] = var1;
         this.C[this.W] = var2;
         this.j[this.W] = var3;
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor((var4 & 0xFF) << 24 | (var1 & 0xFF) << 16 | (var2 & 0xFF) << 8 | var3 & 0xFF);
      }

      public VertexConsumer setColor(int var1) {
         if (!this.J) {
            this.T.setColor(var1);
            return this;
         } else {
            this.m[this.W] = var1;
            this.U |= 1;
            return this;
         }
      }

      public VertexConsumer setUv(float var1, float var2) {
         if (!this.J) {
            this.T.setUv(var1, var2);
            return this;
         } else {
            this.h[this.W] = var1;
            this.z[this.W] = var2;
            this.U |= 2;
            return this;
         }
      }

      public VertexConsumer setUv1(int var1, int var2) {
         if (!this.J) {
            this.T.setUv1(var1, var2);
            return this;
         } else {
            this.o[this.W] = var1;
            this.Z[this.W] = var2;
            this.U |= 4;
            return this;
         }
      }

      public VertexConsumer setUv2(int var1, int var2) {
         if (!this.J) {
            this.T.setUv2(var1, var2);
            return this;
         } else {
            this.x[this.W] = var1;
            this.P[this.W] = var2;
            this.U |= 8;
            return this;
         }
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         if (!this.J) {
            this.T.setNormal(var1, var2, var3);
            return this;
         } else {
            this.L = var1;
            this.E = var2;
            this.r = var3;
            this.U |= 16;
            return this;
         }
      }

      public VertexConsumer setLineWidth(float var1) {
         if (!this.J) {
            this.T.setLineWidth(var1);
            return this;
         } else {
            this.p = var1;
            this.U |= 32;
            return this;
         }
      }

      private void g(int var1) {
         double[] var2 = this.u.forward(this.R[var1], this.C[var1]);
         this.T.addVertex((float)var2[0], (float)var2[1], this.j[var1]);
         if ((this.U & 1) != 0) {
            this.T.setColor(this.m[var1]);
         }

         if ((this.U & 2) != 0) {
            this.T.setUv(this.h[var1], this.z[var1]);
         }

         if ((this.U & 4) != 0) {
            this.T.setUv1(this.o[var1], this.Z[var1]);
         }

         if ((this.U & 8) != 0) {
            this.T.setUv2(this.x[var1], this.P[var1]);
         }

         if ((this.U & 16) != 0) {
            this.T.setNormal(this.L, this.E, this.r);
         }

         if ((this.U & 32) != 0) {
            this.T.setLineWidth(this.p);
         }

         if (this.V) {
            this.T.setUv1(this.n, this.y);
         }
      }

      private void h() {
         if (this.V) {
            this.n = Math.clamp(Math.round((Math.abs(this.h[0]) - this.j[0]) * 64.0F), 0, 32767);
            this.y = Math.clamp(Math.round((Math.abs(this.z[0]) - this.j[0]) * 64.0F), 0, 32767);
         }

         if (this.X) {
            for (int var14 = 0; var14 < 4; var14++) {
               if (!this.u.front(this.R[var14], this.C[var14])) {
                  return;
               }
            }

            for (int var15 = 0; var15 < 4; var15++) {
               this.g(var15);
            }
         } else {
            float var1 = 0.0F;
            float var2 = 1.0F;
            float var3 = 0.0F;
            float var4 = 1.0F;
            if (this.t != null) {
               float[] var5;
               float[] var6;
               if (this.R[0] == this.R[1] && this.R[3] == this.R[2] && this.C[0] == this.C[3] && this.C[1] == this.C[2]) {
                  var5 = e(this.R[0], this.R[3], this.t.left(), this.t.right());
                  var6 = e(this.C[0], this.C[1], this.t.top(), this.t.bottom());
               } else if (this.C[0] == this.C[1] && this.C[3] == this.C[2] && this.R[0] == this.R[3] && this.R[1] == this.R[2]) {
                  var5 = e(this.C[0], this.C[3], this.t.top(), this.t.bottom());
                  var6 = e(this.R[0], this.R[1], this.t.left(), this.t.right());
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
               var17 = Math.max(var17, (float)Math.hypot(this.R[var8] - this.R[var7], this.C[var8] - this.C[var7]));
            }

            var17 *= Math.max(var2 - var1, var4 - var3);
            int var19 = Math.clamp((int)Math.ceil(var17 / 12.0F), 1, 24);
            if (var19 == 1 && var16 != 0) {
               for (int var21 = 0; var21 < 4; var21++) {
                  if (!this.u.front(this.R[var21], this.C[var21])) {
                     return;
                  }
               }

               for (int var22 = 0; var22 < 4; var22++) {
                  this.g(var22);
               }
            } else {
               for (int var20 = 0; var20 < var19; var20++) {
                  float var9 = var1 + (var2 - var1) * var20 / var19;
                  float var10 = var1 + (var2 - var1) * (var20 + 1) / var19;

                  for (int var11 = 0; var11 < var19; var11++) {
                     float var12 = var3 + (var4 - var3) * var11 / var19;
                     float var13 = var3 + (var4 - var3) * (var11 + 1) / var19;
                     if (this.k(var9, var12) && this.k(var9, var13) && this.k(var10, var13) && this.k(var10, var12)) {
                        this.g(var9, var12);
                        this.g(var9, var13);
                        this.g(var10, var13);
                        this.g(var10, var12);
                     }
                  }
               }
            }
         }
      }

      private static float[] e(float var0, float var1, int var2, int var3) {
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

      private boolean k(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         return this.u.front(D(this.R, var3, var4, var5, var6), D(this.C, var3, var4, var5, var6));
      }

      private void g(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         double[] var7 = this.u.forward(D(this.R, var3, var4, var5, var6), D(this.C, var3, var4, var5, var6));
         this.T.addVertex((float)var7[0], (float)var7[1], D(this.j, var3, var4, var5, var6));
         if ((this.U & 1) != 0) {
            this.T.setColor(this.Q(var3, var4, var5, var6));
         }

         if ((this.U & 2) != 0) {
            this.T.setUv(D(this.h, var3, var4, var5, var6), D(this.z, var3, var4, var5, var6));
         }

         if ((this.U & 4) != 0) {
            this.T.setUv1(Math.round(q(this.o, var3, var4, var5, var6)), Math.round(q(this.Z, var3, var4, var5, var6)));
         }

         if ((this.U & 8) != 0) {
            this.T.setUv2(Math.round(q(this.x, var3, var4, var5, var6)), Math.round(q(this.P, var3, var4, var5, var6)));
         }

         if ((this.U & 16) != 0) {
            this.T.setNormal(this.L, this.E, this.r);
         }

         if ((this.U & 32) != 0) {
            this.T.setLineWidth(this.p);
         }

         if (this.V) {
            this.T.setUv1(this.n, this.y);
         }
      }

      private static float D(float[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private static float q(int[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private int Q(float var1, float var2, float var3, float var4) {
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
}
