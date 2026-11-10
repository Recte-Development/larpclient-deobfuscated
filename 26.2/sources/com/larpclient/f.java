package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class f {
   public static final int j = 12;
   public static final int s = 48;
   public static final double h = 1.25;
   public static final double Y = 4.5;
   private static final int c = 25;
   private final float[] P = new float[625];
   private final byte[] l = new byte[625];
   private final int[] z = new int[625];
   private final int G;
   private final int J;
   private final double u;
   private int d;
   private int N;

   public f(Vec3 var1) {
      this.G = (int)Math.floor(var1.x);
      this.J = (int)Math.floor(var1.z);
      this.u = var1.y;
   }

   private int S(double var1, double var3) {
      int var5 = (int)Math.floor(var1) - this.G + 12;
      int var6 = (int)Math.floor(var3) - this.J + 12;
      return var5 >= 0 && var6 >= 0 && var5 < 25 && var6 < 25 ? var5 * 25 + var6 : -1;
   }

   public void o(double var1, double var3) {
      int var5 = this.S(var1, var3);
      if (var5 >= 0 && this.l[var5] == 0) {
         this.l[var5] = 1;
         this.z[this.N++] = var5;
      }
   }

   public void e(double var1, double var3, double var5) {
      int var7 = (int)Math.ceil(var5);

      for (int var8 = -var7; var8 <= var7; var8++) {
         for (int var9 = -var7; var9 <= var7; var9++) {
            if (var8 * var8 + var9 * var9 <= (var5 + 0.75) * (var5 + 0.75)) {
               this.o(var1 + var8, var3 + var9);
            }
         }
      }
   }

   public void R(b5.c var1, int var2) {
      while (this.d < this.N && var2-- > 0) {
         int var3 = this.z[this.d++];
         int var4 = var3 / 25 - 12;
         int var5 = var3 % 25 - 12;
         double var6 = this.G + var4 + 0.5;
         double var8 = this.J + var5 + 0.5;
         b5.e var10 = var1 == null ? null : var1.p(new Vec3(var6, this.u + 1.25, var8), new Vec3(var6, this.u - 4.5, var8));
         this.P[var3] = var10 != null && var10.normal().y > 0.5 ? (float)var10.point().y : Float.NaN;
         this.l[var3] = 2;
      }
   }

   public double R(double var1, double var3) {
      int var5 = this.S(var1, var3);
      return var5 >= 0 && this.l[var5] == 2 ? this.P[var5] : Double.NaN;
   }

   public double C() {
      double var1 = this.R(this.G + 0.5, this.J + 0.5);
      return Double.isNaN(var1) ? this.u : var1;
   }

   public int O() {
      return this.N - this.d;
   }
}
