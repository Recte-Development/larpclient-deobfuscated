package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;

public final class k0 {
   private final Map<Integer, Long> d = new HashMap<>();
   private BlockPos a;
   private long g;
   private long I;
   private long n;

   public void I(long var1, IntPredicate var3, Predicate<BlockPos> var4) {
      this.d.entrySet().removeIf(var3x -> var1 >= var3x.getValue() || !var3.test(var3x.getKey()));
      if (this.a != null && (var1 >= this.g || var4.test(this.a))) {
         this.a = null;
      }
   }

   public boolean G(long var1, int var3) {
      return var1 >= this.I && !this.d.containsKey(var3);
   }

   public boolean g(long var1) {
      return var1 >= this.n && this.a == null && this.d.isEmpty();
   }

   public boolean E() {
      return !this.d.isEmpty();
   }

   public boolean d() {
      return this.a != null;
   }

   public void G(long var1, int var3, int var4) {
      this.h(var1, var3, var4, 10);
   }

   public void a(long var1, BlockPos var3, int var4, boolean var5) {
      this.c(var1, var3, var4, var5, 10);
   }

   public void h(long var1, int var3, int var4, int var5) {
      this.I = var1 + Math.max(1, var4);
      this.d.put(var3, var1 + Math.clamp(var5, 2, 40));
   }

   public void c(long var1, BlockPos var3, int var4, boolean var5, int var6) {
      this.n = var1 + Math.max(1, var4);
      if (var5) {
         this.a = var3.immutable();
         this.g = var1 + Math.clamp(var6, 2, 40);
      }
   }

   public void i(long var1, int var3) {
      this.n = var1 + Math.max(1, var3);
   }

   public void Z() {
      this.d.clear();
      this.a = null;
      this.I = this.n = this.g = 0L;
   }
}
