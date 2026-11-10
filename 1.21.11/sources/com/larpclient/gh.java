package com.larpclient;

import net.minecraft.class_238;
import net.minecraft.class_243;

public record gh(class_243 point, class_243 normal, class_243 ring, class_243 ringNormal, double groundY) {
   public gh(class_243 var1, class_243 var2, class_243 var3, class_243 var4) {
      this(var1, var2, var3, var4, Double.NaN);
   }

   public static gh sample(class_238 var0, class_243 var1, class_243 var2, i3.k var3) {
      class_243 var4 = var0.method_1005();
      class_243 var5 = var2.method_1027() < 1.0E-8 ? var4.method_1020(var1).method_1029() : var2.method_1029();
      class_243 var6 = var0.method_992(var1, var1.method_1019(var5.method_1021(64.0)))
         .orElseGet(
            () -> new class_243(
               Math.clamp(var1.field_1352, var0.field_1323, var0.field_1320),
               Math.clamp(var1.field_1351, var0.field_1322, var0.field_1325),
               Math.clamp(var1.field_1350, var0.field_1321, var0.field_1324)
            )
         );
      class_243 var7 = var6.method_1020(var4).method_1029();
      if (var7.method_1027() < 1.0E-8) {
         var7 = new class_243(0.0, 1.0, 0.0);
      }

      class_243 var8 = new class_243(var4.field_1352, var0.field_1322, var4.field_1350);
      i3.j var9 = var3.U(var8.method_1031(0.0, 0.25, 0.0), var8.method_1031(0.0, -4.0, 0.0));
      return var9 != null && var9.normal().field_1351 > 0.5
         ? new gh(
            var6.method_1019(var7.method_1021(0.025)), var7, var9.point().method_1019(var9.normal().method_1021(0.035)), var9.normal(), var9.point().field_1351
         )
         : new gh(var6.method_1019(var7.method_1021(0.025)), var7, var6.method_1019(var7.method_1021(0.04)), var7);
   }
}
