package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q678a9f2b892508e5;
import net.minecraft.class_332;
import net.minecraft.class_4588;
import net.minecraft.class_8030;

public final class lb {
   public static final class_8030 Z = new class_8030(0, 0, 1, 1);
   private static final double v = -10000.0;
   private static lb.m Q;
   private static Object p;
   private static lb.m W;
   private static boolean j;
   private static int r;
   private static int A = -1;
   private static final double[] N = new double[4];
   private static final double[] m = new double[4];
   private static boolean h;
   private static boolean s;
   private static Object y;
   private static double F = 1.0;
   private static double u;
   private static double g;
   private static final lb.w B = new lb.w();

   private lb() {
   }

   private static int L(double var0, int var2) {
      return (int)Math.clamp(var0, 0.0, Math.max(1, var2));
   }

   public static void K(Object var0, lb.m var1) {
      if (h && p != null) {
         System.arraycopy(N, 0, m, 0, 4);
         y = p;
         s = (boolean)1;
      }

      h = (boolean)0;
      if (var0 != y) {
         s = (boolean)0;
      }

      p = var1 == null ? null : var0;
      Q = var1;
   }

   public static void y(class_332 var0, lb.m var1, boolean var2, Runnable var3) {
      lb.o var4 = (lb.o)((q678a9f2b892508e5)var0).larpGuiRenderState();
      var4.larpWarpBegin(var1, var2);
      r++;

      try {
         var3.run();
      } finally {
         r--;
         var4.larpWarpEnd();
      }
   }

   public static boolean T() {
      return r > 0;
   }

   public static void w(double var0, double var2, double var4) {
      F = Math.max(0.01, var0);
      u = var2;
      g = var4;
   }

   public static double[] Q(Object var0) {
      return s && var0 == y ? (double[])m.clone() : null;
   }

   public static lb.m X(Object var0) {
      return var0 != null && var0 == p ? Q : null;
   }

   public static void r(int var0, lb.m var1, boolean var2) {
      A = var1 == null ? -1 : var0;
      W = var1;
      j = var2;
   }

   public static lb.m Y() {
      return A >= 0 ? W : null;
   }

   private static void N(float var0, float var1) {
      if (j && A >= 0 && A <= 1) {
         double var2 = u + (var0 - u) / F;
         double var4 = g + (var1 - g) / F;
         if (!h) {
            N[0] = N[2] = var2;
            N[1] = N[3] = var4;
            h = (boolean)1;
         } else {
            N[0] = Math.min(N[0], var2);
            N[1] = Math.min(N[1], var4);
            N[2] = Math.max(N[2], var2);
            N[3] = Math.max(N[3], var4);
         }
      }
   }

   public static lb.w m(class_4588 var0, lb.m var1, boolean var2) {
      return S(var0, var1, var2, null, lb.a.I);
   }

   public static lb.w S(class_4588 var0, lb.m var1, boolean var2, class_8030 var3, lb.a var4) {
      return B.P(var0, var1, var2, var1.clipsFlat() && var4 != lb.a.A ? var3 : null, var4);
   }

   public enum a {
      I,
      A,
      u;
   }

   public interface m {
      double[] forward(double var1, double var3);

      double[] inverse(double var1, double var3);

      double[] bounds(double var1, double var3, double var5, double var7);

      default boolean front(double var1, double var3) {
         return true;
      }

      default boolean clipsFlat() {
         return false;
      }

      default class_8030 C(class_8030 var1, int var2, int var3) {
         if (var1 == null) {
            return null;
         }

         double[] var4 = this.bounds(var1.method_49620(), var1.method_49618(), var1.method_49621(), var1.method_49619());
         int var5 = lb.L(Math.floor(var4[0]), var2);
         int var6 = lb.L(Math.floor(var4[1]), var3);
         int var7 = lb.L(Math.ceil(var4[2]), var2);
         int var8 = lb.L(Math.ceil(var4[3]), var3);
         return var7 > var5 && var8 > var6 ? new class_8030(var5, var6, var7 - var5, var8 - var6) : lb.Z;
      }
   }

   public static final class n implements lb.m {
      public static final double q = 0.02;
      private final double L;
      private final double e;
      private final double E;
      private final double a;
      private final double i;
      private final double R;
      private final double Y;
      private final double n;
      private final double o;
      private final double B;
      private final double g;
      private final double x;
      private final double f;
      private final double z;
      private final double F;
      private final double X;
      private final double N;
      private final double j;

      public n(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         this.L = var1;
         this.e = var3;
         this.E = var5;
         this.a = var7;
         this.i = var9;
         this.R = var11;
         this.Y = var13;
         this.n = var15;
         this.o = var17;
         this.B = var9 * var17 - var11 * var15;
         this.g = var5 * var15 - var3 * var17;
         this.x = var3 * var11 - var5 * var9;
         this.f = var11 * var13 - var7 * var17;
         this.z = var1 * var17 - var5 * var13;
         this.F = var5 * var7 - var1 * var11;
         this.X = var7 * var15 - var9 * var13;
         this.N = var3 * var13 - var1 * var15;
         this.j = var1 * var9 - var3 * var7;
      }

      public double Q(double var1, double var3) {
         return this.Y * var1 + this.n * var3 + this.o;
      }

      @Override
      public boolean front(double var1, double var3) {
         return this.Q(var1, var3) >= 0.02;
      }

      @Override
      public boolean clipsFlat() {
         return true;
      }

      @Override
      public double[] forward(double var1, double var3) {
         double var5 = Math.max(0.02, this.Q(var1, var3));
         return new double[]{(this.L * var1 + this.e * var3 + this.E) / var5, (this.a * var1 + this.i * var3 + this.R) / var5};
      }

      @Override
      public double[] inverse(double var1, double var3) {
         double var5 = this.B * var1 + this.g * var3 + this.x;
         double var7 = this.f * var1 + this.z * var3 + this.F;
         double var9 = this.X * var1 + this.N * var3 + this.j;
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
            double var22 = this.Q(var20[0], var20[1]) - 0.02;
            double var24 = this.Q(var21[0], var21[1]) - 0.02;
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

   public interface o {
      void larpWarpBegin(lb.m var1, boolean var2);

      void larpWarpEnd();
   }

   public static final class w implements class_4588 {
      private static final float x = 12.0F;
      private static final int m = 1;
      private static final int H = 2;
      private static final int B = 4;
      private static final int i = 8;
      private static final int t = 16;
      private static final int F = 32;
      private class_4588 u;
      private lb.m c;
      private class_8030 d;
      private boolean j;
      private boolean o;
      private boolean n;
      private int V = -1;
      private int l;
      private int Y;
      private int z;
      private final float[] p = new float[4];
      private final float[] D = new float[4];
      private final float[] C = new float[4];
      private final float[] y = new float[4];
      private final float[] S = new float[4];
      private final int[] v = new int[4];
      private final int[] A = new int[4];
      private final int[] s = new int[4];
      private final int[] K = new int[4];
      private final int[] O = new int[4];
      private float k;
      private float r;
      private float h;
      private float I;

      private lb.w P(class_4588 var1, lb.m var2, boolean var3, class_8030 var4, lb.a var5) {
         this.u = var1;
         this.c = var2;
         this.j = var3;
         this.d = var4;
         this.o = (boolean)(var5 == lb.a.A ? 1 : 0);
         this.n = (boolean)(var5 == lb.a.u ? 1 : 0);
         this.V = -1;
         this.l = 0;
         return this;
      }

      public void E() {
         if (this.V == 3) {
            this.F();
         } else {
            for (int var1 = 0; var1 <= this.V; var1++) {
               this.U(var1);
            }
         }

         this.V = -1;
         this.l = 0;
         this.u = null;
      }

      public class_4588 method_22912(float var1, float var2, float var3) {
         lb.N(var1, var2);
         if (!this.j) {
            double[] var4 = this.c.forward(var1, var2);
            this.u.method_22912((float)var4[0], (float)var4[1], var3);
            return this;
         }

         if (this.V == 3) {
            this.F();
            this.V = -1;
            this.l = 0;
         }

         this.V++;
         this.p[this.V] = var1;
         this.D[this.V] = var2;
         this.C[this.V] = var3;
         return this;
      }

      public class_4588 method_1336(int var1, int var2, int var3, int var4) {
         return this.method_39415((var4 & 0xFF) << 24 | (var1 & 0xFF) << 16 | (var2 & 0xFF) << 8 | var3 & 0xFF);
      }

      public class_4588 method_39415(int var1) {
         if (!this.j) {
            this.u.method_39415(var1);
            return this;
         } else {
            this.v[this.V] = var1;
            this.l |= 1;
            return this;
         }
      }

      public class_4588 method_22913(float var1, float var2) {
         if (!this.j) {
            this.u.method_22913(var1, var2);
            return this;
         } else {
            this.y[this.V] = var1;
            this.S[this.V] = var2;
            this.l |= 2;
            return this;
         }
      }

      public class_4588 method_60796(int var1, int var2) {
         if (!this.j) {
            this.u.method_60796(var1, var2);
            return this;
         } else {
            this.A[this.V] = var1;
            this.s[this.V] = var2;
            this.l |= 4;
            return this;
         }
      }

      public class_4588 method_22921(int var1, int var2) {
         if (!this.j) {
            this.u.method_22921(var1, var2);
            return this;
         } else {
            this.K[this.V] = var1;
            this.O[this.V] = var2;
            this.l |= 8;
            return this;
         }
      }

      public class_4588 method_22914(float var1, float var2, float var3) {
         if (!this.j) {
            this.u.method_22914(var1, var2, var3);
            return this;
         } else {
            this.k = var1;
            this.r = var2;
            this.h = var3;
            this.l |= 16;
            return this;
         }
      }

      public class_4588 method_75298(float var1) {
         if (!this.j) {
            this.u.method_75298(var1);
            return this;
         } else {
            this.I = var1;
            this.l |= 32;
            return this;
         }
      }

      private void U(int var1) {
         double[] var2 = this.c.forward(this.p[var1], this.D[var1]);
         this.u.method_22912((float)var2[0], (float)var2[1], this.C[var1]);
         if ((this.l & 1) != 0) {
            this.u.method_39415(this.v[var1]);
         }

         if ((this.l & 2) != 0) {
            this.u.method_22913(this.y[var1], this.S[var1]);
         }

         if ((this.l & 4) != 0) {
            this.u.method_60796(this.A[var1], this.s[var1]);
         }

         if ((this.l & 8) != 0) {
            this.u.method_22921(this.K[var1], this.O[var1]);
         }

         if ((this.l & 16) != 0) {
            this.u.method_22914(this.k, this.r, this.h);
         }

         if ((this.l & 32) != 0) {
            this.u.method_75298(this.I);
         }

         if (this.n) {
            this.u.method_60796(this.Y, this.z);
         }
      }

      private void F() {
         if (this.n) {
            this.Y = Math.clamp(Math.round((Math.abs(this.y[0]) - this.C[0]) * 64.0F), 0, 32767);
            this.z = Math.clamp(Math.round((Math.abs(this.S[0]) - this.C[0]) * 64.0F), 0, 32767);
         }

         if (this.o) {
            for (int var14 = 0; var14 < 4; var14++) {
               if (!this.c.front(this.p[var14], this.D[var14])) {
                  return;
               }
            }

            for (int var15 = 0; var15 < 4; var15++) {
               this.U(var15);
            }
         } else {
            float var1 = 0.0F;
            float var2 = 1.0F;
            float var3 = 0.0F;
            float var4 = 1.0F;
            if (this.d != null) {
               float[] var5;
               float[] var6;
               if (this.p[0] == this.p[1] && this.p[3] == this.p[2] && this.D[0] == this.D[3] && this.D[1] == this.D[2]) {
                  var5 = X(this.p[0], this.p[3], this.d.method_49620(), this.d.method_49621());
                  var6 = X(this.D[0], this.D[1], this.d.method_49618(), this.d.method_49619());
               } else if (this.D[0] == this.D[1] && this.D[3] == this.D[2] && this.p[0] == this.p[3] && this.p[1] == this.p[2]) {
                  var5 = X(this.D[0], this.D[3], this.d.method_49618(), this.d.method_49619());
                  var6 = X(this.p[0], this.p[1], this.d.method_49620(), this.d.method_49621());
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
               var17 = Math.max(var17, (float)Math.hypot(this.p[var8] - this.p[var7], this.D[var8] - this.D[var7]));
            }

            var17 *= Math.max(var2 - var1, var4 - var3);
            int var19 = Math.clamp((int)Math.ceil(var17 / 12.0F), 1, 24);
            if (var19 == 1 && var16 != 0) {
               for (int var21 = 0; var21 < 4; var21++) {
                  if (!this.c.front(this.p[var21], this.D[var21])) {
                     return;
                  }
               }

               for (int var22 = 0; var22 < 4; var22++) {
                  this.U(var22);
               }
            } else {
               for (int var20 = 0; var20 < var19; var20++) {
                  float var9 = var1 + (var2 - var1) * var20 / var19;
                  float var10 = var1 + (var2 - var1) * (var20 + 1) / var19;

                  for (int var11 = 0; var11 < var19; var11++) {
                     float var12 = var3 + (var4 - var3) * var11 / var19;
                     float var13 = var3 + (var4 - var3) * (var11 + 1) / var19;
                     if (this.g(var9, var12) && this.g(var9, var13) && this.g(var10, var13) && this.g(var10, var12)) {
                        this.A(var9, var12);
                        this.A(var9, var13);
                        this.A(var10, var13);
                        this.A(var10, var12);
                     }
                  }
               }
            }
         }
      }

      private static float[] X(float var0, float var1, int var2, int var3) {
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

      private boolean g(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         return this.c.front(M(this.p, var3, var4, var5, var6), M(this.D, var3, var4, var5, var6));
      }

      private void A(float var1, float var2) {
         float var3 = (1.0F - var1) * (1.0F - var2);
         float var4 = (1.0F - var1) * var2;
         float var5 = var1 * var2;
         float var6 = var1 * (1.0F - var2);
         double[] var7 = this.c.forward(M(this.p, var3, var4, var5, var6), M(this.D, var3, var4, var5, var6));
         this.u.method_22912((float)var7[0], (float)var7[1], M(this.C, var3, var4, var5, var6));
         if ((this.l & 1) != 0) {
            this.u.method_39415(this.G(var3, var4, var5, var6));
         }

         if ((this.l & 2) != 0) {
            this.u.method_22913(M(this.y, var3, var4, var5, var6), M(this.S, var3, var4, var5, var6));
         }

         if ((this.l & 4) != 0) {
            this.u.method_60796(Math.round(R(this.A, var3, var4, var5, var6)), Math.round(R(this.s, var3, var4, var5, var6)));
         }

         if ((this.l & 8) != 0) {
            this.u.method_22921(Math.round(R(this.K, var3, var4, var5, var6)), Math.round(R(this.O, var3, var4, var5, var6)));
         }

         if ((this.l & 16) != 0) {
            this.u.method_22914(this.k, this.r, this.h);
         }

         if ((this.l & 32) != 0) {
            this.u.method_75298(this.I);
         }

         if (this.n) {
            this.u.method_60796(this.Y, this.z);
         }
      }

      private static float M(float[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private static float R(int[] var0, float var1, float var2, float var3, float var4) {
         return var0[0] * var1 + var0[1] * var2 + var0[2] * var3 + var0[3] * var4;
      }

      private int G(float var1, float var2, float var3, float var4) {
         int var5 = 0;

         for (byte var6 = 0; var6 < 32; var6 += 8) {
            float var7 = (this.v[0] >>> var6 & 0xFF) * var1
               + (this.v[1] >>> var6 & 0xFF) * var2
               + (this.v[2] >>> var6 & 0xFF) * var3
               + (this.v[3] >>> var6 & 0xFF) * var4;
            var5 |= Math.clamp(Math.round(var7), 0, 255) << var6;
         }

         return var5;
      }
   }

   public record z(double pivotX, double pivotY, double targetX, double targetY, double scale, double sin, double cos, double focal) implements lb.m {
      public static lb.z of(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
         return new lb.z(var0, var2, var4, var6, var8, Math.sin(var10), Math.cos(var10), var12);
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
