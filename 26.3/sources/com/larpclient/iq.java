package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class iq {
   static Map<Integer, iq.c> X(int[] var0, int[] var1, List<iq.x> var2) {
      return e(var0, var1, var2, Map.of());
   }

   static Map<Integer, iq.c> e(int[] var0, int[] var1, List<iq.x> var2, Map<Integer, iq.c> var3) {
      ArrayList var4 = new ArrayList();
      int[] var5 = new int[var0.length];

      for (iq.x var7 : var2) {
         if (var7.source >= 0 && var7.source < var0.length && var0[var7.source] == var7.real && var7.count > 0) {
            int var8 = Math.min(var7.count, var1[var7.source] - var5[var7.source]);
            if (var8 > 0) {
               var5[var7.source] = var5[var7.source] + var8;
               var4.add(new iq.x(var7.source, var7.real, var7.shown, var8));
            }
         }
      }

      ArrayList var20 = new ArrayList();

      for (int var21 = 0; var21 < var0.length; var21++) {
         if (var0[var21] >= 0 && var1[var21] > var5[var21]) {
            var20.add(new iq.x(var21, var0[var21], var0[var21], var1[var21] - var5[var21]));
         }
      }

      var20.addAll(var4);
      LinkedHashMap var22 = new LinkedHashMap();

      for (iq.x var9 : var20) {
         List var10 = var22.computeIfAbsent(var9.shown, var0x -> new ArrayList());
         int var11 = -1;

         for (int var12 = 0; var12 < var10.size(); var12++) {
            if (((iq.x)var10.get(var12)).source == var9.source && ((iq.x)var10.get(var12)).real == var9.real) {
               var11 = var12;
               break;
            }
         }

         if (var11 < 0) {
            var10.add(var9);
         } else {
            iq.x var29 = (iq.x)var10.get(var11);
            var10.set(var11, new iq.x(var9.source, var9.real, var9.shown, var29.count + var9.count));
         }
      }

      LinkedHashMap var24 = new LinkedHashMap();
      LinkedHashSet var25 = new LinkedHashSet();

      for (int var26 = 0; var26 < var0.length; var26++) {
         if (var1[var26] == 0 || var0[var26] >= 0) {
            var25.add(var26);
         }
      }

      LinkedHashMap var27 = new LinkedHashMap();
      HashMap var28 = new HashMap();
      var22.forEach((var1x, var2x) -> var28.put(var1x, (var2x.stream().mapToInt(iq.x::count).sum() + 63) / 64));
      var3.forEach((var3x, var4x) -> {
         if (var4x.count > 0 && var25.contains(var3x) && var28.getOrDefault(var4x.shown, 0) > 0) {
            var27.put(var3x, var4x.shown);
            var28.merge(var4x.shown, -1, Integer::sum);
         }
      });

      for (Entry var13 : var22.entrySet()) {
         for (iq.x var15 : (List)var13.getValue()) {
            if ((Integer)var28.get(var13.getKey()) > 0) {
               int var16 = var15.source;
               if (!var3.isEmpty()) {
                  for (int var18 : var25) {
                     if (var18 < var15.source && (var18 < 9 ? 1 : 0) == (var15.source < 9 ? 1 : 0) && !var27.containsKey(var18)) {
                        iq.c var19 = (iq.c)var3.get(var18);
                        if (var19 == null || var19.count == 0) {
                           var16 = var18;
                           break;
                        }
                     }
                  }
               }

               if (var25.contains(var16) && !var27.containsKey(var16)) {
                  var27.put(var16, (Integer)var13.getKey());
                  var28.merge((Integer)var13.getKey(), -1, Integer::sum);
               }
            }
         }
      }

      for (Entry var33 : var22.entrySet()) {
         ArrayDeque var34 = new ArrayDeque((Collection)var33.getValue());

         while (!var34.isEmpty()) {
            int var35 = -1;

            for (Entry var39 : var27.entrySet()) {
               if (((Integer)var39.getValue()).equals(var33.getKey()) && var25.contains(var39.getKey())) {
                  var35 = (Integer)var39.getKey();
                  break;
               }
            }

            if (var35 < 0) {
               for (int var37 = 0; var37 < 2 && var35 < 0; var37++) {
                  for (int var42 : var25) {
                     if (!var27.containsKey(var42)
                        && (var37 != 0 || (var42 < 9 ? 1 : 0) == (((iq.x)var34.getFirst()).source < 9 ? 1 : 0))
                        && (var37 != 1 || var42 >= 9)) {
                        var35 = var42;
                        break;
                     }
                  }
               }
            }

            if (var35 < 0) {
               return Map.of();
            }

            var25.remove(var35);
            ArrayList var38 = new ArrayList();
            int var41 = 0;

            while (!var34.isEmpty() && var41 < 64) {
               iq.x var43 = (iq.x)var34.removeFirst();
               int var44 = Math.min(64 - var41, var43.count);
               var38.add(new iq.x(var43.source, var43.real, var43.shown, var44));
               var41 += var44;
               if (var44 < var43.count) {
                  var34.addFirst(new iq.x(var43.source, var43.real, var43.shown, var43.count - var44));
               }
            }

            var24.put(var35, new iq.c((Integer)var33.getKey(), var41, List.copyOf(var38)));
         }
      }

      for (int var32 = 0; var32 < var0.length; var32++) {
         if (var0[var32] >= 0 && var1[var32] > 0) {
            var24.putIfAbsent(var32, new iq.c(-1, 0, List.of()));
         }
      }

      return var24;
   }

   record c(int shown, int count, List<iq.x> portions) {
   }

   record x(int source, int real, int shown, int count) {
   }
}
