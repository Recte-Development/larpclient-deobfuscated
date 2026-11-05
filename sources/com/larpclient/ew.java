package com.larpclient;

import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class ew {
   public static final int G = 3;
   public static final int P = 256;
   private static final double g = 0.008333333333333333;
   private final Vec3[][] m = new Vec3[3][256];
   private final double[] S = new double[256];
   private final Vec3[] I = new Vec3[3];
   private int v;
   private int J;
   private double C = Double.NaN;
   private double y;
   private double Y;
   private double U;
   private double V;

   public void H() {
      this.v = this.J = 0;
      this.C = Double.NaN;
      this.Y = 0.0;
      Arrays.fill(this.I, null);

      for (Vec3[] var4 : this.m) {
         Arrays.fill(var4, null);
      }
   }

   public void X(double var1, double var3, double var5, double var7) {
      if (!Double.isNaN(this.C) && (var1 < this.C || var1 - this.C > 1.0)) {
         this.H();
      }

      if (Double.isNaN(this.C)) {
         this.C = this.y = var1;
         this.V = var1;
         this.U = var5;
         this.b(var1, this.Y, var5);
      }

      double var9 = var1 - this.C;
      double var11 = this.Y + var9 * var3 * Math.PI * 2.0;

      while (this.y + 0.008333333333333333 <= var1) {
         this.y += 0.008333333333333333;
         double var13 = var9 > 0.0 ? Math.clamp((this.y - this.C) / var9, 0.0, 1.0) : 1.0;
         this.b(this.y, this.Y + (var11 - this.Y) * var13, this.U + (var5 - this.U) * var13);
      }

      this.Y = var11;

      for (int var15 = 0; var15 < 3; var15++) {
         this.I[var15] = r(this.Y, var5, var15);
      }

      this.C = var1;

      for (this.U = var5; this.J > 0 && var1 - this.S[this.v] > var7; this.J--) {
         this.v = (this.v + 1) % 256;
      }
   }

   private void b(double var1, double var3, double var5) {
      if (this.J == 256) {
         this.v = (this.v + 1) % 256;
         this.J--;
      }

      int var7 = (this.v + this.J++) % 256;
      this.S[var7] = var1;

      for (int var8 = 0; var8 < 3; var8++) {
         this.m[var8][var7] = r(var3, var5, var8);
      }
   }

   private static Vec3 r(double var0, double var2, int var4) {
      double var5 = var4 * Math.PI * 2.0 / 3.0;
      double var7 = var0 + var5;
      return new Vec3(Math.cos(var7) * var2, 0.5 + 0.5 * Math.sin(var0 * 0.65 + var5), Math.sin(var7) * var2);
   }

   public int Y() {
      return this.J;
   }

   public double b(double var1) {
      return Double.isNaN(this.C) ? 0.0 : yc.H((var1 - this.V) / 0.15);
   }

   public Vec3 r(int var1) {
      return this.I[var1];
   }

   public Vec3 u(int var1, int var2) {
      return this.m[var1][(this.v + var2) % 256];
   }

   public double h(int var1) {
      return this.S[(this.v + var1) % 256];
   }

   public static double c(double var0, double var2) {
      return Math.min(var0, var2 / 8.0);
   }

   public static Vec3 k(Vec3 var0, Vec3 var1, double var2, double var4) {
      double var6 = var4 * 3.2;
      return var1.add(var0.x, var6 + var0.y * Math.max(0.0, var2 - var6 * 2.0), var0.z);
   }
}
