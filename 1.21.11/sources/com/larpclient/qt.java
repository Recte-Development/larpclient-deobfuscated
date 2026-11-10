package com.larpclient;

import net.minecraft.class_3532;

public final class qt {
   private float g;
   private float M;
   private int W = Integer.MIN_VALUE;
   private float[] b;

   public void H() {
      this.g = this.M = 0.0F;
      this.W = Integer.MIN_VALUE;
      this.b = null;
   }

   public float[] p(int var1, float var2, float var3, float var4, float var5, double var6) {
      if (var1 == this.W && this.b != null) {
         return (float[])this.b.clone();
      }

      if (var1 - this.W != 1) {
         this.g = this.M = 0.0F;
      }

      this.W = var1;
      float var8 = (float)Math.clamp(var6, 30.0, 360.0) / 20.0F;
      float var9 = var8 / 4.0F;
      float var10 = class_3532.method_15393(var4 - var2);
      float var11 = class_3532.method_15363(var5, -90.0F, 90.0F) - var3;
      double var12 = Math.hypot(var10, var11);
      double var14 = Math.sqrt(var9 * var9 + 2.0F * var9 * var12) - var9;
      double var16 = Math.min(var8, var14);
      float var18 = var12 < 1.0E-9 ? 0.0F : (float)(var16 / var12);
      this.g = F(this.g, var10 * var18, var9);
      this.M = F(this.M, var11 * var18, var9);
      double var19 = Math.hypot(this.g, this.M);
      if (var19 > var8) {
         this.g = (float)(this.g * (var8 / var19));
         this.M = (float)(this.M * (var8 / var19));
      }

      float var21 = L(var10, this.g);
      float var22 = L(var11, this.M);
      if (var21 != this.g) {
         this.g = var21;
      }

      if (var22 != this.M) {
         this.M = var22;
      }

      this.b = new float[]{var2 + var21, class_3532.method_15363(var3 + var22, -90.0F, 90.0F)};
      return (float[])this.b.clone();
   }

   private static float F(float var0, float var1, float var2) {
      return var0 + class_3532.method_15363(var1 - var0, -var2, var2);
   }

   private static float L(float var0, float var1) {
      if (var0 == 0.0F) {
         return 0.0F;
      } else {
         return Math.signum(var0) == Math.signum(var1) && Math.abs(var1) > Math.abs(var0) ? var0 : var1;
      }
   }
}
