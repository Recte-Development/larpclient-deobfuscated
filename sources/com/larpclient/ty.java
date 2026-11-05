package com.larpclient;

public final class ty {
   private static long R;
   private static double i;
   private static double G;
   private static float L;

   public static void w() {
      R++;
   }

   public static void H(float var0) {
      L = Math.clamp(var0, 0.0F, 1.0F);
      double var1 = ((float)R + L) * 0.05;
      G = Math.clamp(var1 - i, 0.0, 0.1);
      i = var1;
   }

   public static double K() {
      return i;
   }

   public static double N() {
      return G;
   }

   public static float g() {
      return L;
   }

   public static double P() {
      return R * 0.05;
   }

   private ty() {
   }
}
