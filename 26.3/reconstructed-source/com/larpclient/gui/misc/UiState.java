package com.larpclient.gui.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.Category;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.LarpLog;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class UiState {
   public static long revision;
   public static String category = "RENDER";
   public static final UiState.ClickGuiState dEFAULT_CLICK_GUI = new UiState.ClickGuiState("Modules", "", "", 0, 0);
   public static UiState.ClickGuiState clickGui = dEFAULT_CLICK_GUI;
   public static UiState.HudEditorState hudEditor = new UiState.HudEditorState(new UiState.HudPosition(1.0, 0.05), false, true, false);
   public static final Map<String, UiState.HudStyle> hudStyles = new LinkedHashMap<>();
   public static final Map<String, UiState.HudPosition> hudPositions = new LinkedHashMap<>();

   public static String migratePage(String var0) {
      String var1 = switch (var0) {
         case "Modules", "Configs", "Settings" -> var0;
         case "Appearance", "Account", "HUD Editor" -> "Settings";
         case "Local", "Cloud", "Import", "Marketplace", "Experimental" -> "Configs";
         default -> "Modules";
      };
      if (!var1.equals(var0)) {
         LarpLog.info("GUI", "Restored obsolete destination {} as {}", var0, var1);
      }

      return var1;
   }

   public static JsonObject save() {
      JsonObject var0 = new JsonObject();
      var0.addProperty("category", category);
      var0.add("clickGui", clickGui.save());
      var0.addProperty("accent", Theme.accent);
      var0.addProperty("secondary", Theme.accentEnd);
      var0.addProperty("bloom", PostProcessing.bloomQuality.name());
      var0.addProperty("blur", PostProcessing.blurQuality.name());
      var0.addProperty("bloomIntensity", PostProcessing.bloomStrength);
      var0.addProperty("bloomRadius", PostProcessing.bloomSpread);
      JsonObject var1 = new JsonObject();
      hudPositions.forEach((var1x, var2x) -> {
         JsonArray var3x = new JsonArray();
         var3x.add(var2x.xFraction());
         var3x.add(var2x.yFraction());
         var1.add(var1x, var3x);
      });
      var0.add("hud", var1);
      JsonObject var2 = new JsonObject();
      hudStyles.forEach((var1x, var2x) -> {
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
      var3.addProperty("x", hudEditor.panel().xFraction());
      var3.addProperty("y", hudEditor.panel().yFraction());
      var3.addProperty("collapsed", hudEditor.collapsed());
      var3.addProperty("snapping", hudEditor.snapping());
      var3.addProperty("grid", hudEditor.grid());
      var0.add("hudEditor", var3);
      return var0;
   }

   private static float readClampedFloat(JsonElement var0, float var1, float var2) {
      float var3 = var0.getAsFloat();
      if (!Float.isFinite(var3)) {
         throw new IllegalArgumentException("Nonfinite post setting");
      } else {
         return Math.clamp(var3, var1, var2);
      }
   }

   public static void load(JsonObject var0) {
      revision++;
      hudStyles.clear();
      hudPositions.clear();
      hudEditor = new UiState.HudEditorState(new UiState.HudPosition(1.0, 0.05), false, true, false);
      clickGui = dEFAULT_CLICK_GUI;

      for (Entry var2 : var0.entrySet()) {
         try {
            JsonElement var3 = (JsonElement)var2.getValue();
            switch ((String)var2.getKey()) {
               case "category":
                  category = var3.getAsString().equals("COSMETICS")
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
                  clickGui = UiState.ClickGuiState.load(var3.getAsJsonObject());
                  break;
               case "accent":
                  Theme.accent = var3.getAsInt();
                  break;
               case "secondary":
                  Theme.accentEnd = var3.getAsInt();
                  break;
               case "bloom":
                  PostProcessing.bloomQuality = PostProcessing.Quality.valueOf(var3.getAsString());
                  break;
               case "bloomIntensity":
                  PostProcessing.bloomStrength = readClampedFloat(var3, 0.0F, 2.0F);
                  break;
               case "bloomRadius":
                  PostProcessing.bloomSpread = readClampedFloat(var3, 0.5F, 3.0F);
                  break;
               case "blur":
                  PostProcessing.blurQuality = PostProcessing.Quality.valueOf(var3.getAsString());
                  break;
               case "hudEditor":
                  JsonObject var15 = var3.getAsJsonObject();
                  hudEditor = new UiState.HudEditorState(
                     new UiState.HudPosition(var15.has("x") ? var15.get("x").getAsDouble() : 1.0, var15.has("y") ? var15.get("y").getAsDouble() : 0.05),
                     var15.has("collapsed") && var15.get("collapsed").getAsBoolean(),
                     !var15.has("snapping") || var15.get("snapping").getAsBoolean(),
                     var15.has("grid") && var15.get("grid").getAsBoolean()
                  );
                  break;
               case "hudStyles":
                  for (Entry var16 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonObject var17 = ((JsonElement)var16.getValue()).getAsJsonObject();
                        hudStyles.put(
                           (String)var16.getKey(),
                           new UiState.HudStyle(
                              var17.has("scale") ? var17.get("scale").getAsDouble() : 1.0,
                              var17.has("hidden") && var17.get("hidden").getAsBoolean(),
                              var17.get("dock") instanceof JsonPrimitive var9 && var9.isString() ? var9.getAsString() : null
                           )
                        );
                     } catch (RuntimeException var12) {
                        LarpLog.warn("Config", "Invalid HUD style {} ignored", var16.getKey());
                     }
                  }
                  break;
               case "hud":
                  for (Entry var7 : var3.getAsJsonObject().entrySet()) {
                     try {
                        JsonArray var8 = ((JsonElement)var7.getValue()).getAsJsonArray();
                        hudPositions.put((String)var7.getKey(), new UiState.HudPosition(var8.get(0).getAsDouble(), var8.get(1).getAsDouble()));
                     } catch (RuntimeException var11) {
                        LarpLog.warn("Config", "Invalid HUD position {} ignored: {}", var7.getKey(), var11.toString());
                     }
                  }
                  break;
               default:
                  LarpLog.warn("Config", "Unknown UI setting {} ignored", var2.getKey());
            }
         } catch (RuntimeException var13) {
            LarpLog.warn("Config", "Invalid UI setting {} ignored: {}", var2.getKey(), var13.toString());
         }
      }
   }

   public record ClickGuiState(String page, String module, String query, int moduleScroll, int settingScroll) {
      public ClickGuiState(String page, String module, String query, int moduleScroll, int settingScroll) {
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

      public static UiState.ClickGuiState load(JsonObject var0) {
         return new UiState.ClickGuiState(
            UiState.migratePage(var0.has("page") ? var0.get("page").getAsString() : "Modules"),
            var0.has("module") ? var0.get("module").getAsString() : "",
            var0.has("query") ? var0.get("query").getAsString() : "",
            var0.has("moduleScroll") ? var0.get("moduleScroll").getAsInt() : 0,
            var0.has("settingScroll") ? var0.get("settingScroll").getAsInt() : 0
         );
      }
   }

   public record HudEditorState(UiState.HudPosition panel, boolean collapsed, boolean snapping, boolean grid) {
   }

   public record HudPosition(double x, double y) {
      private final double xFraction;
      private final double yFraction;

      public HudPosition(double x, double y) {
         if (Double.isFinite(x) && Double.isFinite(y)) {
            x = Math.clamp(x, 0.0, 1.0);
            y = Math.clamp(y, 0.0, 1.0);
            this.xFraction = x;
            this.yFraction = y;
         } else {
            throw new IllegalArgumentException("Nonfinite HUD position");
         }
      }
   }

   public record HudStyle(double scale, boolean hidden, String dock) {
      public static final double MIN_SCALE = 0.2;
      public static final double MAX_SCALE = 4.0;
      public static final double CLOUD_MIN_SCALE = 0.5;
      public static final double CLOUD_MAX_SCALE = 2.5;

      public HudStyle(double scale, boolean hidden, String dock) {
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

      public HudStyle(double var1, boolean var3) {
         this(var1, var3, null);
      }
   }
}
