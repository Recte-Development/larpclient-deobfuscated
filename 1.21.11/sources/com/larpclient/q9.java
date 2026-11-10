package com.larpclient;

import net.minecraft.class_2338;
import net.minecraft.class_238;

public final class q9 {
   public static final int u = 4;

   public static boolean i(class_2338 var0, int var1, q9.d var2) {
      int var3 = var1 == 1 ? 1 : 0;
      int var4 = var1 == 2 ? 1 : 0;
      int var5 = var1 == 0 ? 1 : 3;
      int var6 = 0;

      while (var6 < var5) {
         class_2338 var7 = var0.method_10069(var3 * var6, 0, var4 * var6);
         if (var2.z(var7) != q9.z.r) {
            return false;
         }

         if (var1 == 2 || var2.z(var7.method_10095()) == q9.z.S && var2.z(var7.method_10072()) == q9.z.S) {
            if (var1 == 1 || var2.z(var7.method_10078()) == q9.z.S && var2.z(var7.method_10067()) == q9.z.S) {
               var6++;
               continue;
            }

            return false;
         }

         return false;
      }

      return var1 == 0 || var2.z(var0.method_10069(-var3, 0, -var4)) == q9.z.S && var2.z(var0.method_10069(var3 * 3, 0, var4 * 3)) == q9.z.S;
   }

   public static int z(class_2338 var0, int var1, q9.d var2) {
      if (var0.method_10264() <= var1 - 3 && var2.z(var0) == q9.z.r) {
         for (int var3 = 0; var3 < 3; var3++) {
            byte var4 = 1;

            for (int var5 = 0; var5 < 4; var5++) {
               if (!i(var0.method_10086(var5), var3, var2)) {
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

   public static class_238 h(class_2338 var0, int var1) {
      return new class_238(
         var0.method_10263(),
         var0.method_10264(),
         var0.method_10260(),
         var0.method_10263() + (var1 == 1 ? 3 : 1),
         var0.method_10264() + 4,
         var0.method_10260() + (var1 == 2 ? 3 : 1)
      );
   }

   private q9() {
   }

   @FunctionalInterface
   public interface d {
      q9.z z(class_2338 var1);
   }

   public enum z {
      r,
      S,
      B;
   }
}
