package com.larpclient;

import net.minecraft.class_1109;
import net.minecraft.class_1113;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3419;
import net.minecraft.class_1113.class_1114;
import net.minecraft.class_1140.class_11518;

public final class tu {
   private static final class_2960 U = class_2960.method_60655("larp-client", "notification");
   private static boolean d;

   private tu() {
   }

   public static class_1109 d() {
      return new class_1109(U, class_3419.field_15250, 1.0F, 1.0F, class_1113.method_43221(), false, 0, class_1114.field_5478, 0.0, 0.0, 0.0, true);
   }

   public static void E() {
      class_310 var0 = class_310.method_1551();
      if (var0 != null) {
         if (!var0.method_18854()) {
            var0.execute(tu::E);
         } else if (!(var0.field_1690.method_71978(class_3419.field_15250) <= 0.0F)) {
            try {
               class_11518 var1 = var0.method_1483().method_4873(d());
               if (!d) {
                  d = (boolean)1;
                  q8.G("ESP", "Notification sound uses Minecraft Master volume; playback={}", var1);
               }
            } catch (RuntimeException var2) {
               q8.O("ESP", "Notification playback failed; alerts remain available, reload resources to retry", var2);
            }
         }
      }
   }
}
