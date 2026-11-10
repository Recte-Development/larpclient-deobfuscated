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
import net.minecraft.class_1923;

public final class fq {
   public static final int w = 8192;
   private final Map<Long, fq.g[]> L = new LinkedHashMap<>();
   private final Set<Long> h = new HashSet<>();
   private final Set<Long> l = new HashSet<>();
   private final Set<Long> x = new HashSet<>();
   private int y;
   private int m;

   public void T() {
      this.L.clear();
      this.h.clear();
      this.l.clear();
      this.x.clear();
      this.y = 0;
   }

   public void a(long var1) {
      this.L.remove(var1);
      this.h.remove(var1);
      this.l.remove(var1);
      this.x.remove(var1);
   }

   public static boolean S(fq.g[] var0, int var1, int var2) {
      for (int var3 = 1; var3 <= Math.min(var1, var0.length - 1); var3++) {
         int var4 = (var2 + var3 - 1) * 16;
         int var5 = var4 + 15;
         if (var5 >= -64 && var4 <= 184 && var5 < 62 && var0[var3] == fq.g.i) {
            return true;
         }
      }

      return false;
   }

   public boolean P(int var1, int var2, int var3, int var4, BitSet var5, BitSet var6, List<fq.g> var7) {
      if (var3 > 0 && var3 <= 256) {
         if (var3 != this.y || var4 != this.m) {
            this.T();
            this.y = var3;
            this.m = var4;
         }

         long var8 = class_1923.method_8331(var1, var2);
         int var10 = !this.L.containsKey(var8) && this.L.size() >= 8192 ? 1 : 0;
         if (var10 != 0) {
            this.a(this.L.keySet().iterator().next());
         }

         fq.g[] var11 = this.L.computeIfAbsent(var8, var1x -> {
            fq.g[] var2x = new fq.g[this.y + 2];
            Arrays.fill(var2x, fq.g.J);
            return var2x;
         });
         int var12 = 0;

         for (int var13 = 0; var13 < var11.length; var13++) {
            if (var5 != null && var5.get(var13)) {
               var11[var13] = var12 < var7.size() ? (fq.g)var7.get(var12) : fq.g.J;
               var12++;
            } else if (var6 != null && var6.get(var13)) {
               var11[var13] = fq.g.l;
            }
         }

         int var14 = S(var11, this.y, this.m) && this.h.add(var8) ? 1 : 0;
         return var14 != 0 || var10 != 0;
      } else {
         return false;
      }
   }

   public Set<Long> O() {
      return Set.copyOf(this.h);
   }

   public Set<Long> M() {
      return Set.copyOf(this.x);
   }

   public int R() {
      return this.L.size();
   }

   public static fq.g k(byte[] var0) {
      if (var0 != null && var0.length == 2048) {
         byte var1 = 1;
         byte var2 = 1;

         for (byte var6 : var0) {
            var1 &= var6 == 0 ? 1 : 0;
            var2 &= var6 == -1 ? 1 : 0;
            if (var1 == 0 && var2 == 0) {
               return fq.g.I;
            }
         }

         return var1 != 0 ? fq.g.i : fq.g.G;
      } else {
         return fq.g.J;
      }
   }

   public static List<List<Long>> X(Set<Long> var0) {
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
            class_1923 var9 = new class_1923(var7);

            for (int var10 = -2; var10 <= 2; var10++) {
               for (int var11 = -2; var11 <= 2; var11++) {
                  if (var10 != 0 || var11 != 0) {
                     long var12 = class_1923.method_8331(var9.field_9181 + var10, var9.field_9180 + var11);
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

   public static long V(List<Long> var0, int var1, int var2) {
      if (var0.isEmpty()) {
         throw new IllegalArgumentException("Empty candidate group");
      } else {
         return var0.stream().min(Comparator.<Long>comparingDouble(var2x -> {
            class_1923 var3 = new class_1923(var2x);
            double var4 = (double)var3.field_9181 - var1;
            double var6 = (double)var3.field_9180 - var2;
            return var4 * var4 + var6 * var6;
         }).thenComparingLong(Long::longValue)).orElseThrow();
      }
   }

   public List<Long> s(int var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();

      for (List var6 : X(this.h)) {
         if (var6.size() >= var1) {
            boolean var7 = var6.stream().anyMatch(this.l::contains);
            this.l.addAll(var6);
            if (!var7) {
               long var8 = V(var6, var2, var3);
               if (this.x.add(var8)) {
                  var4.add(var8);
               }
            }
         }
      }

      return List.copyOf(var4);
   }

   public boolean j(LongPredicate var1) {
      byte var2 = 0;

      for (long var4 : List.copyOf(this.L.keySet())) {
         if (!var1.test(var4)) {
            this.a(var4);
            var2 = 1;
         }
      }

      return (boolean)var2;
   }

   public enum g {
      J,
      l,
      i,
      G,
      I;
   }
}
