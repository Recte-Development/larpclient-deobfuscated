package com.larpclient.module;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class t {
   private static int m = Integer.MIN_VALUE;
   private static class_243 a = class_243.field_1353;
   private static class_243 b = class_243.field_1353;

   private t() {
   }

   public static void g() {
      m = Integer.MIN_VALUE;
      a = class_243.field_1353;
      b = class_243.field_1353;
   }

   public static class_243 d(class_238 var0) {
      class_746 var1 = class_310.method_1551().field_1724;
      if (var1 == null) {
         return var0.method_1005();
      }

      class_243 var2 = var1.method_33571();
      class_243 var3 = null;
      double var4 = Double.MAX_VALUE;

      for (double var6 = var0.field_1323; var6 <= var0.field_1320; var6 += 0.1F) {
         for (double var8 = var0.field_1322; var8 <= var0.field_1325; var8 += 0.1F) {
            for (double var10 = var0.field_1321; var10 <= var0.field_1324; var10 += 0.1F) {
               class_243 var12 = new class_243(var6, var8, var10);
               double var13 = var2.method_1022(var12);
               if (var13 < var4) {
                  var4 = var13;
                  var3 = var12;
               }
            }
         }
      }

      return var3 != null ? var3 : var0.method_1005();
   }

   public static class_243 F(class_1297 var0, class_238 var1, double var2) {
      if (class_310.method_1551().field_1724 == null) {
         return var1.method_1005();
      }

      float var4 = 0.005F;
      float var5 = 0.015F;
      float var6 = 0.0015F;
      float var7 = 0.015F;
      double var8 = var1.method_17939();
      double var10 = var1.method_17940();
      double var12 = var1.method_17941();
      if (m != var0.method_5628()) {
         m = var0.method_5628();
         a = new class_243(0.0, var10 * 0.55F, 0.0);
         b = new class_243(i(-0.02F, 0.02F), i(-0.02F, 0.02F), i(-0.02F, 0.02F));
      }

      a = a.method_1019(b);
      double var14 = (var8 - 0.1F) / 2.0;
      double var16 = (var12 - 0.1F) / 2.0;
      if (a.field_1352 >= var14) {
         b = new class_243(-i(var4, var5), b.field_1351, b.field_1350);
      } else if (a.field_1352 <= -var14) {
         b = new class_243(i(var4, var5), b.field_1351, b.field_1350);
      }

      if (a.field_1351 >= var10 * 0.75) {
         b = new class_243(b.field_1352, -i(var6, var7), b.field_1350);
      } else if (a.field_1351 <= var10 * 0.3F) {
         b = new class_243(b.field_1352, i(var6, var7), b.field_1350);
      }

      if (a.field_1350 >= var16) {
         b = new class_243(b.field_1352, b.field_1351, -i(var4, var5));
      } else if (a.field_1350 <= -var16) {
         b = new class_243(b.field_1352, b.field_1351, i(var4, var5));
      }

      a = a.method_1031(i(-0.05F, 0.05F), 0.0, i(-0.05F, 0.05F));
      a = new class_243(
         class_3532.method_15350(a.field_1352, -var14, var14),
         class_3532.method_15350(a.field_1351, var10 * 0.3F, var10 * 0.75),
         class_3532.method_15350(a.field_1350, -var16, var16)
      );
      class_243 var18 = new class_243(var1.method_1005().field_1352, var1.field_1322, var1.method_1005().field_1350);
      class_243 var19 = var18.method_1019(a);
      if (!f.D(a_.A(var19).q(), var2, var1)) {
         float var20 = (float)(var8 / 2.0) * 0.8F;

         for (float var21 = -var20; var21 <= var20; var21 += 0.1F) {
            for (float var22 = -var20; var22 <= var20; var22 += 0.1F) {
               for (float var23 = (float)(var10 * 0.9F); var23 >= var10 * 0.3F; var23 -= 0.1F) {
                  class_243 var24 = var18.method_1031(var21, var23, var22);
                  if (f.D(a_.A(var24).q(), var2, var1)) {
                     a = new class_243(var21, var23, var22);
                     return var18.method_1019(a);
                  }
               }
            }
         }
      }

      return var19;
   }

   public static class_243 a(class_238 var0, class_243 var1, double var2) {
      class_310 var4 = class_310.method_1551();
      if (var1 != null && var4.field_1724 != null && var4.field_1687 != null) {
         if (C(var0, var1, var2)) {
            return var1;
         }

         class_243 var5 = null;
         double var6 = Double.MAX_VALUE;

         for (double var8 = var0.field_1323; var8 <= var0.field_1320; var8 += 0.12) {
            for (double var10 = var0.field_1322; var10 <= var0.field_1325; var10 += 0.12) {
               for (double var12 = var0.field_1321; var12 <= var0.field_1324; var12 += 0.12) {
                  class_243 var14 = new class_243(var8, var10, var12);
                  if (C(var0, var14, var2)) {
                     double var15 = var14.method_1025(var1);
                     if (var15 < var6) {
                        var6 = var15;
                        var5 = var14;
                     }
                  }
               }
            }
         }

         return var5 != null ? var5 : var1;
      } else {
         return var1;
      }
   }

   private static boolean C(class_238 var0, class_243 var1, double var2) {
      class_310 var4 = class_310.method_1551();
      if (var4.field_1724 == null) {
         return false;
      }

      class_243 var5 = var4.field_1724.method_33571();
      double var6 = var5.method_1022(var1);
      if (var6 > var2) {
         return false;
      }

      class_243 var8 = var1.method_1020(var5).method_1029();
      if (!f.D(var8, var6 + 0.2, var0)) {
         return false;
      }

      class_3965 var9 = var4.field_1687.method_17742(new class_3959(var5, var1, class_3960.field_17558, class_242.field_1348, var4.field_1724));
      return var9 == null || var9.method_17783() == class_240.field_1333 || var5.method_1025(var9.method_17784()) >= var5.method_1025(var1) - 1.0E-4;
   }

   private static float i(float var0, float var1) {
      return (float)(ThreadLocalRandom.current().nextDouble() * (var1 - var0) + var0);
   }
}
