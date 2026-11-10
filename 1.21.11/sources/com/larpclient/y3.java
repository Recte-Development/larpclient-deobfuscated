package com.larpclient;

import java.util.Arrays;
import net.minecraft.class_243;

public final class y3 {
   public static final int N = 3;
   public static final int y = 256;
   private static final double u = 0.008333333333333333;
   private final class_243[][] T = new class_243[3][256];
   private final double[] e = new double[256];
   private final class_243[] B = new class_243[3];
   private int U;
   private int L;
   private double a = Double.NaN;
   private double r;
   private double x;
   private double M;
   private double t;

   public void I() {
      this.U = this.L = 0;
      this.a = Double.NaN;
      this.x = 0.0;
      Arrays.fill(this.B, null);

      for (class_243[] var4 : this.T) {
         Arrays.fill(var4, null);
      }
   }

   public void v(double var1, double var3, double var5, double var7) {
      if (!Double.isNaN(this.a) && (var1 < this.a || var1 - this.a > 1.0)) {
         this.I();
      }

      if (Double.isNaN(this.a)) {
         this.a = this.r = var1;
         this.t = var1;
         this.M = var5;
         this.q(var1, this.x, var5);
      }

      double var9 = var1 - this.a;
      double var11 = this.x + var9 * var3 * Math.PI * 2.0;

      while (this.r + 0.008333333333333333 <= var1) {
         this.r += 0.008333333333333333;
         double var13 = var9 > 0.0 ? Math.clamp((this.r - this.a) / var9, 0.0, 1.0) : 1.0;
         this.q(this.r, this.x + (var11 - this.x) * var13, this.M + (var5 - this.M) * var13);
      }

      this.x = var11;

      for (int var15 = 0; var15 < 3; var15++) {
         this.B[var15] = a(this.x, var5, var15);
      }

      this.a = var1;

      for (this.M = var5; this.L > 0 && var1 - this.e[this.U] > var7; this.L--) {
         this.U = (this.U + 1) % 256;
      }
   }

   private void q(double var1, double var3, double var5) {
      if (this.L == 256) {
         this.U = (this.U + 1) % 256;
         this.L--;
      }

      int var7 = (this.U + this.L++) % 256;
      this.e[var7] = var1;

      for (int var8 = 0; var8 < 3; var8++) {
         this.T[var8][var7] = a(var3, var5, var8);
      }
   }

   private static class_243 a(double var0, double var2, int var4) {
      double var5 = var4 * Math.PI * 2.0 / 3.0;
      double var7 = var0 + var5;
      return new class_243(Math.cos(var7) * var2, 0.5 + 0.5 * Math.sin(var0 * 0.65 + var5), Math.sin(var7) * var2);
   }

   public int r() {
      return this.L;
   }

   public double J(double var1) {
      return Double.isNaN(this.a) ? 0.0 : u9.L((var1 - this.t) / 0.15);
   }

   public class_243 D(int var1) {
      return this.B[var1];
   }

   public class_243 J(int var1, int var2) {
      return this.T[var1][(this.U + var2) % 256];
   }

   public double y(int var1) {
      return this.e[(this.U + var1) % 256];
   }

   public static double H(double var0, double var2) {
      return Math.min(var0, var2 / 8.0);
   }

   public static class_243 u(class_243 var0, class_243 var1, double var2, double var4) {
      double var6 = var4 * 3.2;
      return var1.method_1031(var0.field_1352, var6 + var0.field_1351 * Math.max(0.0, var2 - var6 * 2.0), var0.field_1350);
   }
}
