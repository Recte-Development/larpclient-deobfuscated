package com.larpclient;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class hm {
   public static final float M = 0.262F;
   public static final float w = 0.0115F;
   public static final float d = 0.155F;
   private static final float A = 0.012F;
   private static final float l = (float)Math.tan(Math.toRadians(8.0));
   public static final float H = 0.15F;
   public static final float N = 0.155F + 0.262F * l + 0.0115F + 0.09F;
   private static final int g = 96;
   private static final int W = 10;
   private static final int e = 48;
   private static final int p = 12;
   private static final int n = 16776168;
   private static final int S = 16765034;
   private static final int u = 16764757;
   private static final int m = 16757294;
   private static final int Y = 16748316;
   private static final float[] R = new float[]{0.0F, 0.012F, 0.026F, 0.046F};
   private static final float[] v = new float[]{0.62F, 0.36F, 0.12F, 0.0F};
   private static final float[] t = new float[]{0.0F, 0.22F, 0.48F, 0.75F, 1.0F};
   private static final float[] c = new float[]{1.0F, 0.72F, 0.32F, 0.08F, 0.0F};
   private static final float[] Q = new float[291];
   private static final float[] o = new float[291];
   private static final float[] r = new float[3201];
   private static final int[] J = new int[1067];
   private static final float[] z = new float[13];
   private static final float[] D = new float[13];
   private static final float[] I = new float[291];

   private hm() {
   }

   public static void H(ia var0, Matrix4f var1, double var2, double var4, double var6) {
      float var8 = (float)var2;
      float var9 = (float)Math.clamp(var4, 0.0, 1.0);
      s(var0, var1, var8, var9);
      Vector3f var10 = var0.viewer(var1, new Vector3f());
      float var11 = 1.0F + 0.07F * (float)Math.sin(var6 * 1.5);
      var0.blend(_0.v.z);
      W(var0, var1, var8, var9 * var11, var6, var10);
      var0.blend(_0.v.c);
      d(var0, var1, var8, var9 * var11, var6, var10);
      var0.blend(_0.v.z);
   }

   public static void k(ia var0, Matrix4f var1, double var2, double var4, double var6) {
      float var8 = (float)var2;
      float var9 = (float)Math.clamp(var4, 0.0, 1.0);
      s(var0, var1, var8, var9);
      var0.blend(_0.v.c);
      d(var0, var1, var8, var9, var6, var0.viewer(var1, new Vector3f()));
      var0.blend(_0.v.z);
   }

   private static void s(ia var0, Matrix4f var1, float var2, float var3) {
      int var4 = (int)(var3 * 255.0F) << 24;
      var0.blend(_0.v.z);
      var0.solid(var3 >= 0.995);

      try {
         for (int var5 = 0; var5 < 96; var5++) {
            for (int var6 = 0; var6 < 10; var6++) {
               T(var0, var1, var2, var4, var5, var6);
               T(var0, var1, var2, var4, var5 + 1, var6);
               T(var0, var1, var2, var4, var5 + 1, var6 + 1);
               T(var0, var1, var2, var4, var5, var6 + 1);
            }
         }
      } finally {
         var0.solid(false);
      }
   }

   private static void T(ia var0, Matrix4f var1, float var2, int var3, int var4, int var5) {
      int var6 = var4 * 11 + var5;
      int var7 = var6 * 3;
      var0.modelVertex(var1, r[var7] * var2, -0.5F - r[var7 + 1] * var2, r[var7 + 2] * var2, var3 | J[var6]);
   }

   private static float H(int var0, double var1) {
      double var3 = var0 * Math.PI * 2.0 / 96.0;
      return 1.0F + 0.2F * (float)Math.sin(var3 * 3.0 - var1 * 0.9) * (float)Math.sin(var3 * 2.0 + var1 * 0.55);
   }

   private static void d(ia var0, Matrix4f var1, float var2, float var3, double var4, Vector3f var6) {
      for (int var7 = 0; var7 <= 96; var7++) {
         float var8 = Q[var7 * 3] * var2;
         float var9 = -0.5F - Q[var7 * 3 + 1] * var2;
         float var10 = Q[var7 * 3 + 2] * var2;
         float var11 = var6.x - var8;
         float var12 = var6.y - var9;
         float var13 = var6.z - var10;
         float var14 = o[var7 * 3];
         float var15 = -o[var7 * 3 + 1];
         float var16 = o[var7 * 3 + 2];
         float var17 = var15 * var13 - var16 * var12;
         float var18 = var16 * var11 - var14 * var13;
         float var19 = var14 * var12 - var15 * var11;
         float var20 = (float)Math.sqrt(var17 * var17 + var18 * var18 + var19 * var19);
         if (var20 < 1.0E-6F) {
            var17 = 0.0F;
            var18 = 1.0F;
            var19 = 0.0F;
            var20 = 1.0F;
         }

         I[var7 * 3] = var17 / var20;
         I[var7 * 3 + 1] = var18 / var20;
         I[var7 * 3 + 2] = var19 / var20;
      }

      for (int var21 = 0; var21 < 96; var21++) {
         float var22 = I[var21 * 3] * I[var21 * 3 + 3] + I[var21 * 3 + 1] * I[var21 * 3 + 4] + I[var21 * 3 + 2] * I[var21 * 3 + 5] < 0.0F ? -1.0F : 1.0F;
         float var23 = var3 * H(var21, var4);

         for (int var24 = 0; var24 < R.length - 1; var24++) {
            float var25 = R[var24] * var2;
            float var26 = R[var24 + 1] * var2;
            int var27 = I(16776168, 16748316, var24 / 3.0F, v[var24] * var23);
            int var28 = I(16776168, 16748316, (var24 + 1) / 3.0F, v[var24 + 1] * var23);

            for (byte var29 = -1; var29 <= 1; var29 += 2) {
               L(var0, var1, var2, var21, var29 * var25, 1.0F, var27);
               L(var0, var1, var2, var21, var29 * var26, 1.0F, var28);
               L(var0, var1, var2, var21 + 1, var29 * var26, var22, var28);
               L(var0, var1, var2, var21 + 1, var29 * var25, var22, var27);
            }
         }
      }
   }

   private static void L(ia var0, Matrix4f var1, float var2, int var3, float var4, float var5, int var6) {
      float var7 = var4 * var5;
      var0.modelVertex(
         var1,
         Q[var3 * 3] * var2 + I[var3 * 3] * var7,
         -0.5F - Q[var3 * 3 + 1] * var2 + I[var3 * 3 + 1] * var7,
         Q[var3 * 3 + 2] * var2 + I[var3 * 3 + 2] * var7,
         var6
      );
   }

   private static void W(ia var0, Matrix4f var1, float var2, float var3, double var4, Vector3f var6) {
      float var7 = 0.0F;
      float var8 = -0.5F - 0.155F * var2;
      float var9 = 0.012F * var2;
      float var10 = var6.x - var7;
      float var11 = var6.y - var8;
      float var12 = var6.z - var9;
      float var13 = (float)Math.sqrt(var10 * var10 + var11 * var11 + var12 * var12);
      if (!(var13 < 1.0E-5F)) {
         var10 /= var13;
         var11 /= var13;
         var12 /= var13;
         float var14 = -var12;
         float var15 = var10;
         float var16 = (float)Math.sqrt(var14 * var14 + var15 * var15);
         if (var16 < 1.0E-4F) {
            var14 = 1.0F;
            var15 = 0.0F;
            var16 = 1.0F;
         }

         var14 /= var16;
         var15 /= var16;
         float var17 = -var11 * var15;
         float var18 = var12 * var14 - var10 * var15;
         float var19 = var11 * var14;

         for (int var20 = 0; var20 < 48; var20++) {
            int var21 = var20 * 96 / 48;
            float var22 = Q[var21 * 3] * var2;
            float var23 = -0.5F - Q[var21 * 3 + 1] * var2;
            float var24 = Q[var21 * 3 + 2] * var2;
            float var25 = var3 * H(var21, var4);
            H(var0, var1, var22, var23, var24, 0.15F * var2, 0.085F * var25, 16757294, 16748316, var14, var15, var17, var18, var19);
            H(var0, var1, var22, var23, var24, 0.0675F * var2, 0.2F * var25, 16764757, 16757294, var14, var15, var17, var18, var19);
         }
      }
   }

   private static void H(
      ia var0,
      Matrix4f var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      int var7,
      int var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13
   ) {
      for (int var14 = 0; var14 < t.length - 1; var14++) {
         float var15 = t[var14] * var5;
         float var16 = t[var14 + 1] * var5;
         int var17 = I(var7, var8, t[var14], var6 * c[var14]);
         int var18 = I(var7, var8, t[var14 + 1], var6 * c[var14 + 1]);

         for (int var19 = 0; var19 < 12; var19++) {
            y(var0, var1, var2, var3, var4, var15, var19, var9, var10, var11, var12, var13, var17);
            y(var0, var1, var2, var3, var4, var16, var19, var9, var10, var11, var12, var13, var18);
            y(var0, var1, var2, var3, var4, var16, var19 + 1, var9, var10, var11, var12, var13, var18);
            y(var0, var1, var2, var3, var4, var15, var19 + 1, var9, var10, var11, var12, var13, var17);
         }
      }
   }

   private static void y(
      ia var0, Matrix4f var1, float var2, float var3, float var4, float var5, int var6, float var7, float var8, float var9, float var10, float var11, int var12
   ) {
      float var13 = z[var6] * var5;
      float var14 = D[var6] * var5;
      var0.modelVertex(var1, var2 + var7 * var13 + var9 * var14, var3 + var10 * var14, var4 + var8 * var13 + var11 * var14, var12);
   }

   private static int I(int var0, int var1, float var2, float var3) {
      int var4 = Math.clamp((int)(var3 * 255.0F), 0, 255);
      return var4 << 24 | vv.Q(var0, var1, var2) & 16777215;
   }

   static {
      for (int var0 = 0; var0 <= 96; var0++) {
         double var1 = var0 * Math.PI * 2.0 / 96.0;
         float var3 = (float)Math.sin(var1);
         float var4 = (float)(-Math.cos(var1));
         float var5 = var3 * 0.262F;
         float var6 = var4 * 0.262F;
         float var7 = 0.155F - var6 * l;
         Q[var0 * 3] = var5;
         Q[var0 * 3 + 1] = var7;
         Q[var0 * 3 + 2] = var6 + 0.012F;
         float var8 = (float)Math.cos(var1);
         float var9 = (float)Math.sin(var1);
         float var10 = -var9 * l;
         float var11 = (float)Math.sqrt(var8 * var8 + var10 * var10 + var9 * var9);
         o[var0 * 3] = var8 / var11;
         o[var0 * 3 + 1] = var10 / var11;
         o[var0 * 3 + 2] = var9 / var11;

         for (int var12 = 0; var12 <= 10; var12++) {
            double var13 = var12 * Math.PI * 2.0 / 10.0;
            float var15 = (float)Math.cos(var13) * 0.0115F;
            float var16 = (float)Math.sin(var13) * 0.0115F;
            int var17 = (var0 * 11 + var12) * 3;
            r[var17] = var5 + var3 * var15;
            r[var17 + 1] = var7 + var16;
            r[var17 + 2] = var6 + 0.012F + var4 * var15;
            J[var17 / 3] = vv.Q(16765034, 16776168, 0.5 + 0.5 * Math.sin(var13));
         }
      }

      for (int var18 = 0; var18 <= 12; var18++) {
         double var19 = var18 * Math.PI * 2.0 / 12.0;
         z[var18] = (float)Math.cos(var19);
         D[var18] = (float)Math.sin(var19);
      }
   }
}
