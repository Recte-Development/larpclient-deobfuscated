package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.Module;
import com.larpclient.module.df;
import com.larpclient.module.dh;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class um {
   private static final int P = 256;
   private final Map<String, Map<String, Integer>> L = new LinkedHashMap<>();
   private List<um.w> y;
   private List<Module> Z;

   public static boolean h(df<?> var0) {
      return var0 instanceof dh || var0 instanceof com.larpclient.module.d6 var1 && !var1.isSearchable();
   }

   public static Module A(List<Module> var0, df<?> var1) {
      for (Module var3 : var0) {
         for (df var5 : var3.getSettings()) {
            if (var5 == var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public int h(Module var1, df<?> var2) {
      Map var3 = this.L.get(var1.getName());
      return var3 == null ? -1 : var3.getOrDefault(var2.D(), -1);
   }

   public void v(Module var1, df<?> var2, int var3) {
      if (!h(var2)) {
         throw new IllegalArgumentException("Only toggle and choice settings take a shortcut");
      }

      if (var3 != -1 && !cc.N(var3)) {
         throw new IllegalArgumentException("Invalid bind code");
      }

      String var4 = t(var1.getName());
      String var5 = m(var2.D());
      if (var3 == -1) {
         Map var6 = this.L.get(var4);
         if (var6 != null && var6.remove(var5) != null && var6.isEmpty()) {
            this.L.remove(var4);
         }
      } else {
         if (this.h(var1, var2) == -1 && this.r() >= 256) {
            throw new IllegalArgumentException("Too many setting shortcuts");
         }

         this.L.computeIfAbsent(var4, var0 -> new LinkedHashMap<>()).put(var5, var3);
      }

      this.y = null;
   }

   public int r() {
      return this.L.values().stream().mapToInt(Map::size).sum();
   }

   public List<um.w> l(List<Module> var1) {
      if (this.y != null && this.Z == var1) {
         return this.y;
      }

      ArrayList var2 = new ArrayList();

      for (Module var4 : var1) {
         Map var5 = this.L.get(var4.getName());
         if (var5 != null) {
            for (df var7 : var4.getSettings()) {
               Integer var8 = (Integer)var5.get(var7.D());
               if (var8 != null && h(var7)) {
                  var2.add(new um.w(var4, var7, var8));
               }
            }
         }
      }

      this.Z = var1;
      return this.y = List.copyOf(var2);
   }

   public um T() {
      return q(this.S());
   }

   public JsonObject S() {
      JsonObject var1 = new JsonObject();
      this.L.forEach((var1x, var2) -> {
         JsonObject var3 = new JsonObject();
         var2.forEach(var3::addProperty);
         var1.add(var1x, var3);
      });
      return var1;
   }

   public static um q(JsonElement var0) {
      um var1 = new um();
      if (var0 != null && var0.isJsonObject()) {
         for (Entry var3 : var0.getAsJsonObject().entrySet()) {
            if (((JsonElement)var3.getValue()).isJsonObject()) {
               for (Entry var5 : ((JsonElement)var3.getValue()).getAsJsonObject().entrySet()) {
                  try {
                     if (var1.r() >= 256) {
                        return var1;
                     }

                     JsonElement var6 = (JsonElement)var5.getValue();
                     if (var6.isJsonPrimitive() && var6.getAsJsonPrimitive().isNumber()) {
                        double var7 = var6.getAsDouble();
                        if (var7 == Math.rint(var7) && cc.N((int)var7)) {
                           var1.L.computeIfAbsent(t((String)var3.getKey()), var0x -> new LinkedHashMap<>()).put(m((String)var5.getKey()), (int)var7);
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

   private static String m(String var0) {
      if (var0 != null && !var0.isEmpty() && var0.length() <= 128 && !var0.chars().anyMatch(Character::isISOControl)) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid setting identifier");
      }
   }

   public record w(Module module, df<?> setting, int code) {
   }
}
