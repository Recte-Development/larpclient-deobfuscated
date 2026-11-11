package com.larpclient.gui.theme;

public final class BackgroundThemeColors {
   private BackgroundThemeColors() {
   }

   public static int themeColor(String var0, int var1, float var2) {
      double[] var3 = new double[]{(var1 >> 16 & 0xFF) / 255.0, (var1 >> 8 & 0xFF) / 255.0, (var1 & 0xFF) / 255.0};
      double var4 = var2 * 100.0;

      double[] var6 = switch (var0) {
         case "Water" -> scale(var3, 0.2);
         case "Caustic" -> scale(var3, 0.25);
         case "Nebula" -> scale(var3, 0.5 * var4 + 0.1);
         case "Aurora" -> addScaled(vec3(0.0186, 0.028, 0.059), var3, 0.044 * var4);
         case "Starfall" -> tanh(addScaled(vec3(0.0109, 0.0163, 0.0388), var3, 0.0157 * var4));
         case "BlackHole" -> vec3(0.006, 0.006, 0.01);
         case "Plasma" -> scale(var3, 0.65 * var4);
         case "Voronoi" -> scale(var3, 0.3 * var4);
         case "Core Caustic" -> coreCausticColor(clampMinimum(var3));
         case "Core Nebula" -> coreNebulaColor(clampMinimum(var3));
         case "Sunset" -> sunsetColor(clampMinimum(var3));
         case "Thunderstorm" -> thunderstormColor(clampMinimum(var3));
         case "Christmas" -> christmasColor(clampMinimum(var3));
         case "Volcano" -> volcanoColor();
         case "Sandstorm" -> sandstormColor(clampMinimum(var3));
         default -> scale(var3, 0.2);
      };
      return 0xFF000000 | toByte(var6[0]) << 16 | toByte(var6[1]) << 8 | toByte(var6[2]);
   }

   private static double[] coreCausticColor(double[] var0) {
      double[] var1 = vec3(var0, vec3(1.0, 1.0, 1.0), 0.55);
      return toneMap(addScaled(scale(var0, 0.46275), var1, 0.018), 0.55);
   }

   private static double[] coreNebulaColor(double[] var0) {
      double[] var1 = addScaled(scale(var0, 0.16), vec3(0.004, 0.003, 0.014), 1.0);
      double[] var2 = vec3(var1, scale(var0, 0.55), 0.55);
      double[] var3 = vec3(var0, vec3(1.0, 0.54, 0.2), 0.42);
      double[] var4 = vec3(var0, vec3(0.16, 0.52, 1.0), 0.45);
      var2 = addScaled(var2, vec3(var4, var3, 0.5), 0.42);
      var2 = addScaled(var2, vec3(scale(var1, 2.0), var3, 0.55), 0.35);
      return toneMap(var2, 0.45);
   }

   private static double[] thunderstormColor(double[] var0) {
      double[] var1 = vec3(0.064, 0.071, 0.088);
      var1 = vec3(var1, multiply(var1, addScaled(vec3(0.55, 0.55, 0.55), var0, 0.9)), 0.34);
      return vec3(1.0 - Math.exp(-var1[0] * 1.15), 1.0 - Math.exp(-var1[1] * 1.15), 1.0 - Math.exp(-var1[2] * 1.15));
   }

   private static double[] christmasColor(double[] var0) {
      double[] var1 = vec3(0.2, 0.15, 0.3);
      var1 = vec3(var1, multiply(var1, addScaled(vec3(0.6, 0.6, 0.6), var0, 0.8)), 0.14);
      return vec3(1.0 - Math.exp(-var1[0] * 1.25), 1.0 - Math.exp(-var1[1] * 1.25), 1.0 - Math.exp(-var1[2] * 1.25));
   }

   private static double[] volcanoColor() {
      double[] var0 = addScaled(scale(vec3(0.2, 0.062, 0.03), 0.44000000000000006), vec3(1.0, 0.33, 0.06), 0.008);
      return vec3(1.0 - Math.exp(-var0[0] * 1.2), 1.0 - Math.exp(-var0[1] * 1.2), 1.0 - Math.exp(-var0[2] * 1.2));
   }

   private static double[] sandstormColor(double[] var0) {
      double[] var1 = blendSaturation(scale(vec3(0.66, 0.4, 0.18), 0.85), var0);
      return vec3(1.0 - Math.exp(-var1[0] * 1.25), 1.0 - Math.exp(-var1[1] * 1.25), 1.0 - Math.exp(-var1[2] * 1.25));
   }

   private static double[] blendSaturation(double[] var0, double[] var1) {
      double var2 = Math.max(var1[0], Math.max(var1[1], var1[2]));
      double var4 = (var2 - Math.min(var1[0], Math.min(var1[1], var1[2]))) / Math.max(var2, 1.0E-4);
      double[] var6 = scale(var1, 1.0 / Math.max(luminance(var1), 0.2));
      double var7 = luminance(var0);
      double var9 = Math.clamp((var7 - 0.9) / 0.9, 0.0, 1.0);
      double var11 = 0.85 * var4 * (1.0 - var9 * var9 * (3.0 - 2.0 * var9));
      return vec3(var0, scale(var6, var7), var11);
   }

   private static double luminance(double[] var0) {
      return var0[0] * 0.2126 + var0[1] * 0.7152 + var0[2] * 0.0722;
   }

   private static double[] sunsetColor(double[] var0) {
      double[] var1 = multiply(vec3(1.0, 0.62, 0.25), addScaled(vec3(0.835, 0.835, 0.835), var0, 0.264));
      var1 = addScaled(var1, vec3(1.0, 0.65, 0.25), 0.075);
      var1 = addScaled(var1, vec3(1.0, 0.85, 0.4), 0.068);
      var1 = addScaled(var1, vec3(1.0, 0.9, 0.7), 0.0175);
      var1 = addScaled(var1, vec3(1.0, 0.65, 0.3), 0.014);
      var1 = addScaled(var1, vec3(1.0, 0.72, 0.38), 0.077);
      double[] var2 = vec3(vec3(0.45, 0.05, 0.1), vec3(1.0, 0.62, 0.25), 0.6);
      var1 = vec3(var1, addScaled(scale(var1, 0.85), var2, 0.25), 0.55);
      return toneMap(var1, 0.45);
   }

   private static double[] toneMap(double[] var0, double var1) {
      double[] var3 = new double[3];

      for (int var4 = 0; var4 < 3; var4++) {
         var3[var4] = Math.pow(Math.max(0.0, var0[var4] / (1.0 + var0[var4] * var1)), 0.92);
      }

      return var3;
   }

   private static double[] clampMinimum(double[] var0) {
      return vec3(Math.max(var0[0], 0.05), Math.max(var0[1], 0.05), Math.max(var0[2], 0.05));
   }

   private static double[] tanh(double[] var0) {
      return vec3(Math.tanh(var0[0]), Math.tanh(var0[1]), Math.tanh(var0[2]));
   }

   private static double[] vec3(double var0, double var2, double var4) {
      return new double[]{var0, var2, var4};
   }

   private static double[] scale(double[] var0, double var1) {
      return vec3(var0[0] * var1, var0[1] * var1, var0[2] * var1);
   }

   private static double[] multiply(double[] var0, double[] var1) {
      return vec3(var0[0] * var1[0], var0[1] * var1[1], var0[2] * var1[2]);
   }

   private static double[] addScaled(double[] var0, double[] var1, double var2) {
      return vec3(var0[0] + var1[0] * var2, var0[1] + var1[1] * var2, var0[2] + var1[2] * var2);
   }

   private static double[] vec3(double[] var0, double[] var1, double var2) {
      return vec3(var0[0] + (var1[0] - var0[0]) * var2, var0[1] + (var1[1] - var0[1]) * var2, var0[2] + (var1[2] - var0[2]) * var2);
   }

   private static int toByte(double var0) {
      return (int)Math.round(Math.clamp(var0, 0.0, 1.0) * 255.0);
   }
}
