package com.larpclient;

public final class hz {
   private static long W;
   private static double s;
   private static double H;
   private static float a;

   public static void A() {
      W++;
   }

   public static void H(float var0) {
      a = Math.clamp(var0, 0.0F, 1.0F);
      double var1 = ((float)W + a) * 0.05;
      H = Math.clamp(var1 - s, 0.0, 0.1);
      s = var1;
   }

   public static double Q() {
      return s;
   }

   public static double V() {
      return H;
   }

   public static float C() {
      return a;
   }

   public static double S() {
      return W * 0.05;
   }

   private hz() {
   }
}
