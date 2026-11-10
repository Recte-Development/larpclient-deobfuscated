package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class oy {
   private static final int[][] k = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
   private static final int[][] i = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   public static List<oy.i> p(oy.o var0, int var1, int var2, oy.g var3, oy.y var4, int var5) {
      if (var3.u(var1, var2)) {
         return List.of();
      }

      HashMap var6 = new HashMap();
      ArrayDeque var7 = new ArrayDeque();
      long var8 = K(var1, var2);
      var6.put(var8, var8);
      var7.add(new long[]{var1, var2});

      while (!var7.isEmpty() && var6.size() <= var5) {
         long[] var10 = (long[])var7.poll();
         int var11 = (int)var10[0];
         int var12 = (int)var10[1];

         for (int[] var16 : k) {
            int var17 = var11 + var16[0];
            int var18 = var12 + var16[1];
            List var19 = s(var0, var3, var4, var6, var7, var11, var12, var17, var18, var8);
            if (var19 != null) {
               return var19;
            }
         }

         for (int[] var23 : i) {
            int var24 = var11 + var23[0];
            int var25 = var12 + var23[1];
            if (J(var0, var4, var11 + var23[0], var12) && J(var0, var4, var11, var12 + var23[1])) {
               List var26 = s(var0, var3, var4, var6, var7, var11, var12, var24, var25, var8);
               if (var26 != null) {
                  return var26;
               }
            }
         }
      }

      return null;
   }

   private static boolean J(oy.o var0, oy.y var1, int var2, int var3) {
      return var1.contains(var2, var3) && var0.v(var2, var3);
   }

   private static List<oy.i> s(
      oy.o var0, oy.g var1, oy.y var2, Map<Long, Long> var3, ArrayDeque<long[]> var4, int var5, int var6, int var7, int var8, long var9
   ) {
      long var11 = K(var7, var8);
      if (!var3.containsKey(var11) && J(var0, var2, var7, var8)) {
         var3.put(var11, K(var5, var6));
         if (var1.u(var7, var8)) {
            return K(var3, var11, var9);
         }

         var4.add(new long[]{var7, var8});
         return null;
      } else {
         return null;
      }
   }

   private static List<oy.i> K(Map<Long, Long> var0, long var1, long var3) {
      ArrayList var5 = new ArrayList();
      long var6 = var1;

      while (var6 != var3) {
         var5.add(new oy.i(I(var6), d(var6)));
         var6 = (Long)var0.get(var6);
      }

      Collections.reverse(var5);
      return var5;
   }

   static long K(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   static int I(long var0) {
      return (int)(var0 >> 32);
   }

   static int d(long var0) {
      return (int)var0;
   }

   private oy() {
   }

   public interface g {
      boolean u(int var1, int var2);
   }

   public record i(int x, int z) {
   }

   public interface o {
      boolean v(int var1, int var2);
   }

   public record y(int minX, int minZ, int maxX, int maxZ) {
      public boolean contains(int var1, int var2) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minZ && var2 <= this.maxZ;
      }
   }
}
