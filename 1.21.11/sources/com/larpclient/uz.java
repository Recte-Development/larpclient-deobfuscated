package com.larpclient;

import java.util.List;
import java.util.Locale;
import net.minecraft.class_310;
import net.minecraft.class_634;

public final class uz {
   private static final List<String> G = List.of("paper", "purpur", "folia", "pufferfish", "leaves", "gale", "leaf");

   public static boolean Z(String var0) {
      if (var0 == null) {
         return false;
      }

      String var1 = var0.toLowerCase(Locale.ROOT);

      for (String var3 : G) {
         if (var1.contains(var3)) {
            return true;
         }
      }

      return false;
   }

   public static String i() {
      class_634 var0 = class_310.method_1551().method_1562();
      return var0 == null ? null : var0.method_52790();
   }

   public static boolean l() {
      return Z(i());
   }

   private uz() {
   }
}
