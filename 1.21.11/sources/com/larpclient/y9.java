package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public final class y9 {
   public static final int D = 2048;
   public static final int S = 2048;
   public static final int s = 64;
   public static final long J = 600L;
   private final LinkedHashMap<Long, y9.d> M = new LinkedHashMap<>();
   private final ArrayDeque<y9.w> k = new ArrayDeque<>();
   private long I = Long.MIN_VALUE;
   private int y;
   private long r;
   private long j;
   private long e;
   private long b;
   private long f;
   private long F;
   private long Z;
   private long z;
   private long V;

   public void J() {
      this.M.clear();
      this.k.clear();
      this.I = Long.MIN_VALUE;
      this.y = 0;
      this.r = this.j = this.e = this.b = this.f = this.F = this.Z = this.z = this.V = 0L;
   }

   public void P() {
      this.r++;
   }

   public boolean B(long var1) {
      if (this.I != var1) {
         this.I = var1;
         this.y = 0;
      }

      return this.y < 2048;
   }

   public void X(y9.b var1, long var2) {
      this.F = this.F + Math.max(0L, var2);
      if (var1 == y9.b.E) {
         this.j = this.j + Math.max(0L, var2);
      }
   }

   private void H(y9.w var1) {
      if (this.k.size() == 64) {
         this.k.removeFirst();
         this.V++;
      }

      this.k.addLast(var1);
   }

   public void U(long var1, long var3, String var5, boolean var6, boolean var7, y9.b var8, long var9) {
      if (var8 == y9.b.E) {
         this.j++;
      }

      if (!this.B(var9)) {
         this.F++;
      } else {
         this.y++;
         y9.d var11 = this.M.get(var1);
         if (var11 != null && var9 - var11.tick() >= 600L) {
            this.M.remove(var1);
            this.z++;
            var11 = null;
         }

         if (var11 != null || var6) {
            if (var6) {
               this.e++;
            }

            if (var11 == null && var6) {
               this.H(new y9.w(var1, var3, "<not previously observed>", var5, "identifying state received", var8, var9));
            }

            if (var11 != null && !var11.identity().equals(var5)) {
               String var12;
               if (var11.clue() && !var6) {
                  if (var7) {
                     this.b++;
                     var12 = "clue to terrain, cause unknown";
                  } else {
                     this.f++;
                     var12 = "clue replaced, cause unknown";
                  }
               } else if (var6) {
                  var12 = var11.clue() ? "identifying state changed" : "identifying state received again";
               } else {
                  var12 = null;
               }

               if (var12 != null) {
                  this.H(new y9.w(var1, var3, var11.identity(), var5, var12, var8, var9));
               }
            }

            if (var11 == null && this.M.size() == 2048) {
               this.M.remove(this.M.keySet().iterator().next());
               this.Z++;
            }

            this.M.put(var1, new y9.d(var3, var5, var6, var9));
         }
      }
   }

   public void H(long var1) {
      this.M.values().removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.z++;
         return true;
      });
      this.k.removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.V++;
         return true;
      });
   }

   public void k(long var1) {
      this.M.values().removeIf(var2 -> var2.chunk() == var1);
      this.k.removeIf(var2 -> var2.chunk() == var1);
   }

   public List<Long> U(long var1) {
      return this.M.entrySet().stream().filter(var2 -> var2.getValue().chunk() == var1).map(Entry::getKey).toList();
   }

   public List<y9.w> E(int var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < Math.max(0, var1) && !this.k.isEmpty(); var3++) {
         var2.add(this.k.removeFirst());
      }

      return List.copyOf(var2);
   }

   public y9.s V() {
      return new y9.s(this.r, this.j, this.e, this.b, this.f, this.F, this.Z, this.z, this.V, this.M.size());
   }

   public enum b {
      u,
      E;
   }

   private record d(long chunk, String identity, boolean clue, long tick) {
   }

   public record s(
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

   public record w(long position, long chunk, String before, String after, String transition, y9.b source, long tick) {
   }
}
