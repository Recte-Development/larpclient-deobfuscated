package com.larpclient;

import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

final class ss {
   static boolean B(class_1799 var0, class_1799 var1) {
      return d(D(var0), var0.method_7960() ? 0 : 1, D(var1), var1.method_7960() ? 0 : 1, class_1799.method_31577(var0, var1));
   }

   static boolean h(class_1799 var0, class_1799 var1) {
      return d(D(var0), var0.method_7947(), D(var1), var1.method_7947(), class_1799.method_31577(var0, var1));
   }

   static boolean d(int var0, int var1, int var2, int var3, boolean var4) {
      if (var1 > 0 && var3 > 0) {
         if (var1 != var3) {
            return false;
         } else {
            return var0 >= 0 ? var0 == var2 : var2 < 0 && var4;
         }
      } else {
         return var1 <= 0 && var3 <= 0;
      }
   }

   private static int D(class_1799 var0) {
      class_9209 var1 = var0.method_31574(class_1802.field_8204) ? (class_9209)var0.method_58694(class_9334.field_49646) : null;
      return var1 == null ? -1 : var1.comp_2315();
   }
}
