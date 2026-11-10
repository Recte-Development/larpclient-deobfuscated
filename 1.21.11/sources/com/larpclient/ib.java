package com.larpclient;

import com.larpclient.module.HUDModule;

public final class ib {
   public static uw.r D(String var0) {
      return switch (var0) {
         case "SF Pro" -> uw.r.d;
         case "Minecraft" -> uw.r.r;
         default -> uw.r.V;
      };
   }

   public static uw.r o() {
      HUDModule var0 = HUDModule.y();
      return D(var0 == null ? "Inter" : var0.a.getValue());
   }

   public static double g() {
      return o() == uw.r.r ? 9.0 : 11.0;
   }

   public static int x(String var0, double var1) {
      return (int)Math.ceil(uw.z(var0, o()) * var1 / g());
   }

   public static String t(String var0, double var1, double var3) {
      return uw.n(var0, Math.max(0, (int)Math.floor(var1 * g() / var3)), o());
   }

   public static double i(String var0) {
      return switch (var0) {
         case "SF Pro" -> 5.25;
         case "Minecraft" -> 4.5;
         default -> 5.125;
      };
   }

   public static double y(double var0, double var2, double var4) {
      HUDModule var6 = HUDModule.y();
      String var7 = var6 == null ? "Inter" : var6.a.getValue();
      return var0 + var2 / 2.0 - i(var7) * var4 / g();
   }

   private ib() {
   }
}
