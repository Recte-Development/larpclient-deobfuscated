package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class ir {
   private final Map<ir.r, Integer> a = p();
   private final Map<ir.r, String> s = p();
   private Object Q;
   private int b;

   private static <V> Map<ir.r, V> p() {
      return new LinkedHashMap<ir.r, V>(64, 0.75F, true) {
         @Override
         protected boolean removeEldestEntry(Entry<ir.r, V> var1) {
            return this.size() > 512;
         }
      };
   }

   public void t(Object var1, int var2) {
      if (this.Q != var1 || this.b != var2) {
         this.a.clear();
         this.s.clear();
         this.Q = var1;
         this.b = var2;
      }
   }

   public int C(String var1, int var2, IntSupplier var3) {
      return var1.length() > 256 ? var3.getAsInt() : this.a.computeIfAbsent(new ir.r(var1, var2, 0), var1x -> var3.getAsInt());
   }

   public String x(String var1, int var2, int var3, Supplier<String> var4) {
      return var1.length() > 256 ? (String)var4.get() : this.s.computeIfAbsent(new ir.r(var1, var2, var3), var1x -> (String)var4.get());
   }

   int J() {
      return this.a.size();
   }

   int a() {
      return this.s.size();
   }

   private record r(String text, int style, int width) {
   }
}
