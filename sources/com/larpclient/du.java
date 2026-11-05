package com.larpclient;

import java.util.Arrays;

public final class du {
   public static final float l = 0.9F;
   public static final float Z = 4.8F;
   public static final float W = 12.0F;
   public static final int R = 64;
   private static final float[][] g = new float[][]{
      {0.9F, 0.64F}, {1.4F, 0.57F}, {2.06F, 0.51F}, {2.73F, 0.46F}, {3.39F, 0.39F}, {3.97F, 0.3F}, {4.39F, 0.18F}
   };
   private static final float[] a = new float[]{1.0F, 0.86F, 0.95F, 1.08F, 0.9F, 0.97F};
   private static final float T = 0.88F;
   private static final float Y = 0.24F;
   private static final float b = 0.08F;
   private static final float K = 0.4F;
   private static du.h X;

   private du() {
   }

   public static synchronized du.h p() {
      if (X == null) {
         X = d();
      }

      return X;
   }

   static du.h d() {
      du.u var0 = new du.u();
      var0.q(0.3F, -0.05F, 0.05F, 1.0F, 0.9F, 0.95F, 8, 5, 11);
      var0.q(-0.45F, 0.4F, -0.1F, 0.6F, 0.55F, 0.58F, 7, 4, 23);
      int var1 = a.length;
      int[] var2 = null;

      for (int var3 = 0; var3 < g.length; var3++) {
         float var4 = g[var3][0];
         float var5 = g[var3][1];
         float var6 = M(var4);
         float var7 = k(var6, true);
         float var8 = k(var6, false);
         float var9 = g[Math.max(0, var3 - 1)][1];
         float var10 = var3 + 1 < g.length ? g[var3 + 1][1] : 0.0F;
         float var11 = (var3 + 1 < g.length ? g[var3 + 1][0] : 4.8F) - g[Math.max(0, var3 - 1)][0];
         float var12 = (var9 - var10) / var11;
         int[] var13 = new int[var1 + 1];

         for (int var14 = 0; var14 <= var1; var14++) {
            double var15 = (Math.PI * 2) * var14 / var1 + 0.4F * var6;
            float var17 = var5 * a[var14 % var1];
            float var18 = var7 + (float)Math.cos(var15) * var17;
            float var19 = var8 + (float)Math.sin(var15) * var17 * 0.88F;
            var13[var14] = var0.h(
               var18,
               var4,
               var19,
               (float)Math.cos(var15),
               var12,
               (float)Math.sin(var15) / 0.88F,
               t((float)var14 / var1),
               j(0.25F + 0.75F * var6),
               0.1F + 0.9F * var6,
               1.0F
            );
         }

         if (var2 != null) {
            for (int var30 = 0; var30 < var1; var30++) {
               var0.B(var2[var30], var13[var30], var13[var30 + 1], var2[var30 + 1]);
            }
         } else {
            int var29 = var0.h(var7, var4, var8, 0.0F, -1.0F, 0.0F, t(0.5F), j(0.25F), 0.1F, 1.0F);
            float[] var31 = new float[]{0.0F, -1.0F, 0.0F};
            int[] var16 = new int[var1 + 1];

            for (int var32 = 0; var32 <= var1; var32++) {
               var16[var32] = var0.F(var13[var32], var31);
            }

            for (int var33 = 0; var33 < var1; var33++) {
               var0.B(var29, var16[var33], var16[var33 + 1], var16[var33 + 1]);
            }
         }

         var2 = var13;
      }

      float var20 = k(1.0F, true);
      float var21 = k(1.0F, false);

      for (int var22 = 0; var22 < var1; var22++) {
         int var23 = var2[var22];
         int var24 = var2[var22 + 1];
         float[] var25 = var0.y(var23, var24, var20, 4.8F, var21);
         int var26 = var0.F(var23, var25);
         int var27 = var0.F(var24, var25);
         int var28 = var0.h(var20, 4.8F, var21, var25[0], var25[1], var25[2], t((var22 + 0.5F) / var1), j(1.0F), 1.0F, 1.0F);
         var0.B(var26, var28, var28, var27);
      }

      return var0.r();
   }

   static float t(float var0) {
      return (0.5F + 31.0F * var0) / 64.0F;
   }

   static float j(float var0) {
      return (0.5F + 63.0F * var0) / 64.0F;
   }

   static float M(float var0) {
      return Math.clamp((var0 - 0.9F) / 3.9F, 0.0F, 1.0F);
   }

   private static float k(float var0, boolean var1) {
      return var1 ? 0.08F * var0 : -0.24F * (float)Math.sin(Math.PI * var0);
   }

   public static float u(float var0, float var1, double var2) {
      return var0 + (var0 - 0.9F) * (float)(var2 - 1.0) * var1;
   }

   public static double n() {
      return 2.846154F;
   }

   public static void V(du.h var0, int var1, double var2, double var4, boolean var6, float[] var7) {
      float var8 = var0.positions[var1 * 3];
      float var9 = var0.positions[var1 * 3 + 1];
      float var10 = var0.positions[var1 * 3 + 2];
      float var11 = var0.stretch[var1];
      float var12 = (float)var4;
      float var13 = var6 ? -1.0F : 1.0F;
      int var14 = var11 > 0.0F ? 1 : 0;
      float var15 = var14 != 0 ? 1.0F + (float)(var2 - 1.0) * var11 : var12;
      var7[0] = var8 * var12 * var13;
      var7[1] = var14 != 0 ? u(var9, var11, var2) : var9 * var12;
      var7[2] = var10 * var12;
      float var16 = var0.normals[var1 * 3] / var12 * var13;
      float var17 = var0.normals[var1 * 3 + 1] / var15;
      float var18 = var0.normals[var1 * 3 + 2] / var12;
      float var19 = (float)Math.sqrt(var16 * var16 + var17 * var17 + var18 * var18);
      var7[3] = var16 / var19;
      var7[4] = var17 / var19;
      var7[5] = var18 / var19;
   }

   public static int W(int var0, int var1, float var2) {
      float var3 = Math.clamp((var2 - 0.1F) / 0.9F, 0.0F, 1.0F);
      int var4 = -16777216;

      for (byte var5 = 16; var5 >= 0; var5 -= 8) {
         int var6 = var0 >> var5 & 0xFF;
         int var7 = var1 >> var5 & 0xFF;
         var4 |= Math.round(var6 + (var7 - var6) * var3) << var5;
      }

      return var4;
   }

   public static float e(float var0) {
      float var1 = Math.clamp((var0 - 0.45F) / 0.55F, 0.0F, 1.0F);
      return var1 * var1 * (3.0F - 2.0F * var1);
   }

   public static int d(int var0, double var1, float var3) {
      float var4 = (float)(Math.clamp(var1, 0.0, 1.0) * e(var3) * 2.2);
      int var5 = -16777216;

      for (byte var6 = 16; var6 >= 0; var6 -= 8) {
         var5 |= Math.min(255, Math.round((var0 >> var6 & 0xFF) * var4)) << var6;
      }

      return var5;
   }

   public static int[] p() {
      byte var0 = 64;
      int var1 = var0 / 2;
      int var2 = var0 / 4;
      int[] var3 = new int[var0 * var0];

      for (int var4 = 0; var4 < var0; var4++) {
         for (int var5 = 0; var5 < var1; var5++) {
            float var6;
            float var7;
            if (var4 < var2) {
               var6 = 0.8F + 0.07F * f(var5, var4, 5);
               var7 = 0.0F;
            } else {
               float var8 = (var4 - var2 + 0.5F) / (var0 - var2);
               float var9 = (var5 + 0.5F) / var1;
               float var10 = 0.0F;

               for (int var11 = 0; var11 < 3; var11++) {
                  float var12 = (float)(var11 * 0.33 + 0.12 + 0.05 * Math.sin(var8 * 9.0F + var11 * 2.1));
                  float var13 = Math.abs(var9 - (var12 - (float)Math.floor(var12)));
                  var13 = Math.min(var13, 1.0F - var13) * var1;
                  float var14 = (float)(0.55 + 0.45 * Math.sin(var8 * 13.0F + var11 * 1.7));
                  var10 += (float)Math.exp(-var13 * var13 / 1.2) * var14;
               }

               var10 = Math.min(1.0F, var10);
               var6 = Math.min(1.0F, 0.86F + 0.05F * f(var5, var4, 9) + 0.2F * var10);
               var7 = (float)Math.pow(Math.clamp((var8 - 0.45F) / 0.55F, 0.0F, 1.0F), 1.5) * (0.35F + 0.65F * var10);
            }

            var3[var4 * var0 + var5] = L(var6);
            var3[var4 * var0 + var5 + var1] = L(var7);
         }
      }

      return var3;
   }

   static float f(int var0, int var1, int var2) {
      int var3 = var0 * 374761393 + var1 * 668265263 + var2 * Integer.MAX_VALUE;
      var3 = (var3 ^ var3 >>> 13) * 1274126177;
      var3 ^= var3 >>> 16;
      return (var3 & 65535) / 32767.5F - 1.0F;
   }

   private static int L(float var0) {
      int var1 = Math.round(Math.clamp(var0, 0.0F, 1.0F) * 255.0F);
      return 0xFF000000 | var1 << 16 | var1 << 8 | var1;
   }

   public record h(float[] positions, float[] normals, float[] uvs, float[] gradient, float[] stretch, int[] quads) {
      public int vertices() {
         return this.gradient.length;
      }

      public int faces() {
         return this.quads.length / 4;
      }
   }

   private static final class u {
      private float[] z = new float[384];
      private float[] M = new float[384];
      private float[] u = new float[256];
      private float[] e = new float[128];
      private float[] H = new float[128];
      private int[] d = new int[512];
      private int g;
      private int q;

      int h(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
         if (this.g == this.e.length) {
            this.z = Arrays.copyOf(this.z, this.g * 6);
            this.M = Arrays.copyOf(this.M, this.g * 6);
            this.u = Arrays.copyOf(this.u, this.g * 4);
            this.e = Arrays.copyOf(this.e, this.g * 2);
            this.H = Arrays.copyOf(this.H, this.g * 2);
         }

         float var11 = (float)Math.sqrt(var4 * var4 + var5 * var5 + var6 * var6);
         if (var11 < 1.0E-6F) {
            var4 = 0.0F;
            var5 = 1.0F;
            var6 = 0.0F;
            var11 = 1.0F;
         }

         this.z[this.g * 3] = var1;
         this.z[this.g * 3 + 1] = var2;
         this.z[this.g * 3 + 2] = var3;
         this.M[this.g * 3] = var4 / var11;
         this.M[this.g * 3 + 1] = var5 / var11;
         this.M[this.g * 3 + 2] = var6 / var11;
         this.u[this.g * 2] = var7;
         this.u[this.g * 2 + 1] = var8;
         this.e[this.g] = var9;
         this.H[this.g] = var10;
         return this.g++;
      }

      int F(int var1, float[] var2) {
         return this.h(
            this.z[var1 * 3],
            this.z[var1 * 3 + 1],
            this.z[var1 * 3 + 2],
            var2[0],
            var2[1],
            var2[2],
            this.u[var1 * 2],
            this.u[var1 * 2 + 1],
            this.e[var1],
            this.H[var1]
         );
      }

      float[] y(int var1, int var2, float var3, float var4, float var5) {
         float var6 = this.z[var1 * 3];
         float var7 = this.z[var1 * 3 + 1];
         float var8 = this.z[var1 * 3 + 2];
         float var9 = var3 - var6;
         float var10 = var4 - var7;
         float var11 = var5 - var8;
         float var12 = this.z[var2 * 3] - var6;
         float var13 = this.z[var2 * 3 + 1] - var7;
         float var14 = this.z[var2 * 3 + 2] - var8;
         return new float[]{var10 * var14 - var11 * var13, var11 * var12 - var9 * var14, var9 * var13 - var10 * var12};
      }

      void B(int var1, int var2, int var3, int var4) {
         if (this.q * 4 == this.d.length) {
            this.d = Arrays.copyOf(this.d, this.d.length * 2);
         }

         this.d[this.q * 4] = var1;
         this.d[this.q * 4 + 1] = var2;
         this.d[this.q * 4 + 2] = var3;
         this.d[this.q * 4 + 3] = var4;
         this.q++;
      }

      void q(float var1, float var2, float var3, float var4, float var5, float var6, int var7, int var8, int var9) {
         int[][] var10 = new int[var8 + 1][var7 + 1];

         for (int var11 = 0; var11 <= var8; var11++) {
            double var12 = Math.PI * var11 / var8;

            for (int var14 = 0; var14 <= var7; var14++) {
               double var15 = (Math.PI * 2) * var14 / var7;
               float var17 = var11 != 0 && var11 != var8 ? 1.0F + 0.07F * du.f(var11, var14 % var7, var9) : 1.0F;
               float var18 = (float)(Math.sin(var12) * Math.cos(var15));
               float var19 = (float)(-Math.cos(var12));
               float var20 = (float)(Math.sin(var12) * Math.sin(var15));
               var10[var11][var14] = this.h(
                  var1 + var18 * var4 * var17,
                  var2 + var19 * var5 * var17,
                  var3 + var20 * var6 * var17,
                  var18 / var4,
                  var19 / var5,
                  var20 / var6,
                  du.t((float)var14 / var7),
                  du.j(0.25F * var11 / var8),
                  0.1F * var11 / var8,
                  0.0F
               );
            }
         }

         for (int var21 = 0; var21 < var8; var21++) {
            for (int var22 = 0; var22 < var7; var22++) {
               this.B(var10[var21][var22], var10[var21 + 1][var22], var10[var21 + 1][var22 + 1], var10[var21][var22 + 1]);
            }
         }
      }

      du.h r() {
         return new du.h(
            Arrays.copyOf(this.z, this.g * 3),
            Arrays.copyOf(this.M, this.g * 3),
            Arrays.copyOf(this.u, this.g * 2),
            Arrays.copyOf(this.e, this.g),
            Arrays.copyOf(this.H, this.g),
            Arrays.copyOf(this.d, this.q * 4)
         );
      }
   }
}
