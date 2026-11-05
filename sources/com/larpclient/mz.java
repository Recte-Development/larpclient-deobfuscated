package com.larpclient;

import net.minecraft.world.phys.Vec3;

public final class mz {
   public static final int L = 12;
   public static final int f = 48;
   public static final double I = 1.25;
   public static final double S = 4.5;
   private static final int G = 25;
   private final float[] d = new float[625];
   private final byte[] v = new byte[625];
   private final int[] Z = new int[625];
   private final int c;
   private final int u;
   private final double V;
   private int o;
   private int s;

   public mz(Vec3 var1) {
      this.c = (int)Math.floor(var1.x);
      this.u = (int)Math.floor(var1.z);
      this.V = var1.y;
   }

   private int y(double var1, double var3) {
      int var5 = (int)Math.floor(var1) - this.c + 12;
      int var6 = (int)Math.floor(var3) - this.u + 12;
      return var5 >= 0 && var6 >= 0 && var5 < 25 && var6 < 25 ? var5 * 25 + var6 : -1;
   }

   public void H(double var1, double var3) {
      int var5 = this.y(var1, var3);
      if (var5 >= 0 && this.v[var5] == 0) {
         this.v[var5] = 1;
         this.Z[this.s++] = var5;
      }
   }

   public void M(double var1, double var3, double var5) {
      int var7 = (int)Math.ceil(var5);

      for (int var8 = -var7; var8 <= var7; var8++) {
         for (int var9 = -var7; var9 <= var7; var9++) {
            if (var8 * var8 + var9 * var9 <= (var5 + 0.75) * (var5 + 0.75)) {
               this.H(var1 + var8, var3 + var9);
            }
         }
      }
   }

   public void J(w8.z var1, int var2) {
      while (this.o < this.s && var2-- > 0) {
         int var3 = this.Z[this.o++];
         int var4 = var3 / 25 - 12;
         int var5 = var3 % 25 - 12;
         double var6 = this.c + var4 + 0.5;
         double var8 = this.u + var5 + 0.5;
         w8.n var10 = var1 == null ? null : var1.X(new Vec3(var6, this.V + 1.25, var8), new Vec3(var6, this.V - 4.5, var8));
         this.d[var3] = var10 != null && var10.normal().y > 0.5 ? (float)var10.point().y : Float.NaN;
         this.v[var3] = 2;
      }
   }

   public double H(double var1, double var3) {
      int var5 = this.y(var1, var3);
      return var5 >= 0 && this.v[var5] == 2 ? this.d[var5] : Double.NaN;
   }

   public double X() {
      double var1 = this.H(this.c + 0.5, this.u + 0.5);
      return Double.isNaN(var1) ? this.V : var1;
   }

   public int y() {
      return this.s - this.o;
   }
}
