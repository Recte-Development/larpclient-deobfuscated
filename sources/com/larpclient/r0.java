package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public record r0(List<JsonObject> items, int total, int offset, int limit) {
   public static r0 validate(JsonObject var0) {
      try {
         int var1 = var0.get("total").getAsInt();
         int var2 = var0.get("offset").getAsInt();
         int var3 = var0.get("limit").getAsInt();
         if (var1 >= 0 && var2 >= 0 && var3 >= 1 && var3 <= 100) {
            JsonArray var4 = var0.getAsJsonArray("items");
            if (var4.size() > var3) {
               throw oj.f();
            }

            ArrayList var5 = new ArrayList();

            for (JsonElement var7 : var4) {
               JsonObject var8 = yj.W(var7).deepCopy();

               for (String var10 : List.of("description", "author", "minecraftVersion", "larpVersion")) {
                  if (!var8.has(var10) || var8.get(var10).isJsonNull()) {
                     var8.addProperty(var10, "");
                  }
               }

               _4.Y(var8, "id");
               yj.S(yj.y(var8, "name", 32));
               _4.g(var8);
               yj.y(var8, "description", 4000);
               yj.y(var8, "author", 160);
               yj.y(var8, "minecraftVersion", 40);
               yj.y(var8, "larpVersion", 40);
               var5.add(var8.deepCopy());
            }

            return new r0(List.copyOf(var5), var1, var2, var3);
         } else {
            throw oj.f();
         }
      } catch (RuntimeException var11) {
         throw oj.f();
      }
   }
}
