package com.larpclient;

public final class d7 {
   private d7() {
   }

   public static d7.m D(boolean var0, boolean var1, boolean var2, double var3, double var5, double var7, double var9, double var11) {
      if (var2) {
         return new d7.m(-var7 + 0.1125, 0.0, 0.0, 0.0, true);
      } else if (var0) {
         double var15 = var11 + 0.005;
         return new d7.m(var15, var5 * Math.PI * 2.0, Math.PI / 2, 0.0, false);
      } else if (var1) {
         double var13 = var3 / 8.0 + var5 * Math.PI * 2.0;
         return new d7.m(
            0.1 + Math.sin(var13) * 0.02,
            var5 * Math.PI * 2.0 + var3 * 0.004,
            (Math.PI / 2) + Math.toRadians(Math.sin(var13 * 0.7) * 4.0),
            Math.toRadians(Math.cos(var13 * 0.5) * 3.0),
            false
         );
      } else {
         return new d7.m(-var7 + 0.0625, var3 / 20.0 + var5, Math.toRadians(var3 * 7.5), Math.toRadians(var3 * 3.25), false);
      }
   }

   public record m(double height, double yaw, double pitch, double roll, boolean billboard) {
   }
}
