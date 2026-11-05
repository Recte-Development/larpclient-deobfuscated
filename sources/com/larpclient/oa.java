package com.larpclient;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongPredicate;

public final class oa {
   private final Map<Long, oa.j> s = new HashMap<>();
   private final Map<Long, oa.j> Y = new HashMap<>();
   private final Map<Long, Long> H = new HashMap<>();
   private final LinkedHashMap<Long, Long> y = new LinkedHashMap<>();
   public static final int f = 1024;
   private static final long Q = 1200L;
   private static final long C = 6000L;

   public Map<Long, oa.j> H() {
      return Map.copyOf(this.s);
   }

   public void n(long var1) {
      this.H.remove(var1);
   }

   public Map<Long, oa.j> E() {
      return Map.copyOf(this.Y);
   }

   public int x() {
      return this.Y.size();
   }

   public int y() {
      return this.H.size();
   }

   public int J() {
      return this.y.size();
   }

   public boolean S(long var1, long var3, long var5) {
      Long var7 = this.H.get(var1);
      return var7 == null || var3 - var7 >= var5;
   }

   public boolean Z(long var1, oa.j var3, boolean var4, long var5) {
      if (this.s.size() >= 1024 && !this.s.containsKey(var1)) {
         return false;
      }

      this.H.put(var1, var5);
      this.s.put(var1, var3);
      return this.I(var1, var3, var4, var5);
   }

   public boolean I(long var1, oa.j var3, boolean var4, long var5) {
      if (!this.s.containsKey(var1)) {
         return false;
      }

      if (!var4) {
         this.Y.remove(var1);
         return false;
      }

      int var7 = this.Y.put(var1, var3) == null ? 1 : 0;
      Long var8 = this.y.get(var1);
      if (var7 != 0 && (var8 == null || var5 - var8 >= 1200L)) {
         this.y.remove(var1);
         this.y.put(var1, var5);

         while (this.y.size() > 1024) {
            this.y.remove(this.y.keySet().iterator().next());
         }

         return true;
      } else {
         return false;
      }
   }

   public void L(long var1) {
      this.Y.remove(var1);
      this.s.remove(var1);
      this.H.remove(var1);
   }

   public void l(LongPredicate var1, long var2) {
      this.s.keySet().removeIf(var1x -> !var1.test(var1x));
      this.Y.keySet().removeIf(var1x -> !var1.test(var1x));
      this.H.keySet().removeIf(var1x -> !var1.test(var1x));
      this.y.values().removeIf(var2x -> var2 - var2x >= 6000L);
   }

   public void U() {
      this.s.clear();
      this.Y.clear();
      this.H.clear();
      this.y.clear();
   }

   public record j(int mature, int buds, int spawners, int sampleY, l4 base, da sus, List<oa.o> hits) {
      public j(int mature, int buds, int spawners, int sampleY, l4 base, da sus, List<oa.o> hits) {
         hits = List.copyOf(hits);
         this.mature = mature;
         this.buds = buds;
         this.spawners = spawners;
         this.sampleY = sampleY;
         this.base = base;
         this.sus = sus;
         this.hits = hits;
      }

      public j(int var1, int var2, int var3, int var4, l4 var5) {
         this(var1, var2, var3, var4, var5, da.EMPTY, List.of());
      }

      public j(int var1, int var2, int var3, int var4) {
         this(var1, var2, var3, var4, l4.EMPTY);
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

   public record o(int x, int y, int z, boolean trial, String name) {
   }
}
