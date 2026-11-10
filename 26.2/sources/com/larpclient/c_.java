package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public record c_(List<JsonObject> items, int total, int offset, int limit) {
   public static c_ validate(JsonObject var0) {
      try {
         int var1 = var0.get("total").getAsInt();
         int var2 = var0.get("offset").getAsInt();
         int var3 = var0.get("limit").getAsInt();
         if (var1 >= 0 && var2 >= 0 && var3 >= 1 && var3 <= 100) {
            JsonArray var4 = var0.getAsJsonArray("items");
            if (var4.size() > var3) {
               throw bh.o();
            }

            ArrayList var5 = new ArrayList();

            for (JsonElement var7 : var4) {
               JsonObject var8 = km.I(var7).deepCopy();

               for (String var10 : List.of("description", "author", "minecraftVersion", "larpVersion")) {
                  if (!var8.has(var10) || var8.get(var10).isJsonNull()) {
                     var8.addProperty(var10, "");
                  }
               }

               ut.A(var8, "id");
               km.r(km.a(var8, "name", 32));
               ut.o(var8);
               km.a(var8, "description", 4000);
               km.a(var8, "author", 160);
               km.a(var8, "minecraftVersion", 40);
               km.a(var8, "larpVersion", 40);
               var5.add(var8.deepCopy());
            }

            return new c_(List.copyOf(var5), var1, var2, var3);
         } else {
            throw bh.o();
         }
      } catch (RuntimeException var11) {
         throw bh.o();
      }
   }
}
