package com.larpclient;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.class_1280;
import net.minecraft.class_1282;
import net.minecraft.class_1293;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1893;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_9892;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class hw {
   public static final double y = 6.0;

   public static float y(double var0, double var2) {
      return d(var0, var2, 6.0);
   }

   public static float d(double var0, double var2, double var4) {
      if (Double.isFinite(var0) && Double.isFinite(var2) && Double.isFinite(var4) && !(var4 <= 0.0)) {
         double var6 = var4 * 2.0;
         if (var0 > var6) {
            return 0.0F;
         }

         double var8 = (1.0 - Math.clamp(var0 / var6, 0.0, 1.0)) * Math.clamp(var2, 0.0, 1.0);
         return (float)((var8 * var8 + var8) / 2.0 * 7.0 * var6 + 1.0);
      } else {
         return 0.0F;
      }
   }

   public static boolean t(double var0, double var2, double var4) {
      return Double.isFinite(var0) && var0 <= var2 && var0 < var4;
   }

   public static float d(class_1309 var0, class_243 var1) {
      return b(var0, var1, 6.0);
   }

   public static float b(class_1309 var0, class_243 var1, double var2) {
      return O(var0, var1, var2)[0];
   }

   public static float G(class_1309 var0, class_243 var1, class_2338 var2) {
      return r(var0, var1, var2, class_243.field_1353);
   }

   public static float r(class_1309 var0, class_243 var1, class_2338 var2, class_243 var3) {
      return M(var0, var1, var2, var3, 6.0)[0];
   }

   public static float[] O(class_1309 var0, class_243 var1, double... var2) {
      return M(var0, var1, null, class_243.field_1353, var2);
   }

   private static float[] M(class_1309 var0, class_243 var1, class_2338 var2, class_243 var3, double... var4) {
      float[] var5 = new float[var4.length];
      if (var0 instanceof class_1657 var6 && var6.method_31549().field_7480) {
         return var5;
      } else {
         double var15 = var0.method_73189().method_1019(var3).method_1022(var1);
         double var8 = 0.0;

         for (double var13 : var4) {
            if (Double.isFinite(var13)) {
               var8 = Math.max(var8, var13 * 2.0);
            }
         }

         if (var15 > var8) {
            return var5;
         }

         float var16 = b(var1, var0, var2, var3);

         for (int var17 = 0; var17 < var4.length; var17++) {
            var5[var17] = X(var0, d(var15, var16, var4[var17]));
         }

         return var5;
      }
   }

   public static float w(class_243 var0, class_1297 var1, class_2338 var2) {
      return b(var0, var1, var2, class_243.field_1353);
   }

   public static float b(class_243 var0, class_1297 var1, class_2338 var2, class_243 var3) {
      if (var2 == null && var3.equals(class_243.field_1353)) {
         return class_9892.method_61731(var0, var1);
      }

      class_238 var4 = var1.method_5829().method_997(var3);
      class_238 var5 = var2 == null ? null : new class_238(var2);
      double var6 = 1.0 / ((var4.field_1320 - var4.field_1323) * 2.0 + 1.0);
      double var8 = 1.0 / ((var4.field_1325 - var4.field_1322) * 2.0 + 1.0);
      double var10 = 1.0 / ((var4.field_1324 - var4.field_1321) * 2.0 + 1.0);
      double var12 = (1.0 - Math.floor(1.0 / var6) * var6) / 2.0;
      double var14 = (1.0 - Math.floor(1.0 / var10) * var10) / 2.0;
      if (!(var6 < 0.0) && !(var8 < 0.0) && !(var10 < 0.0)) {
         int var16 = 0;
         int var17 = 0;

         for (double var18 = 0.0; var18 <= 1.0; var18 += var6) {
            for (double var20 = 0.0; var20 <= 1.0; var20 += var8) {
               for (double var22 = 0.0; var22 <= 1.0; var22 += var10) {
                  class_243 var24 = new class_243(
                     class_3532.method_16436(var18, var4.field_1323, var4.field_1320) + var12,
                     class_3532.method_16436(var20, var4.field_1322, var4.field_1325),
                     class_3532.method_16436(var22, var4.field_1321, var4.field_1324) + var14
                  );
                  if ((var5 == null || var5.method_992(var24, var0).isEmpty())
                     && var1.method_73183().method_17742(new class_3959(var24, var0, class_3960.field_17558, class_242.field_1348, var1)).method_17783()
                        == class_240.field_1333) {
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

   public static float X(class_1309 var0, float var1) {
      if (var1 <= 0.0F) {
         return 0.0F;
      }

      if (var0 instanceof class_1657) {
         var1 = switch (var0.method_73183().method_8407()) {
            case field_5801 -> 0.0F;
            case field_5805 -> Math.min(var1 / 2.0F + 1.0F, var1);
            case field_5802 -> var1;
            case field_5807 -> var1 * 1.5F;
            default -> throw new MatchException(null, null);
         };
      }

      class_1282 var2 = var0.method_73183().method_48963().method_48807(null);
      var1 = class_1280.method_5496(var0, var1, var2, var0.method_6096(), (float)var0.method_45325(class_5134.field_23725));
      class_1293 var3 = var0.method_6112(class_1294.field_5907);
      if (var3 != null) {
         var1 *= Math.max(0.0F, 1.0F - 0.2F * (var3.method_5578() + 1));
      }

      int var4 = 0;

      for (class_1304 var8 : new class_1304[]{class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166}) {
         class_9304 var9 = (class_9304)var0.method_6118(var8).method_58694(class_9334.field_49633);
         if (var9 != null) {
            for (Entry var11 : var9.method_57539()) {
               if (((class_6880)var11.getKey()).method_40225(class_1893.field_9111)) {
                  var4 += var11.getIntValue();
               }

               if (((class_6880)var11.getKey()).method_40225(class_1893.field_9107)) {
                  var4 += 2 * var11.getIntValue();
               }
            }
         }
      }

      return class_1280.method_5497(var1, Math.min(20, var4));
   }

   private hw() {
   }
}
