package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_746;

public final class e7 {
   private static class_746 B;
   private static class_3965 g;
   private static BooleanSupplier r;
   private static int e;
   private static boolean X;
   private static int H = -1;

   public static void m(class_310 var0, class_3965 var1, BooleanSupplier var2) {
      if (B != var0.field_1724 || g == null || var0.field_1724.field_6012 - e > 5) {
         B = var0.field_1724;
         g = var1;
         r = var2;
         e = B.field_6012;
         X = (boolean)0;
         H = -1;
      }
   }

   private static boolean v(class_746 var0) {
      return var0 == B && g != null && var0.field_6012 - e <= 5 && class_310.method_1551().field_1724 == var0 && !var0.method_5765() && !t9.I();
   }

   public static float J(class_746 var0) {
      if (!v(var0)) {
         return var0.method_36454();
      }

      X = (boolean)1;
      class_243 var1 = g.method_17784().method_1020(var0.method_33571());
      return (float)Math.toDegrees(Math.atan2(-var1.field_1352, var1.field_1350));
   }

   public static float w(class_746 var0) {
      if (!v(var0)) {
         return var0.method_36455();
      }

      class_243 var1 = g.method_17784().method_1020(var0.method_33571());
      return (float)(-Math.toDegrees(Math.atan2(var1.field_1351, Math.hypot(var1.field_1352, var1.field_1350))));
   }

   public static void u(class_746 var0) {
      if (v(var0) && X) {
         if (H < 0) {
            H = var0.field_6012;
            return;
         }

         if (var0.field_6012 <= H) {
            return;
         }

         if (r != null) {
            if (!r.getAsBoolean()) {
               return;
            }

            r = null;
            H = var0.field_6012 - 1;
         }

         if (var0.field_6012 <= H + 2) {
            return;
         }
      }

      B = null;
      g = null;
      r = null;
      X = (boolean)0;
      H = -1;
   }
}
