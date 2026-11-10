package com.larpclient;

import java.util.ArrayList;
import net.minecraft.class_1041;
import net.minecraft.class_1268;
import net.minecraft.class_310;

public final class ci {
   private static final wd N = new wd("ViewModel outline");
   private static class_1268 W;
   private static class_1268 D;
   private static boolean v;

   public static void k(class_1268 var0, class_1268 var1) {
      W = var0;
      D = var1;
   }

   private static boolean H() {
      class_310 var0 = class_310.method_1551();
      return bw.f() && var0.field_1690.method_31044().method_31034() && !var0.field_1690.field_1842;
   }

   public static boolean V() {
      if (!H()) {
         H();
         return false;
      } else {
         v = (boolean)1;
         return N.P();
      }
   }

   public static void q() {
      if (!H()) {
         H();
      } else {
         class_1041 var0 = class_310.method_1551().method_22683();
         double var1 = (double)var0.method_4489() / Math.max(1, var0.method_4486());
         float var3 = (float)Math.max(2.0, var1);
         int var4 = (int)Math.ceil(var3 * 2.0F) + 2;
         ArrayList var5 = new ArrayList(2);

         for (class_1268 var9 : class_1268.values()) {
            bw.s var10 = bw.n(var9);
            if (var10 != null) {
               int var11 = var9 == W ? ei.D(ez.a, 0.95) : (var9 == D ? -922746881 : 1627389951);
               var5.add(
                  new wd.e(
                     (int)Math.floor(var10.x() * var1) - var4,
                     (int)Math.floor(var10.y() * var1) - var4,
                     (int)Math.ceil(var10.width() * var1) + var4 * 2,
                     (int)Math.ceil(var10.height() * var1) + var4 * 2,
                     new ba.e(var11, var11, var3, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false)
                  )
               );
            }
         }

         N.b(var5);
      }
   }

   public static void H() {
      if (v) {
         v = (boolean)0;
         D = null;
         W = null;
         N.close();
      }
   }

   private ci() {
   }
}
