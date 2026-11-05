package com.larpclient;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public final class e7 {
   public static final int E = 4;

   public static boolean o(BlockPos var0, int var1, e7.j var2) {
      int var3 = var1 == 1 ? 1 : 0;
      int var4 = var1 == 2 ? 1 : 0;
      int var5 = var1 == 0 ? 1 : 3;
      int var6 = 0;

      while (var6 < var5) {
         BlockPos var7 = var0.offset(var3 * var6, 0, var4 * var6);
         if (var2.K(var7) != e7.o.f) {
            return false;
         }

         if (var1 == 2 || var2.K(var7.north()) == e7.o.i && var2.K(var7.south()) == e7.o.i) {
            if (var1 == 1 || var2.K(var7.east()) == e7.o.i && var2.K(var7.west()) == e7.o.i) {
               var6++;
               continue;
            }

            return false;
         }

         return false;
      }

      return var1 == 0 || var2.K(var0.offset(-var3, 0, -var4)) == e7.o.i && var2.K(var0.offset(var3 * 3, 0, var4 * 3)) == e7.o.i;
   }

   public static int j(BlockPos var0, int var1, e7.j var2) {
      if (var0.getY() <= var1 - 3 && var2.K(var0) == e7.o.f) {
         for (int var3 = 0; var3 < 3; var3++) {
            byte var4 = 1;

            for (int var5 = 0; var5 < 4; var5++) {
               if (!o(var0.above(var5), var3, var2)) {
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

   public static AABB X(BlockPos var0, int var1) {
      return new AABB(var0.getX(), var0.getY(), var0.getZ(), var0.getX() + (var1 == 1 ? 3 : 1), var0.getY() + 4, var0.getZ() + (var1 == 2 ? 3 : 1));
   }

   private e7() {
   }

   @FunctionalInterface
   public interface j {
      e7.o K(BlockPos var1);
   }

   public enum o {
      f,
      i,
      K;
   }
}
