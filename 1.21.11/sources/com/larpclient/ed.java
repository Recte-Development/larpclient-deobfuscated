package com.larpclient;

import java.util.Arrays;
import net.minecraft.class_4587;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class ed {
   public static final String O = "ak47";
   public static final double l = -1.0;
   public static final double H = 8.0;
   public static final double g = -1.5;
   public static final double N = -2.0;
   public static final double v = 1.2;
   public static final double B = 3.3;
   public static final double h = 0.64;
   public static final float Y = -0.11886F;
   public static final float z = -0.77511F;
   public static final float j = -0.04688F;
   public static final float D = -0.11886F;
   public static final float b = -0.19011F;
   public static final int M = 32;
   public static final int Q = 2;
   public static final int F = 12;
   public static final int U = 8;
   static final float p = 1.0F;
   static final float R = 4.5F;
   static final float q = 1.6F;
   static final float d = 14.0F;
   static final int r = 4;
   static final float Z = 7.0F;
   static final float W = 0.035F;
   private static final float w = 0.03125F;
   private static final float y = 0.0625F;
   private static final int x = 2;
   private static final int s = 4;

   private ed() {
   }

   public static float[] l(yg.e var0) {
      if (var0.parts().size() != 1) {
         throw new IllegalArgumentException("The rifle mesh has one part");
      }

      float[] var1 = var0.parts().getFirst().vertices();
      int var2 = var1.length / 15;
      float[] var3 = new float[var2 * 4 * 8];
      int var4 = 0;

      for (int var5 = 0; var5 < var2; var5++) {
         int var6 = var5 * 15;
         int[] var7 = null;
         if (var5 + 1 < var2) {
            int var8 = var6 + 15;
            if (s(var1, var6, var8) && s(var1, var6 + 10, var8 + 5)) {
               var7 = new int[]{var6, var6 + 5, var6 + 10, var8 + 10};
            } else if (s(var1, var6, var8) && s(var1, var6 + 5, var8 + 10)) {
               var7 = new int[]{var8, var8 + 5, var8 + 10, var6 + 10};
            }
         }

         if (var7 == null) {
            var7 = new int[]{var6, var6 + 5, var6 + 10, var6 + 10};
         } else {
            var5++;
         }

         float[] var14 = M(var1, var7[0], var7[1], var7[2]);

         for (int var12 : var7) {
            int var13 = var4++ * 8;
            System.arraycopy(var1, var12, var3, var13, 5);
            System.arraycopy(var14, 0, var3, var13 + 5, 3);
         }
      }

      return Arrays.copyOf(var3, var4 * 8);
   }

   private static boolean s(float[] var0, int var1, int var2) {
      return var0[var1] == var0[var2] && var0[var1 + 1] == var0[var2 + 1] && var0[var1 + 2] == var0[var2 + 2];
   }

   private static float[] M(float[] var0, int var1, int var2, int var3) {
      Vector3f var4 = new Vector3f(var0[var2] - var0[var1], var0[var2 + 1] - var0[var1 + 1], var0[var2 + 2] - var0[var1 + 2])
         .cross(var0[var3] - var0[var1], var0[var3 + 1] - var0[var1 + 1], var0[var3 + 2] - var0[var1 + 2]);
      if (var4.lengthSquared() < 1.0E-20F) {
         return new float[]{0.0F, -1.0F, 0.0F};
      }

      var4.normalize();
      return new float[]{var4.x, var4.y, var4.z};
   }

   public static void r(class_4587 var0, boolean var1, double var2, double var4, double var6, double var8) {
      if (var1) {
         var0.method_22904(-0.0625, 0.5, -0.09375);
         em.W(var0, new Quaternionf().rotationZYX((float)var6, (float)var4, (float)var2));
      } else {
         var0.method_22904(-0.125, 0.075, 0.20625);
         em.W(var0, t());
      }

      var0.method_22905((float)var8, (float)var8, (float)var8);
   }

   public static Quaternionf t() {
      float var0 = (float)Math.sin(0.64);
      float var1 = (float)Math.cos(0.64);
      return new Quaternionf().setFromNormalized(new Matrix3f(0.0F, 0.0F, 1.0F, -var1, var0, 0.0F, -var0, -var1, 0.0F));
   }

   public static int f(float var0, int var1) {
      if (!(var0 >= 0.0F)) {
         return -1;
      }

      int var2 = (int)Math.floor(var0 * 32.0F / 2.0F) - var1;
      return var2 >= 0 && var2 < 12 ? var2 : -1;
   }

   public static float t(float var0, int var1) {
      return var0 * 32.0F - var1 * 2;
   }

   public static float[] q(float var0) {
      ed.z var1 = new ed.z();
      int var2 = f(var0, 0);
      if (var2 >= 0) {
         float var3 = t(var0, var2);
         if (var3 < 1.0F) {
            float var4 = (float)Math.sqrt(1.0F - var3 / 1.0F);
            float var5 = 0.3F * var4;
            float var6 = 0.085F * var4;
            float var7 = 0.5F;
            float var8 = 1.0F;
            float var9 = -0.78511F;
            float var10 = var9 - var5;
            var1.s(
               new float[]{-var6, -0.11886F, var9, var7, 0.0F},
               new float[]{var6, -0.11886F, var9, var7, 1.0F},
               new float[]{var6, -0.11886F, var10, var8, 1.0F},
               new float[]{-var6, -0.11886F, var10, var8, 0.0F}
            );
            var1.s(
               new float[]{0.0F, -0.11886F - var6, var9, var7, 0.0F},
               new float[]{0.0F, -0.11886F + var6, var9, var7, 1.0F},
               new float[]{0.0F, -0.11886F + var6, var10, var8, 1.0F},
               new float[]{0.0F, -0.11886F - var6, var10, var8, 0.0F}
            );
            double var11 = var2 * 1.1;
            float var13 = var6 * 1.5F;
            float var14 = (float)Math.cos(var11) * var13;
            float var15 = (float)Math.sin(var11) * var13;
            float var16 = var9 - 0.02F;
            var1.s(
               new float[]{-var14 + var15, -0.11886F - var15 - var14, var16, var7, 0.0F},
               new float[]{var14 + var15, -0.11886F + var15 - var14, var16, var8, 0.0F},
               new float[]{var14 - var15, -0.11886F + var15 + var14, var16, var8, 1.0F},
               new float[]{-var14 - var15, -0.11886F - var15 + var14, var16, var7, 1.0F}
            );
         }
      }

      for (int var17 = 0; var17 < 2; var17++) {
         int var18 = f(var0, var17);
         if (var18 >= 0) {
            float var19 = t(var0, var18) * 4.5F;
            float var20 = Math.max(0.0F, var19 - 1.6F);
            if (!(var20 >= 14.0F)) {
               float var21 = (float)Math.sin(var18 * 2.39) * 0.022F;
               float var22 = (float)Math.sin(var18 * 1.71 + 1.0) * 0.018F;
               Vector3f var23 = new Vector3f(var21, var22, -1.0F).normalize();
               float var24 = 0.018F;
               float var25 = 0.15625F;
               float var12 = 0.015625F;
               float var26 = 0.484375F;
               float[] var27 = O(var23, var20);
               float[] var28 = O(var23, Math.min(var19, 14.0F));
               var1.s(
                  new float[]{var27[0] - var24, var27[1], var27[2], var12, var25},
                  new float[]{var27[0] + var24, var27[1], var27[2], var12, var25},
                  new float[]{var28[0] + var24, var28[1], var28[2], var26, var25},
                  new float[]{var28[0] - var24, var28[1], var28[2], var26, var25}
               );
               var1.s(
                  new float[]{var27[0], var27[1] - var24, var27[2], var12, var25},
                  new float[]{var27[0], var27[1] + var24, var27[2], var12, var25},
                  new float[]{var28[0], var28[1] + var24, var28[2], var26, var25},
                  new float[]{var28[0], var28[1] - var24, var28[2], var26, var25}
               );
            }
         }
      }

      return var1.f();
   }

   private static float[] O(Vector3f var0, float var1) {
      return new float[]{var0.x * var1, -0.11886F + var0.y * var1, -0.77511F + var0.z * var1};
   }

   public static float[] s(float var0, Vector3f var1) {
      ed.z var2 = new ed.z();

      for (int var3 = 0; var3 < 4; var3++) {
         int var4 = f(var0, var3);
         if (var4 >= 0) {
            float var5 = t(var0, var4);
            if (!(var5 >= 7.0F)) {
               float var6 = (float)Math.sin(var4 * 3.7) * 0.008F;
               Vector3f var7 = new Vector3f(-0.04688F, -0.11886F, -0.19011F)
                  .add(new Vector3f(-0.055F + var6, -0.045F, 0.012F).mul(var5))
                  .add(new Vector3f(var1).mul(0.0175F * var5 * var5));
               Quaternionf var8 = new Quaternionf().rotationXYZ(var5 * 0.9F + var4, var5 * 1.3F, var4 * 0.7F);
               H(var2, var7, var8, 0.011F, 0.011F, 0.028F, 0.28125F);
            }
         }
      }

      return var2.f();
   }

   private static void H(ed.z var0, Vector3f var1, Quaternionf var2, float var3, float var4, float var5, float var6) {
      float[][] var7 = new float[][]{{1.0F, 0.0F, 0.0F}, {-1.0F, 0.0F, 0.0F}, {0.0F, 1.0F, 0.0F}, {0.0F, -1.0F, 0.0F}, {0.0F, 0.0F, 1.0F}, {0.0F, 0.0F, -1.0F}};
      float[] var8 = new float[]{var3, var4, var5};

      for (float[] var12 : var7) {
         int var13 = var12[0] != 0.0F ? 0 : (var12[1] != 0.0F ? 1 : 2);
         int var14 = (var13 + 1) % 3;
         int var15 = (var13 + 2) % 3;
         float var16 = var12[var13];
         float var17 = (var13 == 2 ? 1.5F : 0.5F) * 0.03125F;
         float[][] var18 = new float[4][];
         float[][] var19 = new float[][]{{-1.0F, -1.0F}, {1.0F, -1.0F}, {1.0F, 1.0F}, {-1.0F, 1.0F}};

         for (int var20 = 0; var20 < 4; var20++) {
            float[] var21 = var19[var16 > 0.0F ? var20 : 3 - var20];
            Vector3f var22 = new Vector3f();
            var22.setComponent(var13, var16 * var8[var13]);
            var22.setComponent(var14, var21[0] * var8[var14]);
            var22.setComponent(var15, var21[1] * var8[var15]);
            var2.transform(var22).add(var1);
            var18[var20] = new float[]{var22.x, var22.y, var22.z, var17, var6};
         }

         Vector3f var23 = var2.transform(new Vector3f(var12[0], var12[1], var12[2]));
         var0.w(var18, var23);
      }
   }

   private static final class z {
      private float[] K = new float[512];
      private int J;

      void s(float[] var1, float[] var2, float[] var3, float[] var4) {
         this.w(new float[][]{var1, var2, var3, var4}, null);
         this.w(new float[][]{var4, var3, var2, var1}, null);
      }

      void w(float[][] var1, Vector3f var2) {
         if (var2 == null) {
            Vector3f var3 = new Vector3f(var1[1][0] - var1[0][0], var1[1][1] - var1[0][1], var1[1][2] - var1[0][2])
               .cross(var1[2][0] - var1[0][0], var1[2][1] - var1[0][1], var1[2][2] - var1[0][2]);
            var2 = var3.lengthSquared() < 1.0E-20F ? new Vector3f(0.0F, -1.0F, 0.0F) : var3.normalize();
         }

         if (this.J + 32 > this.K.length) {
            this.K = Arrays.copyOf(this.K, this.K.length * 2);
         }

         for (float[] var6 : var1) {
            System.arraycopy(var6, 0, this.K, this.J, 5);
            this.K[this.J + 5] = var2.x;
            this.K[this.J + 6] = var2.y;
            this.K[this.J + 7] = var2.z;
            this.J += 8;
         }
      }

      float[] f() {
         return Arrays.copyOf(this.K, this.J);
      }
   }
}
