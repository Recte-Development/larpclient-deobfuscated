package com.larpclient;

import net.minecraft.class_243;

public final class f7 {
   public static final int o = 12;
   public static final int Z = 48;
   public static final double Y = 1.25;
   public static final double r = 4.5;
   private static final int n = 25;
   private final float[] W = new float[625];
   private final byte[] T = new byte[625];
   private final int[] t = new int[625];
   private final int I;
   private final int b;
   private final double S;
   private int V;
   private int J;

   public f7(class_243 var1) {
      this.I = (int)Math.floor(var1.field_1352);
      this.b = (int)Math.floor(var1.field_1350);
      this.S = var1.field_1351;
   }

   private int X(double var1, double var3) {
      int var5 = (int)Math.floor(var1) - this.I + 12;
      int var6 = (int)Math.floor(var3) - this.b + 12;
      return var5 >= 0 && var6 >= 0 && var5 < 25 && var6 < 25 ? var5 * 25 + var6 : -1;
   }

   public void o(double var1, double var3) {
      int var5 = this.X(var1, var3);
      if (var5 >= 0 && this.T[var5] == 0) {
         this.T[var5] = 1;
         this.t[this.J++] = var5;
      }
   }

   public void t(double var1, double var3, double var5) {
      int var7 = (int)Math.ceil(var5);

      for (int var8 = -var7; var8 <= var7; var8++) {
         for (int var9 = -var7; var9 <= var7; var9++) {
            if (var8 * var8 + var9 * var9 <= (var5 + 0.75) * (var5 + 0.75)) {
               this.o(var1 + var8, var3 + var9);
            }
         }
      }
   }

   public void u(i3.k var1, int var2) {
      while (this.V < this.J && var2-- > 0) {
         int var3 = this.t[this.V++];
         int var4 = var3 / 25 - 12;
         int var5 = var3 % 25 - 12;
         double var6 = this.I + var4 + 0.5;
         double var8 = this.b + var5 + 0.5;
         i3.j var10 = var1 == null ? null : var1.U(new class_243(var6, this.S + 1.25, var8), new class_243(var6, this.S - 4.5, var8));
         this.W[var3] = var10 != null && var10.normal().field_1351 > 0.5 ? (float)var10.point().field_1351 : Float.NaN;
         this.T[var3] = 2;
      }
   }

   public double H(double var1, double var3) {
      int var5 = this.X(var1, var3);
      return var5 >= 0 && this.T[var5] == 2 ? this.W[var5] : Double.NaN;
   }

   public double x() {
      double var1 = this.H(this.I + 0.5, this.b + 0.5);
      return Double.isNaN(var1) ? this.S : var1;
   }

   public int h() {
      return this.J - this.V;
   }
}
