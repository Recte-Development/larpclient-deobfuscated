package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class cj {
   public static void Q(JsonObject var0) {
      if (var0.get("CustomSky") instanceof JsonObject var1 && var1.get("settings") instanceof JsonObject var2) {
         JsonElement var13 = var2.remove("Sun and Moon");
         if (var13 != null) {
            if (!var2.has("Show Vanilla Sun")) {
               var2.add("Show Vanilla Sun", var13.deepCopy());
            }

            if (!var2.has("Show Vanilla Moon")) {
               var2.add("Show Vanilla Moon", var13.deepCopy());
            }
         }
      }

      if (var0.get("SelfVisuals") instanceof JsonObject var6
         && var6.get("settings") instanceof JsonObject var9
         && var9.get("Style") != null
         && var9.get("Style").isJsonPrimitive()
         && var9.get("Style").getAsString().equals("Theme Gradient")) {
         var9.addProperty("Style", "Rainbow Outline");
      }

      for (String var4 : new String[]{"Trails", "BreadCrumbs"}) {
         JsonElement var5 = var0.remove(var4);
         if (var5 != null && !var0.has("Player Trail")) {
            var0.add("Player Trail", var5);
         }
      }

      JsonElement var8 = var0.remove("Distance Fog");
      if (var8 != null && !var0.has("Fog")) {
         var0.add("Fog", var8);
      }

      if (var0.get("HUD") instanceof JsonObject var11 && var11.get("settings") instanceof JsonObject var17) {
         var17.remove("Speed Shared Background");
         if (!var17.has("Speed Card Background")) {
            var17.addProperty(
               "Speed Card Background", !m(var17, "Speed Header", -1291845632, -583713981) && !m(var17, "Speed Background", -1291845632, -652929251)
            );
         }
      }
   }

   private static boolean m(JsonObject var0, String var1, int... var2) {
      JsonElement var3 = var0.get(var1);
      if (var3 != null && var3.isJsonPrimitive() && var3.getAsJsonPrimitive().isNumber()) {
         long var4 = var3.getAsLong() & 4294967295L;

         for (int var9 : var2) {
            if (var4 == (var9 & 4294967295L)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private cj() {
   }
}
