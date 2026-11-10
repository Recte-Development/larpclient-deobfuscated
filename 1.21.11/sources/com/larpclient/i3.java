package com.larpclient;

import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_3726;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_638;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class i3 {
   public static i3.j c(class_638 var0, class_243 var1, class_243 var2) {
      if (var0 != null
         && !(var1.method_1025(var2) > 64.0)
         && var0.method_22340(class_2338.method_49638(var1))
         && var0.method_22340(class_2338.method_49638(var2))) {
         class_3965 var3 = var0.method_17742(new class_3959(var1, var2, class_3960.field_17558, class_242.field_1348, class_3726.method_16194()));
         if (var3.method_17783() == class_240.field_1332 && !var3.method_17781()) {
            class_2350 var4 = var3.method_17780();
            return new i3.j(var3.method_17784(), new class_243(var4.method_10148(), var4.method_10164(), var4.method_10165()));
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private i3() {
   }

   public record j(class_243 point, class_243 normal) {
   }

   @FunctionalInterface
   public interface k {
      i3.j U(class_243 var1, class_243 var2);
   }
}
