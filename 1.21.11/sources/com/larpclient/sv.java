package com.larpclient;

import java.util.function.Function;
import java.util.function.ToIntFunction;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;

public final class sv {
   public static boolean m(class_2338 var0, ToIntFunction<class_2338> var1, Function<class_2338, class_2680> var2) {
      int var3 = var1.applyAsInt(var0);
      if (var3 >= 3 && var3 <= 5 && !H((class_2680)var2.apply(var0))) {
         byte var4 = 0;

         for (class_2350 var8 : class_2350.values()) {
            class_2338 var9 = var0.method_10093(var8);
            int var10 = var1.applyAsInt(var9);
            if (var10 < 0 || var10 > var3 || H((class_2680)var2.apply(var9))) {
               return false;
            }

            var4 |= var10 == var3 - 1 ? 1 : 0;
         }

         return (boolean)var4;
      } else {
         return false;
      }
   }

   private static boolean H(class_2680 var0) {
      return var0.method_26213() > 0 || var0.method_27852(class_2246.field_10092) || var0.method_27852(class_2246.field_22423);
   }

   private sv() {
   }
}
