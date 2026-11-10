package com.larpclient;

import java.util.Locale;
import java.util.regex.Pattern;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_2561;
import net.minecraft.class_266;
import net.minecraft.class_269;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_8646;
import net.minecraft.class_9013;
import net.minecraft.class_274.class_275;

public final class j2 {
   private static final Pattern L = Pattern.compile("\\bhp\\b");
   private static final j2.s H = new j2.s();
   private static final j2.s s = new j2.s();
   private static boolean K;

   private j2() {
   }

   public static float T(class_1309 var0) {
      float var1 = var0.method_6032();
      if (var0 instanceof class_1657 var2 && var2 != class_310.method_1551().field_1724) {
         int var3 = o(var2);
         if (var3 <= 0) {
            return var1;
         }

         float var4 = Math.max(0.0F, var0.method_6067());
         if (!(Math.abs(var3 - Math.ceil(var1)) <= 1.0) && !(Math.abs(var3 - Math.ceil(var1 + var4)) <= 1.0)) {
            if (!K) {
               K = (boolean)1;
               q8.G("ESP", "Synced health {} disagrees with scoreboard health {}; using scoreboard", var1, var3);
            }

            return Math.min(var3, Math.max(1.0F, var0.method_6063()));
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static double z(class_1309 var0) {
      return Math.clamp(T(var0) / Math.max(1.0F, var0.method_6063()), 0.0F, 1.0F);
   }

   private static int o(class_1657 var0) {
      class_638 var1 = class_310.method_1551().field_1687;
      if (var1 == null) {
         return -1;
      }

      class_269 var2 = var1.method_8428();
      int var3 = X(var2, var2.method_1189(class_8646.field_45158), H, var0);
      return var3 > 0 ? var3 : X(var2, var2.method_1189(class_8646.field_45156), s, var0);
   }

   private static int X(class_269 var0, class_266 var1, j2.s var2, class_1657 var3) {
      if (var1 != null && var2.y(var1)) {
         class_9013 var4 = var0.method_55430(var3, var1);
         return var4 == null ? -1 : var4.method_55397();
      } else {
         return -1;
      }
   }

   private static final class s {
      private class_266 J;
      private class_2561 c;
      private boolean i;

      boolean y(class_266 var1) {
         if (var1.method_1118() == class_275.field_1471) {
            return true;
         }

         class_2561 var2 = var1.method_1114();
         if (var1 == this.J && var2 == this.c) {
            return this.i;
         }

         String var3 = var2.getString().toLowerCase(Locale.ROOT);
         this.J = var1;
         this.c = var2;
         this.i = (boolean)(!var3.contains("❤") && !var3.contains("♥") && !var3.contains("health") && !j2.L.matcher(var3).find() ? 0 : 1);
         return this.i;
      }
   }
}
