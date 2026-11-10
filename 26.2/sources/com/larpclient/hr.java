package com.larpclient;

import java.util.Arrays;
import net.minecraft.world.phys.Vec3;

public final class hr {
   public static final int e = 3;
   public static final int P = 256;
   private static final double q = 0.008333333333333333;
   private final Vec3[][] i = new Vec3[3][256];
   private final double[] N = new double[256];
   private final Vec3[] G = new Vec3[3];
   private int w;
   private int E;
   private double A = Double.NaN;
   private double c;
   private double T;
   private double u;
   private double z;

   public void J() {
      this.w = this.E = 0;
      this.A = Double.NaN;
      this.T = 0.0;
      Arrays.fill(this.G, null);

      for (Vec3[] var4 : this.i) {
         Arrays.fill(var4, null);
      }
   }

   public void B(double var1, double var3, double var5, double var7) {
      if (!Double.isNaN(this.A) && (var1 < this.A || var1 - this.A > 1.0)) {
         this.J();
      }

      if (Double.isNaN(this.A)) {
         this.A = this.c = var1;
         this.z = var1;
         this.u = var5;
         this.x(var1, this.T, var5);
      }

      double var9 = var1 - this.A;
      double var11 = this.T + var9 * var3 * Math.PI * 2.0;

      while (this.c + 0.008333333333333333 <= var1) {
         this.c += 0.008333333333333333;
         double var13 = var9 > 0.0 ? Math.clamp((this.c - this.A) / var9, 0.0, 1.0) : 1.0;
         this.x(this.c, this.T + (var11 - this.T) * var13, this.u + (var5 - this.u) * var13);
      }

      this.T = var11;

      for (int var15 = 0; var15 < 3; var15++) {
         this.G[var15] = C(this.T, var5, var15);
      }

      this.A = var1;

      for (this.u = var5; this.E > 0 && var1 - this.N[this.w] > var7; this.E--) {
         this.w = (this.w + 1) % 256;
      }
   }

   private void x(double var1, double var3, double var5) {
      if (this.E == 256) {
         this.w = (this.w + 1) % 256;
         this.E--;
      }

      int var7 = (this.w + this.E++) % 256;
      this.N[var7] = var1;

      for (int var8 = 0; var8 < 3; var8++) {
         this.i[var8][var7] = C(var3, var5, var8);
      }
   }

   private static Vec3 C(double var0, double var2, int var4) {
      double var5 = var4 * Math.PI * 2.0 / 3.0;
      double var7 = var0 + var5;
      return new Vec3(Math.cos(var7) * var2, 0.5 + 0.5 * Math.sin(var0 * 0.65 + var5), Math.sin(var7) * var2);
   }

   public int D() {
      return this.E;
   }

   public double O(double var1) {
      return Double.isNaN(this.A) ? 0.0 : st.h((var1 - this.z) / 0.15);
   }

   public Vec3 M(int var1) {
      return this.G[var1];
   }

   public Vec3 U(int var1, int var2) {
      return this.i[var1][(this.w + var2) % 256];
   }

   public double o(int var1) {
      return this.N[(this.w + var1) % 256];
   }

   public static double a(double var0, double var2) {
      return Math.min(var0, var2 / 8.0);
   }

   public static Vec3 C(Vec3 var0, Vec3 var1, double var2, double var4) {
      double var6 = var4 * 3.2;
      return var1.add(var0.x, var6 + var0.y * Math.max(0.0, var2 - var6 * 2.0), var0.z);
   }
}
