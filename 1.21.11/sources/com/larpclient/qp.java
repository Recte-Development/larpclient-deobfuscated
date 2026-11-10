package com.larpclient;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public final class qp {
   public static final double d = 64.0;
   private static final float e = 2.0F;
   public static final String w = "Enter coordinates as X Y Z or X Z";
   private static final Pattern c = Pattern.compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)");

   private qp() {
   }

   public static qp.w b(String var0, qp.w var1) {
      String var2 = var0 == null ? "" : var0.strip().replaceAll("^[(\\[]+|[)\\]]+$", "").strip();
      if (var2.isEmpty()) {
         return var1;
      }

      String[] var3 = var2.split("[\\s,;]+");
      if (var3.length != 2 && var3.length != 3) {
         throw new IllegalArgumentException("Enter coordinates as X Y Z or X Z");
      }

      int var4 = s(var3[0], var1.x(), 30000000);
      int var5 = var3.length == 3 ? s(var3[1], var1.y(), 4096) : var1.y();
      int var6 = s(var3[var3.length - 1], var1.z(), 30000000);
      return new qp.w(var4, var5, var6);
   }

   private static int s(String var0, int var1, int var2) {
      boolean var3 = var0.startsWith("~");
      String var4 = var3 ? var0.substring(1) : var0;
      if (var3 && var4.isEmpty()) {
         return var1;
      } else if (!c.matcher(var4).matches()) {
         throw new IllegalArgumentException("Enter coordinates as X Y Z or X Z");
      } else {
         double var5 = Double.parseDouble(var4) + (var3 ? var1 : 0);
         if (Math.abs(var5) > var2) {
            throw new IllegalArgumentException("Those coordinates are outside the world");
         } else {
            return (int)Math.floor(var5);
         }
      }
   }

   public static String N(qp.w var0) {
      return var0.x() + " " + var0.y() + " " + var0.z();
   }

   public static String Z(double var0) {
      return var0 < 999.5 ? Math.round(var0) + "m" : String.format(Locale.ROOT, "%.1fkm", var0 / 1000.0);
   }

   public static double w(double var0) {
      return var0 > 64.0 ? 64.0 / var0 : 1.0;
   }

   public static float W(List<float[]> var0, float var1, float var2, float var3, float var4) {
      float var5 = var2;
      byte var6 = 1;

      while (var6 != 0) {
         var6 = 0;

         for (float[] var8 : var0) {
            int var9 = var1 - var3 < var8[2] && var1 + var3 > var8[0] && var5 - var4 < var8[3] && var5 + var4 > var8[1] ? 1 : 0;
            if (var9 != 0) {
               var5 = var8[1] - var4 - 2.0F;
               var6 = 1;
            }
         }
      }

      var0.add(new float[]{var1 - var3, var5 - var4, var1 + var3, var5 + var4});
      return var5;
   }

   public static double A(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public record w(int x, int y, int z) {
   }
}
