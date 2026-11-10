package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;

public final class cv {
   private final Map<Integer, Long> g = new HashMap<>();
   private BlockPos t;
   private long B;
   private long J;
   private long R;

   public void E(long var1, IntPredicate var3, Predicate<BlockPos> var4) {
      this.g.entrySet().removeIf(var3x -> var1 >= var3x.getValue() || !var3.test(var3x.getKey()));
      if (this.t != null && (var1 >= this.B || var4.test(this.t))) {
         this.t = null;
      }
   }

   public boolean I(long var1, int var3) {
      return var1 >= this.J && !this.g.containsKey(var3);
   }

   public boolean R(long var1) {
      return var1 >= this.R && this.t == null && this.g.isEmpty();
   }

   public boolean v() {
      return !this.g.isEmpty();
   }

   public boolean o() {
      return this.t != null;
   }

   public void L(long var1, int var3, int var4) {
      this.f(var1, var3, var4, 10);
   }

   public void k(long var1, BlockPos var3, int var4, boolean var5) {
      this.w(var1, var3, var4, var5, 10);
   }

   public void f(long var1, int var3, int var4, int var5) {
      this.J = var1 + Math.max(1, var4);
      this.g.put(var3, var1 + Math.clamp(var5, 2, 40));
   }

   public void w(long var1, BlockPos var3, int var4, boolean var5, int var6) {
      this.R = var1 + Math.max(1, var4);
      if (var5) {
         this.t = var3.immutable();
         this.B = var1 + Math.clamp(var6, 2, 40);
      }
   }

   public void N(long var1, int var3) {
      this.R = var1 + Math.max(1, var3);
   }

   public void a() {
      this.g.clear();
      this.t = null;
      this.J = this.R = this.B = 0L;
   }
}
