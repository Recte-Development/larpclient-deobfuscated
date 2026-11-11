package com.larpclient.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.network.ApiException;
import com.larpclient.network.ApiResponseValidator;
import java.util.ArrayList;
import java.util.List;

public record ConfigListPage(List<JsonObject> items, int total, int offset, int limit) {
   public static ConfigListPage validate(JsonObject var0) {
      try {
         int var1 = var0.get("total").getAsInt();
         int var2 = var0.get("offset").getAsInt();
         int var3 = var0.get("limit").getAsInt();
         if (var1 >= 0 && var2 >= 0 && var3 >= 1 && var3 <= 100) {
            JsonArray var4 = var0.getAsJsonArray("items");
            if (var4.size() > var3) {
               throw ApiException.invalidResponse();
            }

            ArrayList var5 = new ArrayList();

            for (JsonElement var7 : var4) {
               JsonObject var8 = ProfileValidator.asObject(var7).deepCopy();

               for (String var10 : List.of("description", "author", "minecraftVersion", "larpVersion")) {
                  if (!var8.has(var10) || var8.get(var10).isJsonNull()) {
                     var8.addProperty(var10, "");
                  }
               }

               ApiResponseValidator.readId(var8, "id");
               ProfileValidator.validateName(ProfileValidator.readString(var8, "name", 32));
               ApiResponseValidator.readShareCode(var8);
               ProfileValidator.readString(var8, "description", 4000);
               ProfileValidator.readString(var8, "author", 160);
               ProfileValidator.readString(var8, "minecraftVersion", 40);
               ProfileValidator.readString(var8, "larpVersion", 40);
               var5.add(var8.deepCopy());
            }

            return new ConfigListPage(List.copyOf(var5), var1, var2, var3);
         } else {
            throw ApiException.invalidResponse();
         }
      } catch (RuntimeException var11) {
         throw ApiException.invalidResponse();
      }
   }
}
