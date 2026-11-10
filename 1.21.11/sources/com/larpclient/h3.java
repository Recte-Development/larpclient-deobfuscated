package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import net.minecraft.class_2338;

public final class h3 {
   private final Map<Integer, Long> t = new HashMap<>();
   private class_2338 j;
   private long H;
   private long g;
   private long m;

   public void q(long var1, IntPredicate var3, Predicate<class_2338> var4) {
      this.t.entrySet().removeIf(var3x -> var1 >= var3x.getValue() || !var3.test(var3x.getKey()));
      if (this.j != null && (var1 >= this.H || var4.test(this.j))) {
         this.j = null;
      }
   }

   public boolean T(long var1, int var3) {
      return var1 >= this.g && !this.t.containsKey(var3);
   }

   public boolean f(long var1) {
      return var1 >= this.m && this.j == null && this.t.isEmpty();
   }

   public boolean z() {
      return !this.t.isEmpty();
   }

   public boolean q() {
      return this.j != null;
   }

   public void J(long var1, int var3, int var4) {
      this.P(var1, var3, var4, 10);
   }

   public void g(long var1, class_2338 var3, int var4, boolean var5) {
      this.J(var1, var3, var4, var5, 10);
   }

   public void P(long var1, int var3, int var4, int var5) {
      this.g = var1 + Math.max(1, var4);
      this.t.put(var3, var1 + Math.clamp(var5, 2, 40));
   }

   public void J(long var1, class_2338 var3, int var4, boolean var5, int var6) {
      this.m = var1 + Math.max(1, var4);
      if (var5) {
         this.j = var3.method_10062();
         this.H = var1 + Math.clamp(var6, 2, 40);
      }
   }

   public void B(long var1, int var3) {
      this.m = var1 + Math.max(1, var3);
   }

   public void N() {
      this.t.clear();
      this.j = null;
      this.g = this.m = this.H = 0L;
   }
}
