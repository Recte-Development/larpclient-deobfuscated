package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class sk {
   final List<sk.x> O = new ArrayList<>();
   private Map<sk.f, sk.t> S = Map.of();
   private final Map<sk.f, Integer> q = new LinkedHashMap<>();
   private final Map<sk.f, Integer> U = new HashMap<>();
   private int D;
   private final Map<sk.f, Integer> g = new HashMap<>();

   void a(sk.f var1, int var2) {
      this.g.put(var1, var2);
   }

   void S(String var1, int var2, int var3, sk.f var4, Map<sk.f, sk.t> var5) {
      this.n(var5);
      this.O.add(new sk.x(var1, var2, var3, var4));
      this.q.clear();
      this.U.clear();
   }

   void n(Map<sk.f, sk.t> var1) {
      if (!this.S.equals(var1)) {
         this.D++;
         this.q.keySet().removeIf(var2x -> this.D - this.U.getOrDefault(var2x, 0) > 4 || !var1.containsKey(var2x) || ((sk.t)var1.get(var2x)).count == 0);
         LinkedHashSet var2 = new LinkedHashSet();

         for (Entry var4 : var1.entrySet()) {
            sk.t var5 = this.S.get(var4.getKey());
            sk.t var6 = (sk.t)var4.getValue();
            int var7 = var6.count - (var5 != null && var5.map == var6.map ? var5.count : 0);
            if (var5 != null && var5.map != var6.map) {
               this.q.remove(var4.getKey());
            }

            if (var7 > 0 && (var5 != null || !((sk.f)var4.getKey()).kind.equals("stock"))) {
               var2.add((sk.f)var4.getKey());
               this.q.merge((sk.f)var4.getKey(), var7, Integer::sum);
               this.U.put((sk.f)var4.getKey(), this.D);
            }
         }

         for (Entry var16 : this.S.entrySet()) {
            sk.f var18 = (sk.f)var16.getKey();
            sk.t var20 = (sk.t)var16.getValue();
            sk.t var22 = (sk.t)var1.get(var18);
            if (var22 != null || var18.kind.equals("inventory") || var18.kind.equals("cursor")) {
               int var8 = var20.count - (var22 != null && var20.map == var22.map ? var22.count : 0);
               if (var8 > 0) {
                  this.q.computeIfPresent(var18, (var2x, var3) -> Math.min(var3, var22 != null && var20.map == var22.map ? var22.count : 0));
               }

               if (var8 > 0) {
                  Integer var9 = this.g.remove(var18);
                  List var10 = this.O.stream().filter(var2x -> var18.equals(var2x.R) && var2x.o == var20.map).toList();
                  if (var9 != null && var9 == var20.map) {
                     var8 -= Math.min(var8, Math.max(0, var20.count - var10.size()));
                  }

                  ArrayList var11 = new ArrayList(var10);
                  if (var9 != null) {
                     var11.sort(Comparator.comparingInt(var1x -> var1x.z == var9 ? 0 : 1));
                  }

                  for (sk.x var13 : var11) {
                     if (var8 <= 0) {
                        break;
                     }

                     var13.R = null;
                     var8--;
                  }
               }
            }
         }

         var2.addAll(this.q.keySet());

         for (sk.x var17 : this.O) {
            if (var17.R == null) {
               for (sk.f var21 : var2) {
                  if (this.q.getOrDefault(var21, 0) > 0 && ((sk.t)var1.get(var21)).map == var17.o) {
                     var17.R = var21;
                     this.q.merge(var21, -1, Integer::sum);
                     break;
                  }
               }
            }
         }

         this.S = new LinkedHashMap<>(var1);
      }
   }

   int Z(String var1, int var2) {
      int var3 = 0;

      for (sk.x var5 : this.O) {
         if (var5.X.equals(var1) && !var5.l()) {
            if (var5.o == var2) {
               var3++;
            }

            if (var5.z == var2) {
               var3--;
            }
         }
      }

      return var3;
   }

   sk.x l(sk.f var1, int var2) {
      for (sk.x var4 : this.O) {
         if (var1.equals(var4.R) && var4.o == var2 && !var4.l()) {
            return var4;
         }
      }

      return null;
   }

   void b() {
      this.O.removeIf(sk.x::l);
   }

   void f(sk.f var1) {
      this.O.removeIf(var1x -> var1x.l() && var1.equals(var1x.R));
   }

   void P() {
      this.O.clear();
      this.S = Map.of();
      this.q.clear();
      this.U.clear();
      this.D = 0;
      this.g.clear();
   }

   record f(String kind, String key, int slot) {
   }

   record t(int map, int count) {
   }

   static final class x {
      final String X;
      final int o;
      final int z;
      sk.f R;

      x(String var1, int var2, int var3, sk.f var4) {
         this.X = var1;
         this.o = var2;
         this.z = var3;
         this.R = var4;
      }

      boolean l() {
         return this.R != null && this.R.kind.equals("stock") && this.R.key.equals(this.X);
      }
   }
}
