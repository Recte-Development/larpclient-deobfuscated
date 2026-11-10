package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_238;
import net.minecraft.class_243;

public final class fh {
   public static final double T = 0.25;
   public static final double b = 0.012;

   public static fh.x I(class_238 var0, double var1, double var3, fh.v var5, boolean var6) {
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      if (var1 > 0.0 && Double.isFinite(var1)) {
         double var9 = Math.min(8.0, var1 + Math.max(var0.method_17939(), var0.method_17941()) / 2.0 + 0.25);
         class_243 var11 = var0.method_1005();
         int var12 = (int)Math.floor((var11.field_1352 - var9) / 0.25);
         int var13 = (int)Math.ceil((var11.field_1352 + var9) / 0.25);
         int var14 = (int)Math.floor((var11.field_1350 - var9) / 0.25);
         int var15 = (int)Math.ceil((var11.field_1350 + var9) / 0.25);

         for (int var16 = var12; var16 < var13; var16++) {
            for (int var17 = var14; var17 < var15; var17++) {
               double var18 = var16 * 0.25;
               double var20 = var17 * 0.25;
               fh.c var22 = var5.b(var18 + 0.125, var20 + 0.125);
               if (var22 != null) {
                  double var23 = Math.max(var18, var22.minX);
                  double var25 = Math.min(var18 + 0.25, var22.maxX);
                  double var27 = Math.max(var20, var22.minZ);
                  double var29 = Math.min(var20 + 0.25, var22.maxZ);
                  if (!(var23 >= var25)
                     && !(var27 >= var29)
                     && k(var5, var23 + 1.0E-5, var27 + 1.0E-5, var22.y)
                     && k(var5, var25 - 1.0E-5, var27 + 1.0E-5, var22.y)
                     && k(var5, var25 - 1.0E-5, var29 - 1.0E-5, var22.y)
                     && k(var5, var23 + 1.0E-5, var29 - 1.0E-5, var22.y)) {
                     class_243 var31 = new class_243(var23, var22.y + 0.012, var27);
                     class_243 var32 = new class_243(var25, var22.y + 0.012, var27);
                     class_243 var33 = new class_243(var25, var22.y + 0.012, var29);
                     class_243 var34 = new class_243(var23, var22.y + 0.012, var29);
                     Y(var0, var1, var3, var31, var32, var33, var7, var6 ? var8 : null);
                     Y(var0, var1, var3, var31, var33, var34, var7, var6 ? var8 : null);
                     L(var0, var1, var3, var5, var31, var32, 0.0, -0.003, var22.y, var7);
                     L(var0, var1, var3, var5, var32, var33, 0.003, 0.0, var22.y, var7);
                     L(var0, var1, var3, var5, var33, var34, 0.0, 0.003, var22.y, var7);
                     L(var0, var1, var3, var5, var34, var31, -0.003, 0.0, var22.y, var7);
                  }
               }
            }
         }

         return new fh.x(List.copyOf(var7), List.copyOf(var8));
      } else {
         return new fh.x(List.of(), List.of());
      }
   }

   private static boolean k(fh.v var0, double var1, double var3, double var5) {
      fh.c var7 = var0.b(var1, var3);
      return var7 != null && Math.abs(var7.y - var5) < 0.001;
   }

   private static void L(
      class_238 var0, double var1, double var3, fh.v var5, class_243 var6, class_243 var7, double var8, double var10, double var12, List<fh.t> var14
   ) {
      fh.c var15 = var5.b((var6.field_1352 + var7.field_1352) / 2.0 + var8, (var6.field_1350 + var7.field_1350) / 2.0 + var10);
      if (var15 != null && !(var12 - var15.y < 0.001) && !(var12 - var15.y > 1.05)) {
         class_243 var16 = var6.method_1031(var8, 0.0, var10);
         class_243 var17 = var7.method_1031(var8, 0.0, var10);
         class_243 var18 = new class_243(var17.field_1352, var15.y + 0.012, var17.field_1350);
         class_243 var19 = new class_243(var16.field_1352, var15.y + 0.012, var16.field_1350);
         Y(var0, var1, var3, var16, var17, var18, var14, null);
         Y(var0, var1, var3, var16, var18, var19, var14, null);
      }
   }

   public static double E(class_238 var0, class_243 var1, double var2, double var4) {
      return ug.L(var1.method_1031(0.0, var2 - 0.012, 0.0), var0) - var4;
   }

   private static void Y(class_238 var0, double var1, double var3, class_243 var5, class_243 var6, class_243 var7, List<fh.t> var8, List<fh.u> var9) {
      class_243[] var10 = new class_243[]{var5, var6, var7};
      double[] var11 = new double[]{E(var0, var5, var3, var1), E(var0, var6, var3, var1), E(var0, var7, var3, var1)};
      if (!(var11[0] > 0.0) || !(var11[1] > 0.0) || !(var11[2] > 0.0)) {
         if (var9 != null || !(var11[0] <= 0.0) || !(var11[1] <= 0.0) || !(var11[2] <= 0.0)) {
            ArrayList var12 = new ArrayList(4);
            ArrayList var13 = new ArrayList(2);

            for (int var14 = 0; var14 < 3; var14++) {
               int var15 = (var14 + 1) % 3;
               if (var11[var14] <= 0.0) {
                  var12.add(var10[var14]);
               }

               if ((var11[var14] <= 0.0 ? 1 : 0) != (var11[var15] <= 0.0 ? 1 : 0)) {
                  class_243 var16 = U(var0, var1, var3, var10[var14], var10[var15], var11[var14] <= 0.0);
                  var12.add(var16);
                  var13.add(var16);
               }
            }

            if (var13.size() == 2 && ((class_243)var13.get(0)).method_1025((class_243)var13.get(1)) > 1.0E-12) {
               var8.add(new fh.t((class_243)var13.get(0), (class_243)var13.get(1)));
            }

            if (var9 != null) {
               for (int var17 = 1; var17 + 1 < var12.size(); var17++) {
                  var9.add(new fh.u((class_243)var12.get(0), (class_243)var12.get(var17), (class_243)var12.get(var17 + 1)));
               }
            }
         }
      }
   }

   private static class_243 U(class_238 var0, double var1, double var3, class_243 var5, class_243 var6, boolean var7) {
      double var8 = 0.0;
      double var10 = 1.0;

      for (int var12 = 0; var12 < 12; var12++) {
         double var13 = (var8 + var10) / 2.0;
         if ((E(var0, var5.method_35590(var6, var13), var3, var1) <= 0.0 ? 1 : 0) == var7) {
            var8 = var13;
         } else {
            var10 = var13;
         }
      }

      return var5.method_35590(var6, (var8 + var10) / 2.0);
   }

   public static fh.c d(List<class_238> var0, double var1, double var3, double var5) {
      class_238 var7 = null;

      for (class_238 var9 : var0) {
         if (!(var1 < var9.field_1323)
            && !(var1 >= var9.field_1320)
            && !(var3 < var9.field_1321)
            && !(var3 >= var9.field_1324)
            && !(var9.field_1325 < var5 - 1.5)
            && !(var9.field_1325 > var5 + 1.05)) {
            byte var10 = 0;

            for (class_238 var12 : var0) {
               if (var1 >= var12.field_1323
                  && var1 < var12.field_1320
                  && var3 >= var12.field_1321
                  && var3 < var12.field_1324
                  && var12.field_1322 <= var9.field_1325 + 0.001
                  && var12.field_1325 > var9.field_1325 + 0.001) {
                  var10 = 1;
                  break;
               }
            }

            if (var10 == 0
               && (
                  var7 == null
                     || Math.abs(var9.field_1325 - var5) < Math.abs(var7.field_1325 - var5)
                     || Math.abs(var9.field_1325 - var5) == Math.abs(var7.field_1325 - var5) && var9.field_1325 > var7.field_1325
               )) {
               var7 = var9;
            }
         }
      }

      return var7 == null ? null : new fh.c(var7.field_1323, var7.field_1321, var7.field_1320, var7.field_1324, var7.field_1325);
   }

   private fh() {
   }

   public record c(double minX, double minZ, double maxX, double maxZ, double y) {
   }

   public record t(class_243 from, class_243 to) {
   }

   public record u(class_243 a, class_243 b, class_243 c) {
   }

   @FunctionalInterface
   public interface v {
      fh.c b(double var1, double var3);
   }

   public record x(List<fh.t> outline, List<fh.u> fill) {
   }
}
