package com.larpclient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class wy {
   public static long y(int var0, int var1, int var2) {
      return (var0 & 67108863L) << 38 | (var2 & 67108863L) << 12 | var1 & 4095;
   }

   public static List<wy.s> k(fx var0, int var1, int var2) {
      return B(var0, var1, var2, false);
   }

   public static List<wy.s> B(fx var0, int var1, int var2, boolean var3) {
      fx.i var4 = var0.V(var1);
      int var5 = var4.bottom();
      int var6 = var4.top();
      fx.o var7 = var0.X();
      boolean var8 = var0.l();
      int var9 = var8 ? var7.minX() : var7.minZ();
      int var10 = var8 ? var7.maxX() : var7.maxZ();
      ArrayList var11 = new ArrayList();
      HashSet var12 = new HashSet();
      int[] var13 = new int[]{Integer.MIN_VALUE};
      int var14 = var0.w();
      int var15 = var2 - var5;

      for (int var16 = 0; var16 < var14; var16++) {
         int[] var17 = var0.O(var16, var1);
         int var18 = var0.b(var16, var1);
         int var19 = var3 ? -var0.l(var16, var1) : var0.l(var16, var1);
         int var20 = var19 > 0 ? var9 : var10;
         int var21 = var19 > 0 ? var10 : var9;
         int var22 = Math.abs(var21 - var20) + 1;
         boolean var23 = var0.S(var16, var1);
         int var24 = 0;
         if (var16 == 0) {
            if (var15 > 0 && var2 <= var6 + 1 && var22 >= var15 + 1) {
               for (int var34 = 1; var34 <= var15; var34++) {
                  int var37 = var2 + 1 - var34;
                  ArrayList var38 = new ArrayList(3);

                  for (int var39 = var37 + 1; var39 >= var37 - 1; var39--) {
                     if (var39 >= var5 && var39 <= var6) {
                        var38.add(W(var8, var20 + var34 * var19, var39, var18));
                     }
                  }

                  var11.add(f(wy.f.Z, var8, var20 + (var34 - 1) * var19, var37, var18, var19, 0, var38, false));
               }

               for (int var35 = var15 - 1; var35 >= 0; var35--) {
                  var11.add(c(wy.f.S, var8, var20 + var35 * var19, -var19, var17, var18, var5, var6, var23, var12, var13));
               }

               var24 = var15;
            }
         } else {
            int[] var25 = var0.O(var16 - 1, var1);
            int var26 = var17[0] > var25[0] ? 1 : -1;
            int var27 = var26 > 0 ? var17[0] : var17[1];
            int var28 = var26 > 0 ? var17[1] : var17[0];

            for (int var29 = var27; var29 != var28 + var26; var29 += var26) {
               int var30 = var26 > 0 ? Math.min(var29 - 1, var18) : Math.max(var29 + 1, var18);
               ArrayList var31 = new ArrayList();

               for (int var32 = var6; var32 >= var5; var32--) {
                  wy.y var33 = W(var8, var20, var32, var29);
                  if (var12.add(var33.key())) {
                     var31.add(var33);
                  }
               }

               var13[0] = var29;
               var11.add(f(wy.f.H, var8, var20, var5, var30, 0, var26, var31, false));
            }

            var24 = 1;
         }

         for (int var36 = var24; var36 < var22; var36++) {
            var11.add(c(wy.f.D, var8, var20 + var36 * var19, var19, var17, var18, var5, var6, var23, var12, var13));
         }
      }

      return Collections.unmodifiableList(var11);
   }

   private static wy.s c(wy.f var0, boolean var1, int var2, int var3, int[] var4, int var5, int var6, int var7, boolean var8, Set<Long> var9, int[] var10) {
      ArrayList var11 = new ArrayList();
      int var12 = var10[0] != Integer.MIN_VALUE && Math.abs(var10[0] - var4[0]) > Math.abs(var10[0] - var4[1]) ? 0 : 1;

      for (int var13 = var7; var13 >= var6; var13--) {
         for (int var14 = 0; var14 <= var4[1] - var4[0]; var14++) {
            int var15 = var12 != 0 ? var4[0] + var14 : var4[1] - var14;
            wy.y var16 = W(var1, var2, var13, var15);
            if (var9.add(var16.key())) {
               var11.add(var16);
            }

            var10[0] = var15;
         }

         var12 = var12 == 0 ? 1 : 0;
      }

      return f(var0, var1, var2 - var3, var6, var5, var3, 0, var11, var8);
   }

   private static wy.s f(wy.f var0, boolean var1, int var2, int var3, int var4, int var5, int var6, List<wy.y> var7, boolean var8) {
      int var9 = var1 ? var2 : var4;
      int var10 = var1 ? var4 : var2;
      int var11 = var1 ? var5 : var6;
      int var12 = var1 ? var6 : var5;
      return new wy.s(var0, var9, var3, var10, var11, var12, List.copyOf(var7), var8);
   }

   private static wy.y W(boolean var0, int var1, int var2, int var3) {
      return var0 ? new wy.y(var1, var2, var3) : new wy.y(var3, var2, var1);
   }

   public static wy.s x(List<wy.s> var0, Collection<Long> var1, int var2, int var3) {
      wy.s var4 = null;
      byte var5 = 0;
      int var6 = Integer.MAX_VALUE;

      for (wy.s var8 : var0) {
         byte var9 = var1.contains(bg.Y(var8.standX(), var8.standZ()));
         int var10 = Math.abs(var8.standX() - var2) + Math.abs(var8.standZ() - var3);
         if (var4 == null || var9 != 0 && var5 == 0 || var9 == var5 && var10 < var6) {
            var4 = var8;
            var5 = var9;
            var6 = var10;
         }
      }

      return var4;
   }

   public static List<wy.a> Z(Collection<Long> var0, int var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();
      int[][] var5 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

      for (long var7 : var0) {
         int var9 = bg.S(var7);
         int var10 = bg.R(var7);
         int var11 = Math.abs(var9 - var1) + Math.abs(var10 - var2);

         for (int[] var15 : var5) {
            int var16 = var9 + var15[0];
            int var17 = var10 + var15[1];
            if (Math.abs(var16 - var1) + Math.abs(var17 - var2) < var11 && !var0.contains(bg.Y(var16, var17))) {
               var4.add(new wy.a(var9, var10, var15[0], var15[1]));
            }
         }
      }

      var4.sort(
         Comparator.<wy.a>comparingInt(var2x -> Math.abs(var2x.x() + var2x.dx() - var1) + Math.abs(var2x.z() + var2x.dz() - var2))
            .thenComparingInt(var2x -> Math.min(Math.abs(var2x.x() + var2x.dx() - var1), Math.abs(var2x.z() + var2x.dz() - var2)))
            .thenComparingInt(wy.a::x)
            .thenComparingInt(wy.a::z)
            .thenComparingInt(wy.a::dx)
            .thenComparingInt(wy.a::dz)
      );
      return var4.size() > var3 ? List.copyOf(var4.subList(0, var3)) : List.copyOf(var4);
   }

   private wy() {
   }

   public record a(int x, int z, int dx, int dz) {
   }

   public enum f {
      Z,
      S,
      D,
      H;
   }

   public record s(wy.f kind, int standX, int standY, int standZ, int dx, int dz, List<wy.y> cells, boolean drill) {
      public wy.y drillCentre() {
         return new wy.y(this.standX + this.dx, this.standY + 1, this.standZ + this.dz);
      }
   }

   public record y(int x, int y, int z) {
      public long key() {
         return wy.y(this.x, this.y, this.z);
      }
   }
}
