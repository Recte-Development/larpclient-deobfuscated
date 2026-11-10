package com.larpclient;

import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class r {
   public static final int e = 64;
   public static final double t = 3.0;
   private final IdentityHashMap<Object, Double> D = new IdentityHashMap<>();
   private final IdentityHashMap<Object, Double> C = new IdentityHashMap<>();

   public void u(Object var1, boolean var2, double var3) {
      this.I(var3, var0 -> true);
      if (!var2) {
         this.D.remove(var1);
      } else {
         if (this.D.size() >= 64 && !this.D.containsKey(var1)) {
            y(this.D);
         }

         this.D.put(var1, var3);
      }
   }

   public boolean M(Object var1, double var2) {
      this.I(var2, var0 -> true);
      Double var4 = this.D.remove(var1);
      if (this.C.containsKey(var1)) {
         return false;
      }

      if (this.C.size() >= 64) {
         y(this.C);
      }

      this.C.put(var1, var2);
      return var4 != null && var2 >= var4 && var2 - var4 <= 3.0;
   }

   public void y(Object var1) {
      this.D.remove(var1);
   }

   public void I(double var1, Predicate<Object> var3) {
      this.D.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 3.0 || !var3.test(var3x.getKey()));
      this.C.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 12.0 || !var3.test(var3x.getKey()));
   }

   public int U() {
      return this.D.size();
   }

   public int F() {
      return this.C.size();
   }

   public void E() {
      this.D.clear();
      this.C.clear();
   }

   private static void y(IdentityHashMap<Object, Double> var0) {
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
