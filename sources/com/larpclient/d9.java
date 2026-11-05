package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;

public final class d9 {
   private final Map<Integer, Long> C = new HashMap<>();
   private BlockPos i;
   private long r;
   private long Q;
   private long P;

   public void H(long var1, IntPredicate var3, Predicate<BlockPos> var4) {
      this.C.entrySet().removeIf(var3x -> var1 >= var3x.getValue() || !var3.test(var3x.getKey()));
      if (this.i != null && (var1 >= this.r || var4.test(this.i))) {
         this.i = null;
      }
   }

   public boolean V(long var1, int var3) {
      return var1 >= this.Q && !this.C.containsKey(var3);
   }

   public boolean m(long var1) {
      return var1 >= this.P && this.i == null && this.C.isEmpty();
   }

   public boolean l() {
      return !this.C.isEmpty();
   }

   public boolean c() {
      return this.i != null;
   }

   public void L(long var1, int var3, int var4) {
      this.m(var1, var3, var4, 10);
   }

   public void C(long var1, BlockPos var3, int var4, boolean var5) {
      this.R(var1, var3, var4, var5, 10);
   }

   public void m(long var1, int var3, int var4, int var5) {
      this.Q = var1 + Math.max(1, var4);
      this.C.put(var3, var1 + Math.clamp(var5, 2, 40));
   }

   public void R(long var1, BlockPos var3, int var4, boolean var5, int var6) {
      this.P = var1 + Math.max(1, var4);
      if (var5) {
         this.i = var3.immutable();
         this.r = var1 + Math.clamp(var6, 2, 40);
      }
   }

   public void f(long var1, int var3) {
      this.P = var1 + Math.max(1, var3);
   }

   public void J() {
      this.C.clear();
      this.i = null;
      this.Q = this.P = this.r = 0L;
   }
}
