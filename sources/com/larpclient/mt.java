package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class mt {
   public static void q(JsonObject var0) {
      if (var0.get("CustomSky") instanceof JsonObject var1 && var1.get("settings") instanceof JsonObject var2) {
         JsonElement var12 = var2.remove("Sun and Moon");
         if (var12 != null) {
            if (!var2.has("Show Vanilla Sun")) {
               var2.add("Show Vanilla Sun", var12.deepCopy());
            }

            if (!var2.has("Show Vanilla Moon")) {
               var2.add("Show Vanilla Moon", var12.deepCopy());
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
   }

   private mt() {
   }
}
