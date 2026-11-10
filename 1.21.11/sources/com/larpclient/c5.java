package com.larpclient;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class c5 {
   private static final Map<String, Double> S = new LinkedHashMap<>();
   private static final Map<String, Long> l = new LinkedHashMap<>();
   private static final Map<String, Long> B = new LinkedHashMap<>();

   public static void L(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         l.put(var0, var1);
      }
   }

   public static void V(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         l.merge(var0, var1, Long::sum);
      }
   }

   public static void O() {
      B.clear();
      if (Boolean.getBoolean("larp.debug")) {
         B.putAll(l);
      }

      l.clear();
   }

   public static Map<String, Long> G() {
      return Collections.unmodifiableMap(B.isEmpty() ? l : B);
   }

   public static long X() {
      return Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
   }

   public static void w(String var0, long var1) {
      if (var1 != 0L) {
         S.merge(var0, (System.nanoTime() - var1) / 1000000.0, (var0x, var1x) -> var0x * 0.9 + var1x * 0.1);
      }
   }

   public static Map<String, Double> M() {
      return Collections.unmodifiableMap(S);
   }
}
