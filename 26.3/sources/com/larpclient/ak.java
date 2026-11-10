package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public final class ak {
   public static Map<Integer, ak.k> r(Map<Integer, ak.k> var0, Map<Integer, ak.k> var1, boolean var2) {
      HashMap var3 = new HashMap();
      HashMap var4 = new HashMap();
      HashMap var5 = new HashMap();
      var0.values().forEach(var2x -> {
         var3.merge(var2x.item, var2x.purchased, Integer::sum);
         var4.merge(var2x.item, var2x.count, Integer::sum);
      });
      var1.values().forEach(var1x -> var5.merge(var1x.item, var1x.count, Integer::sum));
      var1.values().forEach(var1x -> var3.merge(var1x.item, var1x.purchased, Math::max));
      if (var2) {
         var5.forEach((var2x, var3x) -> var3.merge(var2x, Math.max(0, var3x - var4.getOrDefault(var2x, 0)), Integer::sum));
      }

      HashMap var6 = new HashMap();

      for (Entry var8 : new TreeMap(var1).entrySet()) {
         ak.k var9 = (ak.k)var8.getValue();
         ak.k var10 = (ak.k)var0.get(var8.getKey());
         int var11 = Math.max(var9.purchased, var10 != null && var10.item.equals(var9.item) ? Math.min(var9.count, var10.purchased) : 0);
         int var12 = Math.min(var11, var3.getOrDefault(var9.item, 0));
         var6.put((Integer)var8.getKey(), new ak.k(var9.item, var9.count, var12));
         var3.merge(var9.item, -var12, Integer::sum);
      }

      for (Entry var14 : new TreeMap(var6).entrySet()) {
         ak.k var15 = (ak.k)var14.getValue();
         ak.k var16 = (ak.k)var0.get(var14.getKey());
         int var17 = var15.count - (var16 != null && var16.item.equals(var15.item) ? var16.count : 0);
         int var18 = Math.min(Math.max(0, var17), Math.min(var15.count - var15.purchased, var3.getOrDefault(var15.item, 0)));
         var6.put((Integer)var14.getKey(), new ak.k(var15.item, var15.count, var15.purchased + var18));
         var3.merge(var15.item, -var18, Integer::sum);
      }

      return var6;
   }

   public record k(String item, int count, int purchased) {
      public k(String item, int count, int purchased) {
         if (count >= 0 && purchased >= 0 && purchased <= count) {
            this.item = item;
            this.count = count;
            this.purchased = purchased;
         } else {
            throw new IllegalArgumentException("Invalid provenance quantity");
         }
      }
   }
}
