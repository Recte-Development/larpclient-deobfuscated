package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class ur {
   public static final int B = 12;
   public static final int o = 48;
   public static final double Z = 1.25;
   public static final double w = 4.5;
   private static final int s = 25;
   private final float[] c = new float[625];
   private final byte[] S = new byte[625];
   private final int[] l = new int[625];
   private final int T;
   private final int K;
   private final double E;
   private int v;
   private int U;

   public ur(Vec3 var1) {
      this.T = (int)Math.floor(var1.x);
      this.K = (int)Math.floor(var1.z);
      this.E = var1.y;
   }

   private int T(double var1, double var3) {
      int var5 = (int)Math.floor(var1) - this.T + 12;
      int var6 = (int)Math.floor(var3) - this.K + 12;
      return var5 >= 0 && var6 >= 0 && var5 < 25 && var6 < 25 ? var5 * 25 + var6 : -1;
   }

   public void I(double var1, double var3) {
      int var5 = this.T(var1, var3);
      if (var5 >= 0 && this.S[var5] == 0) {
         this.S[var5] = 1;
         this.l[this.U++] = var5;
      }
   }

   public void U(double var1, double var3, double var5) {
      int var7 = (int)Math.ceil(var5);

      for (int var8 = -var7; var8 <= var7; var8++) {
         for (int var9 = -var7; var9 <= var7; var9++) {
            if (var8 * var8 + var9 * var9 <= (var5 + 0.75) * (var5 + 0.75)) {
               this.I(var1 + var8, var3 + var9);
            }
         }
      }
   }

   public void j(et.l var1, int var2) {
      while (this.v < this.U && var2-- > 0) {
         int var3 = this.l[this.v++];
         int var4 = var3 / 25 - 12;
         int var5 = var3 % 25 - 12;
         double var6 = this.T + var4 + 0.5;
         double var8 = this.K + var5 + 0.5;
         et.d var10 = var1 == null ? null : var1.J(new Vec3(var6, this.E + 1.25, var8), new Vec3(var6, this.E - 4.5, var8));
         this.c[var3] = var10 != null && var10.normal().y > 0.5 ? (float)var10.point().y : Float.NaN;
         this.S[var3] = 2;
      }
   }

   public double L(double var1, double var3) {
      int var5 = this.T(var1, var3);
      return var5 >= 0 && this.S[var5] == 2 ? this.c[var5] : Double.NaN;
   }

   public double X() {
      double var1 = this.L(this.T + 0.5, this.K + 0.5);
      return Double.isNaN(var1) ? this.E : var1;
   }

   public int U() {
      return this.U - this.v;
   }
}
