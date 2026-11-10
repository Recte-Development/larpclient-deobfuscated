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

public final class br {
   public static final int n = 256;
   private static final int j = 1;
   private static final Gson G = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   private final Path Q;
   private final Map<String, List<sn>> B = new LinkedHashMap<>();
   private boolean U;
   private String q = "";

   public br(Path var1) {
      this.Q = var1;
   }

   public static String w(String var0) {
      return "server:" + (var0 != null && !var0.isBlank() ? var0.strip().toLowerCase(Locale.ROOT) : "unknown");
   }

   public static String u(String var0) {
      return "world:" + var0;
   }

   public List<sn> D(String var1) {
      this.G();
      return List.copyOf(this.B.getOrDefault(var1, List.of()));
   }

   public boolean N(String var1) {
      this.G();
      return this.B.getOrDefault(var1, List.of()).size() >= 256;
   }

   public Optional<sn> Y(String var1, String var2) {
      this.G();
      return this.B.getOrDefault(var1, List.of()).stream().filter(var1x -> var1x.id().equals(var2)).findFirst();
   }

   public void I(String var1, sn var2) {
      if (this.N(var1)) {
         throw new IllegalArgumentException("This server already has 256 waypoints");
      }

      this.B.computeIfAbsent(var1, var0 -> new ArrayList<>()).add(var2);
      this.j();
   }

   public boolean F(String var1, sn var2) {
      this.G();
      List var3 = this.B.get(var1);
      if (var3 == null) {
         return false;
      }

      for (int var4 = 0; var4 < var3.size(); var4++) {
         if (((sn)var3.get(var4)).id().equals(var2.id())) {
            var3.set(var4, var2);
            this.j();
            return true;
         }
      }

      return false;
   }

   public boolean h(String var1, String var2) {
      this.G();
      List var3 = this.B.get(var1);
      if (var3 != null && var3.removeIf(var1x -> var1x.id().equals(var2))) {
         if (var3.isEmpty()) {
            this.B.remove(var1);
         }

         this.j();
         return true;
      } else {
         return false;
      }
   }

   public String d() {
      return this.q;
   }

   private void G() {
      if (!this.U) {
         this.U = (boolean)1;
         if (Files.exists(this.Q)) {
            byte var1 = 0;

            try {
               JsonObject var2 = JsonParser.parseString(Files.readString(this.Q)).getAsJsonObject();
               int var3 = 0;

               for (Entry var5 : var2.getAsJsonObject("worlds").entrySet()) {
                  ArrayList var6 = new ArrayList();

                  for (JsonElement var8 : ((JsonElement)var5.getValue()).getAsJsonArray()) {
                     try {
                        var6.add(i(var8.getAsJsonObject()));
                     } catch (RuntimeException var10) {
                        var1 = 1;
                     }
                  }

                  if (!var6.isEmpty()) {
                     this.B.put((String)var5.getKey(), var6);
                  }

                  var3 += var6.size();
               }

               h.A("Waypoints", "Loaded {} waypoints for {} servers and worlds", var3, this.B.size());
            } catch (IOException | RuntimeException var11) {
               var1 = 1;
               this.B.clear();
               h.m("Waypoints", "Waypoint file could not be read; starting empty", var11);
            }

            if (var1 != 0) {
               this.c();
            }
         }
      }
   }

   private void c() {
      Path var1 = this.Q.resolveSibling(this.Q.getFileName() + ".invalid-" + System.currentTimeMillis());

      try {
         Files.copy(this.Q, var1);
         h.g("Waypoints", "Damaged waypoint data preserved at {}", var1);
      } catch (IOException | RuntimeException var3) {
         h.m("Waypoints", "Could not preserve the damaged waypoint file", var3);
      }
   }

   private static sn i(JsonObject var0) {
      return new sn(
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

   private static JsonObject T(sn var0) {
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

   private void j() {
      JsonObject var1 = new JsonObject();

      for (Entry var3 : this.B.entrySet()) {
         JsonArray var4 = new JsonArray();

         for (sn var6 : (List)var3.getValue()) {
            var4.add(T(var6));
         }

         var1.add((String)var3.getKey(), var4);
      }

      JsonObject var8 = new JsonObject();
      var8.addProperty("version", 1);
      var8.add("worlds", var1);

      try {
         this.C(G.toJson(var8));
         this.q = "";
      } catch (IOException | RuntimeException var7) {
         this.q = "Could not save waypoints; see the log.";
         h.m("Waypoints", "Waypoint save failed", var7);
      }
   }

   private void C(String var1) throws IOException {
      Files.createDirectories(this.Q.getParent());
      Path var2 = Files.createTempFile(this.Q.getParent(), "waypoints-", ".tmp");

      try {
         Files.writeString(var2, var1);

         try {
            Files.move(var2, this.Q, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, this.Q, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }
}
