package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.Category;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class fc {
   public static long S;
   public static String K = "RENDER";
   public static final fc.t e = new fc.t("Modules", "", "", 0, 0);
   public static fc.t J = e;
   public static fc.p h = new fc.p(new fc.g(1.0, 0.05), false, true, false);
   public static final Map<String, fc.n> Q = new LinkedHashMap<>();
   public static final Map<String, fc.g> m = new LinkedHashMap<>();

   public static String O(String var0) {
      String var1 = switch (var0) {
         case "Modules", "Configs", "Settings" -> var0;
         case "Appearance", "Account", "HUD Editor" -> "Settings";
         case "Local", "Cloud", "Import", "Marketplace", "Experimental" -> "Configs";
         default -> "Modules";
      };
      if (!var1.equals(var0)) {
         r4.V("GUI", "Restored obsolete destination {} as {}", var0, var1);
      }

      return var1;
   }

   public static JsonObject e() {
      JsonObject var0 = new JsonObject();
      var0.addProperty("category", K);
      var0.add("clickGui", J.save());
      var0.addProperty("accent", rv.e);
      var0.addProperty("secondary", rv.GQ);
      var0.addProperty("bloom", jk.u.name());
      var0.addProperty("blur", jk.E.name());
      var0.addProperty("bloomIntensity", jk.L);
      var0.addProperty("bloomRadius", jk.s);
      JsonObject var1 = new JsonObject();
      m.forEach((var1x, var2x) -> {
         JsonArray var3x = new JsonArray();
         var3x.add(var2x.x());
         var3x.add(var2x.y());
         var1.add(var1x, var3x);
      });
      var0.add("hud", var1);
      JsonObject var2 = new JsonObject();
      Q.forEach((var1x, var2x) -> {
         JsonObject var3x = new JsonObject();
         var3x.addProperty("scale", var2x.scale());
         var3x.addProperty("hidden", var2x.hidden());
         var2.add(var1x, var3x);
      });
      var0.add("hudStyles", var2);
      JsonObject var3 = new JsonObject();
      var3.addProperty("x", h.panel().x());
      var3.addProperty("y", h.panel().y());
      var3.addProperty("collapsed", h.collapsed());
      var3.addProperty("snapping", h.snapping());
      var3.addProperty("grid", h.grid());
      var0.add("hudEditor", var3);
      return var0;
   }

   private static float X(JsonElement var0, float var1, float var2) {
      float var3 = var0.getAsFloat();
      if (!Float.isFinite(var3)) {
         throw new IllegalArgumentException("Nonfinite post setting");
      } else {
         return Math.clamp(var3, var1, var2);
      }
   }

   public static void H(JsonObject var0) {
      S++;
      Q.clear();
      m.clear();
      h = new fc.p(new fc.g(1.0, 0.05), false, true, false);
      J = e;

      for (Entry var2 : var0.entrySet()) {
         try {
            JsonElement var3 = (JsonElement)var2.getValue();
            switch ((String)var2.getKey()) {
               case "category":
                  K = var3.getAsString().equals("COSMETICS")
                     ? "COSMETICS"
                     : (
                        var3.getAsString().equals("BASE_FINDING")
                           ? "BASE_FINDING"
                           : (
                              var3.getAsString().equals("MISC")
                                 ? "MISC"
                                 : (
                                    var3.getAsString().equals("DONUT_RIGS")
                                       ? "DONUT_RIGS"
                                       : (var3.getAsString().equals("WORLD") ? "RENDER" : Category.valueOf(var3.getAsString()).name())
                                 )
                           )
                     );
                  break;
               case "clickGui":
                  J = fc.t.load(var3.getAsJsonObject());
                  break;
               case "accent":
                  rv.e = var3.getAsInt();
                  break;
               case "secondary":
                  rv.GQ = var3.getAsInt();
                  break;
               case "bloom":
                  jk.u = jk.x.valueOf(var3.getAsString());
                  break;
               case "bloomIntensity":
                  jk.L = X(var3, 0.0F, 2.0F);
                  break;
               case "bloomRadius":
                  jk.s = X(var3, 0.5F, 3.0F);
                  break;
               case "blur":
                  jk.E = jk.x.valueOf(var3.getAsString());
                  break;
               case "hudEditor":
                  JsonObject var13 = var3.getAsJsonObject();
                  h = new fc.p(
                     new fc.g(var13.has("x") ? var13.get("x").getAsDouble() : 1.0, var13.has("y") ? var13.get("y").getAsDouble() : 0.05),
                     var13.has("collapsed") && var13.get("collapsed").getAsBoolean(),
                     !var13.has("snapping") || var13.get("snapping").getAsBoolean(),
                     var13.has("grid") && var13.get("grid").getAsBoolean()
                  );
                  break;
               case "hudStyles":
                  for (Entry var14 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonObject var15 = ((JsonElement)var14.getValue()).getAsJsonObject();
                        Q.put(
                           (String)var14.getKey(),
                           new fc.n(var15.has("scale") ? var15.get("scale").getAsDouble() : 1.0, var15.has("hidden") && var15.get("hidden").getAsBoolean())
                        );
                     } catch (RuntimeException var10) {
                        r4.m("Config", "Invalid HUD style {} ignored", var14.getKey());
                     }
                  }
                  break;
               case "hud":
                  for (Entry var7 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonArray var8 = ((JsonElement)var7.getValue()).getAsJsonArray();
                        m.put((String)var7.getKey(), new fc.g(var8.get(0).getAsDouble(), var8.get(1).getAsDouble()));
                     } catch (RuntimeException var9) {
                        r4.m("Config", "Invalid HUD position {} ignored: {}", var7.getKey(), var9.toString());
                     }
                  }
                  break;
               default:
                  r4.m("Config", "Unknown UI setting {} ignored", var2.getKey());
            }
         } catch (RuntimeException var11) {
            r4.m("Config", "Invalid UI setting {} ignored: {}", var2.getKey(), var11.toString());
         }
      }
   }

   public record g(double x, double y) {
      public g(double x, double y) {
         if (Double.isFinite(x) && Double.isFinite(y)) {
            x = Math.clamp(x, 0.0, 1.0);
            y = Math.clamp(y, 0.0, 1.0);
            this.x = x;
            this.y = y;
         } else {
            throw new IllegalArgumentException("Nonfinite HUD position");
         }
      }
   }

   public record n(double scale, boolean hidden) {
      public n(double scale, boolean hidden) {
         if (!Double.isFinite(scale)) {
            throw new IllegalArgumentException("Nonfinite HUD scale");
         }

         scale = Math.clamp(scale, 0.5, 2.5);
         this.scale = scale;
         this.hidden = hidden;
      }
   }

   public record p(fc.g panel, boolean collapsed, boolean snapping, boolean grid) {
   }

   public record t(String page, String module, String query, int moduleScroll, int settingScroll) {
      public t(String page, String module, String query, int moduleScroll, int settingScroll) {
         if (!Set.of("Modules", "Configs", "Settings").contains(page)) {
            throw new IllegalArgumentException("Unknown ClickGUI page");
         }

         if (module != null && query != null && module.length() <= 128 && query.length() <= 256) {
            if (module.equals("Trails") || module.equals("BreadCrumbs")) {
               module = "Player Trail";
            }

            moduleScroll = Math.clamp(moduleScroll, 0, 100000);
            settingScroll = Math.clamp(settingScroll, 0, 100000);
            this.page = page;
            this.module = module;
            this.query = query;
            this.moduleScroll = moduleScroll;
            this.settingScroll = settingScroll;
         } else {
            throw new IllegalArgumentException("Invalid ClickGUI selection/search");
         }
      }

      public JsonObject save() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("page", this.page);
         var1.addProperty("module", this.module);
         var1.addProperty("query", this.query);
         var1.addProperty("moduleScroll", this.moduleScroll);
         var1.addProperty("settingScroll", this.settingScroll);
         return var1;
      }

      public static fc.t load(JsonObject var0) {
         return new fc.t(
            fc.O(var0.has("page") ? var0.get("page").getAsString() : "Modules"),
            var0.has("module") ? var0.get("module").getAsString() : "",
            var0.has("query") ? var0.get("query").getAsString() : "",
            var0.has("moduleScroll") ? var0.get("moduleScroll").getAsInt() : 0,
            var0.has("settingScroll") ? var0.get("settingScroll").getAsInt() : 0
         );
      }
   }
}
