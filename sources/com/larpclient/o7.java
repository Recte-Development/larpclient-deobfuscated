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
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

@Environment(EnvType.CLIENT)
public final class o7 {
   private static final o7 V = new o7();
   private static final Gson H = new GsonBuilder().setPrettyPrinting().create();
   private final Map<String, Integer> S = new LinkedHashMap<>();
   private final Map<String, Set<String>> o = new LinkedHashMap<>();

   private o7() {
      this.x();
   }

   public static o7 a() {
      return V;
   }

   private static Path z() {
      return ma.h("arrow-game-layout.json");
   }

   public String q() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.getCurrentServer() != null) {
         String var2 = var1.getCurrentServer().ip;
         if (var2 != null && !var2.isBlank()) {
            return var2.trim();
         }
      }

      return var1 != null && var1.getSingleplayerServer() != null ? "singleplayer" : "world";
   }

   public int a() {
      Integer var1 = this.S.get(this.q());
      return var1 == null ? -1 : var1;
   }

   public boolean g() {
      return this.a() >= 0;
   }

   public void g(int var1) {
      if (var1 < 0) {
         this.S.remove(this.q());
      } else {
         this.S.put(this.q(), var1);
      }

      this.G();
   }

   private String p() {
      Minecraft var1 = Minecraft.getInstance();
      return var1 != null && var1.level != null ? var1.level.dimension().identifier().toString() : "minecraft:overworld";
   }

   private String r(BlockPos var1) {
      return this.p() + ";" + var1.getX() + "," + var1.getY() + "," + var1.getZ();
   }

   public boolean o(BlockPos var1) {
      Set var2 = this.o.get(this.q());
      return var1 != null && var2 != null && var2.contains(this.r(var1));
   }

   public int U() {
      Set var1 = this.o.get(this.q());
      return var1 == null ? 0 : var1.size();
   }

   public void o(BlockPos var1) {
      if (var1 == null) {
         this.o.remove(this.q());
      } else {
         this.o.computeIfAbsent(this.q(), var0 -> new LinkedHashSet<>()).add(this.r(var1));
      }

      this.G();
   }

   public void G() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = new JsonObject();

      for (Entry var4 : this.S.entrySet()) {
         var2.addProperty((String)var4.getKey(), (Number)var4.getValue());
      }

      var1.add("servers", var2);
      JsonObject var8 = new JsonObject();

      for (Entry var5 : this.o.entrySet()) {
         JsonArray var6 = new JsonArray();
         ((Set)var5.getValue()).forEach(var6::add);
         var8.add((String)var5.getKey(), var6);
      }

      var1.add("noteblocks", var8);
      Path var10 = z();

      try {
         Files.createDirectories(var10.getParent());
         Files.writeString(var10, H.toJson(var1));
      } catch (Exception var7) {
         r4.m("Rig", "Could not save arrow game layout to {}: {}", var10, var7.getMessage());
      }
   }

   private void x() {
      Path var1 = z();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1));
            if (var2 == null || !var2.isJsonObject()) {
               return;
            }

            JsonObject var3 = var2.getAsJsonObject();
            if (var3.has("servers") && var3.get("servers").isJsonObject()) {
               JsonObject var4 = var3.getAsJsonObject("servers");
               this.S.clear();

               for (String var6 : var4.keySet()) {
                  JsonElement var7 = var4.get(var6);
                  if (var7 != null && var7.isJsonPrimitive() && var7.getAsJsonPrimitive().isNumber()) {
                     this.S.put(var6, var7.getAsInt());
                  }
               }
            }

            if (var3.has("noteblocks") && var3.get("noteblocks").isJsonObject()) {
               JsonObject var12 = var3.getAsJsonObject("noteblocks");
               this.o.clear();

               for (String var14 : var12.keySet()) {
                  JsonElement var15 = var12.get(var14);
                  if (var15 != null && var15.isJsonPrimitive() && var15.getAsJsonPrimitive().isString()) {
                     this.o.put(var14, new LinkedHashSet<>(List.of("minecraft:overworld;" + var15.getAsString())));
                  } else if (var15 != null && var15.isJsonArray()) {
                     LinkedHashSet var8 = new LinkedHashSet();

                     for (JsonElement var10 : var15.getAsJsonArray()) {
                        if (var10.isJsonPrimitive() && var10.getAsJsonPrimitive().isString()) {
                           var8.add(var10.getAsString());
                        }
                     }

                     this.o.put(var14, var8);
                  }
               }
            }
         } catch (Exception var11) {
            r4.m("Rig", "Could not load arrow game layout from {}: {}", var1, var11.getMessage());
         }
      }
   }
}
