package com.larpclient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongPredicate;

public final class d6 {
   private final Map<Long, d6.f> s = new HashMap<>();
   private final Map<Long, d6.f> w = new HashMap<>();
   private final Map<Long, Long> y = new HashMap<>();
   private final LinkedHashMap<Long, Long> F = new LinkedHashMap<>();
   public static final int V = 1024;
   private static final long K = 1200L;
   private static final long X = 6000L;

   public Map<Long, d6.f> c() {
      return Map.copyOf(this.s);
   }

   public void x(long var1) {
      this.y.remove(var1);
   }

   public Map<Long, d6.f> z() {
      return Map.copyOf(this.w);
   }

   public int C() {
      return this.w.size();
   }

   public int h() {
      return this.y.size();
   }

   public int u() {
      return this.F.size();
   }

   public boolean N(long var1, long var3, long var5) {
      Long var7 = this.y.get(var1);
      return var7 == null || var3 - var7 >= var5;
   }

   public boolean V(long var1, d6.f var3, boolean var4, long var5) {
      if (this.s.size() >= 1024 && !this.s.containsKey(var1)) {
         return false;
      }

      this.y.put(var1, var5);
      this.s.put(var1, var3);
      return this.R(var1, var3, var4, var5);
   }

   public boolean R(long var1, d6.f var3, boolean var4, long var5) {
      if (!this.s.containsKey(var1)) {
         return false;
      }

      if (!var4) {
         this.w.remove(var1);
         return false;
      }

      int var7 = this.w.put(var1, var3) == null ? 1 : 0;
      Long var8 = this.F.get(var1);
      if (var7 != 0 && (var8 == null || var5 - var8 >= 1200L)) {
         this.F.remove(var1);
         this.F.put(var1, var5);

         while (this.F.size() > 1024) {
            this.F.remove(this.F.keySet().iterator().next());
         }

         return true;
      } else {
         return false;
      }
   }

   public void h(long var1) {
      this.w.remove(var1);
      this.s.remove(var1);
      this.y.remove(var1);
   }

   public void Y(LongPredicate var1, long var2) {
      this.s.keySet().removeIf(var1x -> !var1.test(var1x));
      this.w.keySet().removeIf(var1x -> !var1.test(var1x));
      this.y.keySet().removeIf(var1x -> !var1.test(var1x));
      this.F.values().removeIf(var2x -> var2 - var2x >= 6000L);
   }

   public void f() {
      this.s.clear();
      this.w.clear();
      this.y.clear();
      this.F.clear();
   }

   public record b(int x, int y, int z, boolean trial, String name) {
   }

   public record f(int mature, int buds, int spawners, int sampleY, ce base, xl sus, List<d6.b> hits) {
      public f(int mature, int buds, int spawners, int sampleY, ce base, xl sus, List<d6.b> hits) {
         hits = List.copyOf(hits);
         this.mature = mature;
         this.buds = buds;
         this.spawners = spawners;
         this.sampleY = sampleY;
         this.base = base;
         this.sus = sus;
         this.hits = hits;
      }

      public f(int var1, int var2, int var3, int var4, ce var5) {
         this(var1, var2, var3, var4, var5, xl.EMPTY, List.of());
      }

      public f(int var1, int var2, int var3, int var4) {
         this(var1, var2, var3, var4, ce.EMPTY);
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
