package com.larpclient;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class kx {
   private static final Map<String, Double> r = new LinkedHashMap<>();
   private static final Map<String, Long> d = new LinkedHashMap<>();
   private static final Map<String, Long> A = new LinkedHashMap<>();

   public static void s(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         d.put(var0, var1);
      }
   }

   public static void U(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         d.merge(var0, var1, Long::sum);
      }
   }

   public static void O() {
      A.clear();
      if (Boolean.getBoolean("larp.debug")) {
         A.putAll(d);
      }

      d.clear();
   }

   public static Map<String, Long> G() {
      return Collections.unmodifiableMap(A.isEmpty() ? d : A);
   }

   public static long G() {
      return Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
   }

   public static void u(String var0, long var1) {
      if (var1 != 0L) {
         r.merge(var0, (System.nanoTime() - var1) / 1000000.0, (var0x, var1x) -> var0x * 0.9 + var1x * 0.1);
      }
   }

   public static Map<String, Double> M() {
      return Collections.unmodifiableMap(r);
   }
}
