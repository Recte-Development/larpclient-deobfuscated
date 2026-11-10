package com.larpclient;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_2338;
import net.minecraft.class_310;
import net.minecraft.class_638;

public final class u2 {
   private static Object I;
   private static final Map<class_2338, Long> M = new HashMap<>();

   private static void q() {
      class_638 var0 = class_310.method_1551().field_1687;
      if (var0 != I) {
         I = var0;
         M.clear();
      }

      long var1 = System.currentTimeMillis();
      M.values().removeIf(var2 -> var2 < var1);
   }

   public static void B(class_2338 var0) {
      q();
      M.put(var0.method_10062(), System.currentTimeMillis() + 10000L);
   }

   public static void f() {
      q();
      class_310 var0 = class_310.method_1551();
      if (var0.field_1687 != null && !M.isEmpty()) {
         for (class_1297 var2 : var0.field_1687.method_18112()) {
            if (var2 instanceof class_1542 var3) {
               X(var3);
            }
         }
      }
   }

   public static void X(class_1542 var0) {
      q();
      if (var0 != null && cz.T().V(var0.method_6983().method_7909()) != null) {
         for (class_2338 var2 : M.keySet()) {
            if (var0.method_5649(var2.method_10263() + 0.5, var2.method_10264() + 0.5, var2.method_10260() + 0.5) < 4.0) {
               jn.Y(var0.method_6983(), true);
               return;
            }
         }
      }
   }
}
