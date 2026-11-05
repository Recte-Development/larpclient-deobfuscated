package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public final class _x {
   public static final int l = 2048;
   public static final int A = 2048;
   public static final int L = 64;
   public static final long q = 600L;
   private final LinkedHashMap<Long, _x.f> i = new LinkedHashMap<>();
   private final ArrayDeque<_x.t> r = new ArrayDeque<>();
   private long R = Long.MIN_VALUE;
   private int u;
   private long p;
   private long s;
   private long z;
   private long c;
   private long g;
   private long t;
   private long a;
   private long U;
   private long X;

   public void V() {
      this.i.clear();
      this.r.clear();
      this.R = Long.MIN_VALUE;
      this.u = 0;
      this.p = this.s = this.z = this.c = this.g = this.t = this.a = this.U = this.X = 0L;
   }

   public void A() {
      this.p++;
   }

   public boolean K(long var1) {
      if (this.R != var1) {
         this.R = var1;
         this.u = 0;
      }

      return this.u < 2048;
   }

   public void l(_x.i var1, long var2) {
      this.t = this.t + Math.max(0L, var2);
      if (var1 == _x.i.q) {
         this.s = this.s + Math.max(0L, var2);
      }
   }

   private void D(_x.t var1) {
      if (this.r.size() == 64) {
         this.r.removeFirst();
         this.X++;
      }

      this.r.addLast(var1);
   }

   public void p(long var1, long var3, String var5, boolean var6, boolean var7, _x.i var8, long var9) {
      if (var8 == _x.i.q) {
         this.s++;
      }

      if (!this.K(var9)) {
         this.t++;
      } else {
         this.u++;
         _x.f var11 = this.i.get(var1);
         if (var11 != null && var9 - var11.tick() >= 600L) {
            this.i.remove(var1);
            this.U++;
            var11 = null;
         }

         if (var11 != null || var6) {
            if (var6) {
               this.z++;
            }

            if (var11 == null && var6) {
               this.D(new _x.t(var1, var3, "<not previously observed>", var5, "identifying state received", var8, var9));
            }

            if (var11 != null && !var11.identity().equals(var5)) {
               String var12;
               if (var11.clue() && !var6) {
                  if (var7) {
                     this.c++;
                     var12 = "clue to terrain, cause unknown";
                  } else {
                     this.g++;
                     var12 = "clue replaced, cause unknown";
                  }
               } else if (var6) {
                  var12 = var11.clue() ? "identifying state changed" : "identifying state received again";
               } else {
                  var12 = null;
               }

               if (var12 != null) {
                  this.D(new _x.t(var1, var3, var11.identity(), var5, var12, var8, var9));
               }
            }

            if (var11 == null && this.i.size() == 2048) {
               this.i.remove(this.i.keySet().iterator().next());
               this.a++;
            }

            this.i.put(var1, new _x.f(var3, var5, var6, var9));
         }
      }
   }

   public void O(long var1) {
      this.i.values().removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.U++;
         return true;
      });
      this.r.removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.X++;
         return true;
      });
   }

   public void k(long var1) {
      this.i.values().removeIf(var2 -> var2.chunk() == var1);
      this.r.removeIf(var2 -> var2.chunk() == var1);
   }

   public List<Long> j(long var1) {
      return this.i.entrySet().stream().filter(var2 -> var2.getValue().chunk() == var1).map(Entry::getKey).toList();
   }

   public List<_x.t> M(int var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < Math.max(0, var1) && !this.r.isEmpty(); var3++) {
         var2.add(this.r.removeFirst());
      }

      return List.copyOf(var2);
   }

   public _x.v J() {
      return new _x.v(this.p, this.s, this.z, this.c, this.g, this.t, this.a, this.U, this.X, this.i.size());
   }

   private record f(long chunk, String identity, boolean clue, long tick) {
   }

   public enum i {
      M,
      q;
   }

   public record t(long position, long chunk, String before, String after, String transition, _x.i source, long tick) {
   }

   public record v(
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
