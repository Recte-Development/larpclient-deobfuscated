package com.larpclient;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongPredicate;

public final class h5 {
   private final Map<Long, h5.n> k = new HashMap<>();
   private final Map<Long, h5.n> T = new HashMap<>();
   private final Map<Long, Long> y = new HashMap<>();
   private final LinkedHashMap<Long, Long> B = new LinkedHashMap<>();
   public static final int P = 1024;
   private static final long C = 1200L;
   private static final long h = 6000L;
   private final Map<Long, h5.n> S = Collections.unmodifiableMap(this.T);

   public Map<Long, h5.n> z() {
      return Map.copyOf(this.k);
   }

   public void P(long var1) {
      this.y.remove(var1);
   }

   public Map<Long, h5.n> E() {
      return Map.copyOf(this.T);
   }

   public Map<Long, h5.n> v() {
      return this.S;
   }

   public int d() {
      return this.T.size();
   }

   public int v() {
      return this.y.size();
   }

   public int k() {
      return this.B.size();
   }

   public boolean p(long var1, long var3, long var5) {
      Long var7 = this.y.get(var1);
      return var7 == null || var3 - var7 >= var5;
   }

   public boolean r(long var1, h5.n var3, boolean var4, long var5) {
      if (this.k.size() >= 1024 && !this.k.containsKey(var1)) {
         return false;
      }

      this.y.put(var1, var5);
      this.k.put(var1, var3);
      return this.Q(var1, var3, var4, var5);
   }

   public boolean Q(long var1, h5.n var3, boolean var4, long var5) {
      if (!this.k.containsKey(var1)) {
         return false;
      }

      if (!var4) {
         this.T.remove(var1);
         return false;
      }

      int var7 = this.T.put(var1, var3) == null ? 1 : 0;
      Long var8 = this.B.get(var1);
      if (var7 != 0 && (var8 == null || var5 - var8 >= 1200L)) {
         this.B.remove(var1);
         this.B.put(var1, var5);

         while (this.B.size() > 1024) {
            this.B.remove(this.B.keySet().iterator().next());
         }

         return true;
      } else {
         return false;
      }
   }

   public void c(long var1) {
      this.T.remove(var1);
      this.k.remove(var1);
      this.y.remove(var1);
   }

   public void P(LongPredicate var1, long var2) {
      this.k.keySet().removeIf(var1x -> !var1.test(var1x));
      this.T.keySet().removeIf(var1x -> !var1.test(var1x));
      this.y.keySet().removeIf(var1x -> !var1.test(var1x));
      this.B.values().removeIf(var2x -> var2 - var2x >= 6000L);
   }

   public void V() {
      this.k.clear();
      this.T.clear();
      this.y.clear();
      this.B.clear();
   }

   public record n(int mature, int buds, int spawners, int sampleY, z base, fe sus, List<h5.v> hits) {
      public n(int mature, int buds, int spawners, int sampleY, z base, fe sus, List<h5.v> hits) {
         hits = List.copyOf(hits);
         this.mature = mature;
         this.buds = buds;
         this.spawners = spawners;
         this.sampleY = sampleY;
         this.base = base;
         this.sus = sus;
         this.hits = hits;
      }

      public n(int var1, int var2, int var3, int var4, z var5) {
         this(var1, var2, var3, var4, var5, fe.EMPTY, List.of());
      }

      public n(int var1, int var2, int var3, int var4) {
         this(var1, var2, var3, var4, z.EMPTY);
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

   public record v(int x, int y, int z, boolean trial, String name) {
   }
}
