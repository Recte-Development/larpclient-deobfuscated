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

public final class oo {
   public static final int q = 8192;
   private final Map<Long, oo.j[]> Z = new LinkedHashMap<>();
   private final Set<Long> j = new HashSet<>();
   private final Set<Long> d = new HashSet<>();
   private final Set<Long> O = new HashSet<>();
   private int c;
   private int t;

   public void w() {
      this.Z.clear();
      this.j.clear();
      this.d.clear();
      this.O.clear();
      this.c = 0;
   }

   public void n(long var1) {
      this.Z.remove(var1);
      this.j.remove(var1);
      this.d.remove(var1);
      this.O.remove(var1);
   }

   public static boolean w(oo.j[] var0, int var1, int var2) {
      for (int var3 = 1; var3 <= Math.min(var1, var0.length - 1); var3++) {
         int var4 = (var2 + var3 - 1) * 16;
         int var5 = var4 + 15;
         if (var5 >= -64 && var4 <= 184 && var5 < 62 && var0[var3] == oo.j.s) {
            return true;
         }
      }

      return false;
   }

   public boolean q(int var1, int var2, int var3, int var4, BitSet var5, BitSet var6, List<oo.j> var7) {
      if (var3 > 0 && var3 <= 256) {
         if (var3 != this.c || var4 != this.t) {
            this.w();
            this.c = var3;
            this.t = var4;
         }

         long var8 = ChunkPos.pack(var1, var2);
         int var10 = !this.Z.containsKey(var8) && this.Z.size() >= 8192 ? 1 : 0;
         if (var10 != 0) {
            this.n(this.Z.keySet().iterator().next());
         }

         oo.j[] var11 = this.Z.computeIfAbsent(var8, var1x -> {
            oo.j[] var2x = new oo.j[this.c + 2];
            Arrays.fill(var2x, oo.j.t);
            return var2x;
         });
         int var12 = 0;

         for (int var13 = 0; var13 < var11.length; var13++) {
            if (var5 != null && var5.get(var13)) {
               var11[var13] = var12 < var7.size() ? (oo.j)var7.get(var12) : oo.j.t;
               var12++;
            } else if (var6 != null && var6.get(var13)) {
               var11[var13] = oo.j.r;
            }
         }

         int var14 = w(var11, this.c, this.t) && this.j.add(var8) ? 1 : 0;
         return var14 != 0 || var10 != 0;
      } else {
         return false;
      }
   }

   public Set<Long> K() {
      return Set.copyOf(this.j);
   }

   public Set<Long> v() {
      return Set.copyOf(this.O);
   }

   public int V() {
      return this.Z.size();
   }

   public static oo.j Z(byte[] var0) {
      if (var0 != null && var0.length == 2048) {
         byte var1 = 1;
         byte var2 = 1;

         for (byte var6 : var0) {
            var1 &= var6 == 0 ? 1 : 0;
            var2 &= var6 == -1 ? 1 : 0;
            if (var1 == 0 && var2 == 0) {
               return oo.j.x;
            }
         }

         return var1 != 0 ? oo.j.s : oo.j.w;
      } else {
         return oo.j.t;
      }
   }

   public static List<List<Long>> D(Set<Long> var0) {
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

   public static long y(List<Long> var0, int var1, int var2) {
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

   public List<Long> t(int var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();

      for (List var6 : D(this.j)) {
         if (var6.size() >= var1) {
            boolean var7 = var6.stream().anyMatch(this.d::contains);
            this.d.addAll(var6);
            if (!var7) {
               long var8 = y(var6, var2, var3);
               if (this.O.add(var8)) {
                  var4.add(var8);
               }
            }
         }
      }

      return List.copyOf(var4);
   }

   public boolean U(LongPredicate var1) {
      byte var2 = 0;

      for (long var4 : List.copyOf(this.Z.keySet())) {
         if (!var1.test(var4)) {
            this.n(var4);
            var2 = 1;
         }
      }

      return (boolean)var2;
   }

   public enum j {
      t,
      r,
      s,
      w,
      x;
   }
}
