package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_310;

@Environment(EnvType.CLIENT)
public final class yh {
   private static final yh T = new yh();
   private static final Gson C = new GsonBuilder().setPrettyPrinting().create();
   private final Map<String, Integer> P = new LinkedHashMap<>();
   private final Map<String, Set<String>> R = new LinkedHashMap<>();

   private yh() {
      this.u();
   }

   public static yh U() {
      return T;
   }

   private static Path r() {
      return j6.l("arrow-game-layout.json");
   }

   public String E() {
      class_310 var1 = class_310.method_1551();
      if (var1 != null && var1.method_1558() != null) {
         String var2 = var1.method_1558().field_3761;
         if (var2 != null && !var2.isBlank()) {
            return var2.trim();
         }
      }

      return var1 != null && var1.method_1576() != null ? "singleplayer" : "world";
   }

   public int H() {
      Integer var1 = this.P.get(this.E());
      return var1 == null ? -1 : var1;
   }

   public boolean A() {
      return this.H() >= 0;
   }

   public void W(int var1) {
      if (var1 < 0) {
         this.P.remove(this.E());
      } else {
         this.P.put(this.E(), var1);
      }

      this.w();
   }

   private String W() {
      class_310 var1 = class_310.method_1551();
      return var1 != null && var1.field_1687 != null ? var1.field_1687.method_27983().method_29177().toString() : "minecraft:overworld";
   }

   private String c(class_2338 var1) {
      return this.W() + ";" + var1.method_10263() + "," + var1.method_10264() + "," + var1.method_10260();
   }

   public boolean m(class_2338 var1) {
      Set var2 = this.R.get(this.E());
      return var1 != null && var2 != null && var2.contains(this.c(var1));
   }

   public int g() {
      Set var1 = this.R.get(this.E());
      return var1 == null ? 0 : var1.size();
   }

   public void O(class_2338 var1) {
      if (var1 == null) {
         this.R.remove(this.E());
      } else {
         this.R.computeIfAbsent(this.E(), var0 -> new LinkedHashSet<>()).add(this.c(var1));
      }

      this.w();
   }

   public void w() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = new JsonObject();

      for (Entry var4 : this.P.entrySet()) {
         var2.addProperty((String)var4.getKey(), (Number)var4.getValue());
      }

      var1.add("servers", var2);
      JsonObject var8 = new JsonObject();

      for (Entry var5 : this.R.entrySet()) {
         JsonArray var6 = new JsonArray();
         ((Set)var5.getValue()).forEach(var6::add);
         var8.add((String)var5.getKey(), var6);
      }

      var1.add("noteblocks", var8);
      Path var10 = r();

      try {
         Files.createDirectories(var10.getParent());
         Files.writeString(var10, C.toJson(var1));
      } catch (Exception var7) {
         q8.L("Rig", "Could not save arrow game layout to {}: {}", var10, var7.getMessage());
      }
   }

   private void u() {
      Path var1 = r();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1));
            if (var2 == null || !var2.isJsonObject()) {
               return;
            }

            JsonObject var3 = var2.getAsJsonObject();
            if (var3.has("servers") && var3.get("servers").isJsonObject()) {
               JsonObject var4 = var3.getAsJsonObject("servers");
               this.P.clear();

               for (String var6 : var4.keySet()) {
                  JsonElement var7 = var4.get(var6);
                  if (var7 != null && var7.isJsonPrimitive() && var7.getAsJsonPrimitive().isNumber()) {
                     this.P.put(var6, var7.getAsInt());
                  }
               }
            }

            if (var3.has("noteblocks") && var3.get("noteblocks").isJsonObject()) {
               JsonObject var12 = var3.getAsJsonObject("noteblocks");
               this.R.clear();

               for (String var14 : var12.keySet()) {
                  JsonElement var15 = var12.get(var14);
                  if (var15 != null && var15.isJsonPrimitive() && var15.getAsJsonPrimitive().isString()) {
                     this.R.put(var14, new LinkedHashSet<>(List.of("minecraft:overworld;" + var15.getAsString())));
                  } else if (var15 != null && var15.isJsonArray()) {
                     LinkedHashSet var8 = new LinkedHashSet();

                     for (JsonElement var10 : var15.getAsJsonArray()) {
                        if (var10.isJsonPrimitive() && var10.getAsJsonPrimitive().isString()) {
                           var8.add(var10.getAsString());
                        }
                     }

                     this.R.put(var14, var8);
                  }
               }
            }
         } catch (Exception var11) {
            q8.L("Rig", "Could not load arrow game layout from {}: {}", var1, var11.getMessage());
         }
      }
   }
}
