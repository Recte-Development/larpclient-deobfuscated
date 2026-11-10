package com.larpclient;

import net.minecraft.class_10799;
import net.minecraft.class_332;

public final class sx {
   private sx() {
   }

   public static void z(class_332 var0, long var1, boolean var3) {
      wo var4 = wo.K;
      int var5 = var0.method_51421();
      int var6 = var0.method_51443();
      var0.method_25294(0, 0, var5, var6, ei.D(-16777216, 1.0 - var4.z(var1, var3)));
      double var7 = var4.P(var1, var3);
      if (!(var7 <= 0.0)) {
         c9.p var9 = c9.r(var5, var6);
         ue.c var10 = var9.glow();
         double var11 = var7 * var4.i(var1, var3);
         var0.method_71048();
         var0.method_25293(
            class_10799.field_56883, c4.C, var10.x(), var10.y(), 0.0F, c9.U, var10.width(), var10.height(), 256, 256, 512, c9.t, ei.D(-1379585, var11 * 0.2)
         );
         ue.c var13 = var9.logo();
         var0.method_71048();
         var0.method_51448().pushMatrix();

         try {
            var0.method_51448().translate(var13.x() + var13.width() / 2.0F, var13.y() + var13.height() / 2.0F);
            var0.method_51448().rotate((float)var4.S(var1, var3));
            float var14 = (float)var4.q(var1, var3);
            var0.method_51448().scale(var14, var14);
            var0.method_25293(
               class_10799.field_56883,
               c4.W,
               -var13.width() / 2,
               -var13.height() / 2,
               0.0F,
               0.0F,
               var13.width(),
               var13.height(),
               64,
               64,
               64,
               64,
               ei.D(-1, var11)
            );
         } finally {
            var0.method_51448().popMatrix();
         }

         ue.c var18 = var9.title();
         int var15 = var4.c(var1, var3);
         if (var15 > 0) {
            var0.method_71048();
            var0.method_25293(
               class_10799.field_56883, c4.C, var18.x(), var18.y(), 0.0F, var15 * 96, var18.width(), var18.height(), 512, 96, 512, c9.t, ei.D(-723722, var7)
            );
         }
      }
   }
}
