package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntSupplier;

public final class wq {
   private final ArrayDeque<wq.l> R = new ArrayDeque<>();
   private final int[] Y = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   private final List<wq.f> l = new ArrayList<>();

   public List<wq.l> u() {
      List var1 = List.copyOf(this.R);
      this.R.clear();
      return var1;
   }

   public int I(int var1) {
      return var1 >= 0 && var1 < 9 ? this.Y[var1] : var1;
   }

   public int i(String var1) {
      wq.f var2 = this.K(var1);
      return var2 == null ? -1 : var2.p;
   }

   public void w(String var1, int var2) {
      int var3 = this.i(var1);
      if (var3 >= 0 && var2 >= 0 && var2 < 9) {
         this.d(var3, this.Y[var2]);
      }
   }

   public void L(int var1, int var2) {
      if (var2 >= 0 && var2 < 9) {
         for (wq.f var4 : this.l) {
            if (var4.D == var1) {
               int var5 = f(var4.d) ? Integer.parseInt(var4.d.substring(6)) : var4.p;
               if (var5 >= 0) {
                  this.d(this.Y[var2], var5);
               }

               return;
            }
         }
      }
   }

   private void d(int var1, int var2) {
      if (var1 >= 0 && var2 >= 0) {
         for (int var3 = 0; var3 < 9; var3++) {
            if (this.Y[var3] == var1) {
               this.Y[var3] = var2;
            } else if (this.Y[var3] == var2) {
               this.Y[var3] = var1;
            }
         }
      }
   }

   public void F(int[] var1) {
      this.l.clear();
      this.R.clear();
      int var2 = 0;

      while (var2 < 9) {
         this.Y[var2] = var2++;
      }

      for (int var3 = 0; var3 < var1.length; var3++) {
         if (var1[var3] > 0) {
            this.l.add(new wq.f(var1[var3], "stock:" + var3));
         }
      }
   }

   private wq.f K(String var1) {
      for (wq.f var3 : this.l) {
         if (var1.equals(var3.d)) {
            return var3;
         }
      }

      return null;
   }

   private static boolean f(String var0) {
      return var0 != null && var0.startsWith("stock:");
   }

   public int b(String var1, int var2) {
      wq.f var3 = this.K(var1);
      return var3 == null ? var2 : var3.D;
   }

   public boolean j() {
      for (wq.f var2 : this.l) {
         if (var2.U != var2.D && !f(var2.d)) {
            return true;
         }
      }

      return false;
   }

   public boolean H() {
      for (int var1 = 0; var1 < this.Y.length; var1++) {
         if (this.Y[var1] != var1) {
            return true;
         }
      }

      return this.l.stream().anyMatch(var0 -> var0.U != var0.D);
   }

   public int n() {
      int var1 = 0;

      for (wq.f var3 : this.l) {
         if (var3.D == 2) {
            var1++;
         }
      }

      return var1;
   }

   public int F() {
      int var1 = 0;

      for (wq.f var3 : this.l) {
         if (var3.D == 2 && var3.d != null && !f(var3.d)) {
            var1++;
         }
      }

      return var1;
   }

   public void u(Map<String, Integer> var1, Set<String> var2, int var3, int var4, boolean var5) {
      this.C(var1, var2, () -> var3, var4, var5);
   }

   public boolean m() {
      return !this.l.isEmpty() && this.l.stream().allMatch(var0 -> f(var0.d));
   }

   public void P(Map<String, Integer> var1) {
      List var2 = this.l
         .stream()
         .filter(var0 -> var0.U == 1 && var0.i && var0.d != null && !f(var0.d))
         .sorted(Comparator.comparingInt(var0 -> var0.D == 1 ? 0 : 1))
         .toList();
      if (!var2.isEmpty()) {
         List var3 = this.l.stream().filter(var0 -> var0.U == 1 && f(var0.d)).toList();
         TreeSet var4 = new TreeSet();

         for (wq.f var6 : var2) {
            var4.add(var6.p);
         }

         for (wq.f var13 : var3) {
            var4.add(Integer.parseInt(var13.d.substring(6)));
         }

         List var12 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) < 1).toList();
         List var14 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) == 1).toList();
         if (var12.size() == var2.size() && var14.size() == var3.size()) {
            wq.f var7 = this.l.stream().filter(var0 -> var0.U == 2 && var0.D == 1 && f(var0.d)).findFirst().orElse(null);
            int var8 = var7 == null ? -1 : Integer.parseInt(var7.d.substring(6));

            for (wq.f var10 : var2) {
               if (var10.D == 2 && var8 >= 0) {
                  this.d(var10.p, var8);
               }
            }

            for (int var15 = 0; var15 < var3.size(); var15++) {
               ((wq.f)var3.get(var15)).d = "stock:" + var14.get(var15);
            }

            for (int var16 = 0; var16 < var2.size(); var16++) {
               wq.f var17 = (wq.f)var2.get(var16);
               var17.p = (Integer)var12.get(var16);
               var17.i = (boolean)0;
               if (var17.D == 2 && var8 >= 0) {
                  this.d(var17.p, var8);
               }
            }
         }
      }
   }

   public void C(Map<String, Integer> var1, Set<String> var2, IntSupplier var3, int var4, boolean var5) {
      if (var5) {
         for (wq.f var7 : this.l) {
            if (var7.d != null && !f(var7.d) && var7.p >= 0 && var7.i) {
               var7.i = (boolean)0;
               String var8 = "stock:" + var7.p;
               if (this.K(var8) == null && var1.getOrDefault(var8, -1) == var7.U) {
                  for (wq.f var10 : this.l) {
                     if (f(var10.d) && var10.U == var7.U && var1.getOrDefault(var10.d, -1) < 1) {
                        int var11 = Integer.parseInt(var10.d.substring(6));
                        this.d(var7.p, var11);
                        var10.d = var8;
                        var7.p = var11;
                        break;
                     }
                  }
               }
            }
         }
      }

      for (wq.f var18 : this.l) {
         if (var18.d != null && (!f(var18.d) || var5) && var1.getOrDefault(var18.d, -1) != var18.U) {
            var18.h = f(var18.d);
            if (var18.h) {
               var18.p = Integer.parseInt(var18.d.substring(6));
               var18.i = (boolean)0;
            }

            var18.d = null;
         }
      }

      ArrayList var17 = new ArrayList(var1.keySet());
      var17.sort(Comparator.<String, Integer>comparing(var0 -> f(var0) ? 1 : 0).thenComparing(var0 -> (String)var0));

      for (String var20 : var17) {
         int var21 = (Integer)var1.get(var20);
         if (var21 >= 1 && this.K(var20) == null) {
            wq.f var22 = null;

            for (wq.f var12 : this.l) {
               if (var12.d == null && var12.U == var21) {
                  if (f(var20) && var20.equals("stock:" + var12.p)) {
                     var22 = var12;
                     break;
                  }

                  if (var22 == null || var22.h && !var12.h) {
                     var22 = var12;
                  }
               }
            }

            if (var22 == null && !var5 && !f(var20)) {
               ArrayList var24 = new ArrayList();

               for (wq.f var13 : this.l) {
                  if (f(var13.d) && var13.U == var21) {
                     var24.add(var13);
                  }
               }

               if (!var24.isEmpty()) {
                  var22 = (wq.f)var24.get(Math.floorMod(var4, var24.size()));
                  var22.h = (boolean)1;
                  var22.i = (boolean)1;
                  var22.p = Integer.parseInt(var22.d.substring(6));
               }
            }

            if (var22 != null) {
               int var25 = var22.h && var2.contains(var20) ? 1 : 0;
               var22.d = var20;
               var22.h = (boolean)0;
               int var27 = var25 != 0 ? var3.getAsInt() : var22.D;
               if (var25 != 0 && var22.D != var27) {
                  ArrayList var28 = new ArrayList();

                  for (wq.f var15 : this.l) {
                     if (f(var15.d) && var15.D == var27) {
                        var28.add(var15);
                     }
                  }

                  if (!var28.isEmpty()) {
                     wq.f var29 = (wq.f)var28.get(Math.floorMod(var4, var28.size()));
                     this.d(var22.p, Integer.parseInt(var29.d.substring(6)));
                     int var30 = var22.D;
                     var22.D = var29.D;
                     var29.D = var30;
                  }
               }

               if (var25 != 0) {
                  if (this.R.size() >= 200) {
                     this.R.removeFirst();
                  }

                  this.R.addLast(new wq.l(var22.U, var22.D));
               }
            }
         }
      }
   }

   private static final class f {
      final int U;
      int D;
      String d;
      boolean h;
      int p = -1;
      boolean i;

      f(int var1, String var2) {
         this.U = var1;
         this.D = var1;
         this.d = var2;
      }
   }

   public record l(int actual, int display) {
   }
}
