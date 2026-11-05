package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class m5 {
   private static final int e = 256;
   private final Map<String, Map<String, Integer>> q = new LinkedHashMap<>();
   private List<m5.w> l;
   private List<Module> B;

   public static boolean C(com.larpclient.module.l6<?> var0) {
      return var0 instanceof com.larpclient.module.ln || var0 instanceof com.larpclient.module.lp var1 && !var1.isSearchable();
   }

   public static Module r(List<Module> var0, com.larpclient.module.l6<?> var1) {
      for (Module var3 : var0) {
         for (com.larpclient.module.l6 var5 : var3.getSettings()) {
            if (var5 == var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public int i(Module var1, com.larpclient.module.l6<?> var2) {
      Map var3 = this.q.get(var1.getName());
      return var3 == null ? -1 : var3.getOrDefault(var2.k(), -1);
   }

   public void O(Module var1, com.larpclient.module.l6<?> var2, int var3) {
      if (!C(var2)) {
         throw new IllegalArgumentException("Only toggle and choice settings take a shortcut");
      }

      if (var3 != -1 && !pe.F(var3)) {
         throw new IllegalArgumentException("Invalid bind code");
      }

      String var4 = o(var1.getName());
      String var5 = u(var2.k());
      if (var3 == -1) {
         Map var6 = this.q.get(var4);
         if (var6 != null && var6.remove(var5) != null && var6.isEmpty()) {
            this.q.remove(var4);
         }
      } else {
         if (this.i(var1, var2) == -1 && this.s() >= 256) {
            throw new IllegalArgumentException("Too many setting shortcuts");
         }

         this.q.computeIfAbsent(var4, var0 -> new LinkedHashMap<>()).put(var5, var3);
      }

      this.l = null;
   }

   public int s() {
      return this.q.values().stream().mapToInt(Map::size).sum();
   }

   public List<m5.w> O(List<Module> var1) {
      if (this.l != null && this.B == var1) {
         return this.l;
      }

      ArrayList var2 = new ArrayList();

      for (Module var4 : var1) {
         Map var5 = this.q.get(var4.getName());
         if (var5 != null) {
            for (com.larpclient.module.l6 var7 : var4.getSettings()) {
               Integer var8 = (Integer)var5.get(var7.k());
               if (var8 != null && C(var7)) {
                  var2.add(new m5.w(var4, var7, var8));
               }
            }
         }
      }

      this.B = var1;
      return this.l = List.copyOf(var2);
   }

   public m5 j() {
      return D(this.g());
   }

   public JsonObject g() {
      JsonObject var1 = new JsonObject();
      this.q.forEach((var1x, var2) -> {
         JsonObject var3 = new JsonObject();
         var2.forEach(var3::addProperty);
         var1.add(var1x, var3);
      });
      return var1;
   }

   public static m5 D(JsonElement var0) {
      m5 var1 = new m5();
      if (var0 != null && var0.isJsonObject()) {
         for (Entry var3 : var0.getAsJsonObject().entrySet()) {
            if (((JsonElement)var3.getValue()).isJsonObject()) {
               for (Entry var5 : ((JsonElement)var3.getValue()).getAsJsonObject().entrySet()) {
                  try {
                     if (var1.s() >= 256) {
                        return var1;
                     }

                     JsonElement var6 = (JsonElement)var5.getValue();
                     if (var6.isJsonPrimitive() && var6.getAsJsonPrimitive().isNumber()) {
                        double var7 = var6.getAsDouble();
                        if (var7 == Math.rint(var7) && pe.F((int)var7)) {
                           var1.q.computeIfAbsent(o((String)var3.getKey()), var0x -> new LinkedHashMap<>()).put(u((String)var5.getKey()), (int)var7);
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

   private static String o(String var0) {
      if (var0 != null && var0.matches("[A-Za-z0-9 _-]{1,128}") && !var0.isBlank()) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   private static String u(String var0) {
      if (var0 != null && !var0.isEmpty() && var0.length() <= 128 && !var0.chars().anyMatch(Character::isISOControl)) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid setting identifier");
      }
   }

   public record w(Module module, com.larpclient.module.l6<?> setting, int code) {
   }
}
