package com.larpclient;

import net.minecraft.util.Mth;

public final class vu {
   public static final double v = 0.08;
   public static final double R = 0.01;

   public static double y(float var0, float var1) {
      return Mth.sin(-var0 * (float) (Math.PI / 180.0)) * Mth.cos(var1 * (float) (Math.PI / 180.0));
   }

   public static double n(float var0) {
      return -Mth.sin(var0 * (float) (Math.PI / 180.0));
   }

   public static double Z(float var0, float var1) {
      return Mth.cos(-var0 * (float) (Math.PI / 180.0)) * Mth.cos(var1 * (float) (Math.PI / 180.0));
   }

   public static void n(vu.x var0, double var1) {
      double var3 = y(var0.A, var0.G);
      double var5 = Z(var0.A, var0.G);
      float var7 = var0.G * (float) (Math.PI / 180.0);
      double var8 = Math.sqrt(var3 * var3 + var5 * var5);
      double var10 = Math.sqrt(var0.S * var0.S + var0.m * var0.m);
      double var12 = Mth.square(Math.cos(var7));
      double var14 = var0.S;
      double var16 = var0.g + var1 * (-1.0 + var12 * 0.75);
      double var18 = var0.m;
      if (var16 < 0.0 && var8 > 0.0) {
         double var20 = var16 * -0.1 * var12;
         var14 += var3 * var20 / var8;
         var16 += var20;
         var18 += var5 * var20 / var8;
      }

      if (var7 < 0.0F && var8 > 0.0) {
         double var22 = var10 * -Mth.sin(var7) * 0.04;
         var14 += -var3 * var22 / var8;
         var16 += var22 * 3.2;
         var18 += -var5 * var22 / var8;
      }

      if (var8 > 0.0) {
         var14 += (var3 / var8 * var10 - var14) * 0.1;
         var18 += (var5 / var8 * var10 - var18) * 0.1;
      }

      var0.S = var14 * 0.99F;
      var0.g = var16 * 0.98F;
      var0.m = var18 * 0.99F;
   }

   public static void M(vu.x var0) {
      double var1 = y(var0.A, var0.G);
      double var3 = n(var0.G);
      double var5 = Z(var0.A, var0.G);
      var0.S = var0.S + (var1 * 0.1 + (var1 * 1.5 - var0.S) * 0.5);
      var0.g = var0.g + (var3 * 0.1 + (var3 * 1.5 - var0.g) * 0.5);
      var0.m = var0.m + (var5 * 0.1 + (var5 * 1.5 - var0.m) * 0.5);
   }

   public static void a(vu.x var0) {
      var0.K = var0.K + var0.S;
      var0.J = var0.J + var0.g;
      var0.R = var0.R + var0.m;
   }

   public static int r(int var0) {
      return 10 * (1 + Math.max(0, var0)) + 5;
   }

   public static float v(double var0, double var2, double var4, double var6) {
      return (float)Math.toDegrees(Math.atan2(-(var4 - var0), var6 - var2));
   }

   public static float p(float var0, float var1, float var2) {
      float var3 = Mth.wrapDegrees(var1 - var0);
      float var4 = Math.min(Math.abs(var3), Math.max(0.5F, Math.min(var2, Math.abs(var3) * 0.45F)));
      return var0 + Math.copySign(var4, var3);
   }

   private vu() {
   }

   public static final class x {
      public double K;
      public double J;
      public double R;
      public double S;
      public double g;
      public double m;
      public float A;
      public float G;

      public vu.x r(double var1, double var3, double var5, double var7, double var9, double var11, float var13, float var14) {
         this.K = var1;
         this.J = var3;
         this.R = var5;
         this.S = var7;
         this.g = var9;
         this.m = var11;
         this.A = var13;
         this.G = var14;
         return this;
      }

      public vu.x i(vu.x var1) {
         return this.r(var1.K, var1.J, var1.R, var1.S, var1.g, var1.m, var1.A, var1.G);
      }

      public double W() {
         return Math.sqrt(this.S * this.S + this.m * this.m);
      }
   }
}
