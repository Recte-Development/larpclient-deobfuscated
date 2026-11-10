package com.larpclient;

import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_9290;
import net.minecraft.class_9331;
import net.minecraft.class_9334;
import net.minecraft.class_8828.class_2585;

final class yk {
   private static final Pattern s = Pattern.compile("\\$(?:[0-9]{1,3}(?:,[0-9]{3})+|[0-9]+)(?:\\.[0-9]+)?[kKmMbB]?(?![\\w.,])");
   private static final Pattern y = Pattern.compile("Worth: " + s.pattern());
   private static final Pattern l = Pattern.compile("~" + s.pattern().replace("\\$", "\\$ "));

   static boolean M(class_1799 var0, class_1799 var1) {
      return !var0.method_7960()
            && !var1.method_7960()
            && var0.method_31574(class_1802.field_8204)
            && var1.method_31574(class_1802.field_8204)
            && var0.method_58694(class_9334.field_49646) != null
            && Objects.equals(var0.method_58694(class_9334.field_49646), var1.method_58694(class_9334.field_49646))
         ? k(var0, var1)
         : false;
   }

   static boolean k(class_1799 var0, class_1799 var1) {
      if (!var0.method_7960() && !var1.method_7960() && var0.method_7909() == var1.method_7909()) {
         HashSet var2 = new HashSet(var0.method_57353().method_57831());
         var2.addAll(var1.method_57353().method_57831());

         for (class_9331 var4 : var2) {
            if (var4 != class_9334.field_49632 && !Objects.equals(var0.method_57353().method_58694(var4), var1.method_57353().method_58694(var4))) {
               return false;
            }
         }

         class_9290 var6 = (class_9290)var0.method_58694(class_9334.field_49632);
         class_9290 var7 = (class_9290)var1.method_58694(class_9334.field_49632);
         if (var6 != null
            && var7 != null
            && !var6.comp_2400().equals(var7.comp_2400())
            && var6.comp_2400().size() == var7.comp_2400().size()
            && var6.comp_2400().size() <= 32
            && var6.comp_2401().size() == var6.comp_2400().size()
            && var7.comp_2401().size() == var7.comp_2400().size()) {
            for (int var5 = 0; var5 < var6.comp_2400().size(); var5++) {
               if (!x((class_2561)var6.comp_2400().get(var5), (class_2561)var7.comp_2400().get(var5))
                  || !x((class_2561)var6.comp_2401().get(var5), (class_2561)var7.comp_2401().get(var5))) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   static boolean x(class_2561 var0, class_2561 var1) {
      if (var0.equals(var1)) {
         return true;
      }

      String var2 = var0.getString();
      String var3 = var1.getString();
      return var2.length() <= 512
         && var3.length() <= 512
         && (y.matcher(var2).matches() && y.matcher(var3).matches() || l.matcher(var2).matches() && l.matcher(var3).matches())
         && R(var0, var1, 0, false);
   }

   private static boolean R(class_2561 var0, class_2561 var1, int var2, boolean var3) {
      if (var0.equals(var1)) {
         return true;
      }

      if (var2 <= 16
         && var0.method_10866().equals(var1.method_10866())
         && var0.method_10855().size() == var1.method_10855().size()
         && var0.method_10855().size() <= 32) {
         if (!var0.method_10851().equals(var1.method_10851())) {
            if (!(var0.method_10851() instanceof class_2585 var4) || !(var1.method_10851() instanceof class_2585 var5)) {
               return false;
            }

            int var12 = var3 && s.matcher("$" + var4.comp_737()).matches() && s.matcher("$" + var5.comp_737()).matches() ? 1 : 0;
            if (var12 == 0 && !b(var4.comp_737(), var5.comp_737())) {
               return false;
            }
         }

         for (int var9 = 0; var9 < var0.method_10855().size(); var9++) {
            class_2561 var10 = var9 == 0 ? null : (class_2561)var0.method_10855().get(var9 - 1);
            int var13 = var10 != null
                  && var10.method_10855().isEmpty()
                  && var10.method_10851() instanceof class_2585 var7
                  && (var7.comp_737().equals("$") || var7.comp_737().equals("$ "))
                  && var10.equals(var1.method_10855().get(var9 - 1))
               ? 1
               : 0;
            if (!R((class_2561)var0.method_10855().get(var9), (class_2561)var1.method_10855().get(var9), var2 + 1, (boolean)var13)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean b(String var0, String var1) {
      if (var0.length() <= 512 && var1.length() <= 512) {
         Matcher var2 = s.matcher(var0);
         Matcher var3 = s.matcher(var1);
         int var4 = 0;
         int var5 = 0;
         byte var6 = 0;

         while (var2.find()) {
            if (!var3.find() || !var0.substring(var4, var2.start()).equals(var1.substring(var5, var3.start()))) {
               return false;
            }

            if (!var2.group().equals(var3.group())) {
               var6 = 1;
            }

            var4 = var2.end();
            var5 = var3.end();
         }

         return var6 != 0 && !var3.find() && var0.substring(var4).equals(var1.substring(var5));
      } else {
         return false;
      }
   }

   static String o(class_1799 var0) {
      class_9290 var1 = (class_9290)var0.method_58694(class_9334.field_49632);
      if (var1 == null) {
         return "absent";
      }

      StringBuilder var2 = new StringBuilder();

      for (class_2561 var4 : var1.comp_2400().stream().limit(8L).toList()) {
         String var5 = var4.toString().replace('\n', ' ').replace('\r', ' ');
         var2.append('[').append(var5, 0, Math.min(var5.length(), 256)).append(']');
      }

      return "lines=" + var1.comp_2400().size() + var2;
   }
}
