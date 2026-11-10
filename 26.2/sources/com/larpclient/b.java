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
public final class b {
   private static final b z = new b();
   private static final Gson p = new GsonBuilder().setPrettyPrinting().create();
   private final Map<String, Integer> y = new LinkedHashMap<>();
   private final Map<String, Set<String>> c = new LinkedHashMap<>();

   private b() {
      this.x();
   }

   public static b m() {
      return z;
   }

   private static Path c() {
      return h5.G("arrow-game-layout.json");
   }

   public String d() {
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
      Integer var1 = this.y.get(this.d());
      return var1 == null ? -1 : var1;
   }

   public boolean W() {
      return this.a() >= 0;
   }

   public void A(int var1) {
      if (var1 < 0) {
         this.y.remove(this.d());
      } else {
         this.y.put(this.d(), var1);
      }

      this.i();
   }

   private String q() {
      Minecraft var1 = Minecraft.getInstance();
      return var1 != null && var1.level != null ? var1.level.dimension().identifier().toString() : "minecraft:overworld";
   }

   private String y(BlockPos var1) {
      return this.q() + ";" + var1.getX() + "," + var1.getY() + "," + var1.getZ();
   }

   public boolean J(BlockPos var1) {
      Set var2 = this.c.get(this.d());
      return var1 != null && var2 != null && var2.contains(this.y(var1));
   }

   public int l() {
      Set var1 = this.c.get(this.d());
      return var1 == null ? 0 : var1.size();
   }

   public void j(BlockPos var1) {
      if (var1 == null) {
         this.c.remove(this.d());
      } else {
         this.c.computeIfAbsent(this.d(), var0 -> new LinkedHashSet<>()).add(this.y(var1));
      }

      this.i();
   }

   public void i() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = new JsonObject();

      for (Entry var4 : this.y.entrySet()) {
         var2.addProperty((String)var4.getKey(), (Number)var4.getValue());
      }

      var1.add("servers", var2);
      JsonObject var8 = new JsonObject();

      for (Entry var5 : this.c.entrySet()) {
         JsonArray var6 = new JsonArray();
         ((Set)var5.getValue()).forEach(var6::add);
         var8.add((String)var5.getKey(), var6);
      }

      var1.add("noteblocks", var8);
      Path var10 = c();

      try {
         Files.createDirectories(var10.getParent());
         Files.writeString(var10, p.toJson(var1));
      } catch (Exception var7) {
         h.g("Rig", "Could not save arrow game layout to {}: {}", var10, var7.getMessage());
      }
   }

   private void x() {
      Path var1 = c();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1));
            if (var2 == null || !var2.isJsonObject()) {
               return;
            }

            JsonObject var3 = var2.getAsJsonObject();
            if (var3.has("servers") && var3.get("servers").isJsonObject()) {
               JsonObject var4 = var3.getAsJsonObject("servers");
               this.y.clear();

               for (String var6 : var4.keySet()) {
                  JsonElement var7 = var4.get(var6);
                  if (var7 != null && var7.isJsonPrimitive() && var7.getAsJsonPrimitive().isNumber()) {
                     this.y.put(var6, var7.getAsInt());
                  }
               }
            }

            if (var3.has("noteblocks") && var3.get("noteblocks").isJsonObject()) {
               JsonObject var12 = var3.getAsJsonObject("noteblocks");
               this.c.clear();

               for (String var14 : var12.keySet()) {
                  JsonElement var15 = var12.get(var14);
                  if (var15 != null && var15.isJsonPrimitive() && var15.getAsJsonPrimitive().isString()) {
                     this.c.put(var14, new LinkedHashSet<>(List.of("minecraft:overworld;" + var15.getAsString())));
                  } else if (var15 != null && var15.isJsonArray()) {
                     LinkedHashSet var8 = new LinkedHashSet();

                     for (JsonElement var10 : var15.getAsJsonArray()) {
                        if (var10.isJsonPrimitive() && var10.getAsJsonPrimitive().isString()) {
                           var8.add(var10.getAsString());
                        }
                     }

                     this.c.put(var14, var8);
                  }
               }
            }
         } catch (Exception var11) {
            h.g("Rig", "Could not load arrow game layout from {}: {}", var1, var11.getMessage());
         }
      }
   }
}
