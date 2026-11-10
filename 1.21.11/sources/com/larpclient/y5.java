package com.larpclient;

import net.minecraft.class_2596;
import net.minecraft.class_2797;
import net.minecraft.class_7472;
import net.minecraft.class_9449;

public final class y5 {
   private y5() {
   }

   public static String A(class_2596<?> var0) {
      String var1 = switch (var0) {
         case class_7472 var4 -> var4.comp_808();
         case class_9449 var5 -> var5.comp_2532();
         case class_2797 var6 -> var6.comp_945().strip().startsWith("/") ? var6.comp_945() : null;
         default -> null;
      };
      return qy.t(var1) ? var1 : null;
   }
}
