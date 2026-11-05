package com.larpclient;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public final class za {
   public static final int C = 4;

   public static boolean f(BlockPos var0, int var1, za.d var2) {
      int var3 = var1 == 1 ? 1 : 0;
      int var4 = var1 == 2 ? 1 : 0;
      int var5 = var1 == 0 ? 1 : 3;
      int var6 = 0;

      while (var6 < var5) {
         BlockPos var7 = var0.offset(var3 * var6, 0, var4 * var6);
         if (var2.G(var7) != za.y.B) {
            return false;
         }

         if (var1 == 2 || var2.G(var7.north()) == za.y.z && var2.G(var7.south()) == za.y.z) {
            if (var1 == 1 || var2.G(var7.east()) == za.y.z && var2.G(var7.west()) == za.y.z) {
               var6++;
               continue;
            }

            return false;
         }

         return false;
      }

      return var1 == 0 || var2.G(var0.offset(-var3, 0, -var4)) == za.y.z && var2.G(var0.offset(var3 * 3, 0, var4 * 3)) == za.y.z;
   }

   public static int s(BlockPos var0, int var1, za.d var2) {
      if (var0.getY() <= var1 - 3 && var2.G(var0) == za.y.B) {
         for (int var3 = 0; var3 < 3; var3++) {
            byte var4 = 1;

            for (int var5 = 0; var5 < 4; var5++) {
               if (!f(var0.above(var5), var3, var2)) {
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

   public static AABB A(BlockPos var0, int var1) {
      return new AABB(var0.getX(), var0.getY(), var0.getZ(), var0.getX() + (var1 == 1 ? 3 : 1), var0.getY() + 4, var0.getZ() + (var1 == 2 ? 3 : 1));
   }

   private za() {
   }

   @FunctionalInterface
   public interface d {
      za.y G(BlockPos var1);
   }

   public enum y {
      B,
      z,
      b;
   }
}
