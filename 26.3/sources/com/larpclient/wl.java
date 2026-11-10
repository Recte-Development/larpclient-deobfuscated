package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import org.joml.Quaternionf;

public final class wl {
   public static final String m = "crypto_rock";
   public static final String G = "diamond_pickaxe";
   public static final int T = 10;
   public static final int P = 18;
   public static final int Q = 8;
   public static final float w = -0.3672F;
   public static final float U = 0.37802F;
   public static final float v = 0.43596F;
   public static final float C = -0.6F;
   public static final float V = -0.8F;
   public static final int y = 8;
   public static final int i = 5;
   public static final float I = 28.0F;
   public static final float z = 9.0F;
   public static final float L = 0.54F;
   private static final float o = 0.03F;
   private static final float x = 0.08F;
   private static final float d = 0.03F;
   private static final float D = 0.42F;
   private static final float k = 0.4F;
   private static final int[] A = new int[]{0, 0, 0, 0, 2, 2, 1, 3, 4};
   public static final float[] R = Z(0.046875F, 0.03125F);

   private wl() {
   }

   public static void z(PoseStack var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13) {
      var0.translate(var1 / 16.0, var3 / 16.0, var5 / 16.0);
      qd.i(var0, new Quaternionf().rotationZYX((float)var11, (float)var9, (float)var7));
      var0.scale((float)var13, (float)var13, (float)var13);
   }

   public static float l(int var0) {
      return 18.0F + var0 * 18.0F;
   }

   public static int W(float var0) {
      return var0 < l(0) ? -1 : (int)Math.floor((var0 - l(0)) / 18.0F);
   }

   public static float t(float var0) {
      int var1 = W(var0);
      return var1 < 0 ? -1.0F : var0 - l(var1);
   }

   public static List<wl.y> P(float var0) {
      ArrayList var1 = new ArrayList();
      int var2 = W(var0);
      if (var2 < 0) {
         return var1;
      }

      int var3 = Math.max(0, var2 - (int)Math.ceil(1.5555556F));

      for (int var4 = var3; var4 <= var2; var4++) {
         float var5 = var0 - l(var4);
         if (!(var5 >= 28.0F)) {
            for (int var6 = 0; var6 < 8; var6++) {
               var1.add(v(var4, var6, var5));
            }
         }
      }

      return var1;
   }

   private static wl.y v(int var0, int var1, float var2) {
      float var3 = (W(var0, var1, 0) - 0.5F) * 2.5F;
      float var4 = 0.045F + 0.075F * W(var0, var1, 1);
      float var5 = 0.19F + 0.12F * W(var0, var1, 2);
      float var6 = W(var0, var1, 3) * 2.0F;
      var2 = Math.max(0.0F, var2 - var6);
      float[] var7 = J(0.37802F, var5, 0.08F, var2);
      float var8 = var7[1];
      float var9 = (float)Math.cos(var3);
      float var10 = (float)Math.sin(var3);
      float var11 = -0.6F * var9 - -0.8F * var10;
      float var12 = -0.6F * var10 + -0.8F * var9;
      float var13 = Math.min(1.0F, var2 / 2.0F);
      float var14 = Math.clamp((26.0F - var2 - var6) / 5.0F, 0.0F, 1.0F);
      float var15 = 0.55F + 0.6F * W(var0, var1, 4);
      return new wl.y(
         A[(int)(W(var0, var1, 5) * A.length) % A.length],
         -0.3672F + var11 * var4 * var8,
         var7[0],
         0.43596F + var12 * var4 * var8,
         var2 * var15 + W(var0, var1, 6) * 6.28F,
         var2 * var15 * 0.6F + W(var0, var1, 7) * 6.28F,
         0.54F * (0.4F + 0.6F * var13) * var14
      );
   }

   public static List<wl.h> u(float var0) {
      ArrayList var1 = new ArrayList();
      int var2 = W(var0);
      if (var2 < 0) {
         return var1;
      }

      float var3 = var0 - l(var2);
      if (var3 >= 9.0F) {
         return var1;
      }

      for (int var4 = 0; var4 < 5; var4++) {
         float var5 = (W(var2, var4 + 40, 0) - 0.5F) * 2.2F;
         float var6 = 0.06F + 0.08F * W(var2, var4 + 40, 1);
         float var7 = 0.1F + 0.1F * W(var2, var4 + 40, 2);
         float[] var8 = J(0.37802F, var7, 0.03F, var3);
         float var9 = (float)Math.cos(var5);
         float var10 = (float)Math.sin(var5);
         float var11 = -0.6F * var9 - -0.8F * var10;
         float var12 = -0.6F * var10 + -0.8F * var9;
         var1.add(
            new wl.h(
               -0.3672F + var11 * var6 * var8[1],
               var8[0],
               0.43596F + var12 * var6 * var8[1],
               var3 * (0.7F + W(var2, var4 + 40, 3)),
               (0.05F + 0.05F * W(var2, var4 + 40, 4)) * Math.clamp((9.0F - var3) / 3.0F, 0.0F, 1.0F)
            )
         );
      }

      return var1;
   }

   private static float[] J(float var0, float var1, float var2, float var3) {
      float var4 = (var1 + (float)Math.sqrt(var1 * var1 + 0.06F * Math.max(0.0F, var0 - var2))) / 0.03F;
      if (var3 <= var4) {
         return new float[]{var0 + var1 * var3 - 0.015F * var3 * var3, var3};
      }

      float var5 = var3 - var4;
      float var6 = (0.03F * var4 - var1) * 0.42F;
      float var7 = var2 + Math.max(0.0F, var6 * var5 - 0.015F * var5 * var5);
      return new float[]{var7, var4 + var5 * 0.4F};
   }

   static float W(int var0, int var1, int var2) {
      int var3 = var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791;
      var3 = (var3 ^ var3 >>> 13) * 1274126177;
      var3 ^= var3 >>> 16;
      return (var3 & 65535) / 65536.0F;
   }

   private static float[] Z(float var0, float var1) {
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

   public record h(float x, float y, float z, float spin, float scale) {
   }

   public record y(int kind, float x, float y, float z, float yaw, float pitch, float scale) {
   }
}
