package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.Module;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public final class yj {
   private static final Pattern e = Pattern.compile(
      "^(?:license[_ -]?key|(?:access|refresh|install|session)[_ -]?token|token|password|client[_ -]?secret|authorization|auth(?:\\.json)?|hwid)$", 2
   );
   private static final Pattern W = Pattern.compile("-----BEGIN .*PRIVATE KEY-----|\\bBearer\\s+\\S+", 2);

   public static String S(String var0) {
      if (var0 != null && var0.matches("[A-Za-z0-9 _-]{1,32}") && !var0.isBlank()) {
         return var0;
      } else {
         throw new IllegalArgumentException("Profile name must be 1–32 letters, digits, spaces, _ or -");
      }
   }

   public static JsonObject J(String var0, JsonObject var1, List<Module> var2) {
      var1 = n(var1);
      JsonObject var3 = new JsonObject();
      var3.addProperty("name", S(var0));
      var3.addProperty("description", "");
      var3.add("tags", new JsonArray());
      var3.addProperty("minecraftVersion", xo.D);
      var3.addProperty("larpVersion", xo.y);
      var3.add("schemaVersion", var1.get("schema").deepCopy());
      JsonObject var4 = var1.deepCopy();
      if (var4.has("ui")) {
         JsonObject var5 = var4.getAsJsonObject("ui");
         if (var5.has("category") && Set.of("MISC", "DONUT_RIGS").contains(var5.get("category").getAsString())) {
            var5.addProperty("category", "PLAYER");
         }

         if (var5.has("category") && Set.of("BASE_FINDING", "COSMETICS").contains(var5.get("category").getAsString())) {
            var5.addProperty("category", "RENDER");
         }
      }

      if (var4.has("ui") && var4.getAsJsonObject("ui").has("clickGui")) {
         JsonObject var8 = var4.getAsJsonObject("ui").getAsJsonObject("clickGui");
         if (var8.has("page")) {
            String var6 = var8.get("page").getAsString();
            if (Set.of("Settings", "Appearance", "Account", "HUD Editor", "Local", "Cloud", "Import", "Marketplace", "Experimental").contains(var6)) {
               var6 = fc.O(var6);
            }

            var8.addProperty("page", var6.equals("Settings") ? "Appearance" : var6);
         }
      }

      var3.add("config", var4);
      return var3;
   }

   public static yj.j S(JsonObject var0, List<Module> var1) {
      String var2 = S(y(var0, "name", 32));
      String var3 = y(var0, "larpVersion", 40);
      String var4 = y(var0, "minecraftVersion", 40);

      for (String var6 : List.of(var3, var4)) {
         if (!var6.matches("[A-Za-z0-9][A-Za-z0-9._+-]{0,39}")) {
            throw new IllegalArgumentException("Invalid source version label");
         }
      }

      if (var0.has("schemaVersion")) {
         b(var0.get("schemaVersion"), 1.0, 2.147483647E9, true);
      }

      JsonObject var9 = n(W(var0.get("config")));
      if (!var3.equals(xo.y) || !var4.equals(xo.D)) {
         r4.V("Config", "Accepted profile from Larp {} / Minecraft {} for Larp {} / Minecraft {}; unavailable settings stay saved", var3, var4, xo.y, xo.D);
      }

      int var10 = 0;

      for (Entry var8 : var9.getAsJsonObject("modules").entrySet()) {
         if (((JsonElement)var8.getValue()).getAsJsonObject().has("enabled") && ((JsonElement)var8.getValue()).getAsJsonObject().get("enabled").getAsBoolean()) {
            var10++;
         }
      }

      return new yj.j(var2, var3, var4, var9.getAsJsonObject("modules").size(), var10, var9.deepCopy());
   }

   public static void o(JsonObject var0, List<Module> var1) {
      n(var0);
   }

   public static JsonObject n(JsonObject var0) {
      return Z(var0, true);
   }

   public static JsonObject F(JsonObject var0) {
      return Z(var0, false);
   }

   private static JsonObject Z(JsonObject var0, boolean var1) {
      J(var0, var1);
      JsonObject var2 = var0.deepCopy();
      if (!var2.has("modules") && !var2.has("schema")) {
         JsonObject var3 = var2;
         var2 = new JsonObject();
         var2.add("modules", var3);
      }

      G(var2, "schema", "ui", "modules");
      if (!var2.has("schema")) {
         var2.addProperty("schema", 2);
      }

      if (!var2.has("ui")) {
         var2.add("ui", new JsonObject());
      }

      b(var2.get("schema"), 1.0, 2.147483647E9, true);
      JsonObject var8 = W(var2.get("modules"));
      if (var8.size() > 200) {
         throw new IllegalArgumentException("Too many modules");
      }

      for (Entry var5 : var8.entrySet()) {
         JsonObject var6 = W((JsonElement)var5.getValue());
         G(var6, "enabled", "keybind", "settings", "legacySettings");
         w.a(var6);
         if (var6.has("enabled")) {
            V(var6.get("enabled"));
         }

         if (var6.has("keybind")) {
            b(var6.get("keybind"), -1.0, 348.0, true);
         }

         if (var6.has("settings") && W(var6.get("settings")).size() > 200) {
            throw new IllegalArgumentException("Too many settings");
         }
      }

      O(W(var2.get("ui")));
      var2 = w.D(var2);
      J(var2, var1);
      return var2;
   }

   public static void U(JsonElement var0) {
      J(var0, true);
   }

   public static void J(JsonElement var0, boolean var1) {
      J(var0, 0, new int[1], var1);
      if (var0.toString().getBytes(StandardCharsets.UTF_8).length > 131072) {
         throw new IllegalArgumentException("Profile exceeds 128 KiB");
      }
   }

   private static void J(JsonElement var0, int var1, int[] var2, boolean var3) {
      if (++var2[0] > 20000 || var1 > 10) {
         throw new IllegalArgumentException("Profile data is too complex");
      }

      if (var0 != null && !var0.isJsonNull()) {
         if (var0.isJsonObject()) {
            if (var0.getAsJsonObject().size() > 512) {
               throw new IllegalArgumentException("Too many profile fields");
            }

            for (Entry var9 : var0.getAsJsonObject().entrySet()) {
               String var6 = (String)var9.getKey();
               if (var6.isEmpty() || var6.length() > 128 || var6.chars().anyMatch(Character::isISOControl)) {
                  throw new IllegalArgumentException("Invalid profile field name");
               }

               if (Set.of("__proto__", "constructor", "prototype").contains(var6) || e.matcher(var6).matches()) {
                  throw new IllegalArgumentException("Remove unsafe or authentication fields before sharing");
               }

               J((JsonElement)var9.getValue(), var1 + 1, var2, var3);
            }
         } else if (var0.isJsonArray()) {
            if (var0.getAsJsonArray().size() > 512) {
               throw new IllegalArgumentException("Too many profile entries");
            }

            for (JsonElement var5 : var0.getAsJsonArray()) {
               J(var5, var1 + 1, var2, var3);
            }
         } else if (var0.getAsJsonPrimitive().isNumber()) {
            b(var0, -1.0E15, 1.0E15, false);
         } else if (var0.getAsJsonPrimitive().isString()) {
            String var7 = var0.getAsString();
            if (var7.length() > 8192 || var7.chars().anyMatch(var0x -> Character.isISOControl(var0x) && var0x != 9 && var0x != 10 && var0x != 13)) {
               throw new IllegalArgumentException("Invalid profile text");
            }

            if (var3 && W.matcher(var7).find()) {
               throw new IllegalArgumentException("Text that looks like a credential, such as \"Bearer ...\", cannot be shared. Change it, then share again.");
            }
         }
      }
   }

   private static void O(JsonObject var0) {
      for (String var2 : List.of("moduleFavorites", "favorites", "moduleFavoritesOpen")) {
         if (var0.has(var2)) {
            throw new IllegalArgumentException("Local preferences are not profile settings");
         }
      }

      for (Entry var12 : var0.entrySet()) {
         JsonElement var3 = (JsonElement)var12.getValue();
         switch ((String)var12.getKey()) {
            case "category":
               y(var0, (String)var12.getKey(), 128);
               break;
            case "accent":
            case "secondary":
               b(var3, -2.1474836E9F, 2.147483647E9, true);
               break;
            case "bloom":
            case "blur":
               y(var0, (String)var12.getKey(), 128);
               break;
            case "bloomIntensity":
               b(var3, 0.0, 2.0, false);
               break;
            case "bloomRadius":
               b(var3, 0.5, 3.0, false);
               break;
            case "hudEditor":
               JsonObject var14 = W(var3);

               for (String var19 : List.of("x", "y")) {
                  if (var14.has(var19)) {
                     b(var14.get(var19), 0.0, 1.0, false);
                  }
               }

               for (String var20 : List.of("collapsed", "snapping", "grid")) {
                  if (var14.has(var20)) {
                     V(var14.get(var20));
                  }
               }
               break;
            case "clickGui":
               JsonObject var13 = W(var3);
               if (var13.has("page")) {
                  y(var13, "page", 128);
               }

               if (var13.has("module")) {
                  y(var13, "module", 128);
               }

               if (var13.has("query")) {
                  y(var13, "query", 256);
               }

               for (String var18 : List.of("moduleScroll", "settingScroll")) {
                  if (var13.has(var18)) {
                     b(var13.get(var18), 0.0, 100000.0, true);
                  }
               }
               break;
            case "hud":
            case "hudStyles":
               JsonObject var6 = W(var3);
               if (var6.size() > 100) {
                  throw new IllegalArgumentException("Too many HUD entries");
               }

               for (Entry var8 : var6.entrySet()) {
                  if (((String)var12.getKey()).equals("hud")) {
                     if (!((JsonElement)var8.getValue()).isJsonArray() || ((JsonElement)var8.getValue()).getAsJsonArray().size() != 2) {
                        throw new IllegalArgumentException("Invalid HUD position");
                     }

                     for (JsonElement var10 : ((JsonElement)var8.getValue()).getAsJsonArray()) {
                        b(var10, 0.0, 1.0, false);
                     }
                  } else {
                     JsonObject var9 = W((JsonElement)var8.getValue());
                     if (var9.has("scale")) {
                        b(var9.get("scale"), 0.5, 2.5, false);
                     }

                     if (var9.has("hidden")) {
                        V(var9.get("hidden"));
                     }
                  }
               }
         }
      }
   }

   public static JsonObject W(JsonElement var0) {
      if (var0 != null && var0.isJsonObject()) {
         return var0.getAsJsonObject();
      } else {
         throw new IllegalArgumentException("Expected config object");
      }
   }

   public static void G(JsonObject var0, String... var1) {
      Set var2 = Set.of(var1);
      if (!var2.containsAll(var0.keySet())) {
         throw new IllegalArgumentException("Unsupported config fields");
      }
   }

   public static String y(JsonObject var0, String var1, int var2) {
      JsonElement var3 = var0.get(var1);
      if (var3 != null
         && var3.isJsonPrimitive()
         && var3.getAsJsonPrimitive().isString()
         && var3.getAsString().length() <= var2
         && !var3.getAsString().chars().anyMatch(var0x -> var0x < 32 && var0x != 10 && var0x != 13 && var0x != 9)) {
         return var3.getAsString().replace('§', ' ');
      } else {
         throw oj.f();
      }
   }

   public static void b(JsonElement var0, double var1, double var3, boolean var5) {
      if (var0 != null && var0.isJsonPrimitive() && var0.getAsJsonPrimitive().isNumber()) {
         double var6 = var0.getAsDouble();
         if (!Double.isFinite(var6) || var6 < var1 || var6 > var3 || var5 && var6 != Math.rint(var6)) {
            throw new IllegalArgumentException("Number outside supported range");
         }
      } else {
         throw new IllegalArgumentException("Expected number");
      }
   }

   public static boolean V(JsonElement var0) {
      if (var0 != null && var0.isJsonPrimitive() && var0.getAsJsonPrimitive().isBoolean()) {
         return var0.getAsBoolean();
      } else {
         throw new IllegalArgumentException("Expected boolean");
      }
   }

   public static String e(JsonElement var0, String... var1) {
      if (var0 != null && var0.isJsonPrimitive() && var0.getAsJsonPrimitive().isString() && List.of(var1).contains(var0.getAsString())) {
         return var0.getAsString();
      } else {
         throw new IllegalArgumentException("Unsupported value");
      }
   }

   public record j(String name, String larpVersion, String minecraftVersion, int modules, int enabled, JsonObject config) {
   }
}
