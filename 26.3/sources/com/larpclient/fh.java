package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class fh {
   final List<fh.j> P = new ArrayList<>();
   private Map<fh.d, fh.k> l = Map.of();
   private final Map<fh.d, Integer> U = new LinkedHashMap<>();
   private final Map<fh.d, Integer> h = new HashMap<>();
   private int K;
   private final Map<fh.d, Integer> N = new HashMap<>();

   void c(fh.d var1, int var2) {
      this.N.put(var1, var2);
   }

   void D(String var1, int var2, int var3, fh.d var4, Map<fh.d, fh.k> var5) {
      this.m(var5);
      this.P.add(new fh.j(var1, var2, var3, var4));
      this.U.clear();
      this.h.clear();
   }

   void m(Map<fh.d, fh.k> var1) {
      if (!this.l.equals(var1)) {
         this.K++;
         this.U.keySet().removeIf(var2x -> this.K - this.h.getOrDefault(var2x, 0) > 4 || !var1.containsKey(var2x) || ((fh.k)var1.get(var2x)).count == 0);
         LinkedHashSet var2 = new LinkedHashSet();

         for (Entry var4 : var1.entrySet()) {
            fh.k var5 = this.l.get(var4.getKey());
            fh.k var6 = (fh.k)var4.getValue();
            int var7 = var6.count - (var5 != null && var5.map == var6.map ? var5.count : 0);
            if (var5 != null && var5.map != var6.map) {
               this.U.remove(var4.getKey());
            }

            if (var7 > 0 && (var5 != null || !((fh.d)var4.getKey()).kind.equals("stock"))) {
               var2.add((fh.d)var4.getKey());
               this.U.merge((fh.d)var4.getKey(), var7, Integer::sum);
               this.h.put((fh.d)var4.getKey(), this.K);
            }
         }

         for (Entry var16 : this.l.entrySet()) {
            fh.d var18 = (fh.d)var16.getKey();
            fh.k var20 = (fh.k)var16.getValue();
            fh.k var22 = (fh.k)var1.get(var18);
            if (var22 != null || var18.kind.equals("inventory") || var18.kind.equals("cursor")) {
               int var8 = var20.count - (var22 != null && var20.map == var22.map ? var22.count : 0);
               if (var8 > 0) {
                  this.U.computeIfPresent(var18, (var2x, var3) -> Math.min(var3, var22 != null && var20.map == var22.map ? var22.count : 0));
               }

               if (var8 > 0) {
                  Integer var9 = this.N.remove(var18);
                  List var10 = this.P.stream().filter(var2x -> var18.equals(var2x.P) && var2x.r == var20.map).toList();
                  if (var9 != null && var9 == var20.map) {
                     var8 -= Math.min(var8, Math.max(0, var20.count - var10.size()));
                  }

                  ArrayList var11 = new ArrayList(var10);
                  if (var9 != null) {
                     var11.sort(Comparator.comparingInt(var1x -> var1x.h == var9 ? 0 : 1));
                  }

                  for (fh.j var13 : var11) {
                     if (var8 <= 0) {
                        break;
                     }

                     var13.P = null;
                     var8--;
                  }
               }
            }
         }

         var2.addAll(this.U.keySet());

         for (fh.j var17 : this.P) {
            if (var17.P == null) {
               for (fh.d var21 : var2) {
                  if (this.U.getOrDefault(var21, 0) > 0 && ((fh.k)var1.get(var21)).map == var17.r) {
                     var17.P = var21;
                     this.U.merge(var21, -1, Integer::sum);
                     break;
                  }
               }
            }
         }

         this.l = new LinkedHashMap<>(var1);
      }
   }

   int v(String var1, int var2) {
      int var3 = 0;

      for (fh.j var5 : this.P) {
         if (var5.f.equals(var1) && !var5.R()) {
            if (var5.r == var2) {
               var3++;
            }

            if (var5.h == var2) {
               var3--;
            }
         }
      }

      return var3;
   }

   fh.j i(fh.d var1, int var2) {
      for (fh.j var4 : this.P) {
         if (var1.equals(var4.P) && var4.r == var2 && !var4.R()) {
            return var4;
         }
      }

      return null;
   }

   void b() {
      this.P.removeIf(fh.j::R);
   }

   void j(fh.d var1) {
      this.P.removeIf(var1x -> var1x.R() && var1.equals(var1x.P));
   }

   void u() {
      this.P.clear();
      this.l = Map.of();
      this.U.clear();
      this.h.clear();
      this.K = 0;
      this.N.clear();
   }

   record d(String kind, String key, int slot) {
   }

   static final class j {
      final String f;
      final int r;
      final int h;
      fh.d P;

      j(String var1, int var2, int var3, fh.d var4) {
         this.f = var1;
         this.r = var2;
         this.h = var3;
         this.P = var4;
      }

      boolean R() {
         return this.P != null && this.P.kind.equals("stock") && this.P.key.equals(this.f);
      }
   }

   record k(int map, int count) {
   }
}
