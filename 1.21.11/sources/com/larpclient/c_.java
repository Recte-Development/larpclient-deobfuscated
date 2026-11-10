package com.larpclient;

import com.larpclient.module.WorldMenuModule;
import java.util.Locale;
import net.minecraft.class_1109;
import net.minecraft.class_1113;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3419;
import net.minecraft.class_408;
import net.minecraft.class_1113.class_1114;

public final class c_ {
   private static long u;
   private static boolean B;

   private c_() {
   }

   public static class_1109 W(c_.d var0) {
      return new class_1109(var0.i(), class_3419.field_15250, var0.N, 1.0F, class_1113.method_43221(), false, 0, class_1114.field_5478, 0.0, 0.0, 0.0, true);
   }

   public static void b(c_.d var0) {
      if (var0 != null) {
         class_310 var1 = class_310.method_1551();
         if (var1 != null && var1.field_1705 != null && (tm.K(var1.field_1755) || var1.field_1755 instanceof class_408 || WorldMenuModule.O())) {
            if (var0 == c_.d.E) {
               long var2 = System.nanoTime();
               if (var2 - u < 40000000L) {
                  return;
               }

               u = var2;
            }

            try {
               var1.method_1483().method_4873(W(var0));
            } catch (RuntimeException var4) {
               if (!B) {
                  B = (boolean)1;
                  q8.u("GUI", "GUI sample playback failed; controls remain available", var4);
               }
            }
         }
      }
   }

   public enum d {
      R("open-gui", 0.8F),
      Y("close-gui", 0.8F),
      q("buttonclick", 0.8F),
      E("slider", 0.8F),
      Q("searchtyping", 0.7F),
      o("switchcategory", 0.8F),
      n("switch-left", 0.8F),
      d("switch-right", 0.8F),
      D("settings-open", 0.8F),
      g("settings-close", 0.8F);

      public final String J;
      public final float N;

      d(String var3, float var4) {
         this.J = var3;
         this.N = var4;
      }

      public class_2960 i() {
         return class_2960.method_60655("larp-client", "gui." + this.name().toLowerCase(Locale.ROOT));
      }
   }
}
