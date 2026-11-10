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

public final class n3 {
   public static final int x = 256;
   private static final int C = 1;
   private static final Gson S = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
   private final Path R;
   private final Map<String, List<nu>> P = new LinkedHashMap<>();
   private boolean X;
   private String N = "";

   public n3(Path var1) {
      this.R = var1;
   }

   public static String r(String var0) {
      return "server:" + (var0 != null && !var0.isBlank() ? var0.strip().toLowerCase(Locale.ROOT) : "unknown");
   }

   public static String U(String var0) {
      return "world:" + var0;
   }

   public List<nu> n(String var1) {
      this.j();
      return List.copyOf(this.P.getOrDefault(var1, List.of()));
   }

   public boolean w(String var1) {
      this.j();
      return this.P.getOrDefault(var1, List.of()).size() >= 256;
   }

   public Optional<nu> Q(String var1, String var2) {
      this.j();
      return this.P.getOrDefault(var1, List.of()).stream().filter(var1x -> var1x.id().equals(var2)).findFirst();
   }

   public void N(String var1, nu var2) {
      if (this.w(var1)) {
         throw new IllegalArgumentException("This server already has 256 waypoints");
      }

      this.P.computeIfAbsent(var1, var0 -> new ArrayList<>()).add(var2);
      this.u();
   }

   public boolean G(String var1, nu var2) {
      this.j();
      List var3 = this.P.get(var1);
      if (var3 == null) {
         return false;
      }

      for (int var4 = 0; var4 < var3.size(); var4++) {
         if (((nu)var3.get(var4)).id().equals(var2.id())) {
            var3.set(var4, var2);
            this.u();
            return true;
         }
      }

      return false;
   }

   public boolean A(String var1, String var2) {
      this.j();
      List var3 = this.P.get(var1);
      if (var3 != null && var3.removeIf(var1x -> var1x.id().equals(var2))) {
         if (var3.isEmpty()) {
            this.P.remove(var1);
         }

         this.u();
         return true;
      } else {
         return false;
      }
   }

   public String x() {
      return this.N;
   }

   private void j() {
      if (!this.X) {
         this.X = (boolean)1;
         if (Files.exists(this.R)) {
            byte var1 = 0;

            try {
               JsonObject var2 = JsonParser.parseString(Files.readString(this.R)).getAsJsonObject();
               int var3 = 0;

               for (Entry var5 : var2.getAsJsonObject("worlds").entrySet()) {
                  ArrayList var6 = new ArrayList();

                  for (JsonElement var8 : ((JsonElement)var5.getValue()).getAsJsonArray()) {
                     try {
                        var6.add(Z(var8.getAsJsonObject()));
                     } catch (RuntimeException var10) {
                        var1 = 1;
                     }
                  }

                  if (!var6.isEmpty()) {
                     this.P.put((String)var5.getKey(), var6);
                  }

                  var3 += var6.size();
               }

               _q.z("Waypoints", "Loaded {} waypoints for {} servers and worlds", var3, this.P.size());
            } catch (IOException | RuntimeException var11) {
               var1 = 1;
               this.P.clear();
               _q.o("Waypoints", "Waypoint file could not be read; starting empty", var11);
            }

            if (var1 != 0) {
               this.q();
            }
         }
      }
   }

   private void q() {
      Path var1 = this.R.resolveSibling(this.R.getFileName() + ".invalid-" + System.currentTimeMillis());

      try {
         Files.copy(this.R, var1);
         _q.R("Waypoints", "Damaged waypoint data preserved at {}", var1);
      } catch (IOException | RuntimeException var3) {
         _q.o("Waypoints", "Could not preserve the damaged waypoint file", var3);
      }
   }

   private static nu Z(JsonObject var0) {
      return new nu(
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

   private static JsonObject b(nu var0) {
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

   private void u() {
      JsonObject var1 = new JsonObject();

      for (Entry var3 : this.P.entrySet()) {
         JsonArray var4 = new JsonArray();

         for (nu var6 : (List)var3.getValue()) {
            var4.add(b(var6));
         }

         var1.add((String)var3.getKey(), var4);
      }

      JsonObject var8 = new JsonObject();
      var8.addProperty("version", 1);
      var8.add("worlds", var1);

      try {
         this.P(S.toJson(var8));
         this.N = "";
      } catch (IOException | RuntimeException var7) {
         this.N = "Could not save waypoints; see the log.";
         _q.o("Waypoints", "Waypoint save failed", var7);
      }
   }

   private void P(String var1) throws IOException {
      Files.createDirectories(this.R.getParent());
      Path var2 = Files.createTempFile(this.R.getParent(), "waypoints-", ".tmp");

      try {
         Files.writeString(var2, var1);

         try {
            Files.move(var2, this.R, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, this.R, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }
}
