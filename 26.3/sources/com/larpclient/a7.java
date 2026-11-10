package com.larpclient;

import net.minecraft.util.Mth;

public final class a7 {
   private float v;
   private float p;
   private int a = Integer.MIN_VALUE;
   private float[] k;

   public void W() {
      this.v = this.p = 0.0F;
      this.a = Integer.MIN_VALUE;
      this.k = null;
   }

   public float[] k(int var1, float var2, float var3, float var4, float var5, double var6) {
      if (var1 == this.a && this.k != null) {
         return (float[])this.k.clone();
      }

      if (var1 - this.a != 1) {
         this.v = this.p = 0.0F;
      }

      this.a = var1;
      float var8 = (float)Math.clamp(var6, 30.0, 360.0) / 20.0F;
      float var9 = var8 / 4.0F;
      float var10 = Mth.wrapDegrees(var4 - var2);
      float var11 = Mth.clamp(var5, -90.0F, 90.0F) - var3;
      double var12 = Math.hypot(var10, var11);
      double var14 = Math.sqrt(var9 * var9 + 2.0F * var9 * var12) - var9;
      double var16 = Math.min(var8, var14);
      float var18 = var12 < 1.0E-9 ? 0.0F : (float)(var16 / var12);
      this.v = g(this.v, var10 * var18, var9);
      this.p = g(this.p, var11 * var18, var9);
      double var19 = Math.hypot(this.v, this.p);
      if (var19 > var8) {
         this.v = (float)(this.v * (var8 / var19));
         this.p = (float)(this.p * (var8 / var19));
      }

      float var21 = S(var10, this.v);
      float var22 = S(var11, this.p);
      if (var21 != this.v) {
         this.v = var21;
      }

      if (var22 != this.p) {
         this.p = var22;
      }

      this.k = new float[]{var2 + var21, Mth.clamp(var3 + var22, -90.0F, 90.0F)};
      return (float[])this.k.clone();
   }

   private static float g(float var0, float var1, float var2) {
      return var0 + Mth.clamp(var1 - var0, -var2, var2);
   }

   private static float S(float var0, float var1) {
      if (var0 == 0.0F) {
         return 0.0F;
      } else {
         return Math.signum(var0) == Math.signum(var1) && Math.abs(var1) > Math.abs(var0) ? var0 : var1;
      }
   }
}
