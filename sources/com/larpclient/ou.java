package com.larpclient;

import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class ou {
   public static final int N = 64;
   public static final double r = 3.0;
   private final IdentityHashMap<Object, Double> I = new IdentityHashMap<>();
   private final IdentityHashMap<Object, Double> s = new IdentityHashMap<>();

   public void f(Object var1, boolean var2, double var3) {
      this.P(var3, var0 -> true);
      if (!var2) {
         this.I.remove(var1);
      } else {
         if (this.I.size() >= 64 && !this.I.containsKey(var1)) {
            I(this.I);
         }

         this.I.put(var1, var3);
      }
   }

   public boolean E(Object var1, double var2) {
      this.P(var2, var0 -> true);
      Double var4 = this.I.remove(var1);
      if (this.s.containsKey(var1)) {
         return false;
      }

      if (this.s.size() >= 64) {
         I(this.s);
      }

      this.s.put(var1, var2);
      return var4 != null && var2 >= var4 && var2 - var4 <= 3.0;
   }

   public void L(Object var1) {
      this.I.remove(var1);
   }

   public void P(double var1, Predicate<Object> var3) {
      this.I.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 3.0 || !var3.test(var3x.getKey()));
      this.s.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 12.0 || !var3.test(var3x.getKey()));
   }

   public int X() {
      return this.I.size();
   }

   public int q() {
      return this.s.size();
   }

   public void k() {
      this.I.clear();
      this.s.clear();
   }

   private static void I(IdentityHashMap<Object, Double> var0) {
      Object var1 = null;
      double var2 = Double.POSITIVE_INFINITY;

      for (Entry var5 : var0.entrySet()) {
         if ((Double)var5.getValue() < var2) {
            var1 = var5.getKey();
            var2 = (Double)var5.getValue();
         }
      }

      var0.remove(var1);
   }
}
