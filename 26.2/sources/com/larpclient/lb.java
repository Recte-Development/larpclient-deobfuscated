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

public final class lb {
   public static final int h = 8192;
   private final Map<Long, lb.g[]> K = new LinkedHashMap<>();
   private final Set<Long> W = new HashSet<>();
   private final Set<Long> L = new HashSet<>();
   private final Set<Long> B = new HashSet<>();
   private int j;
   private int k;

   public void m() {
      this.K.clear();
      this.W.clear();
      this.L.clear();
      this.B.clear();
      this.j = 0;
   }

   public void b(long var1) {
      this.K.remove(var1);
      this.W.remove(var1);
      this.L.remove(var1);
      this.B.remove(var1);
   }

   public static boolean l(lb.g[] var0, int var1, int var2) {
      for (int var3 = 1; var3 <= Math.min(var1, var0.length - 1); var3++) {
         int var4 = (var2 + var3 - 1) * 16;
         int var5 = var4 + 15;
         if (var5 >= -64 && var4 <= 184 && var5 < 62 && var0[var3] == lb.g.w) {
            return true;
         }
      }

      return false;
   }

   public boolean h(int var1, int var2, int var3, int var4, BitSet var5, BitSet var6, List<lb.g> var7) {
      if (var3 > 0 && var3 <= 256) {
         if (var3 != this.j || var4 != this.k) {
            this.m();
            this.j = var3;
            this.k = var4;
         }

         long var8 = ChunkPos.pack(var1, var2);
         int var10 = !this.K.containsKey(var8) && this.K.size() >= 8192 ? 1 : 0;
         if (var10 != 0) {
            this.b(this.K.keySet().iterator().next());
         }

         lb.g[] var11 = this.K.computeIfAbsent(var8, var1x -> {
            lb.g[] var2x = new lb.g[this.j + 2];
            Arrays.fill(var2x, lb.g.R);
            return var2x;
         });
         int var12 = 0;

         for (int var13 = 0; var13 < var11.length; var13++) {
            if (var5 != null && var5.get(var13)) {
               var11[var13] = var12 < var7.size() ? (lb.g)var7.get(var12) : lb.g.R;
               var12++;
            } else if (var6 != null && var6.get(var13)) {
               var11[var13] = lb.g.E;
            }
         }

         int var14 = l(var11, this.j, this.k) && this.W.add(var8) ? 1 : 0;
         return var14 != 0 || var10 != 0;
      } else {
         return false;
      }
   }

   public Set<Long> r() {
      return Set.copyOf(this.W);
   }

   public Set<Long> t() {
      return Set.copyOf(this.B);
   }

   public int L() {
      return this.K.size();
   }

   public static lb.g Z(byte[] var0) {
      if (var0 != null && var0.length == 2048) {
         byte var1 = 1;
         byte var2 = 1;

         for (byte var6 : var0) {
            var1 &= var6 == 0 ? 1 : 0;
            var2 &= var6 == -1 ? 1 : 0;
            if (var1 == 0 && var2 == 0) {
               return lb.g.y;
            }
         }

         return var1 != 0 ? lb.g.w : lb.g.b;
      } else {
         return lb.g.R;
      }
   }

   public static List<List<Long>> j(Set<Long> var0) {
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

   public static long R(List<Long> var0, int var1, int var2) {
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

   public List<Long> V(int var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();

      for (List var6 : j(this.W)) {
         if (var6.size() >= var1) {
            boolean var7 = var6.stream().anyMatch(this.L::contains);
            this.L.addAll(var6);
            if (!var7) {
               long var8 = R(var6, var2, var3);
               if (this.B.add(var8)) {
                  var4.add(var8);
               }
            }
         }
      }

      return List.copyOf(var4);
   }

   public boolean Q(LongPredicate var1) {
      byte var2 = 0;

      for (long var4 : List.copyOf(this.K.keySet())) {
         if (!var1.test(var4)) {
            this.b(var4);
            var2 = 1;
         }
      }

      return (boolean)var2;
   }

   public enum g {
      R,
      E,
      w,
      b,
      y;
   }
}
