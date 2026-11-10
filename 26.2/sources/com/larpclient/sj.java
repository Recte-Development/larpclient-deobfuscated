package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class sj {
   private static final int[][] t = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
   private static final int[][] x = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   public static List<sj.e> u(sj.l var0, int var1, int var2, sj.q var3, sj.o var4, int var5) {
      if (var3.k(var1, var2)) {
         return List.of();
      }

      HashMap var6 = new HashMap();
      ArrayDeque var7 = new ArrayDeque();
      long var8 = F(var1, var2);
      var6.put(var8, var8);
      var7.add(new long[]{var1, var2});

      while (!var7.isEmpty() && var6.size() <= var5) {
         long[] var10 = (long[])var7.poll();
         int var11 = (int)var10[0];
         int var12 = (int)var10[1];

         for (int[] var16 : t) {
            int var17 = var11 + var16[0];
            int var18 = var12 + var16[1];
            List var19 = v(var0, var3, var4, var6, var7, var11, var12, var17, var18, var8);
            if (var19 != null) {
               return var19;
            }
         }

         for (int[] var23 : x) {
            int var24 = var11 + var23[0];
            int var25 = var12 + var23[1];
            if (f(var0, var4, var11 + var23[0], var12) && f(var0, var4, var11, var12 + var23[1])) {
               List var26 = v(var0, var3, var4, var6, var7, var11, var12, var24, var25, var8);
               if (var26 != null) {
                  return var26;
               }
            }
         }
      }

      return null;
   }

   private static boolean f(sj.l var0, sj.o var1, int var2, int var3) {
      return var1.contains(var2, var3) && var0.h(var2, var3);
   }

   private static List<sj.e> v(
      sj.l var0, sj.q var1, sj.o var2, Map<Long, Long> var3, ArrayDeque<long[]> var4, int var5, int var6, int var7, int var8, long var9
   ) {
      long var11 = F(var7, var8);
      if (!var3.containsKey(var11) && f(var0, var2, var7, var8)) {
         var3.put(var11, F(var5, var6));
         if (var1.k(var7, var8)) {
            return X(var3, var11, var9);
         }

         var4.add(new long[]{var7, var8});
         return null;
      } else {
         return null;
      }
   }

   private static List<sj.e> X(Map<Long, Long> var0, long var1, long var3) {
      ArrayList var5 = new ArrayList();
      long var6 = var1;

      while (var6 != var3) {
         var5.add(new sj.e(F(var6), o(var6)));
         var6 = (Long)var0.get(var6);
      }

      Collections.reverse(var5);
      return var5;
   }

   static long F(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   static int F(long var0) {
      return (int)(var0 >> 32);
   }

   static int o(long var0) {
      return (int)var0;
   }

   private sj() {
   }

   public record e(int x, int z) {
   }

   public interface l {
      boolean h(int var1, int var2);
   }

   public record o(int minX, int minZ, int maxX, int maxZ) {
      public boolean contains(int var1, int var2) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minZ && var2 <= this.maxZ;
      }
   }

   public interface q {
      boolean k(int var1, int var2);
   }
}
