package com.larpclient;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongPredicate;

public final class g0 {
   private final Map<Long, g0.t> u = new HashMap<>();
   private final Map<Long, g0.t> j = new HashMap<>();
   private final Map<Long, Long> H = new HashMap<>();
   private final LinkedHashMap<Long, Long> p = new LinkedHashMap<>();
   public static final int Q = 1024;
   private static final long a = 1200L;
   private static final long A = 6000L;
   private final Map<Long, g0.t> r = Collections.unmodifiableMap(this.j);

   public Map<Long, g0.t> A() {
      return Map.copyOf(this.u);
   }

   public void q(long var1) {
      this.H.remove(var1);
   }

   public Map<Long, g0.t> N() {
      return Map.copyOf(this.j);
   }

   public Map<Long, g0.t> K() {
      return this.r;
   }

   public int t() {
      return this.j.size();
   }

   public int P() {
      return this.H.size();
   }

   public int g() {
      return this.p.size();
   }

   public boolean V(long var1, long var3, long var5) {
      Long var7 = this.H.get(var1);
      return var7 == null || var3 - var7 >= var5;
   }

   public boolean G(long var1, g0.t var3, boolean var4, long var5) {
      if (this.u.size() >= 1024 && !this.u.containsKey(var1)) {
         return false;
      }

      this.H.put(var1, var5);
      this.u.put(var1, var3);
      return this.d(var1, var3, var4, var5);
   }

   public boolean d(long var1, g0.t var3, boolean var4, long var5) {
      if (!this.u.containsKey(var1)) {
         return false;
      }

      if (!var4) {
         this.j.remove(var1);
         return false;
      }

      int var7 = this.j.put(var1, var3) == null ? 1 : 0;
      Long var8 = this.p.get(var1);
      if (var7 != 0 && (var8 == null || var5 - var8 >= 1200L)) {
         this.p.remove(var1);
         this.p.put(var1, var5);

         while (this.p.size() > 1024) {
            this.p.remove(this.p.keySet().iterator().next());
         }

         return true;
      } else {
         return false;
      }
   }

   public void u(long var1) {
      this.j.remove(var1);
      this.u.remove(var1);
      this.H.remove(var1);
   }

   public void N(LongPredicate var1, long var2) {
      this.u.keySet().removeIf(var1x -> !var1.test(var1x));
      this.j.keySet().removeIf(var1x -> !var1.test(var1x));
      this.H.keySet().removeIf(var1x -> !var1.test(var1x));
      this.p.values().removeIf(var2x -> var2 - var2x >= 6000L);
   }

   public void q() {
      this.u.clear();
      this.j.clear();
      this.H.clear();
      this.p.clear();
   }

   public record g(int x, int y, int z, boolean trial, String name) {
   }

   public record t(int mature, int buds, int spawners, int sampleY, tm base, b7 sus, List<g0.g> hits) {
      public t(int mature, int buds, int spawners, int sampleY, tm base, b7 sus, List<g0.g> hits) {
         hits = List.copyOf(hits);
         this.mature = mature;
         this.buds = buds;
         this.spawners = spawners;
         this.sampleY = sampleY;
         this.base = base;
         this.sus = sus;
         this.hits = hits;
      }

      public t(int var1, int var2, int var3, int var4, tm var5) {
         this(var1, var2, var3, var4, var5, b7.EMPTY, List.of());
      }

      public t(int var1, int var2, int var3, int var4) {
         this(var1, var2, var3, var4, tm.EMPTY);
      }

      public boolean matureAmethyst(int var1, int var2) {
         long var3 = (long)this.mature + this.buds;
         return this.mature >= var1 && var3 > 0L && this.mature * 100L >= var3 * var2;
      }

      public int maturePercent() {
         long var1 = (long)this.mature + this.buds;
         return var1 == 0L ? 0 : (int)(this.mature * 100L / var1);
      }
   }
}
