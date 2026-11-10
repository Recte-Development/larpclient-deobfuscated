package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class b1 {
   private static final int f = 256;
   private final Map<String, Map<String, Integer>> Y = new LinkedHashMap<>();
   private List<b1.x> Q;
   private List<Module> W;

   public static boolean d(com.larpclient.module.r<?> var0) {
      return var0 instanceof com.larpclient.module.m || var0 instanceof com.larpclient.module.o var1 && !var1.isSearchable();
   }

   public static Module f(List<Module> var0, com.larpclient.module.r<?> var1) {
      for (Module var3 : var0) {
         for (com.larpclient.module.r var5 : var3.getSettings()) {
            if (var5 == var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public int M(Module var1, com.larpclient.module.r<?> var2) {
      Map var3 = this.Y.get(var1.getName());
      return var3 == null ? -1 : var3.getOrDefault(var2.X(), -1);
   }

   public void q(Module var1, com.larpclient.module.r<?> var2, int var3) {
      if (!d(var2)) {
         throw new IllegalArgumentException("Only toggle and choice settings take a shortcut");
      }

      if (var3 != -1 && !p4.g(var3)) {
         throw new IllegalArgumentException("Invalid bind code");
      }

      String var4 = h(var1.getName());
      String var5 = g(var2.X());
      if (var3 == -1) {
         Map var6 = this.Y.get(var4);
         if (var6 != null && var6.remove(var5) != null && var6.isEmpty()) {
            this.Y.remove(var4);
         }
      } else {
         if (this.M(var1, var2) == -1 && this.H() >= 256) {
            throw new IllegalArgumentException("Too many setting shortcuts");
         }

         this.Y.computeIfAbsent(var4, var0 -> new LinkedHashMap<>()).put(var5, var3);
      }

      this.Q = null;
   }

   public int H() {
      return this.Y.values().stream().mapToInt(Map::size).sum();
   }

   public List<b1.x> N(List<Module> var1) {
      if (this.Q != null && this.W == var1) {
         return this.Q;
      }

      ArrayList var2 = new ArrayList();

      for (Module var4 : var1) {
         Map var5 = this.Y.get(var4.getName());
         if (var5 != null) {
            for (com.larpclient.module.r var7 : var4.getSettings()) {
               Integer var8 = (Integer)var5.get(var7.X());
               if (var8 != null && d(var7)) {
                  var2.add(new b1.x(var4, var7, var8));
               }
            }
         }
      }

      this.W = var1;
      return this.Q = List.copyOf(var2);
   }

   public b1 v() {
      return X(this.O());
   }

   public JsonObject O() {
      JsonObject var1 = new JsonObject();
      this.Y.forEach((var1x, var2) -> {
         JsonObject var3 = new JsonObject();
         var2.forEach(var3::addProperty);
         var1.add(var1x, var3);
      });
      return var1;
   }

   public static b1 X(JsonElement var0) {
      b1 var1 = new b1();
      if (var0 != null && var0.isJsonObject()) {
         for (Entry var3 : var0.getAsJsonObject().entrySet()) {
            if (((JsonElement)var3.getValue()).isJsonObject()) {
               for (Entry var5 : ((JsonElement)var3.getValue()).getAsJsonObject().entrySet()) {
                  try {
                     if (var1.H() >= 256) {
                        return var1;
                     }

                     JsonElement var6 = (JsonElement)var5.getValue();
                     if (var6.isJsonPrimitive() && var6.getAsJsonPrimitive().isNumber()) {
                        double var7 = var6.getAsDouble();
                        if (var7 == Math.rint(var7) && p4.g((int)var7)) {
                           var1.Y.computeIfAbsent(h((String)var3.getKey()), var0x -> new LinkedHashMap<>()).put(g((String)var5.getKey()), (int)var7);
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

   private static String h(String var0) {
      if (var0 != null && var0.matches("[A-Za-z0-9 _-]{1,128}") && !var0.isBlank()) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   private static String g(String var0) {
      if (var0 != null && !var0.isEmpty() && var0.length() <= 128 && !var0.chars().anyMatch(Character::isISOControl)) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid setting identifier");
      }
   }

   public record x(Module module, com.larpclient.module.r<?> setting, int code) {
   }
}
