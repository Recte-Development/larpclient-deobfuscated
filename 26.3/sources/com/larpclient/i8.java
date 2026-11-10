package com.larpclient;

import com.google.gson.JsonObject;

public record i8(String skin, String cape, boolean slim, boolean hideCape, String name, JsonObject cosmetics) {
   public static final i8 EMPTY = new i8("", "", false, false, "", new JsonObject());

   public JsonObject json() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("version", 1);
      var1.addProperty("skin", this.skin);
      var1.addProperty("cape", this.cape);
      var1.addProperty("slim", this.slim);
      var1.addProperty("hideCape", this.hideCape);
      var1.addProperty("name", this.name);
      var1.add("cosmetics", this.cosmetics.deepCopy());
      return var1;
   }

   public static i8 parse(JsonObject var0) {
      if (var0.get("version").getAsInt() != 1) {
         throw new IllegalArgumentException("Unsupported appearance version");
      } else {
         String var1 = var0.get("name").getAsString();
         if (!var1.isEmpty() && !var1.matches("[A-Za-z0-9_]{1,16}")) {
            throw new IllegalArgumentException("Invalid cosmetic name");
         } else {
            String var2 = var0.get("skin").getAsString();
            String var3 = var0.get("cape").getAsString();
            if (var2.length() > 175000 || var3.length() > 175000) {
               throw new IllegalArgumentException("Image too large");
            } else if (!var0.getAsJsonPrimitive("slim").isBoolean()) {
               throw new IllegalArgumentException("Invalid skin model");
            } else if (var0.has("hideCape") && !var0.getAsJsonPrimitive("hideCape").isBoolean()) {
               throw new IllegalArgumentException("Invalid cape mode");
            } else {
               JsonObject var4 = q4.V(var0.getAsJsonObject("cosmetics"));
               if (var4.toString().length() > 4096) {
                  throw new IllegalArgumentException("Cosmetics too large");
               } else {
                  return new i8(var2, var3, var0.get("slim").getAsBoolean(), var0.has("hideCape") && var0.get("hideCape").getAsBoolean(), var1, var4.deepCopy());
               }
            }
         }
      }
   }
}
