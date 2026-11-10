package com.larpclient;

import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_9334;

final class gk {
   private gk() {
   }

   static class_1799 j(class_1792 var0, class_1799 var1) {
      int var2 = var1 != null && !var1.method_7960() ? Math.max(1, var1.method_7947()) : 1;
      class_1799 var3 = new class_1799(var0, var2);
      class_2561 var4 = var1 == null ? null : (class_2561)var1.method_58694(class_9334.field_49631);
      if (var4 != null) {
         var3.method_57379(class_9334.field_49631, var4);
      }

      return var3;
   }

   static boolean i(class_1799 var0) {
      String var1 = var0.method_7964().getString().toLowerCase();
      if (!var1.contains("[host]") && !var1.contains("[viewer]")) {
         String var2 = var1.replaceAll("[^0-9]", "");
         if (var2.isEmpty()) {
            return false;
         }

         try {
            int var3 = Integer.parseInt(var2);
            return var3 >= 1 && var3 <= 10 || var3 == 11;
         } catch (NumberFormatException var4) {
            return false;
         }
      } else {
         return true;
      }
   }

   static boolean P(class_1799 var0, class_1792 var1, class_1792 var2) {
      return !var0.method_7960() && var0.method_31574(var1) && !i(var0) && var2 != var1;
   }
}
