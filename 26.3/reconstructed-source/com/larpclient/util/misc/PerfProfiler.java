package com.larpclient.util.misc;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class PerfProfiler {
   private static final Map<String, Double> smoothedTimingsMs = new LinkedHashMap<>();
   private static final Map<String, Long> currentCounters = new LinkedHashMap<>();
   private static final Map<String, Long> lastFrameCounters = new LinkedHashMap<>();

   public static void setCounter(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         currentCounters.put(var0, var1);
      }
   }

   public static void addCounter(String var0, long var1) {
      if (Boolean.getBoolean("larp.debug")) {
         currentCounters.merge(var0, var1, Long::sum);
      }
   }

   public static void endFrame() {
      lastFrameCounters.clear();
      if (Boolean.getBoolean("larp.debug")) {
         lastFrameCounters.putAll(currentCounters);
      }

      currentCounters.clear();
   }

   public static Map<String, Long> getCounters() {
      return Collections.unmodifiableMap(lastFrameCounters.isEmpty() ? currentCounters : lastFrameCounters);
   }

   public static long startTimer() {
      return Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
   }

   public static void endTimer(String var0, long var1) {
      if (var1 != 0L) {
         smoothedTimingsMs.merge(var0, (System.nanoTime() - var1) / 1000000.0, (var0x, var1x) -> var0x * 0.9 + var1x * 0.1);
      }
   }

   public static Map<String, Double> getTimings() {
      return Collections.unmodifiableMap(smoothedTimingsMs);
   }
}
