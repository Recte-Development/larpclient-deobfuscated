package com.larpclient;

public final class qo {
   private static long U;
   private static double q;
   private static double O;
   private static float g;

   public static void q() {
      U++;
   }

   public static void z(float var0) {
      g = Math.clamp(var0, 0.0F, 1.0F);
      double var1 = ((float)U + g) * 0.05;
      O = Math.clamp(var1 - q, 0.0, 0.1);
      q = var1;
   }

   public static double x() {
      return q;
   }

   public static double Q() {
      return O;
   }

   public static float C() {
      return g;
   }

   public static double K() {
      return U * 0.05;
   }

   private qo() {
   }
}
