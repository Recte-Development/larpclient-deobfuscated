package com.larpclient.util.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.util.misc.LarpLog;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

public final class WaypointStore {
   public static final int mAX_PER_SERVER = 256;
   private static final int fORMAT_VERSION = 1;
   private static final Gson gSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   private final Path file;
   private final Map<String, List<Waypoint>> waypointsByKey = new LinkedHashMap<>();
   private boolean loaded;
   private String saveError = "";

   public WaypointStore(Path var1) {
      this.file = var1;
   }

   public static String serverKey(String var0) {
      return "server:" + (var0 != null && !var0.isBlank() ? var0.strip().toLowerCase(Locale.ROOT) : "unknown");
   }

   public static String worldKey(String var0) {
      return "world:" + var0;
   }

   public List<Waypoint> getWaypoints(String var1) {
      this.loadIfNeeded();
      return List.copyOf(this.waypointsByKey.getOrDefault(var1, List.of()));
   }

   public boolean isFull(String var1) {
      this.loadIfNeeded();
      return this.waypointsByKey.getOrDefault(var1, List.of()).size() >= 256;
   }

   public Optional<Waypoint> findWaypoint(String var1, String var2) {
      this.loadIfNeeded();
      return this.waypointsByKey.getOrDefault(var1, List.of()).stream().filter(var1x -> var1x.id().equals(var2)).findFirst();
   }

   public void addWaypoint(String var1, Waypoint var2) {
      if (this.isFull(var1)) {
         throw new IllegalArgumentException("This server already has 256 waypoints");
      }

      this.waypointsByKey.computeIfAbsent(var1, var0 -> new ArrayList<>()).add(var2);
      this.save();
   }

   public boolean updateWaypoint(String var1, Waypoint var2) {
      this.loadIfNeeded();
      List var3 = this.waypointsByKey.get(var1);
      if (var3 == null) {
         return false;
      }

      for (int var4 = 0; var4 < var3.size(); var4++) {
         if (((Waypoint)var3.get(var4)).id().equals(var2.id())) {
            var3.set(var4, var2);
            this.save();
            return true;
         }
      }

      return false;
   }

   public boolean removeWaypoint(String var1, String var2) {
      this.loadIfNeeded();
      List var3 = this.waypointsByKey.get(var1);
      if (var3 != null && var3.removeIf(var1x -> var1x.id().equals(var2))) {
         if (var3.isEmpty()) {
            this.waypointsByKey.remove(var1);
         }

         this.save();
         return true;
      } else {
         return false;
      }
   }

   public String getSaveError() {
      return this.saveError;
   }

   private void loadIfNeeded() {
      if (!this.loaded) {
         this.loaded = (boolean)1;
         if (Files.exists(this.file)) {
            byte var1 = 0;

            try {
               JsonObject var2 = JsonParser.parseString(Files.readString(this.file)).getAsJsonObject();
               int var3 = 0;

               for (Entry var5 : var2.getAsJsonObject("worlds").entrySet()) {
                  ArrayList var6 = new ArrayList();

                  for (JsonElement var8 : ((JsonElement)var5.getValue()).getAsJsonArray()) {
                     try {
                        var6.add(fromJson(var8.getAsJsonObject()));
                     } catch (RuntimeException var10) {
                        var1 = 1;
                     }
                  }

                  if (!var6.isEmpty()) {
                     this.waypointsByKey.put((String)var5.getKey(), var6);
                  }

                  var3 += var6.size();
               }

               LarpLog.info("Waypoints", "Loaded {} waypoints for {} servers and worlds", var3, this.waypointsByKey.size());
            } catch (IOException | RuntimeException var11) {
               var1 = 1;
               this.waypointsByKey.clear();
               LarpLog.error("Waypoints", "Waypoint file could not be read; starting empty", var11);
            }

            if (var1 != 0) {
               this.preserveInvalidFile();
            }
         }
      }
   }

   private void preserveInvalidFile() {
      Path var1 = this.file.resolveSibling(this.file.getFileName() + ".invalid-" + System.currentTimeMillis());

      try {
         Files.copy(this.file, var1);
         LarpLog.warn("Waypoints", "Damaged waypoint data preserved at {}", var1);
      } catch (IOException | RuntimeException var3) {
         LarpLog.error("Waypoints", "Could not preserve the damaged waypoint file", var3);
      }
   }

   private static Waypoint fromJson(JsonObject var0) {
      return new Waypoint(
         var0.get("id").getAsString(),
         var0.get("name").getAsString(),
         var0.get("x").getAsInt(),
         var0.get("y").getAsInt(),
         var0.get("z").getAsInt(),
         var0.get("dimension").getAsString(),
         var0.get("color").getAsInt(),
         !var0.has("visible") || var0.get("visible").getAsBoolean()
      );
   }

   private static JsonObject toJson(Waypoint var0) {
      JsonObject var1 = new JsonObject();
      var1.addProperty("id", var0.id());
      var1.addProperty("name", var0.name());
      var1.addProperty("x", var0.x());
      var1.addProperty("y", var0.y());
      var1.addProperty("z", var0.z());
      var1.addProperty("dimension", var0.dimension());
      var1.addProperty("color", var0.color());
      var1.addProperty("visible", var0.visible());
      return var1;
   }

   private void save() {
      JsonObject var1 = new JsonObject();

      for (Entry var3 : this.waypointsByKey.entrySet()) {
         JsonArray var4 = new JsonArray();

         for (Waypoint var6 : (List)var3.getValue()) {
            var4.add(toJson(var6));
         }

         var1.add((String)var3.getKey(), var4);
      }

      JsonObject var8 = new JsonObject();
      var8.addProperty("version", 1);
      var8.add("worlds", var1);

      try {
         this.writeAtomically(gSON.toJson(var8));
         this.saveError = "";
      } catch (IOException | RuntimeException var7) {
         this.saveError = "Could not save waypoints; see the log.";
         LarpLog.error("Waypoints", "Waypoint save failed", var7);
      }
   }

   private void writeAtomically(String var1) throws IOException {
      Files.createDirectories(this.file.getParent());
      Path var2 = Files.createTempFile(this.file.getParent(), "waypoints-", ".tmp");

      try {
         Files.writeString(var2, var1);

         try {
            Files.move(var2, this.file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, this.file, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }
}
