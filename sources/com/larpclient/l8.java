package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.LongPredicate;
import net.minecraft.world.level.ChunkPos;

public final class l8 {
   public static final int a = 8192;
   private final Map<Long, l8.w[]> A = new LinkedHashMap<>();
   private final Set<Long> H = new HashSet<>();
   private final Set<Long> U = new HashSet<>();
   private final Set<Long> p = new HashSet<>();
   private int g;
   private int x;

   public void v() {
      this.A.clear();
      this.H.clear();
      this.U.clear();
      this.p.clear();
      this.g = 0;
   }

   public void y(long var1) {
      this.A.remove(var1);
      this.H.remove(var1);
      this.U.remove(var1);
      this.p.remove(var1);
   }

   public static boolean B(l8.w[] var0, int var1, int var2) {
      for (int var3 = 1; var3 <= Math.min(var1, var0.length - 1); var3++) {
         int var4 = (var2 + var3 - 1) * 16;
         int var5 = var4 + 15;
         if (var5 >= -64 && var4 <= 184 && var5 < 62 && var0[var3] == l8.w.K) {
            return true;
         }
      }

      return false;
   }

   public boolean f(int var1, int var2, int var3, int var4, BitSet var5, BitSet var6, List<l8.w> var7) {
      if (var3 > 0 && var3 <= 256) {
         if (var3 != this.g || var4 != this.x) {
            this.v();
            this.g = var3;
            this.x = var4;
         }

         long var8 = ChunkPos.pack(var1, var2);
         int var10 = !this.A.containsKey(var8) && this.A.size() >= 8192 ? 1 : 0;
         if (var10 != 0) {
            this.y(this.A.keySet().iterator().next());
         }

         l8.w[] var11 = this.A.computeIfAbsent(var8, var1x -> {
            l8.w[] var2x = new l8.w[this.g + 2];
            Arrays.fill(var2x, l8.w.n);
            return var2x;
         });
         int var12 = 0;

         for (int var13 = 0; var13 < var11.length; var13++) {
            if (var5 != null && var5.get(var13)) {
               var11[var13] = var12 < var7.size() ? (l8.w)var7.get(var12) : l8.w.n;
               var12++;
            } else if (var6 != null && var6.get(var13)) {
               var11[var13] = l8.w.I;
            }
         }

         int var14 = B(var11, this.g, this.x) && this.H.add(var8) ? 1 : 0;
         return var14 != 0 || var10 != 0;
      } else {
         return false;
      }
   }

   public Set<Long> E() {
      return Set.copyOf(this.H);
   }

   public Set<Long> U() {
      return Set.copyOf(this.p);
   }

   public int H() {
      return this.A.size();
   }

   public static l8.w V(byte[] var0) {
      if (var0 != null && var0.length == 2048) {
         byte var1 = 1;
         byte var2 = 1;

         for (byte var6 : var0) {
            var1 &= var6 == 0 ? 1 : 0;
            var2 &= var6 == -1 ? 1 : 0;
            if (var1 == 0 && var2 == 0) {
               return l8.w.d;
            }
         }

         return var1 != 0 ? l8.w.K : l8.w.T;
      } else {
         return l8.w.n;
      }
   }

   public static List<List<Long>> b(Set<Long> var0) {
      TreeSet var1 = new TreeSet(var0);
      ArrayList var2 = new ArrayList();

      while (!var1.isEmpty()) {
         long var3 = (Long)var1.iterator().next();
         var1.remove(var3);
         ArrayDeque var5 = new ArrayDeque();
         var5.add(var3);
         ArrayList var6 = new ArrayList();

         while (!var5.isEmpty()) {
            long var7 = (Long)var5.removeFirst();
            var6.add(var7);
            ChunkPos var9 = ChunkPos.unpack(var7);

            for (int var10 = -2; var10 <= 2; var10++) {
               for (int var11 = -2; var11 <= 2; var11++) {
                  if (var10 != 0 || var11 != 0) {
                     long var12 = ChunkPos.pack(var9.x() + var10, var9.z() + var11);
                     if (var1.remove(var12)) {
                        var5.addLast(var12);
                     }
                  }
               }
            }
         }

         var2.add(List.copyOf(var6));
      }

      return List.copyOf(var2);
   }

   public static long w(List<Long> var0, int var1, int var2) {
      if (var0.isEmpty()) {
         throw new IllegalArgumentException("Empty candidate group");
      } else {
         return var0.stream().min(Comparator.<Long>comparingDouble(var2x -> {
            ChunkPos var3 = ChunkPos.unpack(var2x);
            double var4 = (double)var3.x() - var1;
            double var6 = (double)var3.z() - var2;
            return var4 * var4 + var6 * var6;
         }).thenComparingLong(Long::longValue)).orElseThrow();
      }
   }

   public List<Long> N(int var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();

      for (List var6 : b(this.H)) {
         if (var6.size() >= var1) {
            boolean var7 = var6.stream().anyMatch(this.U::contains);
            this.U.addAll(var6);
            if (!var7) {
               long var8 = w(var6, var2, var3);
               if (this.p.add(var8)) {
                  var4.add(var8);
               }
            }
         }
      }

      return List.copyOf(var4);
   }

   public boolean T(LongPredicate var1) {
      byte var2 = 0;

      for (long var4 : List.copyOf(this.A.keySet())) {
         if (!var1.test(var4)) {
            this.y(var4);
            var2 = 1;
         }
      }

      return (boolean)var2;
   }

   public enum w {
      n,
      I,
      K,
      T,
      d;
   }
}
