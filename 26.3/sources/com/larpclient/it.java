package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public final class it {
   private final Map<it.z, Integer> U = E();
   private final Map<it.z, String> T = E();
   private Object s;
   private int o;

   private static <V> Map<it.z, V> E() {
      return new LinkedHashMap<it.z, V>(64, 0.75F, true) {
         @Override
         protected boolean removeEldestEntry(Entry<it.z, V> var1) {
            return this.size() > 512;
         }
      };
   }

   public void x(Object var1, int var2) {
      if (this.s != var1 || this.o != var2) {
         this.U.clear();
         this.T.clear();
         this.s = var1;
         this.o = var2;
      }
   }

   public int y(String var1, int var2, IntSupplier var3) {
      return var1.length() > 256 ? var3.getAsInt() : this.U.computeIfAbsent(new it.z(var1, var2, 0), var1x -> var3.getAsInt());
   }

   public String N(String var1, int var2, int var3, Supplier<String> var4) {
      return var1.length() > 256 ? (String)var4.get() : this.T.computeIfAbsent(new it.z(var1, var2, var3), var1x -> (String)var4.get());
   }

   int c() {
      return this.U.size();
   }

   int K() {
      return this.T.size();
   }

   private record z(String text, int style, int width) {
   }
}
