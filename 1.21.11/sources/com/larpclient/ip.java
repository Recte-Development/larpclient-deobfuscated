package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_238;
import net.minecraft.class_265;

public record ip(class_238 bounds, List<ip.o> edges, List<class_238> boxes) {
   public static final int MAX_EDGES = 128;
   public static final int MAX_BOXES = 32;

   public static ip build(class_265 var0) {
      if (var0 != null && !var0.method_1110()) {
         class_238 var1 = var0.method_1107();
         ArrayList var2 = new ArrayList();
         boolean[] var3 = new boolean[]{false};
         var0.method_1104(
            (var3x, var5, var7, var9, var11, var13) -> {
               if (var2.size() >= 128) {
                  var3[0] = (boolean)1;
               } else {
                  var2.add(
                     new ip.o(
                        normal(var3x, var1.field_1323, var1.field_1320),
                        normal(var5, var1.field_1322, var1.field_1325),
                        normal(var7, var1.field_1321, var1.field_1324),
                        normal(var9, var1.field_1323, var1.field_1320),
                        normal(var11, var1.field_1322, var1.field_1325),
                        normal(var13, var1.field_1321, var1.field_1324)
                     )
                  );
               }
            }
         );
         ArrayList var4 = new ArrayList();
         var0.method_1089(
            (var3x, var5, var7, var9, var11, var13) -> {
               if (var4.size() >= 32) {
                  var3[0] = (boolean)1;
               } else {
                  var4.add(
                     new class_238(
                        normal(var3x, var1.field_1323, var1.field_1320),
                        normal(var5, var1.field_1322, var1.field_1325),
                        normal(var7, var1.field_1321, var1.field_1324),
                        normal(var9, var1.field_1323, var1.field_1320),
                        normal(var11, var1.field_1322, var1.field_1325),
                        normal(var13, var1.field_1321, var1.field_1324)
                     )
                  );
               }
            }
         );
         return !var3[0] && !var2.isEmpty() ? new ip(var1, List.copyOf(var2), List.copyOf(var4)) : null;
      } else {
         return null;
      }
   }

   private static double normal(double var0, double var2, double var4) {
      return var4 - var2 < 1.0E-8 ? 0.5 : (var0 - var2) / (var4 - var2);
   }

   public static double map(double var0, double var2, double var4) {
      return var2 + var0 * (var4 - var2);
   }

   public record o(double ax, double ay, double az, double bx, double by, double bz) {
   }
}
