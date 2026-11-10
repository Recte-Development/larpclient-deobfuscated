package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import org.joml.Quaternionf;

public final class k {
   public static final String z = "crypto_rock";
   public static final String k = "diamond_pickaxe";
   public static final int h = 10;
   public static final int B = 18;
   public static final int x = 8;
   public static final float j = -0.3672F;
   public static final float J = 0.37802F;
   public static final float o = 0.43596F;
   public static final float t = -0.6F;
   public static final float U = -0.8F;
   public static final int b = 8;
   public static final int v = 5;
   public static final float H = 28.0F;
   public static final float F = 9.0F;
   public static final float n = 0.54F;
   private static final float e = 0.03F;
   private static final float Y = 0.08F;
   private static final float l = 0.03F;
   private static final float c = 0.42F;
   private static final float P = 0.4F;
   private static final int[] p = new int[]{0, 0, 0, 0, 2, 2, 1, 3, 4};
   public static final float[] i = r(0.046875F, 0.03125F);

   private k() {
   }

   public static void m(PoseStack var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13) {
      var0.translate(var1 / 16.0, var3 / 16.0, var5 / 16.0);
      uf.q(var0, new Quaternionf().rotationZYX((float)var11, (float)var9, (float)var7));
      var0.scale((float)var13, (float)var13, (float)var13);
   }

   public static float M(int var0) {
      return 18.0F + var0 * 18.0F;
   }

   public static int T(float var0) {
      return var0 < M(0) ? -1 : (int)Math.floor((var0 - M(0)) / 18.0F);
   }

   public static float L(float var0) {
      int var1 = T(var0);
      return var1 < 0 ? -1.0F : var0 - M(var1);
   }

   public static List<k.a> T(float var0) {
      ArrayList var1 = new ArrayList();
      int var2 = T(var0);
      if (var2 < 0) {
         return var1;
      }

      int var3 = Math.max(0, var2 - (int)Math.ceil(1.5555556F));

      for (int var4 = var3; var4 <= var2; var4++) {
         float var5 = var0 - M(var4);
         if (!(var5 >= 28.0F)) {
            for (int var6 = 0; var6 < 8; var6++) {
               var1.add(a(var4, var6, var5));
            }
         }
      }

      return var1;
   }

   private static k.a a(int var0, int var1, float var2) {
      float var3 = (n(var0, var1, 0) - 0.5F) * 2.5F;
      float var4 = 0.045F + 0.075F * n(var0, var1, 1);
      float var5 = 0.19F + 0.12F * n(var0, var1, 2);
      float var6 = n(var0, var1, 3) * 2.0F;
      var2 = Math.max(0.0F, var2 - var6);
      float[] var7 = y(0.37802F, var5, 0.08F, var2);
      float var8 = var7[1];
      float var9 = (float)Math.cos(var3);
      float var10 = (float)Math.sin(var3);
      float var11 = -0.6F * var9 - -0.8F * var10;
      float var12 = -0.6F * var10 + -0.8F * var9;
      float var13 = Math.min(1.0F, var2 / 2.0F);
      float var14 = Math.clamp((26.0F - var2 - var6) / 5.0F, 0.0F, 1.0F);
      float var15 = 0.55F + 0.6F * n(var0, var1, 4);
      return new k.a(
         p[(int)(n(var0, var1, 5) * p.length) % p.length],
         -0.3672F + var11 * var4 * var8,
         var7[0],
         0.43596F + var12 * var4 * var8,
         var2 * var15 + n(var0, var1, 6) * 6.28F,
         var2 * var15 * 0.6F + n(var0, var1, 7) * 6.28F,
         0.54F * (0.4F + 0.6F * var13) * var14
      );
   }

   public static List<k.f> R(float var0) {
      ArrayList var1 = new ArrayList();
      int var2 = T(var0);
      if (var2 < 0) {
         return var1;
      }

      float var3 = var0 - M(var2);
      if (var3 >= 9.0F) {
         return var1;
      }

      for (int var4 = 0; var4 < 5; var4++) {
         float var5 = (n(var2, var4 + 40, 0) - 0.5F) * 2.2F;
         float var6 = 0.06F + 0.08F * n(var2, var4 + 40, 1);
         float var7 = 0.1F + 0.1F * n(var2, var4 + 40, 2);
         float[] var8 = y(0.37802F, var7, 0.03F, var3);
         float var9 = (float)Math.cos(var5);
         float var10 = (float)Math.sin(var5);
         float var11 = -0.6F * var9 - -0.8F * var10;
         float var12 = -0.6F * var10 + -0.8F * var9;
         var1.add(
            new k.f(
               -0.3672F + var11 * var6 * var8[1],
               var8[0],
               0.43596F + var12 * var6 * var8[1],
               var3 * (0.7F + n(var2, var4 + 40, 3)),
               (0.05F + 0.05F * n(var2, var4 + 40, 4)) * Math.clamp((9.0F - var3) / 3.0F, 0.0F, 1.0F)
            )
         );
      }

      return var1;
   }

   private static float[] y(float var0, float var1, float var2, float var3) {
      float var4 = (var1 + (float)Math.sqrt(var1 * var1 + 0.06F * Math.max(0.0F, var0 - var2))) / 0.03F;
      if (var3 <= var4) {
         return new float[]{var0 + var1 * var3 - 0.015F * var3 * var3, var3};
      }

      float var5 = var3 - var4;
      float var6 = (0.03F * var4 - var1) * 0.42F;
      float var7 = var2 + Math.max(0.0F, var6 * var5 - 0.015F * var5 * var5);
      return new float[]{var7, var4 + var5 * 0.4F};
   }

   static float n(int var0, int var1, int var2) {
      int var3 = var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791;
      var3 = (var3 ^ var3 >>> 13) * 1274126177;
      var3 ^= var3 >>> 16;
      return (var3 & 65535) / 65536.0F;
   }

   private static float[] r(float var0, float var1) {
      float[][] var2 = new float[][]{
         {-0.5F, -0.5F, -0.5F},
         {0.5F, -0.5F, -0.5F},
         {0.5F, 0.5F, -0.5F},
         {-0.5F, 0.5F, -0.5F},
         {-0.5F, -0.5F, 0.5F},
         {0.5F, -0.5F, 0.5F},
         {0.5F, 0.5F, 0.5F},
         {-0.5F, 0.5F, 0.5F}
      };
      int[][] var3 = new int[][]{{0, 3, 2, 1}, {4, 5, 6, 7}, {0, 1, 5, 4}, {3, 7, 6, 2}, {0, 4, 7, 3}, {1, 2, 6, 5}};
      float[] var4 = new float[var3.length * 6 * 5];
      int var5 = 0;

      for (int[] var9 : var3) {
         for (int var13 : new int[]{var9[0], var9[1], var9[2], var9[0], var9[2], var9[3]}) {
            var4[var5++] = var2[var13][0];
            var4[var5++] = var2[var13][1];
            var4[var5++] = var2[var13][2];
            var4[var5++] = var0;
            var4[var5++] = var1;
         }
      }

      return var4;
   }

   public record a(int kind, float x, float y, float z, float yaw, float pitch, float scale) {
   }

   public record f(float x, float y, float z, float spin, float scale) {
   }
}
