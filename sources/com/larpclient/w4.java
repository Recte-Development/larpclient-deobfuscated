package com.larpclient;

import com.google.gson.JsonObject;

public final class w4 {
   public static JsonObject s(JsonObject var0) {
      if (var0 != null && var0.toString().length() <= 4096) {
         JsonObject var1 = new JsonObject();
         if (var0.has("china_hat")) {
            JsonObject var2 = var0.getAsJsonObject("china_hat");
            if (var2.get("version").getAsInt() != 1) {
               throw new IllegalArgumentException("Hat version");
            }

            JsonObject var3 = new JsonObject();
            var3.addProperty("version", 1);

            for (String var7 : new String[]{"enabled", "outline"}) {
               if (!var2.get(var7).isJsonPrimitive() || !var2.getAsJsonPrimitive(var7).isBoolean()) {
                  throw new IllegalArgumentException("Hat flag");
               }

               var3.add(var7, var2.get(var7));
            }

            for (String var13 : new String[]{"top", "rim"}) {
               double var8 = Y(var2, var13, -2.1474836E9F, 2.147483647E9);
               if (var8 != Math.rint(var8)) {
                  throw new IllegalArgumentException("Hat color");
               }

               var3.addProperty(var13, (int)var8);
            }

            var3.addProperty("radius", Y(var2, "radius", 0.25, 1.0));
            var3.addProperty("height", Y(var2, "height", 0.1, 0.7));
            var3.addProperty("offset", Y(var2, "offset", 0.0, 0.5));
            var3.addProperty("opacity", Y(var2, "opacity", 0.1, 1.0));
            var1.add("china_hat", var3);
         }

         return var1;
      } else {
         throw new IllegalArgumentException("Cosmetics too large");
      }
   }

   private static double Y(JsonObject var0, String var1, double var2, double var4) {
      if (var0.get(var1).isJsonPrimitive() && var0.getAsJsonPrimitive(var1).isNumber()) {
         double var6 = var0.get(var1).getAsDouble();
         if (Double.isFinite(var6) && !(var6 < var2) && !(var6 > var4)) {
            return var6;
         } else {
            throw new IllegalArgumentException("Hat range");
         }
      } else {
         throw new IllegalArgumentException("Hat number");
      }
   }
}
