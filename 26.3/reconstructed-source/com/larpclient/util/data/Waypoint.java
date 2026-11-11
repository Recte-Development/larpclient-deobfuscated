package com.larpclient.util.data;

import com.larpclient.util.world.Coordinates;
import java.util.Locale;

public record Waypoint(String id, String name, int x, int y, int z, String dimension, int color, boolean visible) {
   public static final int MAX_NAME = 32;
   public static final int HORIZONTAL_LIMIT = 30000000;
   public static final int VERTICAL_LIMIT = 4096;
   public static final String OVERWORLD = "minecraft:overworld";

   public Waypoint(String id, String name, int x, int y, int z, String dimension, int color, boolean visible) {
      if (id != null && !id.isBlank()) {
         if (name == null || name.isBlank() || name.length() > 32 || !name.equals(cleanName(name))) {
            throw new IllegalArgumentException("Invalid waypoint name");
         }

         if (Math.abs(x) > 30000000 || Math.abs(z) > 30000000 || Math.abs(y) > 4096) {
            throw new IllegalArgumentException("Waypoint outside the world");
         }

         if (dimension != null && !dimension.isBlank()) {
            color |= -16777216;
            this.id = id;
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.dimension = dimension;
            this.color = color;
            this.visible = visible;
         } else {
            throw new IllegalArgumentException("Waypoint without a dimension");
         }
      } else {
         throw new IllegalArgumentException("Waypoint without an id");
      }
   }

   public Waypoint withVisible(boolean var1) {
      return new Waypoint(this.id, this.name, this.x, this.y, this.z, this.dimension, this.color, var1);
   }

   public Waypoint edited(String var1, Coordinates.BlockPos var2, int var3) {
      return new Waypoint(this.id, var1, var2.blockX(), var2.blockY(), var2.blockZ(), this.dimension, var3, this.visible);
   }

   public Coordinates.BlockPos position() {
      return new Coordinates.BlockPos(this.x, this.y, this.z);
   }

   public static String cleanName(String var0) {
      if (var0 == null) {
         return "";
      }

      StringBuilder var1 = new StringBuilder();
      var0.codePoints().filter(var0x -> !Character.isISOControl(var0x) && var0x != 167).forEach(var1x -> {
         if (var1.length() + Character.charCount(var1x) <= 32) {
            var1.appendCodePoint(var1x);
         }
      });
      return var1.toString().strip();
   }

   public static String dimensionLabel(String var0) {
      return switch (var0) {
         case "minecraft:overworld" -> "Overworld";
         case "minecraft:the_nether" -> "Nether";
         case "minecraft:the_end" -> "The End";
         default -> {
            String var3 = var0.substring(var0.indexOf(58) + 1).replace('_', ' ');
            yield var3.isEmpty() ? var0 : var3.substring(0, 1).toUpperCase(Locale.ROOT) + var3.substring(1);
         }
      };
   }
}
