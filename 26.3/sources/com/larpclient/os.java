package com.larpclient;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public final class os {
   public static final int i = 4;

   public static boolean T(BlockPos var0, int var1, os.c var2) {
      int var3 = var1 == 1 ? 1 : 0;
      int var4 = var1 == 2 ? 1 : 0;
      int var5 = var1 == 0 ? 1 : 3;
      int var6 = 0;

      while (var6 < var5) {
         BlockPos var7 = var0.offset(var3 * var6, 0, var4 * var6);
         if (var2.j(var7) != os.b.N) {
            return false;
         }

         if (var1 == 2 || var2.j(var7.north()) == os.b.c && var2.j(var7.south()) == os.b.c) {
            if (var1 == 1 || var2.j(var7.east()) == os.b.c && var2.j(var7.west()) == os.b.c) {
               var6++;
               continue;
            }

            return false;
         }

         return false;
      }

      return var1 == 0 || var2.j(var0.offset(-var3, 0, -var4)) == os.b.c && var2.j(var0.offset(var3 * 3, 0, var4 * 3)) == os.b.c;
   }

   public static int d(BlockPos var0, int var1, os.c var2) {
      if (var0.getY() <= var1 - 3 && var2.j(var0) == os.b.N) {
         for (int var3 = 0; var3 < 3; var3++) {
            byte var4 = 1;

            for (int var5 = 0; var5 < 4; var5++) {
               if (!T(var0.above(var5), var3, var2)) {
                  var4 = 0;
                  break;
               }
            }

            if (var4 != 0) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   public static AABB M(BlockPos var0, int var1) {
      return new AABB(var0.getX(), var0.getY(), var0.getZ(), var0.getX() + (var1 == 1 ? 3 : 1), var0.getY() + 4, var0.getZ() + (var1 == 2 ? 3 : 1));
   }

   private os() {
   }

   public enum b {
      N,
      c,
      T;
   }

   @FunctionalInterface
   public interface c {
      os.b j(BlockPos var1);
   }
}
