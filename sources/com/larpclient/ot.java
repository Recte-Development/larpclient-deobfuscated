package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class ot {
   private final Map<ot.l, Integer> E = U();
   private final Map<ot.l, String> L = U();
   private Object u;
   private int f;

   private static <V> Map<ot.l, V> U() {
      return new LinkedHashMap<ot.l, V>(64, 0.75F, true) {
         @Override
         protected boolean removeEldestEntry(Entry<ot.l, V> var1) {
            return this.size() > 512;
         }
      };
   }

   public void e(Object var1, int var2) {
      if (this.u != var1 || this.f != var2) {
         this.E.clear();
         this.L.clear();
         this.u = var1;
         this.f = var2;
      }
   }

   public int b(String var1, int var2, IntSupplier var3) {
      return var1.length() > 256 ? var3.getAsInt() : this.E.computeIfAbsent(new ot.l(var1, var2, 0), var1x -> var3.getAsInt());
   }

   public String w(String var1, int var2, int var3, Supplier<String> var4) {
      return var1.length() > 256 ? (String)var4.get() : this.L.computeIfAbsent(new ot.l(var1, var2, var3), var1x -> (String)var4.get());
   }

   int C() {
      return this.E.size();
   }

   int F() {
      return this.L.size();
   }

   private record l(String text, int style, int width) {
   }
}
