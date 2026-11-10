package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntSupplier;

public final class p0 {
   private final ArrayDeque<p0.q> H = new ArrayDeque<>();
   private final int[] E = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   private final List<p0.d> l = new ArrayList<>();

   public List<p0.q> T() {
      List var1 = List.copyOf(this.H);
      this.H.clear();
      return var1;
   }

   public int J(int var1) {
      return var1 >= 0 && var1 < 9 ? this.E[var1] : var1;
   }

   public int q(String var1) {
      p0.d var2 = this.K(var1);
      return var2 == null ? -1 : var2.V;
   }

   public void V(String var1, int var2) {
      int var3 = this.q(var1);
      if (var3 >= 0 && var2 >= 0 && var2 < 9) {
         this.h(var3, this.E[var2]);
      }
   }

   public void J(int var1, int var2) {
      if (var2 >= 0 && var2 < 9) {
         for (p0.d var4 : this.l) {
            if (var4.n == var1) {
               int var5 = s(var4.Q) ? Integer.parseInt(var4.Q.substring(6)) : var4.V;
               if (var5 >= 0) {
                  this.h(this.E[var2], var5);
               }

               return;
            }
         }
      }
   }

   private void h(int var1, int var2) {
      if (var1 >= 0 && var2 >= 0) {
         for (int var3 = 0; var3 < 9; var3++) {
            if (this.E[var3] == var1) {
               this.E[var3] = var2;
            } else if (this.E[var3] == var2) {
               this.E[var3] = var1;
            }
         }
      }
   }

   public void d(int[] var1) {
      this.l.clear();
      this.H.clear();
      int var2 = 0;

      while (var2 < 9) {
         this.E[var2] = var2++;
      }

      for (int var3 = 0; var3 < var1.length; var3++) {
         if (var1[var3] > 0) {
            this.l.add(new p0.d(var1[var3], "stock:" + var3));
         }
      }
   }

   private p0.d K(String var1) {
      for (p0.d var3 : this.l) {
         if (var1.equals(var3.Q)) {
            return var3;
         }
      }

      return null;
   }

   private static boolean s(String var0) {
      return var0 != null && var0.startsWith("stock:");
   }

   public int e(String var1, int var2) {
      p0.d var3 = this.K(var1);
      return var3 == null ? var2 : var3.n;
   }

   public boolean E() {
      for (p0.d var2 : this.l) {
         if (var2.H != var2.n && !s(var2.Q)) {
            return true;
         }
      }

      return false;
   }

   public boolean U() {
      for (int var1 = 0; var1 < this.E.length; var1++) {
         if (this.E[var1] != var1) {
            return true;
         }
      }

      return this.l.stream().anyMatch(var0 -> var0.H != var0.n);
   }

   public int z() {
      int var1 = 0;

      for (p0.d var3 : this.l) {
         if (var3.n == 2) {
            var1++;
         }
      }

      return var1;
   }

   public int R() {
      int var1 = 0;

      for (p0.d var3 : this.l) {
         if (var3.n == 2 && var3.Q != null && !s(var3.Q)) {
            var1++;
         }
      }

      return var1;
   }

   public void y(Map<String, Integer> var1, Set<String> var2, int var3, int var4, boolean var5) {
      this.Y(var1, var2, () -> var3, var4, var5);
   }

   public boolean D() {
      return !this.l.isEmpty() && this.l.stream().allMatch(var0 -> s(var0.Q));
   }

   public void K(Map<String, Integer> var1) {
      List var2 = this.l
         .stream()
         .filter(var0 -> var0.H == 1 && var0.h && var0.Q != null && !s(var0.Q))
         .sorted(Comparator.comparingInt(var0 -> var0.n == 1 ? 0 : 1))
         .toList();
      if (!var2.isEmpty()) {
         List var3 = this.l.stream().filter(var0 -> var0.H == 1 && s(var0.Q)).toList();
         TreeSet var4 = new TreeSet();

         for (p0.d var6 : var2) {
            var4.add(var6.V);
         }

         for (p0.d var13 : var3) {
            var4.add(Integer.parseInt(var13.Q.substring(6)));
         }

         List var12 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) < 1).toList();
         List var14 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) == 1).toList();
         if (var12.size() == var2.size() && var14.size() == var3.size()) {
            p0.d var7 = this.l.stream().filter(var0 -> var0.H == 2 && var0.n == 1 && s(var0.Q)).findFirst().orElse(null);
            int var8 = var7 == null ? -1 : Integer.parseInt(var7.Q.substring(6));

            for (p0.d var10 : var2) {
               if (var10.n == 2 && var8 >= 0) {
                  this.h(var10.V, var8);
               }
            }

            for (int var15 = 0; var15 < var3.size(); var15++) {
               ((p0.d)var3.get(var15)).Q = "stock:" + var14.get(var15);
            }

            for (int var16 = 0; var16 < var2.size(); var16++) {
               p0.d var17 = (p0.d)var2.get(var16);
               var17.V = (Integer)var12.get(var16);
               var17.h = (boolean)0;
               if (var17.n == 2 && var8 >= 0) {
                  this.h(var17.V, var8);
               }
            }
         }
      }
   }

   public void Y(Map<String, Integer> var1, Set<String> var2, IntSupplier var3, int var4, boolean var5) {
      if (var5) {
         for (p0.d var7 : this.l) {
            if (var7.Q != null && !s(var7.Q) && var7.V >= 0 && var7.h) {
               var7.h = (boolean)0;
               String var8 = "stock:" + var7.V;
               if (this.K(var8) == null && var1.getOrDefault(var8, -1) == var7.H) {
                  for (p0.d var10 : this.l) {
                     if (s(var10.Q) && var10.H == var7.H && var1.getOrDefault(var10.Q, -1) < 1) {
                        int var11 = Integer.parseInt(var10.Q.substring(6));
                        this.h(var7.V, var11);
                        var10.Q = var8;
                        var7.V = var11;
                        break;
                     }
                  }
               }
            }
         }
      }

      for (p0.d var18 : this.l) {
         if (var18.Q != null && (!s(var18.Q) || var5) && var1.getOrDefault(var18.Q, -1) != var18.H) {
            var18.A = s(var18.Q);
            if (var18.A) {
               var18.V = Integer.parseInt(var18.Q.substring(6));
               var18.h = (boolean)0;
            }

            var18.Q = null;
         }
      }

      ArrayList var17 = new ArrayList(var1.keySet());
      var17.sort(Comparator.<String, Integer>comparing(var0 -> s(var0) ? 1 : 0).thenComparing(var0 -> (String)var0));

      for (String var20 : var17) {
         int var21 = (Integer)var1.get(var20);
         if (var21 >= 1 && this.K(var20) == null) {
            p0.d var22 = null;

            for (p0.d var12 : this.l) {
               if (var12.Q == null && var12.H == var21) {
                  if (s(var20) && var20.equals("stock:" + var12.V)) {
                     var22 = var12;
                     break;
                  }

                  if (var22 == null || var22.A && !var12.A) {
                     var22 = var12;
                  }
               }
            }

            if (var22 == null && !var5 && !s(var20)) {
               ArrayList var24 = new ArrayList();

               for (p0.d var13 : this.l) {
                  if (s(var13.Q) && var13.H == var21) {
                     var24.add(var13);
                  }
               }

               if (!var24.isEmpty()) {
                  var22 = (p0.d)var24.get(Math.floorMod(var4, var24.size()));
                  var22.A = (boolean)1;
                  var22.h = (boolean)1;
                  var22.V = Integer.parseInt(var22.Q.substring(6));
               }
            }

            if (var22 != null) {
               int var25 = var22.A && var2.contains(var20) ? 1 : 0;
               var22.Q = var20;
               var22.A = (boolean)0;
               int var27 = var25 != 0 ? var3.getAsInt() : var22.n;
               if (var25 != 0 && var22.n != var27) {
                  ArrayList var28 = new ArrayList();

                  for (p0.d var15 : this.l) {
                     if (s(var15.Q) && var15.n == var27) {
                        var28.add(var15);
                     }
                  }

                  if (!var28.isEmpty()) {
                     p0.d var29 = (p0.d)var28.get(Math.floorMod(var4, var28.size()));
                     this.h(var22.V, Integer.parseInt(var29.Q.substring(6)));
                     int var30 = var22.n;
                     var22.n = var29.n;
                     var29.n = var30;
                  }
               }

               if (var25 != 0) {
                  if (this.H.size() >= 200) {
                     this.H.removeFirst();
                  }

                  this.H.addLast(new p0.q(var22.H, var22.n));
               }
            }
         }
      }
   }

   private static final class d {
      final int H;
      int n;
      String Q;
      boolean A;
      int V = -1;
      boolean h;

      d(int var1, String var2) {
         this.H = var1;
         this.n = var1;
         this.Q = var2;
      }
   }

   public record q(int actual, int display) {
   }
}
