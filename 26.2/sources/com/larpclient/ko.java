package com.larpclient;

import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class ko {
   public static final int Y = 64;
   public static final double V = 3.0;
   private final IdentityHashMap<Object, Double> G = new IdentityHashMap<>();
   private final IdentityHashMap<Object, Double> K = new IdentityHashMap<>();

   public void q(Object var1, boolean var2, double var3) {
      this.P(var3, var0 -> true);
      if (!var2) {
         this.G.remove(var1);
      } else {
         if (this.G.size() >= 64 && !this.G.containsKey(var1)) {
            C(this.G);
         }

         this.G.put(var1, var3);
      }
   }

   public boolean T(Object var1, double var2) {
      this.P(var2, var0 -> true);
      Double var4 = this.G.remove(var1);
      if (this.K.containsKey(var1)) {
         return false;
      }

      if (this.K.size() >= 64) {
         C(this.K);
      }

      this.K.put(var1, var2);
      return var4 != null && var2 >= var4 && var2 - var4 <= 3.0;
   }

   public void u(Object var1) {
      this.G.remove(var1);
   }

   public void P(double var1, Predicate<Object> var3) {
      this.G.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 3.0 || !var3.test(var3x.getKey()));
      this.K.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 12.0 || !var3.test(var3x.getKey()));
   }

   public int x() {
      return this.G.size();
   }

   public int k() {
      return this.K.size();
   }

   public void R() {
      this.G.clear();
      this.K.clear();
   }

   private static void C(IdentityHashMap<Object, Double> var0) {
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
