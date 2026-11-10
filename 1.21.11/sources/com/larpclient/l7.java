package com.larpclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public final class l7 {
   public static final int v = 256;
   private static final int B = 1;
   private static final Gson z = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   private final Path a;
   private final Map<String, List<cy>> U = new LinkedHashMap<>();
   private boolean s;
   private String n = "";

   public l7(Path var1) {
      this.a = var1;
   }

   public static String e(String var0) {
      return "server:" + (var0 != null && !var0.isBlank() ? var0.strip().toLowerCase(Locale.ROOT) : "unknown");
   }

   public static String t(String var0) {
      return "world:" + var0;
   }

   public List<cy> l(String var1) {
      this.L();
      return List.copyOf(this.U.getOrDefault(var1, List.of()));
   }

   public boolean u(String var1) {
      this.L();
      return this.U.getOrDefault(var1, List.of()).size() >= 256;
   }

   public Optional<cy> W(String var1, String var2) {
      this.L();
      return this.U.getOrDefault(var1, List.of()).stream().filter(var1x -> var1x.id().equals(var2)).findFirst();
   }

   public void S(String var1, cy var2) {
      if (this.u(var1)) {
         throw new IllegalArgumentException("This server already has 256 waypoints");
      }

      this.U.computeIfAbsent(var1, var0 -> new ArrayList<>()).add(var2);
      this.e();
   }

   public boolean Q(String var1, cy var2) {
      this.L();
      List var3 = this.U.get(var1);
      if (var3 == null) {
         return false;
      }

      for (int var4 = 0; var4 < var3.size(); var4++) {
         if (((cy)var3.get(var4)).id().equals(var2.id())) {
            var3.set(var4, var2);
            this.e();
            return true;
         }
      }

      return false;
   }

   public boolean S(String var1, String var2) {
      this.L();
      List var3 = this.U.get(var1);
      if (var3 != null && var3.removeIf(var1x -> var1x.id().equals(var2))) {
         if (var3.isEmpty()) {
            this.U.remove(var1);
         }

         this.e();
         return true;
      } else {
         return false;
      }
   }

   public String u() {
      return this.n;
   }

   private void L() {
      if (!this.s) {
         this.s = (boolean)1;
         if (Files.exists(this.a)) {
            byte var1 = 0;

            try {
               JsonObject var2 = JsonParser.parseString(Files.readString(this.a)).getAsJsonObject();
               int var3 = 0;

               for (Entry var5 : var2.getAsJsonObject("worlds").entrySet()) {
                  ArrayList var6 = new ArrayList();

                  for (JsonElement var8 : ((JsonElement)var5.getValue()).getAsJsonArray()) {
                     try {
                        var6.add(g(var8.getAsJsonObject()));
                     } catch (RuntimeException var10) {
                        var1 = 1;
                     }
                  }

                  if (!var6.isEmpty()) {
                     this.U.put((String)var5.getKey(), var6);
                  }

                  var3 += var6.size();
               }

               q8.G("Waypoints", "Loaded {} waypoints for {} servers and worlds", var3, this.U.size());
            } catch (IOException | RuntimeException var11) {
               var1 = 1;
               this.U.clear();
               q8.u("Waypoints", "Waypoint file could not be read; starting empty", var11);
            }

            if (var1 != 0) {
               this.s();
            }
         }
      }
   }

   private void s() {
      Path var1 = this.a.resolveSibling(this.a.getFileName() + ".invalid-" + System.currentTimeMillis());

      try {
         Files.copy(this.a, var1);
         q8.L("Waypoints", "Damaged waypoint data preserved at {}", var1);
      } catch (IOException | RuntimeException var3) {
         q8.u("Waypoints", "Could not preserve the damaged waypoint file", var3);
      }
   }

   private static cy g(JsonObject var0) {
      return new cy(
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

   private static JsonObject D(cy var0) {
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

   private void e() {
      JsonObject var1 = new JsonObject();

      for (Entry var3 : this.U.entrySet()) {
         JsonArray var4 = new JsonArray();

         for (cy var6 : (List)var3.getValue()) {
            var4.add(D(var6));
         }

         var1.add((String)var3.getKey(), var4);
      }

      JsonObject var8 = new JsonObject();
      var8.addProperty("version", 1);
      var8.add("worlds", var1);

      try {
         this.r(z.toJson(var8));
         this.n = "";
      } catch (IOException | RuntimeException var7) {
         this.n = "Could not save waypoints; see the log.";
         q8.u("Waypoints", "Waypoint save failed", var7);
      }
   }

   private void r(String var1) throws IOException {
      Files.createDirectories(this.a.getParent());
      Path var2 = Files.createTempFile(this.a.getParent(), "waypoints-", ".tmp");

      try {
         Files.writeString(var2, var1);

         try {
            Files.move(var2, this.a, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, this.a, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }
}
