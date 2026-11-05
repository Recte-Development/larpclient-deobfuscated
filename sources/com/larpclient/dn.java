package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public final class dn {
   public static final int c = 2048;
   public static final int S = 2048;
   public static final int N = 64;
   public static final long t = 600L;
   private final LinkedHashMap<Long, dn.c> V = new LinkedHashMap<>();
   private final ArrayDeque<dn.s> n = new ArrayDeque<>();
   private long Q = Long.MIN_VALUE;
   private int M;
   private long J;
   private long E;
   private long W;
   private long G;
   private long K;
   private long w;
   private long T;
   private long Z;
   private long g;

   public void u() {
      this.V.clear();
      this.n.clear();
      this.Q = Long.MIN_VALUE;
      this.M = 0;
      this.J = this.E = this.W = this.G = this.K = this.w = this.T = this.Z = this.g = 0L;
   }

   public void T() {
      this.J++;
   }

   public boolean H(long var1) {
      if (this.Q != var1) {
         this.Q = var1;
         this.M = 0;
      }

      return this.M < 2048;
   }

   public void L(dn.b var1, long var2) {
      this.w = this.w + Math.max(0L, var2);
      if (var1 == dn.b.n) {
         this.E = this.E + Math.max(0L, var2);
      }
   }

   private void x(dn.s var1) {
      if (this.n.size() == 64) {
         this.n.removeFirst();
         this.g++;
      }

      this.n.addLast(var1);
   }

   public void Q(long var1, long var3, String var5, boolean var6, boolean var7, dn.b var8, long var9) {
      if (var8 == dn.b.n) {
         this.E++;
      }

      if (!this.H(var9)) {
         this.w++;
      } else {
         this.M++;
         dn.c var11 = this.V.get(var1);
         if (var11 != null && var9 - var11.tick() >= 600L) {
            this.V.remove(var1);
            this.Z++;
            var11 = null;
         }

         if (var11 != null || var6) {
            if (var6) {
               this.W++;
            }

            if (var11 == null && var6) {
               this.x(new dn.s(var1, var3, "<not previously observed>", var5, "identifying state received", var8, var9));
            }

            if (var11 != null && !var11.identity().equals(var5)) {
               String var12;
               if (var11.clue() && !var6) {
                  if (var7) {
                     this.G++;
                     var12 = "clue to terrain, cause unknown";
                  } else {
                     this.K++;
                     var12 = "clue replaced, cause unknown";
                  }
               } else if (var6) {
                  var12 = var11.clue() ? "identifying state changed" : "identifying state received again";
               } else {
                  var12 = null;
               }

               if (var12 != null) {
                  this.x(new dn.s(var1, var3, var11.identity(), var5, var12, var8, var9));
               }
            }

            if (var11 == null && this.V.size() == 2048) {
               this.V.remove(this.V.keySet().iterator().next());
               this.T++;
            }

            this.V.put(var1, new dn.c(var3, var5, var6, var9));
         }
      }
   }

   public void V(long var1) {
      this.V.values().removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.Z++;
         return true;
      });
      this.n.removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.g++;
         return true;
      });
   }

   public void X(long var1) {
      this.V.values().removeIf(var2 -> var2.chunk() == var1);
      this.n.removeIf(var2 -> var2.chunk() == var1);
   }

   public List<Long> m(long var1) {
      return this.V.entrySet().stream().filter(var2 -> var2.getValue().chunk() == var1).map(Entry::getKey).toList();
   }

   public List<dn.s> C(int var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < Math.max(0, var1) && !this.n.isEmpty(); var3++) {
         var2.add(this.n.removeFirst());
      }

      return List.copyOf(var2);
   }

   public dn.y I() {
      return new dn.y(this.J, this.E, this.W, this.G, this.K, this.w, this.T, this.Z, this.g, this.V.size());
   }

   public enum b {
      w,
      n;
   }

   private record c(long chunk, String identity, boolean clue, long tick) {
   }

   public record s(long position, long chunk, String before, String after, String transition, dn.b source, long tick) {
   }

   public record y(
      long chunks,
      long updates,
      long clues,
      long terrainReplacements,
      long otherReplacements,
      long budgetDrops,
      long evictions,
      long expired,
      long omittedDetails,
      int tracked
   ) {
   }
}
