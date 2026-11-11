package com.larpclient.config;

import com.google.gson.JsonObject;
import java.util.Set;

public final class WorldLyricsSettingsMigration {
   private static final Set<String> lEGACY_KEYS = Set.of(
      "Text List",
      "Count",
      "Minimum Radius",
      "Maximum Radius",
      "Minimum Height",
      "Maximum Height",
      "Minimum Spacing",
      "Base Scale",
      "Scale Variation",
      "Follow Speed",
      "Field Orientation",
      "Text Orientation",
      "Placement Bias",
      "Animation",
      "Float Speed",
      "Float Amount",
      "Rotation Amount",
      "Spawn Duration",
      "Fade Duration",
      "Lifetime",
      "Distance Fade",
      "Fade Start Distance",
      "Maximum Distance",
      "Depth Mode",
      "Opacity",
      "Color Mode",
      "Primary Color",
      "Secondary Color",
      "Shadow",
      "Glow Strength"
   );

   public static void migrate(JsonObject var0) {
      if (var0.get("WorldLyrics") instanceof JsonObject var1 && var1.get("settings") instanceof JsonObject var2) {
         copyIfAbsent(var2, "Count", "Density");
         copyIfAbsent(var2, "Base Scale", "Size");
         copyIfAbsent(var2, "Primary Color", "Color");
         if (!var2.has("Style") && var2.has("Animation") && var2.get("Animation").isJsonPrimitive()) {
            String var9 = var2.get("Animation").getAsString();
            var2.addProperty("Style", var9.equals("DREAMY") ? "Dreamy" : (var9.equals("FLOATING") ? "Floating" : "Calm"));
         }

         for (String var4 : lEGACY_KEYS) {
            if (var2.has(var4)) {
               JsonObject var5 = var1.get("legacySettings") instanceof JsonObject var6 ? var6 : new JsonObject();
               var5.add(var4, var2.remove(var4));
               var1.add("legacySettings", var5);
            }
         }
      }
   }

   private static void copyIfAbsent(JsonObject var0, String var1, String var2) {
      if (!var0.has(var2) && var0.has(var1)) {
         var0.add(var2, var0.get(var1).deepCopy());
      }
   }

   private WorldLyricsSettingsMigration() {
   }
}
