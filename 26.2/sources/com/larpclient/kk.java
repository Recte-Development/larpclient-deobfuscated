package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class kk {
   private final Map<kk.d, Integer> R = T();
   private final Map<kk.d, String> V = T();
   private Object X;
   private int m;

   private static <V> Map<kk.d, V> T() {
      return new LinkedHashMap<kk.d, V>(64, 0.75F, true) {
         @Override
         protected boolean removeEldestEntry(Entry<kk.d, V> var1) {
            return this.size() > 512;
         }
      };
   }

   public void j(Object var1, int var2) {
      if (this.X != var1 || this.m != var2) {
         this.R.clear();
         this.V.clear();
         this.X = var1;
         this.m = var2;
      }
   }

   public int R(String var1, int var2, IntSupplier var3) {
      return var1.length() > 256 ? var3.getAsInt() : this.R.computeIfAbsent(new kk.d(var1, var2, 0), var1x -> var3.getAsInt());
   }

   public String l(String var1, int var2, int var3, Supplier<String> var4) {
      return var1.length() > 256 ? (String)var4.get() : this.V.computeIfAbsent(new kk.d(var1, var2, var3), var1x -> (String)var4.get());
   }

   int E() {
      return this.R.size();
   }

   int S() {
      return this.V.size();
   }

   private record d(String text, int style, int width) {
   }
}
