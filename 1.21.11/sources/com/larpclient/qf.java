package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class qf {
   public static final double Z = 5.0;

   public static float Q(class_1309 var0, class_2338 var1, class_2338 var2) {
      return var0 instanceof class_1657 var3 && var3.method_31549().field_7480 ? 0.0F : q(var0, var1, W(var0, var1, var2));
   }

   public static float q(class_1309 var0, class_2338 var1, float var2) {
      if (var0 instanceof class_1657 var3 && var3.method_31549().field_7480) {
         return 0.0F;
      } else {
         double var5 = var0.method_73189().method_1022(class_243.method_24953(var1));
         return var5 > 10.0 ? 0.0F : hw.X(var0, hw.d(var5, var2, 5.0));
      }
   }

   public static float W(class_1309 var0, class_2338 var1, class_2338 var2) {
      class_243 var3 = class_243.method_24953(var1);
      if (var0.method_73189().method_1022(var3) > 10.0) {
         return 0.0F;
      }

      class_1937 var4 = var0.method_73183();
      return p(
         var3,
         var0.method_5829(),
         var1,
         var2,
         (var2x, var3x) -> var4.method_17742(new class_3959(var2x, var3x, class_3960.field_17558, class_242.field_1348, var0))
      );
   }

   public static List<class_2338> n(class_2338 var0, class_243 var1) {
      class_243 var2 = var1.method_1020(class_243.method_24953(var0));
      ArrayList var3 = new ArrayList(26);

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            for (int var6 = -1; var6 <= 1; var6++) {
               if ((var4 != 0 || var5 != 0 || var6 != 0) && var4 * var2.field_1352 + var5 * var2.field_1351 + var6 * var2.field_1350 > 0.0) {
                  var3.add(var0.method_10069(var4, var5, var6));
               }
            }
         }
      }

      return var3;
   }

   public static float p(class_243 var0, class_238 var1, class_2338 var2, class_2338 var3, BiFunction<class_243, class_243, class_3965> var4) {
      double var5 = 1.0 / ((var1.field_1320 - var1.field_1323) * 2.0 + 1.0);
      double var7 = 1.0 / ((var1.field_1325 - var1.field_1322) * 2.0 + 1.0);
      double var9 = 1.0 / ((var1.field_1324 - var1.field_1321) * 2.0 + 1.0);
      if (!(var5 < 0.0) && !(var7 < 0.0) && !(var9 < 0.0)) {
         double var11 = (1.0 - Math.floor(1.0 / var5) * var5) / 2.0;
         double var13 = (1.0 - Math.floor(1.0 / var9) * var9) / 2.0;
         class_238 var15 = var3 == null ? null : new class_238(var3);
         int var16 = 0;
         int var17 = 0;

         for (double var18 = 0.0; var18 <= 1.0; var18 += var5) {
            for (double var20 = 0.0; var20 <= 1.0; var20 += var7) {
               for (double var22 = 0.0; var22 <= 1.0; var22 += var9) {
                  class_243 var24 = new class_243(
                     class_3532.method_16436(var18, var1.field_1323, var1.field_1320) + var11,
                     class_3532.method_16436(var20, var1.field_1322, var1.field_1325),
                     class_3532.method_16436(var22, var1.field_1321, var1.field_1324) + var13
                  );
                  class_3965 var25 = (class_3965)var4.apply(var24, var0);
                  int var26 = var25.method_17783() == class_240.field_1333 ? 1 : 0;
                  if ((var26 != 0 || var25.method_17777().equals(var2))
                     && (var15 == null || !var15.method_1006(var24) && var15.method_992(var24, var26 != 0 ? var0 : var25.method_17784()).isEmpty())) {
                     var16++;
                  }

                  var17++;
               }
            }
         }

         return (float)var16 / var17;
      } else {
         return 0.0F;
      }
   }

   private qf() {
   }
}
