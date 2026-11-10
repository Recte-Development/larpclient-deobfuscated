package com.larpclient;

import net.minecraft.class_2561;
import net.minecraft.class_310;

public final class b1 {
   private b1() {
   }

   public static void q(class_2561 var0, boolean var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null) {
         if (var1) {
            var2.field_1705.method_1758(var0, false);
         } else {
            var2.method_44714().method_44736(var0, false);
         }
      }
   }
}
