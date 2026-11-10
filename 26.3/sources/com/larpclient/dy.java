package com.larpclient;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongPredicate;

public final class dy {
   private final Map<Long, dy.h> p = new HashMap<>();
   private final Map<Long, dy.h> c = new HashMap<>();
   private final Map<Long, Long> L = new HashMap<>();
   private final LinkedHashMap<Long, Long> I = new LinkedHashMap<>();
   public static final int T = 1024;
   private static final long S = 1200L;
   private static final long d = 6000L;
   private final Map<Long, dy.h> x = Collections.unmodifiableMap(this.c);

   public Map<Long, dy.h> C() {
      return Map.copyOf(this.p);
   }

   public void P(long var1) {
      this.L.remove(var1);
   }

   public Map<Long, dy.h> c() {
      return Map.copyOf(this.c);
   }

   public Map<Long, dy.h> q() {
      return this.x;
   }

   public int x() {
      return this.c.size();
   }

   public int z() {
      return this.L.size();
   }

   public int w() {
      return this.I.size();
   }

   public boolean N(long var1, long var3, long var5) {
      Long var7 = this.L.get(var1);
      return var7 == null || var3 - var7 >= var5;
   }

   public boolean u(long var1, dy.h var3, boolean var4, long var5) {
      if (this.p.size() >= 1024 && !this.p.containsKey(var1)) {
         return false;
      }

      this.L.put(var1, var5);
      this.p.put(var1, var3);
      return this.O(var1, var3, var4, var5);
   }

   public boolean O(long var1, dy.h var3, boolean var4, long var5) {
      if (!this.p.containsKey(var1)) {
         return false;
      }

      if (!var4) {
         this.c.remove(var1);
         return false;
      }

      int var7 = this.c.put(var1, var3) == null ? 1 : 0;
      Long var8 = this.I.get(var1);
      if (var7 != 0 && (var8 == null || var5 - var8 >= 1200L)) {
         this.I.remove(var1);
         this.I.put(var1, var5);

         while (this.I.size() > 1024) {
            this.I.remove(this.I.keySet().iterator().next());
         }

         return true;
      } else {
         return false;
      }
   }

   public void o(long var1) {
      this.c.remove(var1);
      this.p.remove(var1);
      this.L.remove(var1);
   }

   public void F(LongPredicate var1, long var2) {
      this.p.keySet().removeIf(var1x -> !var1.test(var1x));
      this.c.keySet().removeIf(var1x -> !var1.test(var1x));
      this.L.keySet().removeIf(var1x -> !var1.test(var1x));
      this.I.values().removeIf(var2x -> var2 - var2x >= 6000L);
   }

   public void y() {
      this.p.clear();
      this.c.clear();
      this.L.clear();
      this.I.clear();
   }

   public record h(int mature, int buds, int spawners, int sampleY, qq base, hl sus, List<dy.w> hits) {
      public h(int mature, int buds, int spawners, int sampleY, qq base, hl sus, List<dy.w> hits) {
         hits = List.copyOf(hits);
         this.mature = mature;
         this.buds = buds;
         this.spawners = spawners;
         this.sampleY = sampleY;
         this.base = base;
         this.sus = sus;
         this.hits = hits;
      }

      public h(int var1, int var2, int var3, int var4, qq var5) {
         this(var1, var2, var3, var4, var5, hl.EMPTY, List.of());
      }

      public h(int var1, int var2, int var3, int var4) {
         this(var1, var2, var3, var4, qq.EMPTY);
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

   public record w(int x, int y, int z, boolean trial, String name) {
   }
}
