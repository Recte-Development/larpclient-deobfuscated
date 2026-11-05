package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class lm {
   final List<lm.n> p = new ArrayList<>();
   private Map<lm.y, lm.q> P = Map.of();
   private final Map<lm.y, Integer> U = new LinkedHashMap<>();
   private final Map<lm.y, Integer> o = new HashMap<>();
   private int L;
   private final Map<lm.y, Integer> A = new HashMap<>();

   void A(lm.y var1, int var2) {
      this.A.put(var1, var2);
   }

   void O(String var1, int var2, int var3, lm.y var4, Map<lm.y, lm.q> var5) {
      this.E(var5);
      this.p.add(new lm.n(var1, var2, var3, var4));
      this.U.clear();
      this.o.clear();
   }

   void E(Map<lm.y, lm.q> var1) {
      if (!this.P.equals(var1)) {
         this.L++;
         this.U.keySet().removeIf(var2x -> this.L - this.o.getOrDefault(var2x, 0) > 4 || !var1.containsKey(var2x) || ((lm.q)var1.get(var2x)).count == 0);
         LinkedHashSet var2 = new LinkedHashSet();

         for (Entry var4 : var1.entrySet()) {
            lm.q var5 = this.P.get(var4.getKey());
            lm.q var6 = (lm.q)var4.getValue();
            int var7 = var6.count - (var5 != null && var5.map == var6.map ? var5.count : 0);
            if (var5 != null && var5.map != var6.map) {
               this.U.remove(var4.getKey());
            }

            if (var7 > 0 && (var5 != null || !((lm.y)var4.getKey()).kind.equals("stock"))) {
               var2.add((lm.y)var4.getKey());
               this.U.merge((lm.y)var4.getKey(), var7, Integer::sum);
               this.o.put((lm.y)var4.getKey(), this.L);
            }
         }

         for (Entry var16 : this.P.entrySet()) {
            lm.y var18 = (lm.y)var16.getKey();
            lm.q var20 = (lm.q)var16.getValue();
            lm.q var22 = (lm.q)var1.get(var18);
            if (var22 != null || var18.kind.equals("inventory") || var18.kind.equals("cursor")) {
               int var8 = var20.count - (var22 != null && var20.map == var22.map ? var22.count : 0);
               if (var8 > 0) {
                  this.U.computeIfPresent(var18, (var2x, var3) -> Math.min(var3, var22 != null && var20.map == var22.map ? var22.count : 0));
               }

               if (var8 > 0) {
                  Integer var9 = this.A.remove(var18);
                  List var10 = this.p.stream().filter(var2x -> var18.equals(var2x.T) && var2x.w == var20.map).toList();
                  if (var9 != null && var9 == var20.map) {
                     var8 -= Math.min(var8, Math.max(0, var20.count - var10.size()));
                  }

                  ArrayList var11 = new ArrayList(var10);
                  if (var9 != null) {
                     var11.sort(Comparator.comparingInt(var1x -> var1x.a == var9 ? 0 : 1));
                  }

                  for (lm.n var13 : var11) {
                     if (var8 <= 0) {
                        break;
                     }

                     var13.T = null;
                     var8--;
                  }
               }
            }
         }

         var2.addAll(this.U.keySet());

         for (lm.n var17 : this.p) {
            if (var17.T == null) {
               for (lm.y var21 : var2) {
                  if (this.U.getOrDefault(var21, 0) > 0 && ((lm.q)var1.get(var21)).map == var17.w) {
                     var17.T = var21;
                     this.U.merge(var21, -1, Integer::sum);
                     break;
                  }
               }
            }
         }

         this.P = new LinkedHashMap<>(var1);
      }
   }

   int g(String var1, int var2) {
      int var3 = 0;

      for (lm.n var5 : this.p) {
         if (var5.D.equals(var1) && !var5.B()) {
            if (var5.w == var2) {
               var3++;
            }

            if (var5.a == var2) {
               var3--;
            }
         }
      }

      return var3;
   }

   lm.n u(lm.y var1, int var2) {
      for (lm.n var4 : this.p) {
         if (var1.equals(var4.T) && var4.w == var2 && !var4.B()) {
            return var4;
         }
      }

      return null;
   }

   void w() {
      this.p.removeIf(lm.n::B);
   }

   void k(lm.y var1) {
      this.p.removeIf(var1x -> var1x.B() && var1.equals(var1x.T));
   }

   void O() {
      this.p.clear();
      this.P = Map.of();
      this.U.clear();
      this.o.clear();
      this.L = 0;
      this.A.clear();
   }

   static final class n {
      final String D;
      final int w;
      final int a;
      lm.y T;

      n(String var1, int var2, int var3, lm.y var4) {
         this.D = var1;
         this.w = var2;
         this.a = var3;
         this.T = var4;
      }

      boolean B() {
         return this.T != null && this.T.kind.equals("stock") && this.T.key.equals(this.D);
      }
   }

   record q(int map, int count) {
   }

   record y(String kind, String key, int slot) {
   }
}
