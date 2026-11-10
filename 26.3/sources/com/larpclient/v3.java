package com.larpclient;

import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class v3 {
   public static final int X = 64;
   public static final double d = 3.0;
   private final IdentityHashMap<Object, Double> z = new IdentityHashMap<>();
   private final IdentityHashMap<Object, Double> t = new IdentityHashMap<>();

   public void t(Object var1, boolean var2, double var3) {
      this.q(var3, var0 -> true);
      if (!var2) {
         this.z.remove(var1);
      } else {
         if (this.z.size() >= 64 && !this.z.containsKey(var1)) {
            Y(this.z);
         }

         this.z.put(var1, var3);
      }
   }

   public boolean I(Object var1, double var2) {
      this.q(var2, var0 -> true);
      Double var4 = this.z.remove(var1);
      if (this.t.containsKey(var1)) {
         return false;
      }

      if (this.t.size() >= 64) {
         Y(this.t);
      }

      this.t.put(var1, var2);
      return var4 != null && var2 >= var4 && var2 - var4 <= 3.0;
   }

   public void L(Object var1) {
      this.z.remove(var1);
   }

   public void q(double var1, Predicate<Object> var3) {
      this.z.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 3.0 || !var3.test(var3x.getKey()));
      this.t.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 12.0 || !var3.test(var3x.getKey()));
   }

   public int y() {
      return this.z.size();
   }

   public int p() {
      return this.t.size();
   }

   public void F() {
      this.z.clear();
      this.t.clear();
   }

   private static void Y(IdentityHashMap<Object, Double> var0) {
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
