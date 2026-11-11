package com.larpclient.gui.screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.CasinoPaths;
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
public final class ArrowGameLayout {
   private static final ArrowGameLayout iNSTANCE = new ArrowGameLayout();
   private static final Gson gSON = new GsonBuilder().setPrettyPrinting().create();
   private final Map<String, Integer> serverSlots = new LinkedHashMap<>();
   private final Map<String, Set<String>> noteblocks = new LinkedHashMap<>();

   private ArrowGameLayout() {
      this.load();
   }

   public static ArrowGameLayout getInstance() {
      return iNSTANCE;
   }

   private static Path layoutPath() {
      return CasinoPaths.resolve("arrow-game-layout.json");
   }

   public String serverKey() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.getCurrentServer() != null) {
         String var2 = var1.getCurrentServer().ip;
         if (var2 != null && !var2.isBlank()) {
            return var2.trim();
         }
      }

      return var1 != null && var1.getSingleplayerServer() != null ? "singleplayer" : "world";
   }

   public int getSlot() {
      Integer var1 = this.serverSlots.get(this.serverKey());
      return var1 == null ? -1 : var1;
   }

   public boolean hasSlot() {
      return this.getSlot() >= 0;
   }

   public void setSlot(int var1) {
      if (var1 < 0) {
         this.serverSlots.remove(this.serverKey());
      } else {
         this.serverSlots.put(this.serverKey(), var1);
      }

      this.save();
   }

   private String load() {
      Minecraft var1 = Minecraft.getInstance();
      return var1 != null && var1.level != null ? var1.level.dimension().identifier().toString() : "minecraft:overworld";
   }

   private String blockKey(BlockPos var1) {
      return this.load() + ";" + var1.getX() + "," + var1.getY() + "," + var1.getZ();
   }

   public boolean hasNoteblock(BlockPos var1) {
      Set var2 = this.noteblocks.get(this.serverKey());
      return var1 != null && var2 != null && var2.contains(this.blockKey(var1));
   }

   public int noteblockCount() {
      Set var1 = this.noteblocks.get(this.serverKey());
      return var1 == null ? 0 : var1.size();
   }

   public void save(BlockPos var1) {
      if (var1 == null) {
         this.noteblocks.remove(this.serverKey());
      } else {
         this.noteblocks.computeIfAbsent(this.serverKey(), var0 -> new LinkedHashSet<>()).add(this.blockKey(var1));
      }

      this.save();
   }

   public void save() {
      JsonObject var1 = new JsonObject();
      JsonObject var2 = new JsonObject();

      for (Entry var4 : this.serverSlots.entrySet()) {
         var2.addProperty((String)var4.getKey(), (Number)var4.getValue());
      }

      var1.add("servers", var2);
      JsonObject var8 = new JsonObject();

      for (Entry var5 : this.noteblocks.entrySet()) {
         JsonArray var6 = new JsonArray();
         ((Set)var5.getValue()).forEach(var6::add);
         var8.add((String)var5.getKey(), var6);
      }

      var1.add("noteblocks", var8);
      Path var10 = layoutPath();

      try {
         Files.createDirectories(var10.getParent());
         Files.writeString(var10, gSON.toJson(var1));
      } catch (Exception var7) {
         LarpLog.warn("Rig", "Could not save arrow game layout to {}: {}", var10, var7.getMessage());
      }
   }

   private void load() {
      Path var1 = layoutPath();
      if (Files.exists(var1)) {
         try {
            JsonElement var2 = JsonParser.parseString(Files.readString(var1));
            if (var2 == null || !var2.isJsonObject()) {
               return;
            }

            JsonObject var3 = var2.getAsJsonObject();
            if (var3.has("servers") && var3.get("servers").isJsonObject()) {
               JsonObject var4 = var3.getAsJsonObject("servers");
               this.serverSlots.clear();

               for (String var6 : var4.keySet()) {
                  JsonElement var7 = var4.get(var6);
                  if (var7 != null && var7.isJsonPrimitive() && var7.getAsJsonPrimitive().isNumber()) {
                     this.serverSlots.put(var6, var7.getAsInt());
                  }
               }
            }

            if (var3.has("noteblocks") && var3.get("noteblocks").isJsonObject()) {
               JsonObject var12 = var3.getAsJsonObject("noteblocks");
               this.noteblocks.clear();

               for (String var14 : var12.keySet()) {
                  JsonElement var15 = var12.get(var14);
                  if (var15 != null && var15.isJsonPrimitive() && var15.getAsJsonPrimitive().isString()) {
                     this.noteblocks.put(var14, new LinkedHashSet<>(List.of("minecraft:overworld;" + var15.getAsString())));
                  } else if (var15 != null && var15.isJsonArray()) {
                     LinkedHashSet var8 = new LinkedHashSet();

                     for (JsonElement var10 : var15.getAsJsonArray()) {
                        if (var10.isJsonPrimitive() && var10.getAsJsonPrimitive().isString()) {
                           var8.add(var10.getAsString());
                        }
                     }

                     this.noteblocks.put(var14, var8);
                  }
               }
            }
         } catch (Exception var11) {
            LarpLog.warn("Rig", "Could not load arrow game layout from {}: {}", var1, var11.getMessage());
         }
      }
   }
}
