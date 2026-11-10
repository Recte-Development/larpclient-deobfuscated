package com.larpclient;

import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class aa {
   public static final int M = 3;
   public static final int n = 256;
   private static final double s = 0.008333333333333333;
   private final Vec3[][] E = new Vec3[3][256];
   private final double[] I = new double[256];
   private final Vec3[] i = new Vec3[3];
   private int h;
   private int e;
   private double y = Double.NaN;
   private double S;
   private double c;
   private double L;
   private double o;

   public void C() {
      this.h = this.e = 0;
      this.y = Double.NaN;
      this.c = 0.0;
      Arrays.fill(this.i, null);

      for (Vec3[] var4 : this.E) {
         Arrays.fill(var4, null);
      }
   }

   public void M(double var1, double var3, double var5, double var7) {
      if (!Double.isNaN(this.y) && (var1 < this.y || var1 - this.y > 1.0)) {
         this.C();
      }

      if (Double.isNaN(this.y)) {
         this.y = this.S = var1;
         this.o = var1;
         this.L = var5;
         this.z(var1, this.c, var5);
      }

      double var9 = var1 - this.y;
      double var11 = this.c + var9 * var3 * Math.PI * 2.0;

      while (this.S + 0.008333333333333333 <= var1) {
         this.S += 0.008333333333333333;
         double var13 = var9 > 0.0 ? Math.clamp((this.S - this.y) / var9, 0.0, 1.0) : 1.0;
         this.z(this.S, this.c + (var11 - this.c) * var13, this.L + (var5 - this.L) * var13);
      }

      this.c = var11;

      for (int var15 = 0; var15 < 3; var15++) {
         this.i[var15] = s(this.c, var5, var15);
      }

      this.y = var1;

      for (this.L = var5; this.e > 0 && var1 - this.I[this.h] > var7; this.e--) {
         this.h = (this.h + 1) % 256;
      }
   }

   private void z(double var1, double var3, double var5) {
      if (this.e == 256) {
         this.h = (this.h + 1) % 256;
         this.e--;
      }

      int var7 = (this.h + this.e++) % 256;
      this.I[var7] = var1;

      for (int var8 = 0; var8 < 3; var8++) {
         this.E[var8][var7] = s(var3, var5, var8);
      }
   }

   private static Vec3 s(double var0, double var2, int var4) {
      double var5 = var4 * Math.PI * 2.0 / 3.0;
      double var7 = var0 + var5;
      return new Vec3(Math.cos(var7) * var2, 0.5 + 0.5 * Math.sin(var0 * 0.65 + var5), Math.sin(var7) * var2);
   }

   public int F() {
      return this.e;
   }

   public double y(double var1) {
      return Double.isNaN(this.y) ? 0.0 : pr.k((var1 - this.o) / 0.15);
   }

   public Vec3 j(int var1) {
      return this.i[var1];
   }

   public Vec3 n(int var1, int var2) {
      return this.E[var1][(this.h + var2) % 256];
   }

   public double x(int var1) {
      return this.I[(this.h + var1) % 256];
   }

   public static double x(double var0, double var2) {
      return Math.min(var0, var2 / 8.0);
   }

   public static Vec3 K(Vec3 var0, Vec3 var1, double var2, double var4) {
      double var6 = var4 * 3.2;
      return var1.add(var0.x, var6 + var0.y * Math.max(0.0, var2 - var6 * 2.0), var0.z);
   }
}
