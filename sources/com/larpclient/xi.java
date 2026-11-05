package com.larpclient;

import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class xi {
   public static final int i = 3;
   public static final int Y = 256;
   private static final double m = 0.008333333333333333;
   private final Vec3[][] F = new Vec3[3][256];
   private final double[] e = new double[256];
   private final Vec3[] D = new Vec3[3];
   private int d;
   private int s;
   private double n = Double.NaN;
   private double y;
   private double J;
   private double B;
   private double O;

   public void i() {
      this.d = this.s = 0;
      this.n = Double.NaN;
      this.J = 0.0;
      Arrays.fill(this.D, null);

      for (Vec3[] var4 : this.F) {
         Arrays.fill(var4, null);
      }
   }

   public void O(double var1, double var3, double var5, double var7) {
      if (!Double.isNaN(this.n) && (var1 < this.n || var1 - this.n > 1.0)) {
         this.i();
      }

      if (Double.isNaN(this.n)) {
         this.n = this.y = var1;
         this.O = var1;
         this.B = var5;
         this.L(var1, this.J, var5);
      }

      double var9 = var1 - this.n;
      double var11 = this.J + var9 * var3 * Math.PI * 2.0;

      while (this.y + 0.008333333333333333 <= var1) {
         this.y += 0.008333333333333333;
         double var13 = var9 > 0.0 ? Math.clamp((this.y - this.n) / var9, 0.0, 1.0) : 1.0;
         this.L(this.y, this.J + (var11 - this.J) * var13, this.B + (var5 - this.B) * var13);
      }

      this.J = var11;

      for (int var15 = 0; var15 < 3; var15++) {
         this.D[var15] = o(this.J, var5, var15);
      }

      this.n = var1;

      for (this.B = var5; this.s > 0 && var1 - this.e[this.d] > var7; this.s--) {
         this.d = (this.d + 1) % 256;
      }
   }

   private void L(double var1, double var3, double var5) {
      if (this.s == 256) {
         this.d = (this.d + 1) % 256;
         this.s--;
      }

      int var7 = (this.d + this.s++) % 256;
      this.e[var7] = var1;

      for (int var8 = 0; var8 < 3; var8++) {
         this.F[var8][var7] = o(var3, var5, var8);
      }
   }

   private static Vec3 o(double var0, double var2, int var4) {
      double var5 = var4 * Math.PI * 2.0 / 3.0;
      double var7 = var0 + var5;
      return new Vec3(Math.cos(var7) * var2, 0.5 + 0.5 * Math.sin(var0 * 0.65 + var5), Math.sin(var7) * var2);
   }

   public int V() {
      return this.s;
   }

   public double s(double var1) {
      return Double.isNaN(this.n) ? 0.0 : hp.R((var1 - this.O) / 0.15);
   }

   public Vec3 K(int var1) {
      return this.D[var1];
   }

   public Vec3 f(int var1, int var2) {
      return this.F[var1][(this.d + var2) % 256];
   }

   public double K(int var1) {
      return this.e[(this.d + var1) % 256];
   }

   public static double f(double var0, double var2) {
      return Math.min(var0, var2 / 8.0);
   }

   public static Vec3 U(Vec3 var0, Vec3 var1, double var2, double var4) {
      double var6 = var4 * 3.2;
      return var1.add(var0.x, var6 + var0.y * Math.max(0.0, var2 - var6 * 2.0), var0.z);
   }
}
