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

public final class bh {
   public static long k;
   public static String C = "RENDER";
   public static final bh.v i = new bh.v("Modules", "", "", 0, 0);
   public static bh.v c = i;
   public static bh.f z = new bh.f(new bh.u(1.0, 0.05), false, true, false);
   public static final Map<String, bh.b> F = new LinkedHashMap<>();
   public static final Map<String, bh.u> Z = new LinkedHashMap<>();

   public static String a(String var0) {
      String var1 = switch (var0) {
         case "Modules", "Configs", "Settings" -> var0;
         case "Appearance", "Account", "HUD Editor" -> "Settings";
         case "Local", "Cloud", "Import", "Marketplace", "Experimental" -> "Configs";
         default -> "Modules";
      };
      if (!var1.equals(var0)) {
         _q.z("GUI", "Restored obsolete destination {} as {}", var0, var1);
      }

      return var1;
   }

   public static JsonObject Z() {
      JsonObject var0 = new JsonObject();
      var0.addProperty("category", C);
      var0.add("clickGui", c.save());
      var0.addProperty("accent", com.larpclient.c.y);
      var0.addProperty("secondary", com.larpclient.c.t);
      var0.addProperty("bloom", nl.x.name());
      var0.addProperty("blur", nl.Q.name());
      var0.addProperty("bloomIntensity", nl.b);
      var0.addProperty("bloomRadius", nl.C);
      JsonObject var1 = new JsonObject();
      Z.forEach((var1x, var2x) -> {
         JsonArray var3x = new JsonArray();
         var3x.add(var2x.x());
         var3x.add(var2x.y());
         var1.add(var1x, var3x);
      });
      var0.add("hud", var1);
      JsonObject var2 = new JsonObject();
      F.forEach((var1x, var2x) -> {
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
      var3.addProperty("x", z.panel().x());
      var3.addProperty("y", z.panel().y());
      var3.addProperty("collapsed", z.collapsed());
      var3.addProperty("snapping", z.snapping());
      var3.addProperty("grid", z.grid());
      var0.add("hudEditor", var3);
      return var0;
   }

   private static float D(JsonElement var0, float var1, float var2) {
      float var3 = var0.getAsFloat();
      if (!Float.isFinite(var3)) {
         throw new IllegalArgumentException("Nonfinite post setting");
      } else {
         return Math.clamp(var3, var1, var2);
      }
   }

   public static void n(JsonObject var0) {
      k++;
      F.clear();
      Z.clear();
      z = new bh.f(new bh.u(1.0, 0.05), false, true, false);
      c = i;

      for (Entry var2 : var0.entrySet()) {
         try {
            JsonElement var3 = (JsonElement)var2.getValue();
            switch ((String)var2.getKey()) {
               case "category":
                  C = var3.getAsString().equals("COSMETICS")
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
                  c = bh.v.load(var3.getAsJsonObject());
                  break;
               case "accent":
                  com.larpclient.c.y = var3.getAsInt();
                  break;
               case "secondary":
                  com.larpclient.c.t = var3.getAsInt();
                  break;
               case "bloom":
                  nl.x = nl.q.valueOf(var3.getAsString());
                  break;
               case "bloomIntensity":
                  nl.b = D(var3, 0.0F, 2.0F);
                  break;
               case "bloomRadius":
                  nl.C = D(var3, 0.5F, 3.0F);
                  break;
               case "blur":
                  nl.Q = nl.q.valueOf(var3.getAsString());
                  break;
               case "hudEditor":
                  JsonObject var15 = var3.getAsJsonObject();
                  z = new bh.f(
                     new bh.u(var15.has("x") ? var15.get("x").getAsDouble() : 1.0, var15.has("y") ? var15.get("y").getAsDouble() : 0.05),
                     var15.has("collapsed") && var15.get("collapsed").getAsBoolean(),
                     !var15.has("snapping") || var15.get("snapping").getAsBoolean(),
                     var15.has("grid") && var15.get("grid").getAsBoolean()
                  );
                  break;
               case "hudStyles":
                  for (Entry var16 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonObject var17 = ((JsonElement)var16.getValue()).getAsJsonObject();
                        F.put(
                           (String)var16.getKey(),
                           new bh.b(
                              var17.has("scale") ? var17.get("scale").getAsDouble() : 1.0,
                              var17.has("hidden") && var17.get("hidden").getAsBoolean(),
                              var17.get("dock") instanceof JsonPrimitive var9 && var9.isString() ? var9.getAsString() : null
                           )
                        );
                     } catch (RuntimeException var12) {
                        _q.R("Config", "Invalid HUD style {} ignored", var16.getKey());
                     }
                  }
                  break;
               case "hud":
                  for (Entry var7 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonArray var8 = ((JsonElement)var7.getValue()).getAsJsonArray();
                        Z.put((String)var7.getKey(), new bh.u(var8.get(0).getAsDouble(), var8.get(1).getAsDouble()));
                     } catch (RuntimeException var11) {
                        _q.R("Config", "Invalid HUD position {} ignored: {}", var7.getKey(), var11.toString());
                     }
                  }
                  break;
               default:
                  _q.R("Config", "Unknown UI setting {} ignored", var2.getKey());
            }
         } catch (RuntimeException var13) {
            _q.R("Config", "Invalid UI setting {} ignored: {}", var2.getKey(), var13.toString());
         }
      }
   }

   public record b(double scale, boolean hidden, String dock) {
      public static final double MIN_SCALE = 0.2;
      public static final double MAX_SCALE = 4.0;
      public static final double CLOUD_MIN_SCALE = 0.5;
      public static final double CLOUD_MAX_SCALE = 2.5;

      public b(double scale, boolean hidden, String dock) {
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

      public b(double var1, boolean var3) {
         this(var1, var3, null);
      }
   }

   public record f(bh.u panel, boolean collapsed, boolean snapping, boolean grid) {
   }

   public record u(double x, double y) {
      public u(double x, double y) {
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

   public record v(String page, String module, String query, int moduleScroll, int settingScroll) {
      public v(String page, String module, String query, int moduleScroll, int settingScroll) {
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

      public static bh.v load(JsonObject var0) {
         return new bh.v(
            bh.a(var0.has("page") ? var0.get("page").getAsString() : "Modules"),
            var0.has("module") ? var0.get("module").getAsString() : "",
            var0.has("query") ? var0.get("query").getAsString() : "",
            var0.has("moduleScroll") ? var0.get("moduleScroll").getAsInt() : 0,
            var0.has("settingScroll") ? var0.get("settingScroll").getAsInt() : 0
         );
      }
   }
}
