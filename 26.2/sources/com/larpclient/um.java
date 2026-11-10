package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class um {
   final List<um.o> O = new ArrayList<>();
   private Map<um.l, um.f> j = Map.of();
   private final Map<um.l, Integer> M = new LinkedHashMap<>();
   private final Map<um.l, Integer> X = new HashMap<>();
   private int h;
   private final Map<um.l, Integer> x = new HashMap<>();

   void H(um.l var1, int var2) {
      this.x.put(var1, var2);
   }

   void G(String var1, int var2, int var3, um.l var4, Map<um.l, um.f> var5) {
      this.y(var5);
      this.O.add(new um.o(var1, var2, var3, var4));
      this.M.clear();
      this.X.clear();
   }

   void y(Map<um.l, um.f> var1) {
      if (!this.j.equals(var1)) {
         this.h++;
         this.M.keySet().removeIf(var2x -> this.h - this.X.getOrDefault(var2x, 0) > 4 || !var1.containsKey(var2x) || ((um.f)var1.get(var2x)).count == 0);
         LinkedHashSet var2 = new LinkedHashSet();

         for (Entry var4 : var1.entrySet()) {
            um.f var5 = this.j.get(var4.getKey());
            um.f var6 = (um.f)var4.getValue();
            int var7 = var6.count - (var5 != null && var5.map == var6.map ? var5.count : 0);
            if (var5 != null && var5.map != var6.map) {
               this.M.remove(var4.getKey());
            }

            if (var7 > 0 && (var5 != null || !((um.l)var4.getKey()).kind.equals("stock"))) {
               var2.add((um.l)var4.getKey());
               this.M.merge((um.l)var4.getKey(), var7, Integer::sum);
               this.X.put((um.l)var4.getKey(), this.h);
            }
         }

         for (Entry var16 : this.j.entrySet()) {
            um.l var18 = (um.l)var16.getKey();
            um.f var20 = (um.f)var16.getValue();
            um.f var22 = (um.f)var1.get(var18);
            if (var22 != null || var18.kind.equals("inventory") || var18.kind.equals("cursor")) {
               int var8 = var20.count - (var22 != null && var20.map == var22.map ? var22.count : 0);
               if (var8 > 0) {
                  this.M.computeIfPresent(var18, (var2x, var3) -> Math.min(var3, var22 != null && var20.map == var22.map ? var22.count : 0));
               }

               if (var8 > 0) {
                  Integer var9 = this.x.remove(var18);
                  List var10 = this.O.stream().filter(var2x -> var18.equals(var2x.T) && var2x.j == var20.map).toList();
                  if (var9 != null && var9 == var20.map) {
                     var8 -= Math.min(var8, Math.max(0, var20.count - var10.size()));
                  }

                  ArrayList var11 = new ArrayList(var10);
                  if (var9 != null) {
                     var11.sort(Comparator.comparingInt(var1x -> var1x.c == var9 ? 0 : 1));
                  }

                  for (um.o var13 : var11) {
                     if (var8 <= 0) {
                        break;
                     }

                     var13.T = null;
                     var8--;
                  }
               }
            }
         }

         var2.addAll(this.M.keySet());

         for (um.o var17 : this.O) {
            if (var17.T == null) {
               for (um.l var21 : var2) {
                  if (this.M.getOrDefault(var21, 0) > 0 && ((um.f)var1.get(var21)).map == var17.j) {
                     var17.T = var21;
                     this.M.merge(var21, -1, Integer::sum);
                     break;
                  }
               }
            }
         }

         this.j = new LinkedHashMap<>(var1);
      }
   }

   int G(String var1, int var2) {
      int var3 = 0;

      for (um.o var5 : this.O) {
         if (var5.J.equals(var1) && !var5.p()) {
            if (var5.j == var2) {
               var3++;
            }

            if (var5.c == var2) {
               var3--;
            }
         }
      }

      return var3;
   }

   um.o L(um.l var1, int var2) {
      for (um.o var4 : this.O) {
         if (var1.equals(var4.T) && var4.j == var2 && !var4.p()) {
            return var4;
         }
      }

      return null;
   }

   void D() {
      this.O.removeIf(um.o::p);
   }

   void s(um.l var1) {
      this.O.removeIf(var1x -> var1x.p() && var1.equals(var1x.T));
   }

   void j() {
      this.O.clear();
      this.j = Map.of();
      this.M.clear();
      this.X.clear();
      this.h = 0;
      this.x.clear();
   }

   record f(int map, int count) {
   }

   record l(String kind, String key, int slot) {
   }

   static final class o {
      final String J;
      final int j;
      final int c;
      um.l T;

      o(String var1, int var2, int var3, um.l var4) {
         this.J = var1;
         this.j = var2;
         this.c = var3;
         this.T = var4;
      }

      boolean p() {
         return this.T != null && this.T.kind.equals("stock") && this.T.key.equals(this.J);
      }
   }
}
