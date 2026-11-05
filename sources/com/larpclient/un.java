package com.larpclient;

import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class un {
   public static final int h = 64;
   public static final double Y = 3.0;
   private final IdentityHashMap<Object, Double> m = new IdentityHashMap<>();
   private final IdentityHashMap<Object, Double> H = new IdentityHashMap<>();

   public void c(Object var1, boolean var2, double var3) {
      this.W(var3, var0 -> true);
      if (!var2) {
         this.m.remove(var1);
      } else {
         if (this.m.size() >= 64 && !this.m.containsKey(var1)) {
            k(this.m);
         }

         this.m.put(var1, var3);
      }
   }

   public boolean P(Object var1, double var2) {
      this.W(var2, var0 -> true);
      Double var4 = this.m.remove(var1);
      if (this.H.containsKey(var1)) {
         return false;
      }

      if (this.H.size() >= 64) {
         k(this.H);
      }

      this.H.put(var1, var2);
      return var4 != null && var2 >= var4 && var2 - var4 <= 3.0;
   }

   public void w(Object var1) {
      this.m.remove(var1);
   }

   public void W(double var1, Predicate<Object> var3) {
      this.m.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 3.0 || !var3.test(var3x.getKey()));
      this.H.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 12.0 || !var3.test(var3x.getKey()));
   }

   public int k() {
      return this.m.size();
   }

   public int B() {
      return this.H.size();
   }

   public void Q() {
      this.m.clear();
      this.H.clear();
   }

   private static void k(IdentityHashMap<Object, Double> var0) {
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
