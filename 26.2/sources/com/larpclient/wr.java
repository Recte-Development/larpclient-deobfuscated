package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class wr {
   private static final int t = 256;
   private final Map<String, Map<String, Integer>> g = new LinkedHashMap<>();
   private List<wr.n> z;
   private List<Module> S;

   public static boolean l(com.larpclient.module.q<?> var0) {
      return var0 instanceof com.larpclient.module.s || var0 instanceof com.larpclient.module.l var1 && !var1.isSearchable();
   }

   public static Module i(List<Module> var0, com.larpclient.module.q<?> var1) {
      for (Module var3 : var0) {
         for (com.larpclient.module.q var5 : var3.getSettings()) {
            if (var5 == var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public int o(Module var1, com.larpclient.module.q<?> var2) {
      Map var3 = this.g.get(var1.getName());
      return var3 == null ? -1 : var3.getOrDefault(var2.e(), -1);
   }

   public void Q(Module var1, com.larpclient.module.q<?> var2, int var3) {
      if (!l(var2)) {
         throw new IllegalArgumentException("Only toggle and choice settings take a shortcut");
      }

      if (var3 != -1 && !bc.a(var3)) {
         throw new IllegalArgumentException("Invalid bind code");
      }

      String var4 = t(var1.getName());
      String var5 = Z(var2.e());
      if (var3 == -1) {
         Map var6 = this.g.get(var4);
         if (var6 != null && var6.remove(var5) != null && var6.isEmpty()) {
            this.g.remove(var4);
         }
      } else {
         if (this.o(var1, var2) == -1 && this.K() >= 256) {
            throw new IllegalArgumentException("Too many setting shortcuts");
         }

         this.g.computeIfAbsent(var4, var0 -> new LinkedHashMap<>()).put(var5, var3);
      }

      this.z = null;
   }

   public int K() {
      return this.g.values().stream().mapToInt(Map::size).sum();
   }

   public List<wr.n> y(List<Module> var1) {
      if (this.z != null && this.S == var1) {
         return this.z;
      }

      ArrayList var2 = new ArrayList();

      for (Module var4 : var1) {
         Map var5 = this.g.get(var4.getName());
         if (var5 != null) {
            for (com.larpclient.module.q var7 : var4.getSettings()) {
               Integer var8 = (Integer)var5.get(var7.e());
               if (var8 != null && l(var7)) {
                  var2.add(new wr.n(var4, var7, var8));
               }
            }
         }
      }

      this.S = var1;
      return this.z = List.copyOf(var2);
   }

   public wr O() {
      return v(this.f());
   }

   public JsonObject f() {
      JsonObject var1 = new JsonObject();
      this.g.forEach((var1x, var2) -> {
         JsonObject var3 = new JsonObject();
         var2.forEach(var3::addProperty);
         var1.add(var1x, var3);
      });
      return var1;
   }

   public static wr v(JsonElement var0) {
      wr var1 = new wr();
      if (var0 != null && var0.isJsonObject()) {
         for (Entry var3 : var0.getAsJsonObject().entrySet()) {
            if (((JsonElement)var3.getValue()).isJsonObject()) {
               for (Entry var5 : ((JsonElement)var3.getValue()).getAsJsonObject().entrySet()) {
                  try {
                     if (var1.K() >= 256) {
                        return var1;
                     }

                     JsonElement var6 = (JsonElement)var5.getValue();
                     if (var6.isJsonPrimitive() && var6.getAsJsonPrimitive().isNumber()) {
                        double var7 = var6.getAsDouble();
                        if (var7 == Math.rint(var7) && bc.a((int)var7)) {
                           var1.g.computeIfAbsent(t((String)var3.getKey()), var0x -> new LinkedHashMap<>()).put(Z((String)var5.getKey()), (int)var7);
                        }
                     }
                  } catch (RuntimeException var9) {
                  }
               }
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   private static String t(String var0) {
      if (var0 != null && var0.matches("[A-Za-z0-9 _-]{1,128}") && !var0.isBlank()) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   private static String Z(String var0) {
      if (var0 != null && !var0.isEmpty() && var0.length() <= 128 && !var0.chars().anyMatch(Character::isISOControl)) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid setting identifier");
      }
   }

   public record n(Module module, com.larpclient.module.q<?> setting, int code) {
   }
}
