package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntSupplier;

public final class ab {
   private final ArrayDeque<ab.x> s = new ArrayDeque<>();
   private final int[] U = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   private final List<ab.i> z = new ArrayList<>();

   public List<ab.x> e() {
      List var1 = List.copyOf(this.s);
      this.s.clear();
      return var1;
   }

   public int T(int var1) {
      return var1 >= 0 && var1 < 9 ? this.U[var1] : var1;
   }

   public int p(String var1) {
      ab.i var2 = this.Z(var1);
      return var2 == null ? -1 : var2.h;
   }

   public void k(String var1, int var2) {
      int var3 = this.p(var1);
      if (var3 >= 0 && var2 >= 0 && var2 < 9) {
         this.C(var3, this.U[var2]);
      }
   }

   public void D(int var1, int var2) {
      if (var2 >= 0 && var2 < 9) {
         for (ab.i var4 : this.z) {
            if (var4.g == var1) {
               int var5 = b(var4.B) ? Integer.parseInt(var4.B.substring(6)) : var4.h;
               if (var5 >= 0) {
                  this.C(this.U[var2], var5);
               }

               return;
            }
         }
      }
   }

   private void C(int var1, int var2) {
      if (var1 >= 0 && var2 >= 0) {
         for (int var3 = 0; var3 < 9; var3++) {
            if (this.U[var3] == var1) {
               this.U[var3] = var2;
            } else if (this.U[var3] == var2) {
               this.U[var3] = var1;
            }
         }
      }
   }

   public void q(int[] var1) {
      this.z.clear();
      this.s.clear();
      int var2 = 0;

      while (var2 < 9) {
         this.U[var2] = var2++;
      }

      for (int var3 = 0; var3 < var1.length; var3++) {
         if (var1[var3] > 0) {
            this.z.add(new ab.i(var1[var3], "stock:" + var3));
         }
      }
   }

   private ab.i Z(String var1) {
      for (ab.i var3 : this.z) {
         if (var1.equals(var3.B)) {
            return var3;
         }
      }

      return null;
   }

   private static boolean b(String var0) {
      return var0 != null && var0.startsWith("stock:");
   }

   public int Z(String var1, int var2) {
      ab.i var3 = this.Z(var1);
      return var3 == null ? var2 : var3.g;
   }

   public boolean a() {
      for (ab.i var2 : this.z) {
         if (var2.K != var2.g && !b(var2.B)) {
            return true;
         }
      }

      return false;
   }

   public boolean Z() {
      for (int var1 = 0; var1 < this.U.length; var1++) {
         if (this.U[var1] != var1) {
            return true;
         }
      }

      return this.z.stream().anyMatch(var0 -> var0.K != var0.g);
   }

   public int b() {
      int var1 = 0;

      for (ab.i var3 : this.z) {
         if (var3.g == 2) {
            var1++;
         }
      }

      return var1;
   }

   public int w() {
      int var1 = 0;

      for (ab.i var3 : this.z) {
         if (var3.g == 2 && var3.B != null && !b(var3.B)) {
            var1++;
         }
      }

      return var1;
   }

   public void y(Map<String, Integer> var1, Set<String> var2, int var3, int var4, boolean var5) {
      this.L(var1, var2, () -> var3, var4, var5);
   }

   public boolean s() {
      return !this.z.isEmpty() && this.z.stream().allMatch(var0 -> b(var0.B));
   }

   public void d(Map<String, Integer> var1) {
      List var2 = this.z
         .stream()
         .filter(var0 -> var0.K == 1 && var0.W && var0.B != null && !b(var0.B))
         .sorted(Comparator.comparingInt(var0 -> var0.g == 1 ? 0 : 1))
         .toList();
      if (!var2.isEmpty()) {
         List var3 = this.z.stream().filter(var0 -> var0.K == 1 && b(var0.B)).toList();
         TreeSet var4 = new TreeSet();

         for (ab.i var6 : var2) {
            var4.add(var6.h);
         }

         for (ab.i var13 : var3) {
            var4.add(Integer.parseInt(var13.B.substring(6)));
         }

         List var12 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) < 1).toList();
         List var14 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) == 1).toList();
         if (var12.size() == var2.size() && var14.size() == var3.size()) {
            ab.i var7 = this.z.stream().filter(var0 -> var0.K == 2 && var0.g == 1 && b(var0.B)).findFirst().orElse(null);
            int var8 = var7 == null ? -1 : Integer.parseInt(var7.B.substring(6));

            for (ab.i var10 : var2) {
               if (var10.g == 2 && var8 >= 0) {
                  this.C(var10.h, var8);
               }
            }

            for (int var15 = 0; var15 < var3.size(); var15++) {
               ((ab.i)var3.get(var15)).B = "stock:" + var14.get(var15);
            }

            for (int var16 = 0; var16 < var2.size(); var16++) {
               ab.i var17 = (ab.i)var2.get(var16);
               var17.h = (Integer)var12.get(var16);
               var17.W = (boolean)0;
               if (var17.g == 2 && var8 >= 0) {
                  this.C(var17.h, var8);
               }
            }
         }
      }
   }

   public void L(Map<String, Integer> var1, Set<String> var2, IntSupplier var3, int var4, boolean var5) {
      if (var5) {
         for (ab.i var7 : this.z) {
            if (var7.B != null && !b(var7.B) && var7.h >= 0 && var7.W) {
               var7.W = (boolean)0;
               String var8 = "stock:" + var7.h;
               if (this.Z(var8) == null && var1.getOrDefault(var8, -1) == var7.K) {
                  for (ab.i var10 : this.z) {
                     if (b(var10.B) && var10.K == var7.K && var1.getOrDefault(var10.B, -1) < 1) {
                        int var11 = Integer.parseInt(var10.B.substring(6));
                        this.C(var7.h, var11);
                        var10.B = var8;
                        var7.h = var11;
                        break;
                     }
                  }
               }
            }
         }
      }

      for (ab.i var18 : this.z) {
         if (var18.B != null && (!b(var18.B) || var5) && var1.getOrDefault(var18.B, -1) != var18.K) {
            var18.k = b(var18.B);
            if (var18.k) {
               var18.h = Integer.parseInt(var18.B.substring(6));
               var18.W = (boolean)0;
            }

            var18.B = null;
         }
      }

      ArrayList var17 = new ArrayList(var1.keySet());
      var17.sort(Comparator.<String, Integer>comparing(var0 -> b(var0) ? 1 : 0).thenComparing(var0 -> (String)var0));

      for (String var20 : var17) {
         int var21 = (Integer)var1.get(var20);
         if (var21 >= 1 && this.Z(var20) == null) {
            ab.i var22 = null;

            for (ab.i var12 : this.z) {
               if (var12.B == null && var12.K == var21) {
                  if (b(var20) && var20.equals("stock:" + var12.h)) {
                     var22 = var12;
                     break;
                  }

                  if (var22 == null || var22.k && !var12.k) {
                     var22 = var12;
                  }
               }
            }

            if (var22 == null && !var5 && !b(var20)) {
               ArrayList var24 = new ArrayList();

               for (ab.i var13 : this.z) {
                  if (b(var13.B) && var13.K == var21) {
                     var24.add(var13);
                  }
               }

               if (!var24.isEmpty()) {
                  var22 = (ab.i)var24.get(Math.floorMod(var4, var24.size()));
                  var22.k = (boolean)1;
                  var22.W = (boolean)1;
                  var22.h = Integer.parseInt(var22.B.substring(6));
               }
            }

            if (var22 != null) {
               int var25 = var22.k && var2.contains(var20) ? 1 : 0;
               var22.B = var20;
               var22.k = (boolean)0;
               int var27 = var25 != 0 ? var3.getAsInt() : var22.g;
               if (var25 != 0 && var22.g != var27) {
                  ArrayList var28 = new ArrayList();

                  for (ab.i var15 : this.z) {
                     if (b(var15.B) && var15.g == var27) {
                        var28.add(var15);
                     }
                  }

                  if (!var28.isEmpty()) {
                     ab.i var29 = (ab.i)var28.get(Math.floorMod(var4, var28.size()));
                     this.C(var22.h, Integer.parseInt(var29.B.substring(6)));
                     int var30 = var22.g;
                     var22.g = var29.g;
                     var29.g = var30;
                  }
               }

               if (var25 != 0) {
                  if (this.s.size() >= 200) {
                     this.s.removeFirst();
                  }

                  this.s.addLast(new ab.x(var22.K, var22.g));
               }
            }
         }
      }
   }

   private static final class i {
      final int K;
      int g;
      String B;
      boolean k;
      int h = -1;
      boolean W;

      i(int var1, String var2) {
         this.K = var1;
         this.g = var1;
         this.B = var2;
      }
   }

   public record x(int actual, int display) {
   }
}
