package com.larpclient;

public final class ul {
   private ul() {
   }

   public static int V(String var0, int var1, float var2) {
      double[] var3 = new double[]{(var1 >> 16 & 0xFF) / 255.0, (var1 >> 8 & 0xFF) / 255.0, (var1 & 0xFF) / 255.0};
      double var4 = var2 * 100.0;

      double[] var6 = switch (var0) {
         case "Water" -> c(var3, 0.2);
         case "Caustic" -> c(var3, 0.25);
         case "Nebula" -> c(var3, 0.5 * var4 + 0.1);
         case "Aurora" -> w(r(0.0186, 0.028, 0.059), var3, 0.044 * var4);
         case "Starfall" -> N(w(r(0.0109, 0.0163, 0.0388), var3, 0.0157 * var4));
         case "BlackHole" -> r(0.006, 0.006, 0.01);
         case "Plasma" -> c(var3, 0.65 * var4);
         case "Voronoi" -> c(var3, 0.3 * var4);
         case "Core Caustic" -> T(o(var3));
         case "Core Nebula" -> C(o(var3));
         case "Sunset" -> c(o(var3));
         case "Thunderstorm" -> n(o(var3));
         case "Christmas" -> k(o(var3));
         case "Volcano" -> o();
         case "Sandstorm" -> K(o(var3));
         default -> c(var3, 0.2);
      };
      return 0xFF000000 | c(var6[0]) << 16 | c(var6[1]) << 8 | c(var6[2]);
   }

   private static double[] T(double[] var0) {
      double[] var1 = h(var0, r(1.0, 1.0, 1.0), 0.55);
      return a(w(c(var0, 0.46275), var1, 0.018), 0.55);
   }

   private static double[] C(double[] var0) {
      double[] var1 = w(c(var0, 0.16), r(0.004, 0.003, 0.014), 1.0);
      double[] var2 = h(var1, c(var0, 0.55), 0.55);
      double[] var3 = h(var0, r(1.0, 0.54, 0.2), 0.42);
      double[] var4 = h(var0, r(0.16, 0.52, 1.0), 0.45);
      var2 = w(var2, h(var4, var3, 0.5), 0.42);
      var2 = w(var2, h(c(var1, 2.0), var3, 0.55), 0.35);
      return a(var2, 0.45);
   }

   private static double[] n(double[] var0) {
      double[] var1 = r(0.064, 0.071, 0.088);
      var1 = h(var1, d(var1, w(r(0.55, 0.55, 0.55), var0, 0.9)), 0.34);
      return r(1.0 - Math.exp(-var1[0] * 1.15), 1.0 - Math.exp(-var1[1] * 1.15), 1.0 - Math.exp(-var1[2] * 1.15));
   }

   private static double[] k(double[] var0) {
      double[] var1 = r(0.2, 0.15, 0.3);
      var1 = h(var1, d(var1, w(r(0.6, 0.6, 0.6), var0, 0.8)), 0.14);
      return r(1.0 - Math.exp(-var1[0] * 1.25), 1.0 - Math.exp(-var1[1] * 1.25), 1.0 - Math.exp(-var1[2] * 1.25));
   }

   private static double[] o() {
      double[] var0 = w(c(r(0.2, 0.062, 0.03), 0.44000000000000006), r(1.0, 0.33, 0.06), 0.008);
      return r(1.0 - Math.exp(-var0[0] * 1.2), 1.0 - Math.exp(-var0[1] * 1.2), 1.0 - Math.exp(-var0[2] * 1.2));
   }

   private static double[] K(double[] var0) {
      double[] var1 = s(c(r(0.66, 0.4, 0.18), 0.85), var0);
      return r(1.0 - Math.exp(-var1[0] * 1.25), 1.0 - Math.exp(-var1[1] * 1.25), 1.0 - Math.exp(-var1[2] * 1.25));
   }

   private static double[] s(double[] var0, double[] var1) {
      double var2 = Math.max(var1[0], Math.max(var1[1], var1[2]));
      double var4 = (var2 - Math.min(var1[0], Math.min(var1[1], var1[2]))) / Math.max(var2, 1.0E-4);
      double[] var6 = c(var1, 1.0 / Math.max(l(var1), 0.2));
      double var7 = l(var0);
      double var9 = Math.clamp((var7 - 0.9) / 0.9, 0.0, 1.0);
      double var11 = 0.85 * var4 * (1.0 - var9 * var9 * (3.0 - 2.0 * var9));
      return h(var0, c(var6, var7), var11);
   }

   private static double l(double[] var0) {
      return var0[0] * 0.2126 + var0[1] * 0.7152 + var0[2] * 0.0722;
   }

   private static double[] c(double[] var0) {
      double[] var1 = d(r(1.0, 0.62, 0.25), w(r(0.835, 0.835, 0.835), var0, 0.264));
      var1 = w(var1, r(1.0, 0.65, 0.25), 0.075);
      var1 = w(var1, r(1.0, 0.85, 0.4), 0.068);
      var1 = w(var1, r(1.0, 0.9, 0.7), 0.0175);
      var1 = w(var1, r(1.0, 0.65, 0.3), 0.014);
      var1 = w(var1, r(1.0, 0.72, 0.38), 0.077);
      double[] var2 = h(r(0.45, 0.05, 0.1), r(1.0, 0.62, 0.25), 0.6);
      var1 = h(var1, w(c(var1, 0.85), var2, 0.25), 0.55);
      return a(var1, 0.45);
   }

   private static double[] a(double[] var0, double var1) {
      double[] var3 = new double[3];

      for (int var4 = 0; var4 < 3; var4++) {
         var3[var4] = Math.pow(Math.max(0.0, var0[var4] / (1.0 + var0[var4] * var1)), 0.92);
      }

      return var3;
   }

   private static double[] o(double[] var0) {
      return r(Math.max(var0[0], 0.05), Math.max(var0[1], 0.05), Math.max(var0[2], 0.05));
   }

   private static double[] N(double[] var0) {
      return r(Math.tanh(var0[0]), Math.tanh(var0[1]), Math.tanh(var0[2]));
   }

   private static double[] r(double var0, double var2, double var4) {
      return new double[]{var0, var2, var4};
   }

   private static double[] c(double[] var0, double var1) {
      return r(var0[0] * var1, var0[1] * var1, var0[2] * var1);
   }

   private static double[] d(double[] var0, double[] var1) {
      return r(var0[0] * var1[0], var0[1] * var1[1], var0[2] * var1[2]);
   }

   private static double[] w(double[] var0, double[] var1, double var2) {
      return r(var0[0] + var1[0] * var2, var0[1] + var1[1] * var2, var0[2] + var1[2] * var2);
   }

   private static double[] h(double[] var0, double[] var1, double var2) {
      return r(var0[0] + (var1[0] - var0[0]) * var2, var0[1] + (var1[1] - var0[1]) * var2, var0[2] + (var1[2] - var0[2]) * var2);
   }

   private static int c(double var0) {
      return (int)Math.round(Math.clamp(var0, 0.0, 1.0) * 255.0);
   }
}
