package com.larpclient.module;

import java.util.Optional;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1675;
import net.minecraft.class_1799;
import net.minecraft.class_1839;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_746;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class f {
   private f() {
   }

   public static double d(class_746 var0, class_238 var1) {
      class_243 var2 = var0.method_33571();
      class_243 var3 = new class_243(
         class_3532.method_15350(var2.field_1352, var1.field_1323, var1.field_1320),
         class_3532.method_15350(var2.field_1351, var1.field_1322, var1.field_1325),
         class_3532.method_15350(var2.field_1350, var1.field_1321, var1.field_1324)
      );
      return var3.method_1020(var2).method_1033();
   }

   public static boolean m(class_746 var0) {
      return var0 != null && var0.method_73183() != null
         ? !var0.method_6059(class_1294.field_5902)
            && !var0.method_6059(class_1294.field_5919)
            && !var0.method_5799()
            && !var0.method_5771()
            && !var0.method_31549().field_7479
            && !var0.method_6128()
            && !var0.method_6101()
            && !var0.method_5765()
         : false;
   }

   public static boolean c(class_746 var0) {
      if (!m(var0)) {
         return false;
      } else {
         return i() ? !var0.method_24828() : var0.field_6017 > 0.0 && !var0.method_24828();
      }
   }

   public static boolean n(class_746 var0, class_238 var1, float var2, float var3, double var4, boolean var6) {
      class_243 var7 = var0.method_33571();
      class_243 var8 = a_.w(var2, var3);
      class_243 var9 = var7.method_1019(var8.method_1021(var4 + 1.0));
      double var10 = var4;
      if (!var6) {
         class_3965 var12 = var0.method_73183().method_17742(new class_3959(var7, var9, class_3960.field_17558, class_242.field_1348, var0));
         if (var12.method_17783() != class_240.field_1333) {
            var10 = var7.method_1022(var12.method_17784());
         }
      }

      class_243 var13 = var7.method_1019(var8.method_1021(var10));
      return var1.method_992(var7, var13).isPresent() || var1.method_1006(var7);
   }

   public static class_3966 h(class_746 var0, float var1, float var2, double var3, boolean var5) {
      if (var0 != null && var0.method_73183() != null && !(var3 <= 0.0)) {
         class_243 var6 = var0.method_33571();
         class_243 var7 = a_.w(var1, var2);
         class_243 var8 = var6.method_1019(var7.method_1021(var3));
         double var9 = var3 * var3;
         if (!var5) {
            class_3965 var11 = var0.method_73183().method_17742(new class_3959(var6, var8, class_3960.field_17558, class_242.field_1348, var0));
            if (var11.method_17783() != class_240.field_1333) {
               var9 = var6.method_1025(var11.method_17784());
            }
         }

         class_238 var12 = var0.method_5829().method_18804(var7.method_1021(var3)).method_1014(1.0);
         return class_1675.method_18075(
            var0, var6, var8, var12, var1x -> var1x != var0 && var1x.method_5863() && var1x.method_5805() && !var1x.method_7325(), var9
         );
      } else {
         return null;
      }
   }

   public static boolean D(class_243 var0, double var1, class_238 var3) {
      class_746 var4 = class_310.method_1551().field_1724;
      if (var4 == null) {
         return false;
      }

      class_243 var5 = var4.method_33571();
      if (var3.method_1006(var5)) {
         return true;
      }

      Optional var6 = var3.method_992(var5, var5.method_1019(var0.method_1021(var1)));
      return var6.isPresent();
   }

   public static boolean X() {
      class_746 var0 = class_310.method_1551().field_1724;
      if (var0 != null && var0.method_6115()) {
         class_1799 var1 = var0.method_6030();
         if (var1.method_7960()) {
            return false;
         }

         if (var1.method_57826(class_9334.field_50075)) {
            return true;
         }

         class_1839 var2 = var1.method_7976();
         return var2 == class_1839.field_8950 || var2 == class_1839.field_8946;
      } else {
         return false;
      }
   }

   public static boolean i() {
      class_310 var0 = class_310.method_1551();
      if (var0.field_1724 != null && var0.field_1687 != null) {
         class_2338 var1 = var0.field_1724.method_24515();
         if (!var0.field_1687.method_8320(var1).method_27852(class_2246.field_10343)
            && !var0.field_1687.method_8320(var1.method_10084()).method_27852(class_2246.field_10343)
            && !var0.field_1687
               .method_8320(class_2338.method_49637(var0.field_1724.method_23317(), var0.field_1724.method_23318() + 0.5, var0.field_1724.method_23321()))
               .method_27852(class_2246.field_10343)
            && !var0.field_1687
               .method_8320(class_2338.method_49637(var0.field_1724.method_23317(), var0.field_1724.method_23318() + 1.2F, var0.field_1724.method_23321()))
               .method_27852(class_2246.field_10343)) {
            class_238 var2 = var0.field_1724.method_5829();

            for (int var3 = var1.method_10263() - 2; var3 <= var1.method_10263() + 2; var3++) {
               for (int var4 = var1.method_10264() - 1; var4 <= var1.method_10264() + 4; var4++) {
                  for (int var5 = var1.method_10260() - 2; var5 <= var1.method_10260() + 2; var5++) {
                     class_2338 var6 = new class_2338(var3, var4, var5);
                     if (var2.method_994(new class_238(var6)) && var0.field_1687.method_8320(var6).method_27852(class_2246.field_10343)) {
                        return true;
                     }
                  }
               }
            }

            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static class_243 a(class_1297 var0) {
      class_243 var1 = class_310.method_1551().field_1724.method_33571();
      class_238 var2 = var0.method_5829();
      return new class_243(
         class_3532.method_15350(var1.field_1352, var2.field_1323, var2.field_1320),
         class_3532.method_15350(var1.field_1351, var2.field_1322, var2.field_1325),
         class_3532.method_15350(var1.field_1350, var2.field_1321, var2.field_1324)
      );
   }
}
