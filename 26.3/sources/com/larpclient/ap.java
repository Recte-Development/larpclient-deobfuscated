package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class ap {
   public static final int z = 12;
   public static final int H = 48;
   public static final double Q = 1.25;
   public static final double q = 4.5;
   private static final int L = 25;
   private final float[] V = new float[625];
   private final byte[] P = new byte[625];
   private final int[] n = new int[625];
   private final int X;
   private final int T;
   private final double N;
   private int g;
   private int K;

   public ap(Vec3 var1) {
      this.X = (int)Math.floor(var1.x);
      this.T = (int)Math.floor(var1.z);
      this.N = var1.y;
   }

   private int j(double var1, double var3) {
      int var5 = (int)Math.floor(var1) - this.X + 12;
      int var6 = (int)Math.floor(var3) - this.T + 12;
      return var5 >= 0 && var6 >= 0 && var5 < 25 && var6 < 25 ? var5 * 25 + var6 : -1;
   }

   public void N(double var1, double var3) {
      int var5 = this.j(var1, var3);
      if (var5 >= 0 && this.P[var5] == 0) {
         this.P[var5] = 1;
         this.n[this.K++] = var5;
      }
   }

   public void B(double var1, double var3, double var5) {
      int var7 = (int)Math.ceil(var5);

      for (int var8 = -var7; var8 <= var7; var8++) {
         for (int var9 = -var7; var9 <= var7; var9++) {
            if (var8 * var8 + var9 * var9 <= (var5 + 0.75) * (var5 + 0.75)) {
               this.N(var1 + var8, var3 + var9);
            }
         }
      }
   }

   public void p(v9.w var1, int var2) {
      while (this.g < this.K && var2-- > 0) {
         int var3 = this.n[this.g++];
         int var4 = var3 / 25 - 12;
         int var5 = var3 % 25 - 12;
         double var6 = this.X + var4 + 0.5;
         double var8 = this.T + var5 + 0.5;
         v9.d var10 = var1 == null ? null : var1.f(new Vec3(var6, this.N + 1.25, var8), new Vec3(var6, this.N - 4.5, var8));
         this.V[var3] = var10 != null && var10.normal().y > 0.5 ? (float)var10.point().y : Float.NaN;
         this.P[var3] = 2;
      }
   }

   public double y(double var1, double var3) {
      int var5 = this.j(var1, var3);
      return var5 >= 0 && this.P[var5] == 2 ? this.V[var5] : Double.NaN;
   }

   public double v() {
      double var1 = this.y(this.X + 0.5, this.T + 0.5);
      return Double.isNaN(var1) ? this.N : var1;
   }

   public int E() {
      return this.K - this.g;
   }
}
