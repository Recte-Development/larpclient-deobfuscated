package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q8e12f0a5e92d7494.q313984e4ec8a45e1;
import java.util.Objects;
import net.minecraft.class_11417;
import net.minecraft.class_11419;
import net.minecraft.class_11424;
import net.minecraft.class_11432;
import net.minecraft.class_11473;
import net.minecraft.class_1703;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_7743;
import net.minecraft.class_7923;
import net.minecraft.class_9288;
import net.minecraft.class_9290;
import net.minecraft.class_9334;

public final class c0 {
   private static final boolean F = Boolean.getBoolean("larp.menuTrace");
   private static int k;
   private static Object Z;

   private c0() {
   }

   public static void v(class_310 var0) {
      if (F && var0.field_1724 != null) {
         class_1703 var1 = var0.field_1724.field_7512;
         class_437 var2 = var0.field_1755;
         v(var2);
         if (var1 == var0.field_1724.field_7498) {
            k = 0;
         } else {
            String var3 = var2 == null ? "<no screen>" : var2.method_25440().getString();
            int var4 = Objects.hash(var1.field_7763, var3);

            for (class_1735 var6 : var1.field_7761) {
               var4 = 31 * var4 + class_1799.method_57355(var6.method_7677()) * 7 + var6.method_7677().method_7947();
            }

            if (var4 != k) {
               k = var4;
               StringBuilder var9 = new StringBuilder();
               var9.append("Menu '")
                  .append(var3)
                  .append("' id=")
                  .append(var1.field_7763)
                  .append(" state=")
                  .append(var1.method_37421())
                  .append(" type=")
                  .append(var1.getClass().getSimpleName())
                  .append(" slots=")
                  .append(var1.field_7761.size());

               for (int var10 = 0; var10 < var1.field_7761.size(); var10++) {
                  class_1735 var7 = (class_1735)var1.field_7761.get(var10);
                  if (var7.field_7871 != var0.field_1724.method_31548()) {
                     class_1799 var8 = var7.method_7677();
                     if (!var8.method_7960()) {
                        var9.append("\n  #").append(var10).append(' ').append(q(var8));
                     }
                  }
               }

               q8.G("MenuTrace", "{}", var9);
            }
         }
      }
   }

   private static void v(class_437 var0) {
      if (var0 != Z) {
         Z = var0;
         if (var0 instanceof class_11473 var1) {
            class_11419 var2 = ((q313984e4ec8a45e1)var1).larp$dialog();
            if (var2 == null) {
               q8.G("MenuTrace", "Dialog '{}' with no dialog data", var0.method_25440().getString());
               return;
            }

            class_11417 var3 = var2.comp_4305();
            StringBuilder var4 = new StringBuilder("Dialog '")
               .append(var3.comp_4301().getString())
               .append("' type=")
               .append(var2.getClass().getSimpleName())
               .append(" inputs=")
               .append(var3.comp_4394().size());

            for (class_11432 var6 : var3.comp_4304()) {
               var4.append("\n  body ").append(var6);
            }

            for (class_11424 var8 : var3.comp_4394()) {
               var4.append("\n  input ").append(var8);
            }

            var4.append("\n  raw ").append(var2);
            q8.G("MenuTrace", "{}", var4);
         } else if (var0 instanceof class_7743) {
            q8.G("MenuTrace", "Sign editor '{}' opened ({})", var0.method_25440().getString(), var0.getClass().getSimpleName());
         }
      }
   }

   public static String q(class_1799 var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(class_7923.field_41178.method_10221(var0.method_7909()))
         .append(" x")
         .append(var0.method_7947())
         .append(" '")
         .append(var0.method_7964().getString())
         .append('\'');
      class_9290 var2 = (class_9290)var0.method_58694(class_9334.field_49632);
      if (var2 != null) {
         for (class_2561 var4 : var2.comp_2400()) {
            var1.append("\n      | ").append(var4.getString());
         }
      }

      class_9288 var5 = (class_9288)var0.method_58694(class_9334.field_49622);
      if (var5 != null) {
         var1.append("\n      container items=").append(var5.method_59714().iterator().hasNext());
      }

      return var1.toString();
   }
}
