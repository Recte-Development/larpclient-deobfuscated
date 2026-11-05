package com.larpclient;

public final class ea {
   private static long k;
   private static double L;
   private static double o;
   private static float c;

   public static void g() {
      k++;
   }

   public static void r(float var0) {
      c = Math.clamp(var0, 0.0F, 1.0F);
      double var1 = ((float)k + c) * 0.05;
      o = Math.clamp(var1 - L, 0.0, 0.1);
      L = var1;
   }

   public static double y() {
      return L;
   }

   public static double X() {
      return o;
   }

   public static float Z() {
      return c;
   }

   public static double t() {
      return k * 0.05;
   }

   private ea() {
   }
}
