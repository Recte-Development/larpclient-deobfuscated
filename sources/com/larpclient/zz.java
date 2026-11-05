package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class zz {
   final List<zz.o> u = new ArrayList<>();
   private Map<zz.j, zz.d> l = Map.of();
   private final Map<zz.j, Integer> f = new LinkedHashMap<>();
   private final Map<zz.j, Integer> i = new HashMap<>();
   private int L;
   private final Map<zz.j, Integer> x = new HashMap<>();

   void i(zz.j var1, int var2) {
      this.x.put(var1, var2);
   }

   void j(String var1, int var2, int var3, zz.j var4, Map<zz.j, zz.d> var5) {
      this.g(var5);
      this.u.add(new zz.o(var1, var2, var3, var4));
      this.f.clear();
      this.i.clear();
   }

   void g(Map<zz.j, zz.d> var1) {
      if (!this.l.equals(var1)) {
         this.L++;
         this.f.keySet().removeIf(var2x -> this.L - this.i.getOrDefault(var2x, 0) > 4 || !var1.containsKey(var2x) || ((zz.d)var1.get(var2x)).count == 0);
         LinkedHashSet var2 = new LinkedHashSet();

         for (Entry var4 : var1.entrySet()) {
            zz.d var5 = this.l.get(var4.getKey());
            zz.d var6 = (zz.d)var4.getValue();
            int var7 = var6.count - (var5 != null && var5.map == var6.map ? var5.count : 0);
            if (var5 != null && var5.map != var6.map) {
               this.f.remove(var4.getKey());
            }

            if (var7 > 0 && (var5 != null || !((zz.j)var4.getKey()).kind.equals("stock"))) {
               var2.add((zz.j)var4.getKey());
               this.f.merge((zz.j)var4.getKey(), var7, Integer::sum);
               this.i.put((zz.j)var4.getKey(), this.L);
            }
         }

         for (Entry var16 : this.l.entrySet()) {
            zz.j var18 = (zz.j)var16.getKey();
            zz.d var20 = (zz.d)var16.getValue();
            zz.d var22 = (zz.d)var1.get(var18);
            if (var22 != null || var18.kind.equals("inventory") || var18.kind.equals("cursor")) {
               int var8 = var20.count - (var22 != null && var20.map == var22.map ? var22.count : 0);
               if (var8 > 0) {
                  this.f.computeIfPresent(var18, (var2x, var3) -> Math.min(var3, var22 != null && var20.map == var22.map ? var22.count : 0));
               }

               if (var8 > 0) {
                  Integer var9 = this.x.remove(var18);
                  List var10 = this.u.stream().filter(var2x -> var18.equals(var2x.X) && var2x.K == var20.map).toList();
                  if (var9 != null && var9 == var20.map) {
                     var8 -= Math.min(var8, Math.max(0, var20.count - var10.size()));
                  }

                  ArrayList var11 = new ArrayList(var10);
                  if (var9 != null) {
                     var11.sort(Comparator.comparingInt(var1x -> var1x.G == var9 ? 0 : 1));
                  }

                  for (zz.o var13 : var11) {
                     if (var8 <= 0) {
                        break;
                     }

                     var13.X = null;
                     var8--;
                  }
               }
            }
         }

         var2.addAll(this.f.keySet());

         for (zz.o var17 : this.u) {
            if (var17.X == null) {
               for (zz.j var21 : var2) {
                  if (this.f.getOrDefault(var21, 0) > 0 && ((zz.d)var1.get(var21)).map == var17.K) {
                     var17.X = var21;
                     this.f.merge(var21, -1, Integer::sum);
                     break;
                  }
               }
            }
         }

         this.l = new LinkedHashMap<>(var1);
      }
   }

   int a(String var1, int var2) {
      int var3 = 0;

      for (zz.o var5 : this.u) {
         if (var5.O.equals(var1) && !var5.T()) {
            if (var5.K == var2) {
               var3++;
            }

            if (var5.G == var2) {
               var3--;
            }
         }
      }

      return var3;
   }

   zz.o b(zz.j var1, int var2) {
      for (zz.o var4 : this.u) {
         if (var1.equals(var4.X) && var4.K == var2 && !var4.T()) {
            return var4;
         }
      }

      return null;
   }

   void z() {
      this.u.removeIf(zz.o::T);
   }

   void k(zz.j var1) {
      this.u.removeIf(var1x -> var1x.T() && var1.equals(var1x.X));
   }

   void n() {
      this.u.clear();
      this.l = Map.of();
      this.f.clear();
      this.i.clear();
      this.L = 0;
      this.x.clear();
   }

   record d(int map, int count) {
   }

   record j(String kind, String key, int slot) {
   }

   static final class o {
      final String O;
      final int K;
      final int G;
      zz.j X;

      o(String var1, int var2, int var3, zz.j var4) {
         this.O = var1;
         this.K = var2;
         this.G = var3;
         this.X = var4;
      }

      boolean T() {
         return this.X != null && this.X.kind.equals("stock") && this.X.key.equals(this.O);
      }
   }
}
