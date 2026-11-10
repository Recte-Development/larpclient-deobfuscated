package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class zr {
   public static final int U = 8;
   public static final int S = 40;
   private final List<zr.i> h = new ArrayList<>(8);

   public void K(BlockPos var1, long var2, int var4) {
      this.h.removeIf(var1x -> var1x.T.equals(var1));
      if (this.h.size() == 8) {
         this.h.remove(0);
      }

      this.h.add(new zr.i(var1.immutable(), var2, Math.max(0, var4)));
   }

   public List<BlockPos> D() {
      ArrayList var1 = new ArrayList(this.h.size());

      for (zr.i var3 : this.h) {
         var1.add(var3.T);
      }

      return var1;
   }

   public zr.u h(BlockPos var1, boolean var2, long var3, int var5) {
      zr.i var6 = this.b(var1);
      if (var6 == null) {
         return zr.u.J;
      }

      if (!var2) {
         if (var6.i < 0L && var3 - var6.f <= 40L) {
            return zr.u.t;
         }

         this.h.remove(var6);
         return zr.u.J;
      } else {
         if (var6.i < 0L) {
            var6.i = Math.max(var3 + var6.c, var6.f + 1L);
         }

         if (var3 >= var6.i + Math.max(1, var5)) {
            this.h.remove(var6);
            return zr.u.J;
         } else {
            return var3 >= var6.i ? zr.u.g : zr.u.t;
         }
      }
   }

   public long g(BlockPos var1, long var2) {
      zr.i var4 = this.b(var1);
      return var4 == null ? -1L : var2 - var4.f;
   }

   public void c(BlockPos var1) {
      this.h.removeIf(var1x -> var1x.T.equals(var1));
   }

   public boolean v() {
      return this.h.isEmpty();
   }

   public void Z() {
      this.h.clear();
   }

   private zr.i b(BlockPos var1) {
      for (zr.i var3 : this.h) {
         if (var3.T.equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   private static final class i {
      final BlockPos T;
      final long f;
      final int c;
      long i = -1L;

      i(BlockPos var1, long var2, int var4) {
         this.T = var1;
         this.f = var2;
         this.c = var4;
      }
   }

   public enum u {
      t,
      g,
      J;
   }
}
