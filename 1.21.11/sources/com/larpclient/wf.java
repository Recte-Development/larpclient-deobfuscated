package com.larpclient;

import net.minecraft.class_3532;

public final class wf {
   public static final double r = 0.08;
   public static final double G = 0.01;

   public static double x(float var0, float var1) {
      return class_3532.method_15374(-var0 * (float) (Math.PI / 180.0)) * class_3532.method_15362(var1 * (float) (Math.PI / 180.0));
   }

   public static double b(float var0) {
      return -class_3532.method_15374(var0 * (float) (Math.PI / 180.0));
   }

   public static double Q(float var0, float var1) {
      return class_3532.method_15362(-var0 * (float) (Math.PI / 180.0)) * class_3532.method_15362(var1 * (float) (Math.PI / 180.0));
   }

   public static void L(wf.w var0, double var1) {
      double var3 = x(var0.j, var0.i);
      double var5 = Q(var0.j, var0.i);
      float var7 = var0.i * (float) (Math.PI / 180.0);
      double var8 = Math.sqrt(var3 * var3 + var5 * var5);
      double var10 = Math.sqrt(var0.o * var0.o + var0.M * var0.M);
      double var12 = class_3532.method_33723(Math.cos(var7));
      double var14 = var0.o;
      double var16 = var0.A + var1 * (-1.0 + var12 * 0.75);
      double var18 = var0.M;
      if (var16 < 0.0 && var8 > 0.0) {
         double var20 = var16 * -0.1 * var12;
         var14 += var3 * var20 / var8;
         var16 += var20;
         var18 += var5 * var20 / var8;
      }

      if (var7 < 0.0F && var8 > 0.0) {
         double var22 = var10 * -class_3532.method_15374(var7) * 0.04;
         var14 += -var3 * var22 / var8;
         var16 += var22 * 3.2;
         var18 += -var5 * var22 / var8;
      }

      if (var8 > 0.0) {
         var14 += (var3 / var8 * var10 - var14) * 0.1;
         var18 += (var5 / var8 * var10 - var18) * 0.1;
      }

      var0.o = var14 * 0.99F;
      var0.A = var16 * 0.98F;
      var0.M = var18 * 0.99F;
   }

   public static void d(wf.w var0) {
      double var1 = x(var0.j, var0.i);
      double var3 = b(var0.i);
      double var5 = Q(var0.j, var0.i);
      var0.o = var0.o + (var1 * 0.1 + (var1 * 1.5 - var0.o) * 0.5);
      var0.A = var0.A + (var3 * 0.1 + (var3 * 1.5 - var0.A) * 0.5);
      var0.M = var0.M + (var5 * 0.1 + (var5 * 1.5 - var0.M) * 0.5);
   }

   public static void s(wf.w var0) {
      var0.P = var0.P + var0.o;
      var0.G = var0.G + var0.A;
      var0.b = var0.b + var0.M;
   }

   public static int A(int var0) {
      return 10 * (1 + Math.max(0, var0)) + 5;
   }

   public static float F(double var0, double var2, double var4, double var6) {
      return (float)Math.toDegrees(Math.atan2(-(var4 - var0), var6 - var2));
   }

   public static float a(float var0, float var1, float var2) {
      float var3 = class_3532.method_15393(var1 - var0);
      float var4 = Math.min(Math.abs(var3), Math.max(0.5F, Math.min(var2, Math.abs(var3) * 0.45F)));
      return var0 + Math.copySign(var4, var3);
   }

   private wf() {
   }

   public static final class w {
      public double P;
      public double G;
      public double b;
      public double o;
      public double A;
      public double M;
      public float j;
      public float i;

      public wf.w q(double var1, double var3, double var5, double var7, double var9, double var11, float var13, float var14) {
         this.P = var1;
         this.G = var3;
         this.b = var5;
         this.o = var7;
         this.A = var9;
         this.M = var11;
         this.j = var13;
         this.i = var14;
         return this;
      }

      public wf.w P(wf.w var1) {
         return this.q(var1.P, var1.G, var1.b, var1.o, var1.A, var1.M, var1.j, var1.i);
      }

      public double W() {
         return Math.sqrt(this.o * this.o + this.M * this.M);
      }
   }
}
