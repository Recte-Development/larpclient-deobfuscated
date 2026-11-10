package com.larpclient;

public record bb(float u, float v, int width, int height) {
   public static bb cover(int var0, int var1) {
      return cover(var0, var1, 1.0, 0.0, 0.0);
   }

   public static bb cover(int var0, int var1, double var2, double var4, double var6) {
      return cover(var0, var1, 1672, 941, var2, var4, var6);
   }

   public static bb cover(int var0, int var1, int var2, int var3, double var4, double var6, double var8) {
      if (var2 > 0 && var3 > 0) {
         double var10 = Math.max((double)Math.max(1, var0) / var2, (double)Math.max(1, var1) / var3);
         double var12 = var10 * Math.clamp(Double.isFinite(var4) ? var4 : 1.0, 1.0, 1.1);
         int var14 = Math.min(var2, Math.max(1, (int)Math.round(var0 / var12)));
         int var15 = Math.min(var3, Math.max(1, (int)Math.round(var1 / var12)));
         double var16 = Math.min((var2 - var14) / 2.0, Math.max(0.0, (var0 / var10 - var14) / 2.0));
         double var18 = Math.min((var3 - var15) / 2.0, Math.max(0.0, (var1 / var10 - var15) / 2.0));
         double var20 = Double.isFinite(var6) ? Math.clamp(var6, -1.0, 1.0) : 0.0;
         double var22 = Double.isFinite(var8) ? Math.clamp(var8, -1.0, 1.0) : 0.0;
         return new bb((float)((var2 - var14) / 2.0 - var20 * var16), (float)((var3 - var15) / 2.0 - var22 * var18), var14, var15);
      } else {
         throw new IllegalArgumentException("Invalid artwork dimensions");
      }
   }
}
