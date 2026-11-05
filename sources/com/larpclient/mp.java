package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntSupplier;

public final class mp {
   private final ArrayDeque<mp.z> J = new ArrayDeque<>();
   private final int[] t = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   private final List<mp.j> i = new ArrayList<>();

   public List<mp.z> J() {
      List var1 = List.copyOf(this.J);
      this.J.clear();
      return var1;
   }

   public int a(int var1) {
      return var1 >= 0 && var1 < 9 ? this.t[var1] : var1;
   }

   public int J(String var1) {
      mp.j var2 = this.P(var1);
      return var2 == null ? -1 : var2.W;
   }

   public void i(String var1, int var2) {
      int var3 = this.J(var1);
      if (var3 >= 0 && var2 >= 0 && var2 < 9) {
         this.m(var3, this.t[var2]);
      }
   }

   public void U(int var1, int var2) {
      if (var2 >= 0 && var2 < 9) {
         for (mp.j var4 : this.i) {
            if (var4.Y == var1) {
               int var5 = j(var4.X) ? Integer.parseInt(var4.X.substring(6)) : var4.W;
               if (var5 >= 0) {
                  this.m(this.t[var2], var5);
               }

               return;
            }
         }
      }
   }

   private void m(int var1, int var2) {
      if (var1 >= 0 && var2 >= 0) {
         for (int var3 = 0; var3 < 9; var3++) {
            if (this.t[var3] == var1) {
               this.t[var3] = var2;
            } else if (this.t[var3] == var2) {
               this.t[var3] = var1;
            }
         }
      }
   }

   public void I(int[] var1) {
      this.i.clear();
      this.J.clear();
      int var2 = 0;

      while (var2 < 9) {
         this.t[var2] = var2++;
      }

      for (int var3 = 0; var3 < var1.length; var3++) {
         if (var1[var3] > 0) {
            this.i.add(new mp.j(var1[var3], "stock:" + var3));
         }
      }
   }

   private mp.j P(String var1) {
      for (mp.j var3 : this.i) {
         if (var1.equals(var3.X)) {
            return var3;
         }
      }

      return null;
   }

   private static boolean j(String var0) {
      return var0 != null && var0.startsWith("stock:");
   }

   public int F(String var1, int var2) {
      mp.j var3 = this.P(var1);
      return var3 == null ? var2 : var3.Y;
   }

   public boolean L() {
      for (mp.j var2 : this.i) {
         if (var2.z != var2.Y && !j(var2.X)) {
            return true;
         }
      }

      return false;
   }

   public boolean g() {
      for (int var1 = 0; var1 < this.t.length; var1++) {
         if (this.t[var1] != var1) {
            return true;
         }
      }

      return this.i.stream().anyMatch(var0 -> var0.z != var0.Y);
   }

   public int E() {
      int var1 = 0;

      for (mp.j var3 : this.i) {
         if (var3.Y == 2) {
            var1++;
         }
      }

      return var1;
   }

   public int Z() {
      int var1 = 0;

      for (mp.j var3 : this.i) {
         if (var3.Y == 2 && var3.X != null && !j(var3.X)) {
            var1++;
         }
      }

      return var1;
   }

   public void v(Map<String, Integer> var1, Set<String> var2, int var3, int var4, boolean var5) {
      this.c(var1, var2, () -> var3, var4, var5);
   }

   public boolean b() {
      return !this.i.isEmpty() && this.i.stream().allMatch(var0 -> j(var0.X));
   }

   public void V(Map<String, Integer> var1) {
      List var2 = this.i
         .stream()
         .filter(var0 -> var0.z == 1 && var0.o && var0.X != null && !j(var0.X))
         .sorted(Comparator.comparingInt(var0 -> var0.Y == 1 ? 0 : 1))
         .toList();
      if (!var2.isEmpty()) {
         List var3 = this.i.stream().filter(var0 -> var0.z == 1 && j(var0.X)).toList();
         TreeSet var4 = new TreeSet();

         for (mp.j var6 : var2) {
            var4.add(var6.W);
         }

         for (mp.j var13 : var3) {
            var4.add(Integer.parseInt(var13.X.substring(6)));
         }

         List var12 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) < 1).toList();
         List var14 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) == 1).toList();
         if (var12.size() == var2.size() && var14.size() == var3.size()) {
            mp.j var7 = this.i.stream().filter(var0 -> var0.z == 2 && var0.Y == 1 && j(var0.X)).findFirst().orElse(null);
            int var8 = var7 == null ? -1 : Integer.parseInt(var7.X.substring(6));

            for (mp.j var10 : var2) {
               if (var10.Y == 2 && var8 >= 0) {
                  this.m(var10.W, var8);
               }
            }

            for (int var15 = 0; var15 < var3.size(); var15++) {
               ((mp.j)var3.get(var15)).X = "stock:" + var14.get(var15);
            }

            for (int var16 = 0; var16 < var2.size(); var16++) {
               mp.j var17 = (mp.j)var2.get(var16);
               var17.W = (Integer)var12.get(var16);
               var17.o = (boolean)0;
               if (var17.Y == 2 && var8 >= 0) {
                  this.m(var17.W, var8);
               }
            }
         }
      }
   }

   public void c(Map<String, Integer> var1, Set<String> var2, IntSupplier var3, int var4, boolean var5) {
      if (var5) {
         for (mp.j var7 : this.i) {
            if (var7.X != null && !j(var7.X) && var7.W >= 0 && var7.o) {
               var7.o = (boolean)0;
               String var8 = "stock:" + var7.W;
               if (this.P(var8) == null && var1.getOrDefault(var8, -1) == var7.z) {
                  for (mp.j var10 : this.i) {
                     if (j(var10.X) && var10.z == var7.z && var1.getOrDefault(var10.X, -1) < 1) {
                        int var11 = Integer.parseInt(var10.X.substring(6));
                        this.m(var7.W, var11);
                        var10.X = var8;
                        var7.W = var11;
                        break;
                     }
                  }
               }
            }
         }
      }

      for (mp.j var18 : this.i) {
         if (var18.X != null && (!j(var18.X) || var5) && var1.getOrDefault(var18.X, -1) != var18.z) {
            var18.y = j(var18.X);
            if (var18.y) {
               var18.W = Integer.parseInt(var18.X.substring(6));
               var18.o = (boolean)0;
            }

            var18.X = null;
         }
      }

      ArrayList var17 = new ArrayList(var1.keySet());
      var17.sort(Comparator.<String, Integer>comparing(var0 -> j(var0) ? 1 : 0).thenComparing(var0 -> (String)var0));

      for (String var20 : var17) {
         int var21 = (Integer)var1.get(var20);
         if (var21 >= 1 && this.P(var20) == null) {
            mp.j var22 = null;

            for (mp.j var12 : this.i) {
               if (var12.X == null && var12.z == var21) {
                  if (j(var20) && var20.equals("stock:" + var12.W)) {
                     var22 = var12;
                     break;
                  }

                  if (var22 == null || var22.y && !var12.y) {
                     var22 = var12;
                  }
               }
            }

            if (var22 == null && !var5 && !j(var20)) {
               ArrayList var24 = new ArrayList();

               for (mp.j var13 : this.i) {
                  if (j(var13.X) && var13.z == var21) {
                     var24.add(var13);
                  }
               }

               if (!var24.isEmpty()) {
                  var22 = (mp.j)var24.get(Math.floorMod(var4, var24.size()));
                  var22.y = (boolean)1;
                  var22.o = (boolean)1;
                  var22.W = Integer.parseInt(var22.X.substring(6));
               }
            }

            if (var22 != null) {
               int var25 = var22.y && var2.contains(var20) ? 1 : 0;
               var22.X = var20;
               var22.y = (boolean)0;
               int var27 = var25 != 0 ? var3.getAsInt() : var22.Y;
               if (var25 != 0 && var22.Y != var27) {
                  ArrayList var28 = new ArrayList();

                  for (mp.j var15 : this.i) {
                     if (j(var15.X) && var15.Y == var27) {
                        var28.add(var15);
                     }
                  }

                  if (!var28.isEmpty()) {
                     mp.j var29 = (mp.j)var28.get(Math.floorMod(var4, var28.size()));
                     this.m(var22.W, Integer.parseInt(var29.X.substring(6)));
                     int var30 = var22.Y;
                     var22.Y = var29.Y;
                     var29.Y = var30;
                  }
               }

               if (var25 != 0) {
                  if (this.J.size() >= 200) {
                     this.J.removeFirst();
                  }

                  this.J.addLast(new mp.z(var22.z, var22.Y));
               }
            }
         }
      }
   }

   private static final class j {
      final int z;
      int Y;
      String X;
      boolean y;
      int W = -1;
      boolean o;

      j(int var1, String var2) {
         this.z = var1;
         this.Y = var1;
         this.X = var2;
      }
   }

   public record z(int actual, int display) {
   }
}
