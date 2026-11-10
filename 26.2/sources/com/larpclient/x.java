package com.larpclient;

import net.minecraft.util.Mth;

public final class x {
   public static final double y = 0.08;
   public static final double h = 0.01;

   public static double g(float var0, float var1) {
      return Mth.sin(-var0 * (float) (Math.PI / 180.0)) * Mth.cos(var1 * (float) (Math.PI / 180.0));
   }

   public static double L(float var0) {
      return -Mth.sin(var0 * (float) (Math.PI / 180.0));
   }

   public static double L(float var0, float var1) {
      return Mth.cos(-var0 * (float) (Math.PI / 180.0)) * Mth.cos(var1 * (float) (Math.PI / 180.0));
   }

   public static void P(x.w var0, double var1) {
      double var3 = g(var0.A, var0.z);
      double var5 = L(var0.A, var0.z);
      float var7 = var0.z * (float) (Math.PI / 180.0);
      double var8 = Math.sqrt(var3 * var3 + var5 * var5);
      double var10 = Math.sqrt(var0.K * var0.K + var0.S * var0.S);
      double var12 = Mth.square(Math.cos(var7));
      double var14 = var0.K;
      double var16 = var0.p + var1 * (-1.0 + var12 * 0.75);
      double var18 = var0.S;
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

      var0.K = var14 * 0.99F;
      var0.p = var16 * 0.98F;
      var0.S = var18 * 0.99F;
   }

   public static void C(x.w var0) {
      double var1 = g(var0.A, var0.z);
      double var3 = L(var0.z);
      double var5 = L(var0.A, var0.z);
      var0.K = var0.K + (var1 * 0.1 + (var1 * 1.5 - var0.K) * 0.5);
      var0.p = var0.p + (var3 * 0.1 + (var3 * 1.5 - var0.p) * 0.5);
      var0.S = var0.S + (var5 * 0.1 + (var5 * 1.5 - var0.S) * 0.5);
   }

   public static void s(x.w var0) {
      var0.w = var0.w + var0.K;
      var0.L = var0.L + var0.p;
      var0.M = var0.M + var0.S;
   }

   public static int i(int var0) {
      return 10 * (1 + Math.max(0, var0)) + 5;
   }

   public static float N(double var0, double var2, double var4, double var6) {
      return (float)Math.toDegrees(Math.atan2(-(var4 - var0), var6 - var2));
   }

   public static float t(float var0, float var1, float var2) {
      float var3 = Mth.wrapDegrees(var1 - var0);
      float var4 = Math.min(Math.abs(var3), Math.max(0.5F, Math.min(var2, Math.abs(var3) * 0.45F)));
      return var0 + Math.copySign(var4, var3);
   }

   private x() {
   }

   public static final class w {
      public double w;
      public double L;
      public double M;
      public double K;
      public double p;
      public double S;
      public float A;
      public float z;

      public x.w K(double var1, double var3, double var5, double var7, double var9, double var11, float var13, float var14) {
         this.w = var1;
         this.L = var3;
         this.M = var5;
         this.K = var7;
         this.p = var9;
         this.S = var11;
         this.A = var13;
         this.z = var14;
         return this;
      }

      public x.w y(x.w var1) {
         return this.K(var1.w, var1.L, var1.M, var1.K, var1.p, var1.S, var1.A, var1.z);
      }

      public double H() {
         return Math.sqrt(this.K * this.K + this.S * this.S);
      }
   }
}
