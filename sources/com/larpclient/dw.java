package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntSupplier;

public final class dw {
   private final ArrayDeque<dw.t> Z = new ArrayDeque<>();
   private final int[] i = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   private final List<dw.g> B = new ArrayList<>();

   public List<dw.t> B() {
      List var1 = List.copyOf(this.Z);
      this.Z.clear();
      return var1;
   }

   public int h(int var1) {
      return var1 >= 0 && var1 < 9 ? this.i[var1] : var1;
   }

   public int c(String var1) {
      dw.g var2 = this.M(var1);
      return var2 == null ? -1 : var2.m;
   }

   public void B(String var1, int var2) {
      int var3 = this.c(var1);
      if (var3 >= 0 && var2 >= 0 && var2 < 9) {
         this.Z(var3, this.i[var2]);
      }
   }

   public void u(int var1, int var2) {
      if (var2 >= 0 && var2 < 9) {
         for (dw.g var4 : this.B) {
            if (var4.U == var1) {
               int var5 = R(var4.A) ? Integer.parseInt(var4.A.substring(6)) : var4.m;
               if (var5 >= 0) {
                  this.Z(this.i[var2], var5);
               }

               return;
            }
         }
      }
   }

   private void Z(int var1, int var2) {
      if (var1 >= 0 && var2 >= 0) {
         for (int var3 = 0; var3 < 9; var3++) {
            if (this.i[var3] == var1) {
               this.i[var3] = var2;
            } else if (this.i[var3] == var2) {
               this.i[var3] = var1;
            }
         }
      }
   }

   public void h(int[] var1) {
      this.B.clear();
      this.Z.clear();
      int var2 = 0;

      while (var2 < 9) {
         this.i[var2] = var2++;
      }

      for (int var3 = 0; var3 < var1.length; var3++) {
         if (var1[var3] > 0) {
            this.B.add(new dw.g(var1[var3], "stock:" + var3));
         }
      }
   }

   private dw.g M(String var1) {
      for (dw.g var3 : this.B) {
         if (var1.equals(var3.A)) {
            return var3;
         }
      }

      return null;
   }

   private static boolean R(String var0) {
      return var0 != null && var0.startsWith("stock:");
   }

   public int X(String var1, int var2) {
      dw.g var3 = this.M(var1);
      return var3 == null ? var2 : var3.U;
   }

   public boolean h() {
      for (dw.g var2 : this.B) {
         if (var2.z != var2.U && !R(var2.A)) {
            return true;
         }
      }

      return false;
   }

   public boolean R() {
      for (int var1 = 0; var1 < this.i.length; var1++) {
         if (this.i[var1] != var1) {
            return true;
         }
      }

      return this.B.stream().anyMatch(var0 -> var0.z != var0.U);
   }

   public int h() {
      int var1 = 0;

      for (dw.g var3 : this.B) {
         if (var3.U == 2) {
            var1++;
         }
      }

      return var1;
   }

   public int Z() {
      int var1 = 0;

      for (dw.g var3 : this.B) {
         if (var3.U == 2 && var3.A != null && !R(var3.A)) {
            var1++;
         }
      }

      return var1;
   }

   public void t(Map<String, Integer> var1, Set<String> var2, int var3, int var4, boolean var5) {
      this.z(var1, var2, () -> var3, var4, var5);
   }

   public boolean S() {
      return !this.B.isEmpty() && this.B.stream().allMatch(var0 -> R(var0.A));
   }

   public void D(Map<String, Integer> var1) {
      List var2 = this.B
         .stream()
         .filter(var0 -> var0.z == 1 && var0.I && var0.A != null && !R(var0.A))
         .sorted(Comparator.comparingInt(var0 -> var0.U == 1 ? 0 : 1))
         .toList();
      if (!var2.isEmpty()) {
         List var3 = this.B.stream().filter(var0 -> var0.z == 1 && R(var0.A)).toList();
         TreeSet var4 = new TreeSet();

         for (dw.g var6 : var2) {
            var4.add(var6.m);
         }

         for (dw.g var13 : var3) {
            var4.add(Integer.parseInt(var13.A.substring(6)));
         }

         List var12 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) < 1).toList();
         List var14 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) == 1).toList();
         if (var12.size() == var2.size() && var14.size() == var3.size()) {
            dw.g var7 = this.B.stream().filter(var0 -> var0.z == 2 && var0.U == 1 && R(var0.A)).findFirst().orElse(null);
            int var8 = var7 == null ? -1 : Integer.parseInt(var7.A.substring(6));

            for (dw.g var10 : var2) {
               if (var10.U == 2 && var8 >= 0) {
                  this.Z(var10.m, var8);
               }
            }

            for (int var15 = 0; var15 < var3.size(); var15++) {
               ((dw.g)var3.get(var15)).A = "stock:" + var14.get(var15);
            }

            for (int var16 = 0; var16 < var2.size(); var16++) {
               dw.g var17 = (dw.g)var2.get(var16);
               var17.m = (Integer)var12.get(var16);
               var17.I = (boolean)0;
               if (var17.U == 2 && var8 >= 0) {
                  this.Z(var17.m, var8);
               }
            }
         }
      }
   }

   public void z(Map<String, Integer> var1, Set<String> var2, IntSupplier var3, int var4, boolean var5) {
      if (var5) {
         for (dw.g var7 : this.B) {
            if (var7.A != null && !R(var7.A) && var7.m >= 0 && var7.I) {
               var7.I = (boolean)0;
               String var8 = "stock:" + var7.m;
               if (this.M(var8) == null && var1.getOrDefault(var8, -1) == var7.z) {
                  for (dw.g var10 : this.B) {
                     if (R(var10.A) && var10.z == var7.z && var1.getOrDefault(var10.A, -1) < 1) {
                        int var11 = Integer.parseInt(var10.A.substring(6));
                        this.Z(var7.m, var11);
                        var10.A = var8;
                        var7.m = var11;
                        break;
                     }
                  }
               }
            }
         }
      }

      for (dw.g var18 : this.B) {
         if (var18.A != null && (!R(var18.A) || var5) && var1.getOrDefault(var18.A, -1) != var18.z) {
            var18.G = R(var18.A);
            if (var18.G) {
               var18.m = Integer.parseInt(var18.A.substring(6));
               var18.I = (boolean)0;
            }

            var18.A = null;
         }
      }

      ArrayList var17 = new ArrayList(var1.keySet());
      var17.sort(Comparator.<String, Integer>comparing(var0 -> R(var0) ? 1 : 0).thenComparing(var0 -> (String)var0));

      for (String var20 : var17) {
         int var21 = (Integer)var1.get(var20);
         if (var21 >= 1 && this.M(var20) == null) {
            dw.g var22 = null;

            for (dw.g var12 : this.B) {
               if (var12.A == null && var12.z == var21) {
                  if (R(var20) && var20.equals("stock:" + var12.m)) {
                     var22 = var12;
                     break;
                  }

                  if (var22 == null || var22.G && !var12.G) {
                     var22 = var12;
                  }
               }
            }

            if (var22 == null && !var5 && !R(var20)) {
               ArrayList var24 = new ArrayList();

               for (dw.g var13 : this.B) {
                  if (R(var13.A) && var13.z == var21) {
                     var24.add(var13);
                  }
               }

               if (!var24.isEmpty()) {
                  var22 = (dw.g)var24.get(Math.floorMod(var4, var24.size()));
                  var22.G = (boolean)1;
                  var22.I = (boolean)1;
                  var22.m = Integer.parseInt(var22.A.substring(6));
               }
            }

            if (var22 != null) {
               int var25 = var22.G && var2.contains(var20) ? 1 : 0;
               var22.A = var20;
               var22.G = (boolean)0;
               int var27 = var25 != 0 ? var3.getAsInt() : var22.U;
               if (var25 != 0 && var22.U != var27) {
                  ArrayList var28 = new ArrayList();

                  for (dw.g var15 : this.B) {
                     if (R(var15.A) && var15.U == var27) {
                        var28.add(var15);
                     }
                  }

                  if (!var28.isEmpty()) {
                     dw.g var29 = (dw.g)var28.get(Math.floorMod(var4, var28.size()));
                     this.Z(var22.m, Integer.parseInt(var29.A.substring(6)));
                     int var30 = var22.U;
                     var22.U = var29.U;
                     var29.U = var30;
                  }
               }

               if (var25 != 0) {
                  if (this.Z.size() >= 200) {
                     this.Z.removeFirst();
                  }

                  this.Z.addLast(new dw.t(var22.z, var22.U));
               }
            }
         }
      }
   }

   private static final class g {
      final int z;
      int U;
      String A;
      boolean G;
      int m = -1;
      boolean I;

      g(int var1, String var2) {
         this.z = var1;
         this.U = var1;
         this.A = var2;
      }
   }

   public record t(int actual, int display) {
   }
}
