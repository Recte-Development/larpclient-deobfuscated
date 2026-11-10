package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class je {
   private static final int b = 256;
   private final Map<String, Map<String, Integer>> a = new LinkedHashMap<>();
   private List<je.w> M;
   private List<Module> D;

   public static boolean O(com.larpclient.module.lb<?> var0) {
      return var0 instanceof com.larpclient.module.l3 || var0 instanceof com.larpclient.module.li var1 && !var1.isSearchable();
   }

   public static Module S(List<Module> var0, com.larpclient.module.lb<?> var1) {
      for (Module var3 : var0) {
         for (com.larpclient.module.lb var5 : var3.getSettings()) {
            if (var5 == var1) {
               return var3;
            }
         }
      }

      return null;
   }

   public int C(Module var1, com.larpclient.module.lb<?> var2) {
      Map var3 = this.a.get(var1.getName());
      return var3 == null ? -1 : var3.getOrDefault(var2.R(), -1);
   }

   public void v(Module var1, com.larpclient.module.lb<?> var2, int var3) {
      if (!O(var2)) {
         throw new IllegalArgumentException("Only toggle and choice settings take a shortcut");
      }

      if (var3 != -1 && !s5.A(var3)) {
         throw new IllegalArgumentException("Invalid bind code");
      }

      String var4 = B(var1.getName());
      String var5 = D(var2.R());
      if (var3 == -1) {
         Map var6 = this.a.get(var4);
         if (var6 != null && var6.remove(var5) != null && var6.isEmpty()) {
            this.a.remove(var4);
         }
      } else {
         if (this.C(var1, var2) == -1 && this.r() >= 256) {
            throw new IllegalArgumentException("Too many setting shortcuts");
         }

         this.a.computeIfAbsent(var4, var0 -> new LinkedHashMap<>()).put(var5, var3);
      }

      this.M = null;
   }

   public int r() {
      return this.a.values().stream().mapToInt(Map::size).sum();
   }

   public List<je.w> i(List<Module> var1) {
      if (this.M != null && this.D == var1) {
         return this.M;
      }

      ArrayList var2 = new ArrayList();

      for (Module var4 : var1) {
         Map var5 = this.a.get(var4.getName());
         if (var5 != null) {
            for (com.larpclient.module.lb var7 : var4.getSettings()) {
               Integer var8 = (Integer)var5.get(var7.R());
               if (var8 != null && O(var7)) {
                  var2.add(new je.w(var4, var7, var8));
               }
            }
         }
      }

      this.D = var1;
      return this.M = List.copyOf(var2);
   }

   public je b() {
      return M(this.y());
   }

   public JsonObject y() {
      JsonObject var1 = new JsonObject();
      this.a.forEach((var1x, var2) -> {
         JsonObject var3 = new JsonObject();
         var2.forEach(var3::addProperty);
         var1.add(var1x, var3);
      });
      return var1;
   }

   public static je M(JsonElement var0) {
      je var1 = new je();
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
                        if (var7 == Math.rint(var7) && s5.A((int)var7)) {
                           var1.a.computeIfAbsent(B((String)var3.getKey()), var0x -> new LinkedHashMap<>()).put(D((String)var5.getKey()), (int)var7);
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

   private static String B(String var0) {
      if (var0 != null && var0.matches("[A-Za-z0-9 _-]{1,128}") && !var0.isBlank()) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid module identifier");
      }
   }

   private static String D(String var0) {
      if (var0 != null && !var0.isEmpty() && var0.length() <= 128 && !var0.chars().anyMatch(Character::isISOControl)) {
         return var0;
      } else {
         throw new IllegalArgumentException("Invalid setting identifier");
      }
   }

   public record w(Module module, com.larpclient.module.lb<?> setting, int code) {
   }
}
