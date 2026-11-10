package com.larpclient;

import java.util.Locale;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_746;
import net.minecraft.class_9290;
import net.minecraft.class_9334;

public final class t0 {
   private t0() {
   }

   public static class_1799 Y(class_1799 var0) {
      cz var1 = cz.T();
      return var1.B(var0) ? class_1799.field_8037 : var1.i(var0);
   }

   public static boolean L() {
      class_310 var0 = class_310.method_1551();
      return var0 != null && var0.field_1755 != null && var0.field_1755.method_25440().getString().toLowerCase(Locale.ROOT).contains("auction");
   }

   public static boolean V(class_1799 var0) {
      if (jn.N(var0)) {
         return true;
      }

      if (!L()) {
         return false;
      }

      class_746 var1 = class_310.method_1551().field_1724;
      if (var1 == null) {
         return false;
      }

      for (class_1735 var3 : var1.field_7512.field_7761) {
         if (var3.field_7871 == var1.method_31548()) {
            break;
         }

         if (var3.method_7677() == var0) {
            return true;
         }
      }

      return false;
   }

   public static boolean u(class_1799 var0) {
      class_310 var1 = class_310.method_1551();
      if (cz.T().J() && var1.field_1755 != null && var1.field_1755.method_25440().getString().toLowerCase(Locale.ROOT).contains("sell")) {
         class_9290 var2 = (class_9290)var0.method_58694(class_9334.field_49632);
         return var2 != null && var2.comp_2400().stream().anyMatch(var0x -> var0x.getString().toUpperCase(Locale.ROOT).contains("CLICK TO SELL ITEMS"));
      } else {
         return false;
      }
   }

   public static long k() {
      class_746 var0 = class_310.method_1551().field_1724;
      return var0 == null
         ? 0L
         : var0.field_7512.field_7761.stream().filter(var1 -> var1.field_7871 != var0.method_31548()).mapToLong(var0x -> cz.T().L(var0x.method_7677())).sum();
   }

   public static String e(long var0) {
      if (var0 >= 1000000000L) {
         return A(var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000L) {
         return A(var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000L ? A(var0 / 1000.0) + "K" : Long.toString(var0);
      }
   }

   private static String A(double var0) {
      return String.format(Locale.ROOT, "%.1f", var0).replaceAll("\\.0$", "");
   }
}
