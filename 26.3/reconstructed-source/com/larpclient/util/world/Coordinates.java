package com.larpclient.util.world;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public final class Coordinates {
   public static final double mAX_SCALE_DISTANCE = 64.0;
   private static final float lABEL_GAP = 2.0F;
   public static final String fORMAT_HINT = "Enter coordinates as X Y Z or X Z";
   private static final Pattern nUMBER_PATTERN = Pattern.compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)");

   private Coordinates() {
   }

   public static Coordinates.BlockPos parse(String var0, Coordinates.BlockPos var1) {
      String var2 = var0 == null ? "" : var0.strip().replaceAll("^[(\\[]+|[)\\]]+$", "").strip();
      if (var2.isEmpty()) {
         return var1;
      }

      String[] var3 = var2.split("[\\s,;]+");
      if (var3.length != 2 && var3.length != 3) {
         throw new IllegalArgumentException("Enter coordinates as X Y Z or X Z");
      }

      int var4 = parseComponent(var3[0], var1.blockX(), 30000000);
      int var5 = var3.length == 3 ? parseComponent(var3[1], var1.blockY(), 4096) : var1.blockY();
      int var6 = parseComponent(var3[var3.length - 1], var1.blockZ(), 30000000);
      return new Coordinates.BlockPos(var4, var5, var6);
   }

   private static int parseComponent(String var0, int var1, int var2) {
      boolean var3 = var0.startsWith("~");
      String var4 = var3 ? var0.substring(1) : var0;
      if (var3 && var4.isEmpty()) {
         return var1;
      } else if (!nUMBER_PATTERN.matcher(var4).matches()) {
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

   public static String format(Coordinates.BlockPos var0) {
      return var0.blockX() + " " + var0.blockY() + " " + var0.blockZ();
   }

   public static String parse(double var0) {
      return var0 < 999.5 ? Math.round(var0) + "m" : String.format(Locale.ROOT, "%.1fkm", var0 / 1000.0);
   }

   public static double distanceScale(double var0) {
      return var0 > 64.0 ? 64.0 / var0 : 1.0;
   }

   public static float placeLabel(List<float[]> var0, float var1, float var2, float var3, float var4) {
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

   public static double lerp(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public record BlockPos(int x, int y, int z) {
      private final int blockX;
      private final int blockY;
      private final int blockZ;

      public BlockPos(int x, int y, int z) {
         this.blockX = x;
         this.blockY = y;
         this.blockZ = z;
      }
   }
}
