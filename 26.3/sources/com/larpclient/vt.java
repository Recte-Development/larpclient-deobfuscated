package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public final class vt {
   public static final int l = 2048;
   public static final int x = 2048;
   public static final int o = 64;
   public static final long j = 600L;
   private final LinkedHashMap<Long, vt.j> T = new LinkedHashMap<>();
   private final ArrayDeque<vt.x> E = new ArrayDeque<>();
   private long n = Long.MIN_VALUE;
   private int a;
   private long h;
   private long C;
   private long t;
   private long g;
   private long M;
   private long V;
   private long Z;
   private long R;
   private long q;

   public void H() {
      this.T.clear();
      this.E.clear();
      this.n = Long.MIN_VALUE;
      this.a = 0;
      this.h = this.C = this.t = this.g = this.M = this.V = this.Z = this.R = this.q = 0L;
   }

   public void z() {
      this.h++;
   }

   public boolean n(long var1) {
      if (this.n != var1) {
         this.n = var1;
         this.a = 0;
      }

      return this.a < 2048;
   }

   public void c(vt.d var1, long var2) {
      this.V = this.V + Math.max(0L, var2);
      if (var1 == vt.d.l) {
         this.C = this.C + Math.max(0L, var2);
      }
   }

   private void q(vt.x var1) {
      if (this.E.size() == 64) {
         this.E.removeFirst();
         this.q++;
      }

      this.E.addLast(var1);
   }

   public void G(long var1, long var3, String var5, boolean var6, boolean var7, vt.d var8, long var9) {
      if (var8 == vt.d.l) {
         this.C++;
      }

      if (!this.n(var9)) {
         this.V++;
      } else {
         this.a++;
         vt.j var11 = this.T.get(var1);
         if (var11 != null && var9 - var11.tick() >= 600L) {
            this.T.remove(var1);
            this.R++;
            var11 = null;
         }

         if (var11 != null || var6) {
            if (var6) {
               this.t++;
            }

            if (var11 == null && var6) {
               this.q(new vt.x(var1, var3, "<not previously observed>", var5, "identifying state received", var8, var9));
            }

            if (var11 != null && !var11.identity().equals(var5)) {
               String var12;
               if (var11.clue() && !var6) {
                  if (var7) {
                     this.g++;
                     var12 = "clue to terrain, cause unknown";
                  } else {
                     this.M++;
                     var12 = "clue replaced, cause unknown";
                  }
               } else if (var6) {
                  var12 = var11.clue() ? "identifying state changed" : "identifying state received again";
               } else {
                  var12 = null;
               }

               if (var12 != null) {
                  this.q(new vt.x(var1, var3, var11.identity(), var5, var12, var8, var9));
               }
            }

            if (var11 == null && this.T.size() == 2048) {
               this.T.remove(this.T.keySet().iterator().next());
               this.Z++;
            }

            this.T.put(var1, new vt.j(var3, var5, var6, var9));
         }
      }
   }

   public void W(long var1) {
      this.T.values().removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.R++;
         return true;
      });
      this.E.removeIf(var3 -> {
         if (var1 - var3.tick() < 600L) {
            return false;
         }

         this.q++;
         return true;
      });
   }

   public void O(long var1) {
      this.T.values().removeIf(var2 -> var2.chunk() == var1);
      this.E.removeIf(var2 -> var2.chunk() == var1);
   }

   public List<Long> w(long var1) {
      return this.T.entrySet().stream().filter(var2 -> var2.getValue().chunk() == var1).map(Entry::getKey).toList();
   }

   public List<vt.x> x(int var1) {
      ArrayList var2 = new ArrayList();

      for (int var3 = 0; var3 < Math.max(0, var1) && !this.E.isEmpty(); var3++) {
         var2.add(this.E.removeFirst());
      }

      return List.copyOf(var2);
   }

   public vt.p B() {
      return new vt.p(this.h, this.C, this.t, this.g, this.M, this.V, this.Z, this.R, this.q, this.T.size());
   }

   public enum d {
      R,
      l;
   }

   private record j(long chunk, String identity, boolean clue, long tick) {
   }

   public record p(
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

   public record x(long position, long chunk, String before, String after, String transition, vt.d source, long tick) {
   }
}
