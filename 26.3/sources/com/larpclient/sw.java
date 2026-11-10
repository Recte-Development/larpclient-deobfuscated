package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class sw {
   public static final int V = 8;
   public static final int g = 40;
   private final List<sw.c> a = new ArrayList<>(8);

   public void C(BlockPos var1, long var2, int var4) {
      this.a.removeIf(var1x -> var1x.p.equals(var1));
      if (this.a.size() == 8) {
         this.a.remove(0);
      }

      this.a.add(new sw.c(var1.immutable(), var2, Math.max(0, var4)));
   }

   public List<BlockPos> z() {
      ArrayList var1 = new ArrayList(this.a.size());

      for (sw.c var3 : this.a) {
         var1.add(var3.p);
      }

      return var1;
   }

   public sw.k f(BlockPos var1, boolean var2, long var3, int var5) {
      sw.c var6 = this.U(var1);
      if (var6 == null) {
         return sw.k.k;
      }

      if (!var2) {
         if (var6.A < 0L && var3 - var6.n <= 40L) {
            return sw.k.z;
         }

         this.a.remove(var6);
         return sw.k.k;
      } else {
         if (var6.A < 0L) {
            var6.A = Math.max(var3 + var6.W, var6.n + 1L);
         }

         if (var3 >= var6.A + Math.max(1, var5)) {
            this.a.remove(var6);
            return sw.k.k;
         } else {
            return var3 >= var6.A ? sw.k.R : sw.k.z;
         }
      }
   }

   public long Z(BlockPos var1, long var2) {
      sw.c var4 = this.U(var1);
      return var4 == null ? -1L : var2 - var4.n;
   }

   public void e(BlockPos var1) {
      this.a.removeIf(var1x -> var1x.p.equals(var1));
   }

   public boolean z() {
      return this.a.isEmpty();
   }

   public void i() {
      this.a.clear();
   }

   private sw.c U(BlockPos var1) {
      for (sw.c var3 : this.a) {
         if (var3.p.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private static final class c {
      final BlockPos p;
      final long n;
      final int W;
      long A = -1L;

      c(BlockPos var1, long var2, int var4) {
         this.p = var1;
         this.n = var2;
         this.W = var4;
      }
   }

   public enum k {
      z,
      R,
      k;
   }
}
