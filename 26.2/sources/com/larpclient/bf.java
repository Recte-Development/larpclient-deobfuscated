package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public final class bf {
   public static final int k = 2048;
   public static final int z = 2048;
   public static final int a = 64;
   public static final long K = 600L;
   private final LinkedHashMap<Long, bf.u> Q = new LinkedHashMap<>();
   private final ArrayDeque<bf.d> A = new ArrayDeque<>();
   private long O = Long.MIN_VALUE;
   private int d;
   private long L;
   private long D;
   private long U;
   private long r;
   private long v;
   private long c;
   private long E;
   private long T;
   private long x;

   public void F() {
      this.Q.clear();
      this.A.clear();
      this.O = Long.MIN_VALUE;
      this.d = 0;
      this.L = this.D = this.U = this.r = this.v = this.c = this.E = this.T = this.x = 0L;
   }

   public void P() {
      this.L++;
   }

   public boolean p(long var1) {
      if (this.O != var1) {
         this.O = var1;
         this.d = 0;
      }

      return this.d < 2048;
   }

   public void o(bf.v var1, long var2) {
      this.c = this.c + Math.max(0L, var2);
      if (var1 == bf.v.E) {
         this.D = this.D + Math.max(0L, var2);
      }
   }

   private void X(bf.d var1) {
      if (this.A.size() == 64) {
         this.A.removeFirst();
         this.x++;
      }

      this.A.addLast(var1);
   }

   public void G(long var1, long var3, String var5, boolean var6, boolean var7, bf.v var8, long var9) {
      if (var8 == bf.v.E) {
         this.D++;
      }

      if (!this.p(var9)) {
         this.c++;
      } else {
         this.d++;
         bf.u var11 = this.Q.get(var1);
         if (var11 != null && var9 - var11.tick() >= 600L) {
            this.Q.remove(var1);
            this.T++;
            var11 = null;
         }

         if (var11 != null || var6) {
            if (var6) {
               this.U++;
            }

            if (var11 == null && var6) {
               this.X(new bf.d(var1, var3, "<not previously observed>", var5, "identifying state received", var8, var9));
            }

            if (var11 != null && !var11.identity().equals(var5)) {
               String var12;
               if (var11.clue() && !var6) {
                  if (var7) {
                     this.r++;
                     var12 = "clue to terrain, cause unknown";
                  } else {
                     this.v++;
                     var12 = "clue replaced, cause unknown";
                  }
               } else if (var6) {
                  var12 = var11.clue() ? "identifying state changed" : "identifying state received again";
               } else {
                  var12 = null;
               }

               if (var12 != null) {
                  this.X(new bf.d(var1, var3, var11.identity(), var5, var12, var8, var9));
               }
            }

            if (var11 == null && this.Q.size() == 2048) {
               this.Q.remove(this.Q.keySet().iterator().next());
               this.E++;
            }

            this.Q.put(var1, new bf.u(var3, var5, var6, var9));
         }
      }
   }

   public void M(long var1) {
      this.Q.values().removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.T++;
         return true;
      });
      this.A.removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.x++;
         return true;
      });
   }

   public void F(long var1) {
      this.Q.values().removeIf(var2 -> var2.chunk() == var1);
      this.A.removeIf(var2 -> var2.chunk() == var1);
   }

   public List<Long> z(long var1) {
      return this.Q.entrySet().stream().filter(var2 -> var2.getValue().chunk() == var1).map(Entry::getKey).toList();
   }

   public List<bf.d> y(int var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < Math.max(0, var1) && !this.A.isEmpty(); var3++) {
         var2.add(this.A.removeFirst());
      }

      return List.copyOf(var2);
   }

   public bf.c Q() {
      return new bf.c(this.L, this.D, this.U, this.r, this.v, this.c, this.E, this.T, this.x, this.Q.size());
   }

   public record c(
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

   public record d(long position, long chunk, String before, String after, String transition, bf.v source, long tick) {
   }

   private record u(long chunk, String identity, boolean clue, long tick) {
   }

   public enum v {
      c,
      E;
   }
}
