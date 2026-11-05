package com.larpclient;

import com.larp.obf.qbac0b262827a15ac.qa1ea7ee4f1c0c80a;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public final class cp {
   public static final ScreenRectangle N = new ScreenRectangle(0, 0, 1, 1);
   private static final double P = -10000.0;
   private static cp.l d;
   private static Object c;
   private static cp.l O;
   private static boolean x;
   private static int u;
   private static int U = -1;
   private static final double[] F = new double[4];
   private static final double[] j = new double[4];
   private static boolean A;
   private static boolean l;
   private static Object h;
   private static double b = 1.0;
   private static double Z;
   private static double k;
   private static final cp.n f = new cp.n();

   private cp() {
   }

   private static int x(double var0, int var2) {
      return (int)Math.clamp(var0, 0.0, Math.max(1, var2));
   }

   public static void g(Object var0, cp.l var1) {
      if (A && c != null) {
         System.arraycopy(F, 0, j, 0, 4);
         h = c;
         l = (boolean)1;
      }

      A = (boolean)0;
      if (var0 != h) {
         l = (boolean)0;
      }

      c = var1 == null ? null : var0;
      d = var1;
   }

   public static void H(GuiGraphicsExtractor var0, cp.l var1, boolean var2, Runnable var3) {
      cp.b var4 = (cp.b)((qa1ea7ee4f1c0c80a)var0).larpGuiRenderState();
      var4.larpWarpBegin(var1, var2);
      u++;

      try {
         var3.run();
      } finally {
         u--;
         var4.larpWarpEnd();
      }
   }

   public static boolean P() {
      return u > 0;
   }

   public static void e(double var0, double var2, double var4) {
      b = Math.max(0.01, var0);
      Z = var2;
      k = var4;
   }

   public static double[] b(Object var0) {
      return l && var0 == h ? (double[])j.clone() : null;
   }

   public static cp.l J(Object var0) {
      return var0 != null && var0 == c ? d : null;
   }

   public static void U(int var0, cp.l var1, boolean var2) {
      U = var1 == null ? -1 : var0;
      O = var1;
      x = var2;
   }

   public static cp.l U() {
      return U >= 0 ? O : null;
   }

   private static void J(float var0, float var1) {
      if (x && U >= 0 && U <= 1) {
         double var2 = Z + (var0 - Z) / b;
         double var4 = k + (var1 - k) / b;
         if (!A) {
            F[0] = F[2] = var2;
            F[1] = F[3] = var4;
            A = (boolean)1;
         } else {
            F[0] = Math.min(F[0], var2);
            F[1] = Math.min(F[1], var4);
            F[2] = Math.max(F[2], var2);
            F[3] = Math.max(F[3], var4);
         }
      }
   }

   public static cp.n H(VertexConsumer var0, cp.l var1, boolean var2) {
      return n(var0, var1, var2, null, cp.t.m);
   }

   public static cp.n n(VertexConsumer var0, cp.l var1, boolean var2, ScreenRectangle var3, cp.t var4) {
      return f.b(var0, var1, var2, var1.clipsFlat() && var4 != cp.t.U ? var3 : null, var4);
   }

   public interface b {
      void larpWarpBegin(cp.l var1, boolean var2);

      void larpWarpEnd();
   }

   public record k(double pivotX, double pivotY, double targetX, double targetY, double scale, double sin, double cos, double focal) implements cp.l {
      public static cp.k of(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
         return new cp.k(var0, var2, var4, var6, var8, Math.sin(var10), Math.cos(var10), var12);
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

   public interface l {
      double[] forward(double var1, double var3);

      double[] inverse(double var1, double var3);

      double[] bounds(double var1, double var3, double var5, double var7);

      default boolean front(double var1, double var3) {
         return true;
      }

      default boolean clipsFlat() {
         return false;
      }

      default ScreenRectangle k(ScreenRectangle var1, int var2, int var3) {
         if (var1 == null) {
            return null;
         }

         double[] var4 = this.bounds(var1.left(), var1.top(), var1.right(), var1.bottom());
         int var5 = cp.x(Math.floor(var4[0]), var2);
         int var6 = cp.x(Math.floor(var4[1]), var3);
         int var7 = cp.x(Math.ceil(var4[2]), var2);
         int var8 = cp.x(Math.ceil(var4[3]), var3);
         return var7 > var5 && var8 > var6 ? new ScreenRectangle(var5, var6, var7 - var5, var8 - var6) : cp.N;
      }
   }

   public static final class n implements VertexConsumer {
      private static final float W = 12.0F;
      private static final int w = 1;
      private static final int A = 2;
      private static final int X = 4;
      private static final int x = 8;
      private static final int P = 16;
      private static final int h = 32;
      private VertexConsumer D;
      private cp.l s;
      private ScreenRectangle G;
      private boolean Z;
      private boolean I;
      private boolean d;
      private int Q = -1;
      private int R;
      private int b;
      private int H;
      private final float[] g = new float[4];
      private final float[] L = new float[4];
      private final float[] N = new float[4];
      private final float[] c = new float[4];
      private final float[] Y = new float[4];
      private final int[] k = new int[4];
      private final int[] V = new int[4];
      private final int[] e = new int[4];
      private final int[] f = new int[4];
      private final int[] E = new int[4];
      private float C;
      private float q;
      private float U;
      private float t;

      private cp.n b(VertexConsumer var1, cp.l var2, boolean var3, ScreenRectangle var4, cp.t var5) {
         this.D = var1;
         this.s = var2;
         this.Z = var3;
         this.G = var4;
         this.I = (boolean)(var5 == cp.t.U ? 1 : 0);
         this.d = (boolean)(var5 == cp.t.W ? 1 : 0);
         this.Q = -1;
         this.R = 0;
         return this;
      }

      public void Z() {
         if (this.Q == 3) {
            this.b();
         } else {
            for (int var1 = 0; var1 <= this.Q; var1++) {
               this.C(var1);
            }
         }

         this.Q = -1;
         this.R = 0;
         this.D = null;
      }

      public VertexConsumer addVertex(float var1, float var2, float var3) {
         cp.J(var1, var2);
         if (!this.Z) {
            double[] var4 = this.s.forward(var1, var2);
            this.D.addVertex((float)var4[0], (float)var4[1], var3);
            return this;
         }

         if (this.Q == 3) {
            this.b();
            this.Q = -1;
            this.R = 0;
         }

         this.Q++;
         this.g[this.Q] = var1;
         this.L[this.Q] = var2;
         this.N[this.Q] = var3;
         return this;
      }

      public VertexConsumer setColor(int var1, int var2, int var3, int var4) {
         return this.setColor((var4 & 0xFF) << 24 | (var1 & 0xFF) << 16 | (var2 & 0xFF) << 8 | var3 & 0xFF);
      }

      public VertexConsumer setColor(int var1) {
         if (!this.Z) {
            this.D.setColor(var1);
            return this;
         } else {
            this.k[this.Q] = var1;
            this.R |= 1;
            return this;
         }
      }

      public VertexConsumer setUv(float var1, float var2) {
         if (!this.Z) {
            this.D.setUv(var1, var2);
            return this;
         } else {
            this.c[this.Q] = var1;
            this.Y[this.Q] = var2;
            this.R |= 2;
            return this;
         }
      }

      public VertexConsumer setUv1(int var1, int var2) {
         if (!this.Z) {
            this.D.setUv1(var1, var2);
            return this;
         } else {
            this.V[this.Q] = var1;
            this.e[this.Q] = var2;
            this.R |= 4;
            return this;
         }
      }

      public VertexConsumer setUv3(float var1, float var2) {
         if (!this.Z) {
            this.D.setUv3(var1, var2);
         }

         return this;
      }

      public VertexConsumer setUv2(int var1, int var2) {
         if (!this.Z) {
            this.D.setUv2(var1, var2);
            return this;
         } else {
            this.f[this.Q] = var1;
            this.E[this.Q] = var2;
            this.R |= 8;
            return this;
         }
      }

      public VertexConsumer setNormal(float var1, float var2, float var3) {
         if (!this.Z) {
            this.D.setNormal(var1, var2, var3);
            return this;
         } else {
            this.C = var1;
            this.q = var2;
            this.U = var3;
            this.R |= 16;
            return this;
         }
      }

      public VertexConsumer setLineWidth(float var1) {
         if (!this.Z) {
            this.D.setLineWidth(var1);
            return this;
         } else {
            this.t = var1;
            this.R |= 32;
            return this;
         }
      }

      private void C(int var1) {
         double[] var2 = this.s.forward(this.g[var1], this.L[var1]);
         this.D.addVertex((float)var2[0], (float)var2[1], this.N[var1]);
         if ((this.R & 1) != 0) {
            this.D.setColor(this.k[var1]);
         }

         if ((this.R & 2) != 0) {
            this.D.setUv(this.c[var1], this.Y[var1]);
         }

         if ((this.R & 4) != 0) {
            this.D.setUv1(this.V[var1], this.e[var1]);
         }

         if ((this.R & 8) != 0) {
            this.D.setUv2(this.f[var1], this.E[var1]);
         }

         if ((this.R & 16) != 0) {
            this.D.setNormal(this.C, this.q, this.U);
         }

         if ((this.R & 32) != 0) {
            this.D.setLineWidth(this.t);
         }

         if (this.d) {
            this.D.setUv1(this.b, this.H);
         }
      }

      private void b() {
         if (this.d) {
            this.b = Math.clamp(Math.round((Math.abs(this.c[0]) - this.N[0]) * 64.0F), 0, 32767);
            this.H = Math.clamp(Math.round((Math.abs(this.Y[0]) - this.N[0]) * 64.0F), 0, 32767);
         }

         if (this.I) {
            for (int var14 = 0; var14 < 4; var14++) {
               if (!this.s.front(this.g[var14], this.L[var14])) {
                  return;
               }
            }

            for (int var15 = 0; var15 < 4; var15++) {
               this.C(var15);
            }
         } else {
            float var1 = 0.0F;
            float var2 = 1.0F;
            float var3 = 0.0F;
            float var4 = 1.0F;
            if (this.G != null) {
               float[] var5;
               float[] var6;
               if (this.g[0] == this.g[1] && this.g[3] == this.g[2] && this.L[0] == this.L[3] && this.L[1] == this.L[2]) {
                  var5 = Y(this.g[0], this.g[3], this.G.left(), this.G.right());
                  var6 = Y(this.L[0], this.L[1], this.G.top(), this.G.bottom());
               } else if (this.L[0] == this.L[1] && this.L[3] == this.L[2] && this.g[0] == this.g[3] && this.g[1] == this.g[2]) {
                  var5 = Y(this.L[0], this.L[3], this.G.top(), this.G.bottom());
                  var6 = Y(this.g[0], this.g[1], this.G.left(), this.G.right());
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
               var17 = Math.max(var17, (float)Math.hypot(this.g[var8] - this.g[var7], this.L[var8] - this.L[var7]));
            }

            var17 *= Math.max(var2 - var1, var4 - var3);
            int var19 = Math.clamp((int)Math.ceil(var17 / 12.0F), 1, 24);
            if (var19 == 1 && var16 != 0) {
               for (int var21 = 0; var21 < 4; var21++) {
                  if (!this.s.front(this.g[var21], this.L[var21])) {
                     return;
                  }
               }

               for (int var22 = 0; var22 < 4; var22++) {
                  this.C(var22);
               }
            } else {
               for (int var20 = 0; var20 < var19; var20++) {
                  float var9 = var1 + (var2 - var1) * var20 / var19;
                  float var10 = var1 + (var2 - var1) * (var20 + 1) / var19;

                  for (int var11 = 0; var11 < var19; var11++) {
                     float var12 = var3 + (var4 - var3) * var11 / var19;
                     float var13 = var3 + (var4 - var3) * (var11 + 1) / var19;
                     if (this.r(var9, var12) && this.r(var9, var13) && this.r(var10, var13) && this.r(var10, var12)) {
                        this.Y(var9, var12);
                        this.Y(var9, var13);
                        this.Y(var10, var13);
                        this.Y(var10, var12);
                     }
                  }
               }
            }
         }
      }

      private static float[] Y(float var0, float var1, int var2, int var3) {
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

      private boolean r(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         return this.s.front(p(this.g, var3, var4, var5, var6), p(this.L, var3, var4, var5, var6));
      }

      private void Y(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         double[] var7 = this.s.forward(p(this.g, var3, var4, var5, var6), p(this.L, var3, var4, var5, var6));
         this.D.addVertex((float)var7[0], (float)var7[1], p(this.N, var3, var4, var5, var6));
         if ((this.R & 1) != 0) {
            this.D.setColor(this.D(var3, var4, var5, var6));
         }

         if ((this.R & 2) != 0) {
            this.D.setUv(p(this.c, var3, var4, var5, var6), p(this.Y, var3, var4, var5, var6));
         }

         if ((this.R & 4) != 0) {
            this.D.setUv1(Math.round(G(this.V, var3, var4, var5, var6)), Math.round(G(this.e, var3, var4, var5, var6)));
         }

         if ((this.R & 8) != 0) {
            this.D.setUv2(Math.round(G(this.f, var3, var4, var5, var6)), Math.round(G(this.E, var3, var4, var5, var6)));
         }

         if ((this.R & 16) != 0) {
            this.D.setNormal(this.C, this.q, this.U);
         }

         if ((this.R & 32) != 0) {
            this.D.setLineWidth(this.t);
         }

         if (this.d) {
            this.D.setUv1(this.b, this.H);
         }
      }

      private static float p(float[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private static float G(int[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private int D(float var1, float var2, float var3, float var4) {
         int var5 = 0;

         for (byte var6 = 0; var6 < 32; var6 += 8) {
            float var7 = (this.k[0] >>> var6 & 0xFF) * var1
               + (this.k[1] >>> var6 & 0xFF) * var2
               + (this.k[2] >>> var6 & 0xFF) * var3
               + (this.k[3] >>> var6 & 0xFF) * var4;
            var5 |= Math.clamp(Math.round(var7), 0, 255) << var6;
         }

         return var5;
      }
   }

   public static final class p implements cp.l {
      public static final double R = 0.02;
      private final double B;
      private final double o;
      private final double d;
      private final double m;
      private final double M;
      private final double p;
      private final double i;
      private final double r;
      private final double E;
      private final double Z;
      private final double e;
      private final double I;
      private final double q;
      private final double Y;
      private final double H;
      private final double S;
      private final double U;
      private final double W;

      public p(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         this.B = var1;
         this.o = var3;
         this.d = var5;
         this.m = var7;
         this.M = var9;
         this.p = var11;
         this.i = var13;
         this.r = var15;
         this.E = var17;
         this.Z = var9 * var17 - var11 * var15;
         this.e = var5 * var15 - var3 * var17;
         this.I = var3 * var11 - var5 * var9;
         this.q = var11 * var13 - var7 * var17;
         this.Y = var1 * var17 - var5 * var13;
         this.H = var5 * var7 - var1 * var11;
         this.S = var7 * var15 - var9 * var13;
         this.U = var3 * var13 - var1 * var15;
         this.W = var1 * var9 - var3 * var7;
      }

      public double u(double var1, double var3) {
         return this.i * var1 + this.r * var3 + this.E;
      }

      @Override
      public boolean front(double var1, double var3) {
         return this.u(var1, var3) >= 0.02;
      }

      @Override
      public boolean clipsFlat() {
         return true;
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = Math.max(0.02, this.u(var1, var3));
         return new double[]{(this.B * var1 + this.o * var3 + this.d) / var5, (this.m * var1 + this.M * var3 + this.p) / var5};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = this.Z * var1 + this.e * var3 + this.I;
         double var7 = this.q * var1 + this.Y * var3 + this.H;
         double var9 = this.S * var1 + this.U * var3 + this.W;
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
            double var22 = this.u(var20[0], var20[1]) - 0.02;
            double var24 = this.u(var21[0], var21[1]) - 0.02;
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

   public enum t {
      m,
      U,
      W;
   }
}
