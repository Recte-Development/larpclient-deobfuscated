package com.larpclient;

import java.util.Optional;
import net.minecraft.class_238;
import net.minecraft.class_243;

public final class ug {
   public static Optional<class_243> p(class_243 var0, class_243 var1, class_238 var2, double var3) {
      if (!Double.isFinite(var3) || var3 < 0.0) {
         return Optional.empty();
      } else {
         return var2.method_1006(var0) ? Optional.of(var0) : var2.method_992(var0, var0.method_1019(var1.method_1029().method_1021(var3)));
      }
   }

   public static double L(class_243 var0, class_238 var1) {
      return var0.method_1022(
         new class_243(
            Math.clamp(var0.field_1352, var1.field_1323, var1.field_1320),
            Math.clamp(var0.field_1351, var1.field_1322, var1.field_1325),
            Math.clamp(var0.field_1350, var1.field_1321, var1.field_1324)
         )
      );
   }

   public static boolean U(class_243 var0, class_243 var1, class_238 var2) {
      double var3 = var0.method_1025(var1);
      if (var2.method_1006(var0)) {
         return var3 > 1.0E-7;
      }

      Optional var5 = var2.method_992(var0, var1);
      return var5.isPresent() && var0.method_1025((class_243)var5.get()) + 1.0E-7 < var3;
   }

   private ug() {
   }
}
