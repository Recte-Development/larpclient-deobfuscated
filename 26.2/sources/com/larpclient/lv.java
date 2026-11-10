package com.larpclient;

public record lv(int background, int title, int subtitle, int accent, boolean light) {
   private static double luminance(int var0) {
      double var1 = 0.0;

      for (int var3 = 0; var3 < 3; var3++) {
         double var4 = (var0 >>> 16 - var3 * 8 & 0xFF) / 255.0;
         var1 += (var4 <= 0.04045 ? var4 / 12.92 : Math.pow((var4 + 0.055) / 1.055, 2.4)) * (var3 == 0 ? 0.2126 : (var3 == 1 ? 0.7152 : 0.0722));
      }

      return var1;
   }

   public static double contrast(int var0, int var1) {
      double var2 = luminance(var0);
      double var4 = luminance(var1);
      return (Math.max(var2, var4) + 0.05) / (Math.min(var2, var4) + 0.05);
   }

   public static int controlInk(int var0) {
      return contrast(-1, var0) > contrast(-15132391, var0) ? -1 : -15132391;
   }

   public static lv at(double var0, double var2, int var4) {
      int var5 = uh.q(ho.f, ho.e, Math.clamp(var2, 0.0, 1.0)) | 0xFF000000;
      return new lv(var5, ho.u, ho.Z, readableAccent(var4, var5), controlInk(var5) == -15132391);
   }

   public static int readableAccent(int var0, int var1) {
      int var2 = var0 | 0xFF000000;
      if (contrast(var2, var1) >= 3.0) {
         return var2;
      }

      for (int var3 = 1; var3 <= 20; var3++) {
         int var4 = uh.q(var2, ho.u, var3 / 20.0);
         if (contrast(var4, var1) >= 3.0) {
            return var4;
         }
      }

      return ho.u;
   }
}
