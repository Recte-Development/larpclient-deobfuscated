package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;

public final class bv {
   private final Map<Integer, Long> P = new HashMap<>();
   private BlockPos d;
   private long u;
   private long T;
   private long F;

   public void W(long var1, IntPredicate var3, Predicate<BlockPos> var4) {
      this.P.entrySet().removeIf(var3x -> var1 >= var3x.getValue() || !var3.test(var3x.getKey()));
      if (this.d != null && (var1 >= this.u || var4.test(this.d))) {
         this.d = null;
      }
   }

   public boolean R(long var1, int var3) {
      return var1 >= this.T && !this.P.containsKey(var3);
   }

   public boolean P(long var1) {
      return var1 >= this.F && this.d == null && this.P.isEmpty();
   }

   public boolean u() {
      return !this.P.isEmpty();
   }

   public boolean n() {
      return this.d != null;
   }

   public void R(long var1, int var3, int var4) {
      this.s(var1, var3, var4, 10);
   }

   public void X(long var1, BlockPos var3, int var4, boolean var5) {
      this.b(var1, var3, var4, var5, 10);
   }

   public void s(long var1, int var3, int var4, int var5) {
      this.T = var1 + Math.max(1, var4);
      this.P.put(var3, var1 + Math.clamp(var5, 2, 40));
   }

   public void b(long var1, BlockPos var3, int var4, boolean var5, int var6) {
      this.F = var1 + Math.max(1, var4);
      if (var5) {
         this.d = var3.immutable();
         this.u = var1 + Math.clamp(var6, 2, 40);
      }
   }

   public void i(long var1, int var3) {
      this.F = var1 + Math.max(1, var3);
   }

   public void W() {
      this.P.clear();
      this.d = null;
      this.T = this.F = this.u = 0L;
   }
}
