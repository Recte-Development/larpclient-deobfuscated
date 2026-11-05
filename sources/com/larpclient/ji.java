package com.larpclient;

import net.minecraft.util.Mth;

public final class ji {
   private float c;
   private float R;
   private int G = Integer.MIN_VALUE;
   private float[] I;

   public void q() {
      this.c = this.R = 0.0F;
      this.G = Integer.MIN_VALUE;
      this.I = null;
   }

   public float[] f(int var1, float var2, float var3, float var4, float var5, double var6) {
      if (var1 == this.G && this.I != null) {
         return (float[])this.I.clone();
      }

      if (var1 - this.G != 1) {
         this.c = this.R = 0.0F;
      }

      this.G = var1;
      float var8 = (float)Math.clamp(var6, 30.0, 360.0) / 20.0F;
      float var9 = var8 / 4.0F;
      float var10 = Mth.wrapDegrees(var4 - var2);
      float var11 = Mth.clamp(var5, -90.0F, 90.0F) - var3;
      double var12 = Math.hypot(var10, var11);
      double var14 = Math.sqrt(var9 * var9 + 2.0F * var9 * var12) - var9;
      double var16 = Math.min(var8, var14);
      float var18 = var12 < 1.0E-9 ? 0.0F : (float)(var16 / var12);
      this.c = a(this.c, var10 * var18, var9);
      this.R = a(this.R, var11 * var18, var9);
      double var19 = Math.hypot(this.c, this.R);
      if (var19 > var8) {
         this.c = (float)(this.c * (var8 / var19));
         this.R = (float)(this.R * (var8 / var19));
      }

      float var21 = x(var10, this.c);
      float var22 = x(var11, this.R);
      if (var21 != this.c) {
         this.c = var21;
      }

      if (var22 != this.R) {
         this.R = var22;
      }

      this.I = new float[]{var2 + var21, Mth.clamp(var3 + var22, -90.0F, 90.0F)};
      return (float[])this.I.clone();
   }

   private static float a(float var0, float var1, float var2) {
      return var0 + Mth.clamp(var1 - var0, -var2, var2);
   }

   private static float x(float var0, float var1) {
      if (var0 == 0.0F) {
         return 0.0F;
      } else {
         return Math.signum(var0) == Math.signum(var1) && Math.abs(var1) > Math.abs(var0) ? var0 : var1;
      }
   }
}
