package com.larpclient;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class h_ {
   private static final Map<String, Double> p = new LinkedHashMap<>();
   private static final Map<String, Long> J = new LinkedHashMap<>();
   private static final Map<String, Long> s = new LinkedHashMap<>();

   public static void S(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         J.put(var0, var1);
      }
   }

   public static void A(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         J.merge(var0, var1, Long::sum);
      }
   }

   public static void I() {
      s.clear();
      if (Boolean.getBoolean("larp.debug")) {
         s.putAll(J);
      }

      J.clear();
   }

   public static Map<String, Long> D() {
      return Collections.unmodifiableMap(s.isEmpty() ? J : s);
   }

   public static long U() {
      return Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
   }

   public static void n(String var0, long var1) {
      if (var1 != 0L) {
         p.merge(var0, (System.nanoTime() - var1) / 1000000.0, (var0x, var1x) -> var0x * 0.9 + var1x * 0.1);
      }
   }

   public static Map<String, Double> b() {
      return Collections.unmodifiableMap(p);
   }
}
