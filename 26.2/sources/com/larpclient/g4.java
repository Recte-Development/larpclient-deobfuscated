package com.larpclient;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public final class g4 {
   public static final int b = 4;

   public static boolean t(BlockPos var0, int var1, g4.a var2) {
      int var3 = var1 == 1 ? 1 : 0;
      int var4 = var1 == 2 ? 1 : 0;
      int var5 = var1 == 0 ? 1 : 3;
      int var6 = 0;

      while (var6 < var5) {
         BlockPos var7 = var0.offset(var3 * var6, 0, var4 * var6);
         if (var2.Q(var7) != g4.u.M) {
            return false;
         }

         if (var1 == 2 || var2.Q(var7.north()) == g4.u.O && var2.Q(var7.south()) == g4.u.O) {
            if (var1 == 1 || var2.Q(var7.east()) == g4.u.O && var2.Q(var7.west()) == g4.u.O) {
               var6++;
               continue;
            }

            return false;
         }

         return false;
      }

      return var1 == 0 || var2.Q(var0.offset(-var3, 0, -var4)) == g4.u.O && var2.Q(var0.offset(var3 * 3, 0, var4 * 3)) == g4.u.O;
   }

   public static int K(BlockPos var0, int var1, g4.a var2) {
      if (var0.getY() <= var1 - 3 && var2.Q(var0) == g4.u.M) {
         for (int var3 = 0; var3 < 3; var3++) {
            byte var4 = 1;

            for (int var5 = 0; var5 < 4; var5++) {
               if (!t(var0.above(var5), var3, var2)) {
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

   public static AABB t(BlockPos var0, int var1) {
      return new AABB(var0.getX(), var0.getY(), var0.getZ(), var0.getX() + (var1 == 1 ? 3 : 1), var0.getY() + 4, var0.getZ() + (var1 == 2 ? 3 : 1));
   }

   private g4() {
   }

   @FunctionalInterface
   public interface a {
      g4.u Q(BlockPos var1);
   }

   public enum u {
      M,
      O,
      Z;
   }
}
