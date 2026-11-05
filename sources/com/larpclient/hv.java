package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class hv {
   private final Map<hv.v, Integer> t = H();
   private final Map<hv.v, String> Q = H();
   private Object T;
   private int F;

   private static <V> Map<hv.v, V> H() {
      return new LinkedHashMap<hv.v, V>(64, 0.75F, true) {
         @Override
         protected boolean removeEldestEntry(Entry<hv.v, V> var1) {
            return this.size() > 512;
         }
      };
   }

   public void k(Object var1, int var2) {
      if (this.T != var1 || this.F != var2) {
         this.t.clear();
         this.Q.clear();
         this.T = var1;
         this.F = var2;
      }
   }

   public int i(String var1, int var2, IntSupplier var3) {
      return var1.length() > 256 ? var3.getAsInt() : this.t.computeIfAbsent(new hv.v(var1, var2, 0), var1x -> var3.getAsInt());
   }

   public String I(String var1, int var2, int var3, Supplier<String> var4) {
      return var1.length() > 256 ? (String)var4.get() : this.Q.computeIfAbsent(new hv.v(var1, var2, var3), var1x -> (String)var4.get());
   }

   int h() {
      return this.t.size();
   }

   int H() {
      return this.Q.size();
   }

   private record v(String text, int style, int width) {
   }
}
