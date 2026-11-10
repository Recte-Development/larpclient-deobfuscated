package com.larpclient;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class he {
   private static final Map<String, Double> p = new LinkedHashMap<>();
   private static final Map<String, Long> U = new LinkedHashMap<>();
   private static final Map<String, Long> S = new LinkedHashMap<>();

   public static void z(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         U.put(var0, var1);
      }
   }

   public static void Z(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         U.merge(var0, var1, Long::sum);
      }
   }

   public static void t() {
      S.clear();
      if (Boolean.getBoolean("larp.debug")) {
         S.putAll(U);
      }

      U.clear();
   }

   public static Map<String, Long> V() {
      return Collections.unmodifiableMap(S.isEmpty() ? U : S);
   }

   public static long s() {
      return Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
   }

   public static void T(String var0, long var1) {
      if (var1 != 0L) {
         p.merge(var0, (System.nanoTime() - var1) / 1000000.0, (var0x, var1x) -> var0x * 0.9 + var1x * 0.1);
      }
   }

   public static Map<String, Double> R() {
      return Collections.unmodifiableMap(p);
   }
}
