package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.larpclient.module.Category;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class c7 {
   public static long w;
   public static String c = "RENDER";
   public static final c7.x K = new c7.x("Modules", "", "", 0, 0);
   public static c7.x D = K;
   public static c7.d B = new c7.d(new c7.q(1.0, 0.05), false, true, false);
   public static final Map<String, c7.l> y = new LinkedHashMap<>();
   public static final Map<String, c7.q> v = new LinkedHashMap<>();

   public static String X(String var0) {
      String var1 = switch (var0) {
         case "Modules", "Configs", "Settings" -> var0;
         case "Appearance", "Account", "HUD Editor" -> "Settings";
         case "Local", "Cloud", "Import", "Marketplace", "Experimental" -> "Configs";
         default -> "Modules";
      };
      if (!var1.equals(var0)) {
         h.A("GUI", "Restored obsolete destination {} as {}", var0, var1);
      }

      return var1;
   }

   public static JsonObject w() {
      JsonObject var0 = new JsonObject();
      var0.addProperty("category", c);
      var0.add("clickGui", D.save());
      var0.addProperty("accent", ho.J);
      var0.addProperty("secondary", ho.ds);
      var0.addProperty("bloom", wa.h.name());
      var0.addProperty("blur", wa.n.name());
      var0.addProperty("bloomIntensity", wa.w);
      var0.addProperty("bloomRadius", wa.z);
      JsonObject var1 = new JsonObject();
      v.forEach((var1x, var2x) -> {
         JsonArray var3x = new JsonArray();
         var3x.add(var2x.x());
         var3x.add(var2x.y());
         var1.add(var1x, var3x);
      });
      var0.add("hud", var1);
      JsonObject var2 = new JsonObject();
      y.forEach((var1x, var2x) -> {
         JsonObject var3x = new JsonObject();
         var3x.addProperty("scale", var2x.scale());
         var3x.addProperty("hidden", var2x.hidden());
         if (var2x.dock() != null) {
            var3x.addProperty("dock", var2x.dock());
         }

         var2.add(var1x, var3x);
      });
      var0.add("hudStyles", var2);
      JsonObject var3 = new JsonObject();
      var3.addProperty("x", B.panel().x());
      var3.addProperty("y", B.panel().y());
      var3.addProperty("collapsed", B.collapsed());
      var3.addProperty("snapping", B.snapping());
      var3.addProperty("grid", B.grid());
      var0.add("hudEditor", var3);
      return var0;
   }

   private static float Z(JsonElement var0, float var1, float var2) {
      float var3 = var0.getAsFloat();
      if (!Float.isFinite(var3)) {
         throw new IllegalArgumentException("Nonfinite post setting");
      } else {
         return Math.clamp(var3, var1, var2);
      }
   }

   public static void S(JsonObject var0) {
      w++;
      y.clear();
      v.clear();
      B = new c7.d(new c7.q(1.0, 0.05), false, true, false);
      D = K;

      for (Entry var2 : var0.entrySet()) {
         try {
            JsonElement var3 = (JsonElement)var2.getValue();
            switch ((String)var2.getKey()) {
               case "category":
                  c = var3.getAsString().equals("COSMETICS")
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
                  D = c7.x.load(var3.getAsJsonObject());
                  break;
               case "accent":
                  ho.J = var3.getAsInt();
                  break;
               case "secondary":
                  ho.ds = var3.getAsInt();
                  break;
               case "bloom":
                  wa.h = wa.a.valueOf(var3.getAsString());
                  break;
               case "bloomIntensity":
                  wa.w = Z(var3, 0.0F, 2.0F);
                  break;
               case "bloomRadius":
                  wa.z = Z(var3, 0.5F, 3.0F);
                  break;
               case "blur":
                  wa.n = wa.a.valueOf(var3.getAsString());
                  break;
               case "hudEditor":
                  JsonObject var15 = var3.getAsJsonObject();
                  B = new c7.d(
                     new c7.q(var15.has("x") ? var15.get("x").getAsDouble() : 1.0, var15.has("y") ? var15.get("y").getAsDouble() : 0.05),
                     var15.has("collapsed") && var15.get("collapsed").getAsBoolean(),
                     !var15.has("snapping") || var15.get("snapping").getAsBoolean(),
                     var15.has("grid") && var15.get("grid").getAsBoolean()
                  );
                  break;
               case "hudStyles":
                  for (Entry var16 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonObject var17 = ((JsonElement)var16.getValue()).getAsJsonObject();
                        y.put(
                           (String)var16.getKey(),
                           new c7.l(
                              var17.has("scale") ? var17.get("scale").getAsDouble() : 1.0,
                              var17.has("hidden") && var17.get("hidden").getAsBoolean(),
                              var17.get("dock") instanceof JsonPrimitive var9 && var9.isString() ? var9.getAsString() : null
                           )
                        );
                     } catch (RuntimeException var12) {
                        h.g("Config", "Invalid HUD style {} ignored", var16.getKey());
                     }
                  }
                  break;
               case "hud":
                  for (Entry var7 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonArray var8 = ((JsonElement)var7.getValue()).getAsJsonArray();
                        v.put((String)var7.getKey(), new c7.q(var8.get(0).getAsDouble(), var8.get(1).getAsDouble()));
                     } catch (RuntimeException var11) {
                        h.g("Config", "Invalid HUD position {} ignored: {}", var7.getKey(), var11.toString());
                     }
                  }
                  break;
               default:
                  h.g("Config", "Unknown UI setting {} ignored", var2.getKey());
            }
         } catch (RuntimeException var13) {
            h.g("Config", "Invalid UI setting {} ignored: {}", var2.getKey(), var13.toString());
         }
      }
   }

   public record d(c7.q panel, boolean collapsed, boolean snapping, boolean grid) {
   }

   public record l(double scale, boolean hidden, String dock) {
      public static final double MIN_SCALE = 0.2;
      public static final double MAX_SCALE = 4.0;
      public static final double CLOUD_MIN_SCALE = 0.5;
      public static final double CLOUD_MAX_SCALE = 2.5;

      public l(double scale, boolean hidden, String dock) {
         if (!Double.isFinite(scale)) {
            throw new IllegalArgumentException("Nonfinite HUD scale");
         }

         scale = Math.clamp(scale, 0.2, 4.0);
         if (dock != null && (dock.isBlank() || dock.length() > 64)) {
            dock = null;
         }

         this.scale = scale;
         this.hidden = hidden;
         this.dock = dock;
      }

      public l(double var1, boolean var3) {
         this(var1, var3, null);
      }
   }

   public record q(double x, double y) {
      public q(double x, double y) {
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

   public record x(String page, String module, String query, int moduleScroll, int settingScroll) {
      public x(String page, String module, String query, int moduleScroll, int settingScroll) {
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

      public static c7.x load(JsonObject var0) {
         return new c7.x(
            c7.X(var0.has("page") ? var0.get("page").getAsString() : "Modules"),
            var0.has("module") ? var0.get("module").getAsString() : "",
            var0.has("query") ? var0.get("query").getAsString() : "",
            var0.has("moduleScroll") ? var0.get("moduleScroll").getAsInt() : 0,
            var0.has("settingScroll") ? var0.get("settingScroll").getAsInt() : 0
         );
      }
   }
}
