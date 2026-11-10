package com.larpclient;

import net.minecraft.class_238;

public final class wr {
   public static void d(f1 var0, boolean var1, ip var2, class_238 var3, double var4, int var6, double var7, double var9, double var11, double var13) {
      var0.blend(ti.d.l);
      class_238 var15 = var3;

      for (ip.o var17 : var2.edges()) {
         double var18 = c(var17.ax(), var15.field_1323, var15.field_1320);
         double var20 = c(var17.ay(), var15.field_1322, var15.field_1325);
         double var22 = c(var17.az(), var15.field_1321, var15.field_1324);
         double var24 = c(var17.bx(), var15.field_1323, var15.field_1320);
         double var26 = c(var17.by(), var15.field_1322, var15.field_1325);
         double var28 = c(var17.bz(), var15.field_1321, var15.field_1324);
         double var30 = var4 * var7;
         if (var13 > 0.0) {
            int var32 = ei.R(var6, var30 * var13 * (var1 ? 0.25 : 0.09));
            var0.g(var18, var20, var22, var24, var26, var28, var9 * 3.0, var9 * 3.0, var32, var32);
            if (!var1) {
               var0.g(var18, var20, var22, var24, var26, var28, var9 * 5.0, var9 * 5.0, ei.R(var32, 0.35), ei.R(var32, 0.35));
            }
         }

         if (!var1) {
            int var35 = ei.R(var6, var30);
            var0.g(var18, var20, var22, var24, var26, var28, var9, var9, var35, var35);
         }
      }

      var0.blend(ti.d.G);
      if (!var1 && var11 > 0.0) {
         for (class_238 var34 : var2.boxes()) {
            Z(
               var0,
               new class_238(
                  c(var34.field_1323, var15.field_1323, var15.field_1320),
                  c(var34.field_1322, var15.field_1322, var15.field_1325),
                  c(var34.field_1321, var15.field_1321, var15.field_1324),
                  c(var34.field_1320, var15.field_1323, var15.field_1320),
                  c(var34.field_1325, var15.field_1322, var15.field_1325),
                  c(var34.field_1324, var15.field_1321, var15.field_1324)
               ),
               ei.R(var6, var4 * var11)
            );
         }
      }
   }

   private static double c(double var0, double var2, double var4) {
      return ip.map(var0, var2, var4);
   }

   private static void Z(f1 var0, class_238 var1, int var2) {
      double var3 = var1.field_1323;
      double var5 = var1.field_1322;
      double var7 = var1.field_1321;
      double var9 = var1.field_1320;
      double var11 = var1.field_1325;
      double var13 = var1.field_1324;
      B(var0, var2, var3, var5, var7, var9, var5, var7, var9, var11, var7, var3, var11, var7);
      B(var0, var2, var3, var5, var13, var3, var11, var13, var9, var11, var13, var9, var5, var13);
      B(var0, var2, var3, var5, var7, var3, var11, var7, var3, var11, var13, var3, var5, var13);
      B(var0, var2, var9, var5, var7, var9, var5, var13, var9, var11, var13, var9, var11, var7);
      B(var0, var2, var3, var5, var7, var3, var5, var13, var9, var5, var13, var9, var5, var7);
      B(var0, var2, var3, var11, var7, var9, var11, var7, var9, var11, var13, var3, var11, var13);
   }

   private static void B(
      f1 var0,
      int var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      double var22,
      double var24
   ) {
      var0.vertex(var2, var4, var6, var1);
      var0.vertex(var8, var10, var12, var1);
      var0.vertex(var14, var16, var18, var1);
      var0.vertex(var20, var22, var24, var1);
   }

   private wr() {
   }
}
