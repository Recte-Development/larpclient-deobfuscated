package com.larpclient;

public final class cf {
   private static long W;
   private static double s;
   private static double A;
   private static float H;

   public static void o() {
      W++;
   }

   public static void h(float var0) {
      H = Math.clamp(var0, 0.0F, 1.0F);
      double var1 = ((float)W + H) * 0.05;
      A = Math.clamp(var1 - s, 0.0, 0.1);
      s = var1;
   }

   public static double F() {
      return s;
   }

   public static double v() {
      return A;
   }

   public static float l() {
      return H;
   }

   public static double I() {
      return W * 0.05;
   }

   private cf() {
   }
}
