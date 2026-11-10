package com.larpclient;

import net.minecraft.util.Mth;

public final class pk {
   private float W;
   private float b;
   private int e = Integer.MIN_VALUE;
   private float[] J;

   public void s() {
      this.W = this.b = 0.0F;
      this.e = Integer.MIN_VALUE;
      this.J = null;
   }

   public float[] P(int var1, float var2, float var3, float var4, float var5, double var6) {
      if (var1 == this.e && this.J != null) {
         return (float[])this.J.clone();
      }

      if (var1 - this.e != 1) {
         this.W = this.b = 0.0F;
      }

      this.e = var1;
      float var8 = (float)Math.clamp(var6, 30.0, 360.0) / 20.0F;
      float var9 = var8 / 4.0F;
      float var10 = Mth.wrapDegrees(var4 - var2);
      float var11 = Mth.clamp(var5, -90.0F, 90.0F) - var3;
      double var12 = Math.hypot(var10, var11);
      double var14 = Math.sqrt(var9 * var9 + 2.0F * var9 * var12) - var9;
      double var16 = Math.min(var8, var14);
      float var18 = var12 < 1.0E-9 ? 0.0F : (float)(var16 / var12);
      this.W = j(this.W, var10 * var18, var9);
      this.b = j(this.b, var11 * var18, var9);
      double var19 = Math.hypot(this.W, this.b);
      if (var19 > var8) {
         this.W = (float)(this.W * (var8 / var19));
         this.b = (float)(this.b * (var8 / var19));
      }

      float var21 = Y(var10, this.W);
      float var22 = Y(var11, this.b);
      if (var21 != this.W) {
         this.W = var21;
      }

      if (var22 != this.b) {
         this.b = var22;
      }

      this.J = new float[]{var2 + var21, Mth.clamp(var3 + var22, -90.0F, 90.0F)};
      return (float[])this.J.clone();
   }

   private static float j(float var0, float var1, float var2) {
      return var0 + Mth.clamp(var1 - var0, -var2, var2);
   }

   private static float Y(float var0, float var1) {
      if (var0 == 0.0F) {
         return 0.0F;
      } else {
         return Math.signum(var0) == Math.signum(var1) && Math.abs(var1) > Math.abs(var0) ? var0 : var1;
      }
   }
}
