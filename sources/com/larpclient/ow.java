package com.larpclient;

import net.minecraft.util.Mth;

public final class ow {
   private float o;
   private float A;
   private int C = Integer.MIN_VALUE;
   private float[] p;

   public void f() {
      this.o = this.A = 0.0F;
      this.C = Integer.MIN_VALUE;
      this.p = null;
   }

   public float[] W(int var1, float var2, float var3, float var4, float var5, double var6) {
      if (var1 == this.C && this.p != null) {
         return (float[])this.p.clone();
      }

      if (var1 - this.C != 1) {
         this.o = this.A = 0.0F;
      }

      this.C = var1;
      float var8 = (float)Math.clamp(var6, 30.0, 360.0) / 20.0F;
      float var9 = var8 / 4.0F;
      float var10 = Mth.wrapDegrees(var4 - var2);
      float var11 = Mth.clamp(var5, -90.0F, 90.0F) - var3;
      double var12 = Math.hypot(var10, var11);
      double var14 = Math.sqrt(var9 * var9 + 2.0F * var9 * var12) - var9;
      double var16 = Math.min(var8, var14);
      float var18 = var12 < 1.0E-9 ? 0.0F : (float)(var16 / var12);
      this.o = t(this.o, var10 * var18, var9);
      this.A = t(this.A, var11 * var18, var9);
      double var19 = Math.hypot(this.o, this.A);
      if (var19 > var8) {
         this.o = (float)(this.o * (var8 / var19));
         this.A = (float)(this.A * (var8 / var19));
      }

      float var21 = Q(var10, this.o);
      float var22 = Q(var11, this.A);
      if (var21 != this.o) {
         this.o = var21;
      }

      if (var22 != this.A) {
         this.A = var22;
      }

      this.p = new float[]{var2 + var21, Mth.clamp(var3 + var22, -90.0F, 90.0F)};
      return (float[])this.p.clone();
   }

   private static float t(float var0, float var1, float var2) {
      return var0 + Mth.clamp(var1 - var0, -var2, var2);
   }

   private static float Q(float var0, float var1) {
      if (var0 == 0.0F) {
         return 0.0F;
      } else {
         return Math.signum(var0) == Math.signum(var1) && Math.abs(var1) > Math.abs(var0) ? var0 : var1;
      }
   }
}
