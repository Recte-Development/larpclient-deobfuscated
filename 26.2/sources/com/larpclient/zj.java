package com.larpclient;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class zj {
   private static final Map<String, Double> O = new LinkedHashMap<>();
   private static final Map<String, Long> n = new LinkedHashMap<>();
   private static final Map<String, Long> d = new LinkedHashMap<>();

   public static void s(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         n.put(var0, var1);
      }
   }

   public static void K(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         n.merge(var0, var1, Long::sum);
      }
   }

   public static void U() {
      d.clear();
      if (Boolean.getBoolean("larp.debug")) {
         d.putAll(n);
      }

      n.clear();
   }

   public static Map<String, Long> R() {
      return Collections.unmodifiableMap(d.isEmpty() ? n : d);
   }

   public static long m() {
      return Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
   }

   public static void l(String var0, long var1) {
      if (var1 != 0L) {
         O.merge(var0, (System.nanoTime() - var1) / 1000000.0, (var0x, var1x) -> var0x * 0.9 + var1x * 0.1);
      }
   }

   public static Map<String, Double> J() {
      return Collections.unmodifiableMap(O);
   }
}
