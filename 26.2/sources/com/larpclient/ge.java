package com.larpclient;

import java.util.Arrays;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ge {
   public static final float H = 0.303F;
   public static final float f = 0.008F;
   public static final float K = -0.062F;
   public static final float G = 0.06F;
   private static final float W = 0.082F;
   private static final float c = 8.0F;
   private static final int b = 72;
   private static final int i = 12;
   private static final int B = 12;
   private static final int E = 4;
   private static final float r = 0.2F;
   private static final float P = 0.125F;
   private static final byte J = 0;
   private static final byte F = 1;
   private static final byte g = 2;
   private static final byte w = 3;
   private static final byte e = 4;
   private static final byte z = 5;
   private static final int[] D = new int[]{0, 0, 0, 14686268, 2053887, 1097840};
   private static final int S = 128;
   private static final float[][] O = new float[][]{
      {-1.0F, 124.0F, 78.0F, 20.0F},
      {-0.4F, 198.0F, 142.0F, 44.0F},
      {-0.08F, 152.0F, 100.0F, 28.0F},
      {0.1F, 244.0F, 198.0F, 96.0F},
      {0.42F, 255.0F, 234.0F, 164.0F},
      {0.75F, 236.0F, 184.0F, 78.0F},
      {1.0F, 255.0F, 230.0F, 152.0F}
   };
   private static final float[] L = new float[128];
   private static final float[] U = new float[128];
   private static final float[] I = new float[128];
   private static final float[] V = new float[256];
   private static final ge.z Y = i();

   private ge() {
   }

   public static ge.z H() {
      return Y;
   }

   public static void P(d var0, Matrix4f var1, double var2, double var4, boolean var6, double var7) {
      float var9 = (float)var2;
      float var10 = (float)Math.clamp(var7, 0.0, 1.0);
      int var11 = (int)(Math.clamp(var4, 0.0, 1.0) * 255.0) << 24;
      Vector3f var12 = var0.viewer(var1, new Vector3f());
      float var13 = var12.x;
      float var14 = -var12.y - 0.5F - 0.08F * var9;
      float var15 = var12.z;
      float var16 = (float)Math.sqrt(var13 * var13 + var14 * var14 + var15 * var15);
      if (var16 < 1.0E-5F) {
         var13 = 0.0F;
         var14 = 0.0F;
         var15 = -1.0F;
         var16 = 1.0F;
      }

      var13 /= var16;
      var14 /= var16;
      var15 /= var16;
      ge.z var17 = Y;
      float[] var18 = var17.points;
      float[] var19 = var17.normals;
      float[] var20 = var17.shade;
      byte[] var21 = var17.material;
      boolean[] var22 = var17.slotGem;
      var0.blend(gl.c.K);
      var0.solid(var4 >= 0.995);

      try {
         int var23 = 0;

         for (int var24 = var20.length; var23 < var24; var23++) {
            if (!var6 || !var22[var23]) {
               int var25 = var23 * 3;
               int var26 = P(var19[var25], var19[var25 + 1], var19[var25 + 2], var13, var14, var15, var20[var23], var21[var23], var10);
               var0.modelVertex(var1, var18[var25] * var9, -0.5F - var18[var25 + 1] * var9, var18[var25 + 2] * var9, var11 | var26);
            }
         }
      } finally {
         var0.solid(false);
      }
   }

   private static int P(float var0, float var1, float var2, float var3, float var4, float var5, float var6, byte var7, float var8) {
      float var9 = var0 * var3 + var1 * var4 + var2 * var5;
      float var10 = 2.0F * var9 * var0 - var3;
      float var11 = 2.0F * var9 * var1 - var4;
      float var12 = 2.0F * var9 * var2 - var5;
      float var13 = var10 * 0.42F + var11 * 0.78F + var12 * -0.46F;
      float var14 = V[Math.clamp((int)(var13 * 255.0F), 0, 255)];
      float var15;
      float var16;
      float var17;
      switch (var7) {
         case 0:
            int var22 = Math.clamp((int)((var11 * 0.7F + var1 * 0.3F + 1.0F) * 0.5F * 127.0F), 0, 127);
            var15 = L[var22] * var6 + var14 * 255.0F;
            var16 = U[var22] * var6 + var14 * 238.0F;
            var17 = I[var22] * var6 + var14 * 190.0F;
            break;
         case 1:
            float var21 = 0.84F + 0.16F * Math.max(0.0F, var1);
            float var25 = 1.0F - Math.abs(var9);
            var15 = (255.0F * var21 + 8.0F * var25) * var6 + var14 * 255.0F;
            var16 = (247.0F * var21 - 10.0F * var25) * var6 + var14 * 255.0F;
            var17 = (232.0F * var21 + 6.0F * var25) * var6 + var14 * 255.0F;
            break;
         case 2:
            float var20 = 0.5F + 0.5F * Math.max(0.0F, var1);
            float var23 = 1.0F - Math.abs(var9);
            var23 *= var23;
            var15 = (58.0F * var20 + 54.0F * var23) * var6;
            var16 = (10.0F * var20 + 18.0F * var23) * var6;
            var17 = (84.0F * var20 + 70.0F * var23) * var6;
            break;
         default:
            int var18 = D[var7];
            float var19 = 0.55F + 0.75F * Math.max(0.0F, var9);
            var15 = (var18 >> 16 & 0xFF) * var19 * var6 + var14 * 255.0F;
            var16 = (var18 >> 8 & 0xFF) * var19 * var6 + var14 * 255.0F;
            var17 = (var18 & 0xFF) * var19 * var6 + var14 * 255.0F;
      }

      return Math.min((int)(var15 * var8), 255) << 16 | Math.min((int)(var16 * var8), 255) << 8 | Math.min((int)(var17 * var8), 255);
   }

   private static double[] x(double var0) {
      double var2 = Math.abs(Math.cos(var0));
      double var4 = Math.abs(Math.sin(var0));
      double var6 = 1.0 / Math.pow(Math.pow(var2, 8.0) + Math.pow(var4, 8.0), 0.125);
      double var8 = var6 * Math.sin(var0);
      double var10 = -var6 * Math.cos(var0);
      double var12 = Math.signum(var8) * Math.pow(Math.abs(var8), 7.0);
      double var14 = Math.signum(var10) * Math.pow(Math.abs(var10), 7.0);
      double var16 = Math.hypot(var12, var14);
      return new double[]{var8, var10, var12 / var16, var14 / var16};
   }

   private static ge.z i() {
      ge.p var0 = new ge.p();
      s(var0);
      O(var0);
      h(var0);

      for (int var1 = 0; var1 < 4; var1++) {
         double var2 = var1 * Math.PI / 2.0;
         double[] var4 = x(var2);
         double var5 = var4[2];
         double var7 = var4[3];
         double var9 = var4[0] * 0.311F;
         double var11 = var4[1] * 0.311F;
         O(var0, var9, 0.0, var11, var5, var7, 0.0705, 0.0085, 22, 4);
         o(var0, var9 + var5 * 0.002, 0.0, var11 + var7 * 0.002, var5, var7, 0.05, (byte)(var1 % 2 == 0 ? 3 : 4), true);
         double var13 = var2 + (Math.PI / 4);
         double[] var15 = x(var13);
         double var16 = var15[0] * 0.311F;
         double var18 = var15[1] * 0.311F;
         O(var0, var16, 0.0, var18, var15[2], var15[3], 0.036, 0.0065, 14, 3);
         o(var0, var16 + var15[2] * 0.001, 0.0, var18 + var15[3] * 0.001, var15[2], var15[3], 0.032, (byte)(var1 % 2 == 0 ? 5 : 3), false);
      }

      return var0.K();
   }

   private static void s(ge.p var0) {
      double[][] var1 = new double[][]{
         {-0.012, -0.086, 0.55},
         {0.004, -0.092, 0.75},
         {0.016, -0.086, 1.0},
         {0.019, -0.074, 1.0},
         {0.012, -0.066, 0.7},
         {0.008F, -0.062F, 0.68},
         {0.008F, 0.06F, 0.72},
         {0.013, 0.066, 0.78},
         {0.019, 0.074, 1.0},
         {0.016, 0.08, 1.0},
         {0.004, 0.082F, 0.85},
         {-0.012, 0.078, 0.6}
      };
      int var2 = var1.length;
      double[][] var3 = new double[var2][2];

      for (int var4 = 0; var4 < var2; var4++) {
         double[] var5 = var1[(var4 + var2 - 1) % var2];
         double[] var6 = var1[(var4 + 1) % var2];
         double var7 = var6[0] - var5[0];
         double var9 = var6[1] - var5[1];
         double var11 = Math.hypot(var7, var9);
         var3[var4][0] = var9 / var11;
         var3[var4][1] = -var7 / var11;
      }

      for (int var13 = 0; var13 < 72; var13++) {
         double[] var14 = x(var13 * Math.PI * 2.0 / 72.0);
         double[] var15 = x((var13 + 1) * Math.PI * 2.0 / 72.0);

         for (int var16 = 0; var16 < var2; var16++) {
            int var8 = (var16 + 1) % var2;
            T(var0, var14, var1[var16], var3[var16]);
            T(var0, var15, var1[var16], var3[var16]);
            T(var0, var15, var1[var8], var3[var8]);
            T(var0, var14, var1[var8], var3[var8]);
         }
      }
   }

   private static void T(ge.p var0, double[] var1, double[] var2, double[] var3) {
      var0.v(
         var1[0] * 0.303F + var1[2] * var2[0],
         var2[1],
         var1[1] * 0.303F + var1[3] * var2[0],
         var1[2] * var3[0],
         var3[1],
         var1[3] * var3[0],
         var2[2],
         (byte)0,
         false
      );
   }

   private static void O(ge.p var0) {
      byte var1 = 5;
      byte var2 = 32;
      double[][][] var3 = new double[var1 + 1][var2 + 1][];

      for (int var4 = 0; var4 <= var1; var4++) {
         for (int var5 = 0; var5 <= var2; var5++) {
            double var6 = (double)var4 / var1;
            double var8 = var5 * Math.PI * 2.0 / var2;
            double[] var10 = x(var8);
            double var11 = 0.2950000030994415 * Math.sin(var6 * Math.PI / 2.0);
            var3[var4][var5] = new double[]{var10[0] * var11, 0.05 + 0.12 * Math.cos(var6 * Math.PI / 2.0), var10[1] * var11};
         }
      }

      for (int var24 = 0; var24 < var1; var24++) {
         for (int var25 = 0; var25 < var2; var25++) {
            int[][] var26 = new int[][]{{var24, var25}, {var24 + 1, var25}, {var24 + 1, var25 + 1}, {var24, var25 + 1}};

            for (int[] var28 : var26) {
               double[] var29 = var3[var28[0]][var28[1]];
               double[] var12 = var3[Math.max(var28[0] - 1, 0)][var28[1]];
               double[] var13 = var3[Math.min(var28[0] + 1, var1)][var28[1]];
               double[] var14 = var3[var28[0]][(var28[1] + var2 - 1) % var2];
               double[] var15 = var3[var28[0]][(var28[1] + 1) % var2];
               double[] var16 = new double[]{var13[0] - var12[0], var13[1] - var12[1], var13[2] - var12[2]};
               double[] var17 = new double[]{var15[0] - var14[0], var15[1] - var14[1], var15[2] - var14[2]};
               double var18 = var16[1] * var17[2] - var16[2] * var17[1];
               double var20 = var16[2] * var17[0] - var16[0] * var17[2];
               double var22 = var16[0] * var17[1] - var16[1] * var17[0];
               if (var20 < 0.0) {
                  var18 = -var18;
                  var20 = -var20;
                  var22 = -var22;
               }

               if (var28[0] == 0) {
                  var18 = 0.0;
                  var20 = 1.0;
                  var22 = 0.0;
               }

               var0.v(var29[0], var29[1], var29[2], var18, var20, var22, 1.0, (byte)2, false);
            }
         }
      }

      W(var0, 0.0, 0.19, 0.0, 0.027, 8, 5, (byte)0);
   }

   private static double i(int var0) {
      return var0 * Math.PI * 2.0 / 12.0;
   }

   private static double o(int var0) {
      return var0 % 3 == 0 ? 0.2F : 0.125;
   }

   private static double r(double var0, int var2) {
      double var3 = Math.IEEEremainder(var0 - i(var2), Math.PI * 2);
      return Math.abs(var3) / (Math.PI / 12);
   }

   private static int L(double var0) {
      return Math.floorMod(Math.round((float)(var0 / (Math.PI * 2) * 12.0)), 12);
   }

   private static double[] H(double var0, double var2, double var4) {
      int var6 = L(var0);
      double var7 = Math.min(r(var0, var6), 1.0);
      double var9 = 0.014 + o(var6) * Math.pow(1.0 - var7, 1.3);
      double var11 = var9 * var2;
      double var13 = var11 / 0.2F;
      double var15 = 0.0055 * Math.pow(Math.max(0.0, 1.0 - var7 * 2.4), 2.0) * Math.min(1.0, var2 * 4.0);
      double[] var17 = x(var0);
      double var18 = 0.002 + 0.034 * var13 * var13 + var15 + var4;
      return new double[]{var17[0] * 0.303F + var17[2] * var18, 0.0700000022649765 + var11, var17[1] * 0.303F + var17[3] * var18};
   }

   private static double[] w(double var0, double var2, double var4) {
      double var6 = 0.001;
      double[] var8 = H(var0 - var6, var2, var4);
      double[] var9 = H(var0 + var6, var2, var4);
      double[] var10 = H(var0, Math.max(0.0, var2 - var6), var4);
      double[] var11 = H(var0, Math.min(1.0, var2 + var6), var4);
      double[] var12 = new double[]{var9[0] - var8[0], var9[1] - var8[1], var9[2] - var8[2]};
      double[] var13 = new double[]{var11[0] - var10[0], var11[1] - var10[1], var11[2] - var10[2]};
      double var14 = var12[1] * var13[2] - var12[2] * var13[1];
      double var16 = var12[2] * var13[0] - var12[0] * var13[2];
      double var18 = var12[0] * var13[1] - var12[1] * var13[0];
      double[] var20 = x(var0);
      if (var14 * var20[2] + var18 * var20[3] < 0.0) {
         var14 = -var14;
         var16 = -var16;
         var18 = -var18;
      }

      return new double[]{var14, var16, var18};
   }

   private static void h(ge.p var0) {
      short var1 = 144;
      double var2 = -0.016;

      for (int var4 = 0; var4 < var1; var4++) {
         double var5 = var4 * Math.PI * 2.0 / var1;
         double var7 = (var4 + 1) * Math.PI * 2.0 / var1;

         for (int var9 = 0; var9 < 4; var9++) {
            double var10 = var9 / 4.0;
            double var12 = (var9 + 1) / 4.0;
            W(var0, var5, var10, 0.0, 1.0);
            W(var0, var7, var10, 0.0, 1.0);
            W(var0, var7, var12, 0.0, 1.0);
            W(var0, var5, var12, 0.0, 1.0);
         }

         for (int var17 = 0; var17 < 1; var17++) {
            double var19 = var17;
            double var21 = var17 + 1;
            W(var0, var5, var19, var2, -1.0);
            W(var0, var7, var19, var2, -1.0);
            W(var0, var7, var21, var2, -1.0);
            W(var0, var5, var21, var2, -1.0);
         }

         for (double[] var22 : new double[][]{{var5, 0.0}, {var7, 0.0}, {var7, var2}, {var5, var2}}) {
            double[] var13 = H(var22[0], 1.0, var22[1]);
            var0.v(var13[0], var13[1], var13[2], x(var22[0])[2] * 0.3, 1.0, x(var22[0])[3] * 0.3, 1.0, (byte)0, false);
         }
      }

      for (int var14 = 0; var14 < 12; var14++) {
         double[] var15 = H(i(var14), 1.0, -0.005);
         int var6 = var14 % 3 == 0 ? 1 : 0;
         double var16 = var6 != 0 ? 0.025 : 0.019;
         W(var0, var15[0], var15[1] + var16 * 0.7, var15[2], var16, var6 != 0 ? 8 : 6, var6 != 0 ? 5 : 4, (byte)1);
      }
   }

   private static void W(ge.p var0, double var1, double var3, double var5, double var7) {
      double[] var9 = H(var1, var3, var5);
      double[] var10 = w(var1, var3, var5);
      double var11 = 0.8 + 0.2 * Math.min(1.0, var3 * 2.2);
      var0.v(var9[0], var9[1], var9[2], var10[0] * var7, var10[1] * var7, var10[2] * var7, var11, (byte)0, false);
   }

   private static void W(ge.p var0, double var1, double var3, double var5, double var7, int var9, int var10, byte var11) {
      for (int var12 = 0; var12 < var10; var12++) {
         for (int var13 = 0; var13 < var9; var13++) {
            for (int[] var17 : new int[][]{{var12, var13}, {var12 + 1, var13}, {var12 + 1, var13 + 1}, {var12, var13 + 1}}) {
               double var18 = var17[0] * Math.PI / var10;
               double var20 = var17[1] * Math.PI * 2.0 / var9;
               double var22 = Math.sin(var18) * Math.cos(var20);
               double var24 = Math.cos(var18);
               double var26 = Math.sin(var18) * Math.sin(var20);
               var0.v(var1 + var22 * var7, var3 + var24 * var7, var5 + var26 * var7, var22, var24, var26, 1.0, var11, false);
            }
         }
      }
   }

   private static void O(ge.p var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15, int var16) {
      double var17 = -var9;
      double var19 = var7;

      for (int var21 = 0; var21 < var15; var21++) {
         for (int var22 = 0; var22 < var16; var22++) {
            for (int[] var26 : new int[][]{{var21, var22}, {var21 + 1, var22}, {var21 + 1, var22 + 1}, {var21, var22 + 1}}) {
               double var27 = var26[0] * Math.PI * 2.0 / var15;
               double var29 = var26[1] * Math.PI * 2.0 / var16 + (Math.PI / 4);
               double var31 = Math.cos(var27);
               double var33 = Math.sin(var27);
               double var35 = Math.cos(var29);
               double var37 = Math.sin(var29);
               double var39 = var17 * var31;
               double var41 = var19 * var31;
               double var43 = var33;
               double var45 = var39 * var35 + var7 * var37;
               double var47 = var43 * var35;
               double var49 = var41 * var35 + var9 * var37;
               double var51 = var11 + var35 * var13;
               var0.v(
                  var1 + var39 * var51 + var7 * (var37 * var13 + 0.004),
                  var3 + var43 * var51,
                  var5 + var41 * var51 + var9 * (var37 * var13 + 0.004),
                  var45,
                  var47,
                  var49,
                  1.0,
                  (byte)0,
                  false
               );
            }
         }
      }
   }

   private static void o(ge.p var0, double var1, double var3, double var5, double var7, double var9, double var11, byte var13, boolean var14) {
      byte var15 = 12;
      byte var16 = 3;
      double var17 = -var9;
      double var19 = var7;

      for (int var21 = 0; var21 < var16; var21++) {
         for (int var22 = 0; var22 < var15; var22++) {
            for (int[] var26 : new int[][]{{var21, var22}, {var21 + 1, var22}, {var21 + 1, var22 + 1}, {var21, var22 + 1}}) {
               double var27 = var26[0] * Math.PI / 2.0 / var16;
               double var29 = var26[1] * Math.PI * 2.0 / var15;
               double var31 = Math.sin(var27);
               double var33 = Math.cos(var27);
               double var35 = Math.cos(var29) * var31;
               double var37 = Math.sin(var29) * var31;
               double var39 = var17 * var35 + var7 * var33;
               double var41 = var37;
               double var43 = var19 * var35 + var9 * var33;
               var0.v(var1 + var39 * var11, var3 + var41 * var11, var5 + var43 * var11, var39, var41, var43, 1.0, var13, var14);
            }
         }
      }
   }

   static {
      for (int var0 = 0; var0 < 128; var0++) {
         float var1 = var0 / 127.0F * 2.0F - 1.0F;
         int var2 = 0;

         while (var2 < O.length - 2 && var1 > O[var2 + 1][0]) {
            var2++;
         }

         float[] var3 = O[var2];
         float[] var4 = O[var2 + 1];
         float var5 = Math.clamp((var1 - var3[0]) / (var4[0] - var3[0]), 0.0F, 1.0F);
         var5 = var5 * var5 * (3.0F - 2.0F * var5);
         L[var0] = var3[1] + (var4[1] - var3[1]) * var5;
         U[var0] = var3[2] + (var4[2] - var3[2]) * var5;
         I[var0] = var3[3] + (var4[3] - var3[3]) * var5;
      }

      for (int var6 = 0; var6 < V.length; var6++) {
         V[var6] = (float)Math.pow(var6 / 255.0, 26.0);
      }
   }

   private static final class p {
      float[] B = new float[48000];
      float[] K = new float[48000];
      float[] I = new float[16000];
      byte[] k = new byte[16000];
      boolean[] i = new boolean[16000];
      int F;

      void v(double var1, double var3, double var5, double var7, double var9, double var11, double var13, byte var15, boolean var16) {
         if (this.F == this.I.length) {
            this.B = Arrays.copyOf(this.B, this.F * 6);
            this.K = Arrays.copyOf(this.K, this.F * 6);
            this.I = Arrays.copyOf(this.I, this.F * 2);
            this.k = Arrays.copyOf(this.k, this.F * 2);
            this.i = Arrays.copyOf(this.i, this.F * 2);
         }

         double var17 = Math.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
         if (var17 < 1.0E-9) {
            var9 = 1.0;
            var17 = 1.0;
         }

         this.B[this.F * 3] = (float)var1;
         this.B[this.F * 3 + 1] = (float)var3;
         this.B[this.F * 3 + 2] = (float)var5;
         this.K[this.F * 3] = (float)(var7 / var17);
         this.K[this.F * 3 + 1] = (float)(var9 / var17);
         this.K[this.F * 3 + 2] = (float)(var11 / var17);
         this.I[this.F] = (float)var13;
         this.k[this.F] = var15;
         this.i[this.F++] = var16;
      }

      ge.z K() {
         float var1 = 0.0F;

         for (byte var2 = 1; var2 < this.F * 3; var2 += 3) {
            var1 = Math.max(var1, this.B[var2]);
         }

         return new ge.z(
            Arrays.copyOf(this.B, this.F * 3),
            Arrays.copyOf(this.K, this.F * 3),
            Arrays.copyOf(this.I, this.F),
            Arrays.copyOf(this.k, this.F),
            Arrays.copyOf(this.i, this.F),
            var1
         );
      }
   }

   public record z(float[] points, float[] normals, float[] shade, byte[] material, boolean[] slotGem, float rise) {
      public int quads() {
         return this.shade.length / 4;
      }
   }
}
