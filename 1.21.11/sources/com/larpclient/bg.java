package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class bg {
   private static final int[][] H = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
   private static final int[][] v = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   public static List<bg.o> R(bg.z var0, int var1, int var2, bg.i var3, bg.t var4, int var5) {
      if (var3.n(var1, var2)) {
         return List.of();
      }

      HashMap var6 = new HashMap();
      ArrayDeque var7 = new ArrayDeque();
      long var8 = Y(var1, var2);
      var6.put(var8, var8);
      var7.add(new long[]{var1, var2});

      while (!var7.isEmpty() && var6.size() <= var5) {
         long[] var10 = (long[])var7.poll();
         int var11 = (int)var10[0];
         int var12 = (int)var10[1];

         for (int[] var16 : H) {
            int var17 = var11 + var16[0];
            int var18 = var12 + var16[1];
            List var19 = R(var0, var3, var4, var6, var7, var11, var12, var17, var18, var8);
            if (var19 != null) {
               return var19;
            }
         }

         for (int[] var23 : v) {
            int var24 = var11 + var23[0];
            int var25 = var12 + var23[1];
            if (b(var0, var4, var11 + var23[0], var12) && b(var0, var4, var11, var12 + var23[1])) {
               List var26 = R(var0, var3, var4, var6, var7, var11, var12, var24, var25, var8);
               if (var26 != null) {
                  return var26;
               }
            }
         }
      }

      return null;
   }

   private static boolean b(bg.z var0, bg.t var1, int var2, int var3) {
      return var1.contains(var2, var3) && var0.y(var2, var3);
   }

   private static List<bg.o> R(
      bg.z var0, bg.i var1, bg.t var2, Map<Long, Long> var3, ArrayDeque<long[]> var4, int var5, int var6, int var7, int var8, long var9
   ) {
      long var11 = Y(var7, var8);
      if (!var3.containsKey(var11) && b(var0, var2, var7, var8)) {
         var3.put(var11, Y(var5, var6));
         if (var1.n(var7, var8)) {
            return e(var3, var11, var9);
         }

         var4.add(new long[]{var7, var8});
         return null;
      } else {
         return null;
      }
   }

   private static List<bg.o> e(Map<Long, Long> var0, long var1, long var3) {
      ArrayList var5 = new ArrayList();
      long var6 = var1;

      while (var6 != var3) {
         var5.add(new bg.o(S(var6), R(var6)));
         var6 = (Long)var0.get(var6);
      }

      Collections.reverse(var5);
      return var5;
   }

   static long Y(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   static int S(long var0) {
      return (int)(var0 >> 32);
   }

   static int R(long var0) {
      return (int)var0;
   }

   private bg() {
   }

   public interface i {
      boolean n(int var1, int var2);
   }

   public record o(int x, int z) {
   }

   public record t(int minX, int minZ, int maxX, int maxZ) {
      public boolean contains(int var1, int var2) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minZ && var2 <= this.maxZ;
      }
   }

   public interface z {
      boolean y(int var1, int var2);
   }
}
