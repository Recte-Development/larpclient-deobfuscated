package com.larpclient;

public final class uq {
   private uq() {
   }

   public static int p(String var0, int var1, float var2) {
      double[] var3 = new double[]{(var1 >> 16 & 0xFF) / 255.0, (var1 >> 8 & 0xFF) / 255.0, (var1 & 0xFF) / 255.0};
      double var4 = var2 * 100.0;

      double[] var6 = switch (var0) {
         case "Water" -> J(var3, 0.2);
         case "Caustic" -> J(var3, 0.25);
         case "Nebula" -> J(var3, 0.5 * var4 + 0.1);
         case "Aurora" -> Q(J(0.0186, 0.028, 0.059), var3, 0.044 * var4);
         case "Starfall" -> u(Q(J(0.0109, 0.0163, 0.0388), var3, 0.0157 * var4));
         case "BlackHole" -> J(0.006, 0.006, 0.01);
         case "Plasma" -> J(var3, 0.65 * var4);
         case "Voronoi" -> J(var3, 0.3 * var4);
         case "Core Caustic" -> g(L(var3));
         case "Core Nebula" -> M(L(var3));
         case "Sunset" -> H(L(var3));
         case "Thunderstorm" -> O(L(var3));
         case "Christmas" -> Z(L(var3));
         case "Volcano" -> X();
         case "Sandstorm" -> s(L(var3));
         default -> J(var3, 0.2);
      };
      return 0xFF000000 | f(var6[0]) << 16 | f(var6[1]) << 8 | f(var6[2]);
   }

   private static double[] g(double[] var0) {
      double[] var1 = t(var0, J(1.0, 1.0, 1.0), 0.55);
      return g(Q(J(var0, 0.46275), var1, 0.018), 0.55);
   }

   private static double[] M(double[] var0) {
      double[] var1 = Q(J(var0, 0.16), J(0.004, 0.003, 0.014), 1.0);
      double[] var2 = t(var1, J(var0, 0.55), 0.55);
      double[] var3 = t(var0, J(1.0, 0.54, 0.2), 0.42);
      double[] var4 = t(var0, J(0.16, 0.52, 1.0), 0.45);
      var2 = Q(var2, t(var4, var3, 0.5), 0.42);
      var2 = Q(var2, t(J(var1, 2.0), var3, 0.55), 0.35);
      return g(var2, 0.45);
   }

   private static double[] O(double[] var0) {
      double[] var1 = J(0.064, 0.071, 0.088);
      var1 = t(var1, s(var1, Q(J(0.55, 0.55, 0.55), var0, 0.9)), 0.34);
      return J(1.0 - Math.exp(-var1[0] * 1.15), 1.0 - Math.exp(-var1[1] * 1.15), 1.0 - Math.exp(-var1[2] * 1.15));
   }

   private static double[] Z(double[] var0) {
      double[] var1 = J(0.2, 0.15, 0.3);
      var1 = t(var1, s(var1, Q(J(0.6, 0.6, 0.6), var0, 0.8)), 0.14);
      return J(1.0 - Math.exp(-var1[0] * 1.25), 1.0 - Math.exp(-var1[1] * 1.25), 1.0 - Math.exp(-var1[2] * 1.25));
   }

   private static double[] X() {
      double[] var0 = Q(J(J(0.2, 0.062, 0.03), 0.44000000000000006), J(1.0, 0.33, 0.06), 0.008);
      return J(1.0 - Math.exp(-var0[0] * 1.2), 1.0 - Math.exp(-var0[1] * 1.2), 1.0 - Math.exp(-var0[2] * 1.2));
   }

   private static double[] s(double[] var0) {
      double[] var1 = d(J(J(0.66, 0.4, 0.18), 0.85), var0);
      return J(1.0 - Math.exp(-var1[0] * 1.25), 1.0 - Math.exp(-var1[1] * 1.25), 1.0 - Math.exp(-var1[2] * 1.25));
   }

   private static double[] d(double[] var0, double[] var1) {
      double var2 = Math.max(var1[0], Math.max(var1[1], var1[2]));
      double var4 = (var2 - Math.min(var1[0], Math.min(var1[1], var1[2]))) / Math.max(var2, 1.0E-4);
      double[] var6 = J(var1, 1.0 / Math.max(f(var1), 0.2));
      double var7 = f(var0);
      double var9 = Math.clamp((var7 - 0.9) / 0.9, 0.0, 1.0);
      double var11 = 0.85 * var4 * (1.0 - var9 * var9 * (3.0 - 2.0 * var9));
      return t(var0, J(var6, var7), var11);
   }

   private static double f(double[] var0) {
      return var0[0] * 0.2126 + var0[1] * 0.7152 + var0[2] * 0.0722;
   }

   private static double[] H(double[] var0) {
      double[] var1 = s(J(1.0, 0.62, 0.25), Q(J(0.835, 0.835, 0.835), var0, 0.264));
      var1 = Q(var1, J(1.0, 0.65, 0.25), 0.075);
      var1 = Q(var1, J(1.0, 0.85, 0.4), 0.068);
      var1 = Q(var1, J(1.0, 0.9, 0.7), 0.0175);
      var1 = Q(var1, J(1.0, 0.65, 0.3), 0.014);
      var1 = Q(var1, J(1.0, 0.72, 0.38), 0.077);
      double[] var2 = t(J(0.45, 0.05, 0.1), J(1.0, 0.62, 0.25), 0.6);
      var1 = t(var1, Q(J(var1, 0.85), var2, 0.25), 0.55);
      return g(var1, 0.45);
   }

   private static double[] g(double[] var0, double var1) {
      double[] var3 = new double[3];

      for (int var4 = 0; var4 < 3; var4++) {
         var3[var4] = Math.pow(Math.max(0.0, var0[var4] / (1.0 + var0[var4] * var1)), 0.92);
      }

      return var3;
   }

   private static double[] L(double[] var0) {
      return J(Math.max(var0[0], 0.05), Math.max(var0[1], 0.05), Math.max(var0[2], 0.05));
   }

   private static double[] u(double[] var0) {
      return J(Math.tanh(var0[0]), Math.tanh(var0[1]), Math.tanh(var0[2]));
   }

   private static double[] J(double var0, double var2, double var4) {
      return new double[]{var0, var2, var4};
   }

   private static double[] J(double[] var0, double var1) {
      return J(var0[0] * var1, var0[1] * var1, var0[2] * var1);
   }

   private static double[] s(double[] var0, double[] var1) {
      return J(var0[0] * var1[0], var0[1] * var1[1], var0[2] * var1[2]);
   }

   private static double[] Q(double[] var0, double[] var1, double var2) {
      return J(var0[0] + var1[0] * var2, var0[1] + var1[1] * var2, var0[2] + var1[2] * var2);
   }

   private static double[] t(double[] var0, double[] var1, double var2) {
      return J(var0[0] + (var1[0] - var0[0]) * var2, var0[1] + (var1[1] - var0[1]) * var2, var0[2] + (var1[2] - var0[2]) * var2);
   }

   private static int f(double var0) {
      return (int)Math.round(Math.clamp(var0, 0.0, 1.0) * 255.0);
   }
}
