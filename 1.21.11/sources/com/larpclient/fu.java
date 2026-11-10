package com.larpclient;

import java.util.function.Function;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2338.class_2339;

public final class fu {
   public static boolean I(class_2680 var0) {
      return var0.method_27852(class_2246.field_9993)
         || var0.method_27852(class_2246.field_10463)
         || var0.method_27852(class_2246.field_10211)
         || var0.method_27852(class_2246.field_16999)
         || var0.method_27852(class_2246.field_10597)
         || var0.method_27852(class_2246.field_28048);
   }

   public static fu.m t(class_2680 var0, class_2338 var1, Function<class_2338, class_2680> var2) {
      if (!var0.method_27852(class_2246.field_9993) && !var0.method_27852(class_2246.field_10463)) {
         if (var0.method_27852(class_2246.field_10211)) {
            if (((class_2680)var2.apply(var1.method_10074())).method_27852(class_2246.field_10211)) {
               return null;
            } else {
               return D(var1, class_2350.field_11036, var2, class_2246.field_10211) >= 12 ? fu.m.V : null;
            }
         } else {
            if (var0.method_27852(class_2246.field_16999)) {
               return var0.method_11654(class_2741.field_12497) == 3 ? fu.m.O : null;
            }

            if (var0.method_27852(class_2246.field_10597)) {
               if (((class_2680)var2.apply(var1.method_10084())).method_27852(class_2246.field_10597)) {
                  return null;
               } else {
                  return D(var1, class_2350.field_11033, var2, class_2246.field_10597) >= 7 ? fu.m.q : null;
               }
            } else if (var0.method_27852(class_2246.field_28048) && var0.method_11654(class_2741.field_28062) == class_2350.field_11033) {
               class_2680 var4 = (class_2680)var2.apply(var1.method_10084());
               if (var4.method_27852(class_2246.field_28048) && var4.method_11654(class_2741.field_28062) == class_2350.field_11033) {
                  return null;
               } else {
                  return D(
                           var1,
                           class_2350.field_11033,
                           var1x -> {
                              class_2680 var2x = (class_2680)var2.apply(var1x);
                              return var2x.method_27852(class_2246.field_28048) && var2x.method_11654(class_2741.field_28062) != class_2350.field_11033
                                 ? class_2246.field_10124.method_9564()
                                 : var2x;
                           },
                           class_2246.field_28048
                        )
                        >= 5
                     ? fu.m.w
                     : null;
               }
            } else {
               return null;
            }
         }
      } else {
         if (i((class_2680)var2.apply(var1.method_10084()))) {
            return null;
         }

         int var3 = D(var1, class_2350.field_11033, var2, class_2246.field_9993, class_2246.field_10463);
         return var3 < 14 && (!var0.method_27852(class_2246.field_9993) || var0.method_11654(class_2741.field_12517) != 25 || var3 < 8) ? null : fu.m.k;
      }
   }

   private static boolean i(class_2680 var0) {
      return var0.method_27852(class_2246.field_9993) || var0.method_27852(class_2246.field_10463);
   }

   public static int D(class_2338 var0, class_2350 var1, Function<class_2338, class_2680> var2, class_2248... var3) {
      int var4 = 1;
      class_2339 var5 = var0.method_25503();

      while (var4 < 40) {
         var5.method_10098(var1);
         class_2680 var6 = (class_2680)var2.apply(var5);
         byte var7 = 0;

         for (class_2248 var11 : var3) {
            if (var6.method_27852(var11)) {
               var7 = 1;
               break;
            }
         }

         if (var7 == 0) {
            break;
         }

         var4++;
      }

      return var4;
   }

   public static boolean K(class_2680 var0, int var1, class_2338 var2, Function<class_2338, class_2680> var3) {
      if (var1 == 5 && (var0.method_26215() || var0.method_27852(class_2246.field_27161))) {
         for (int var4 = -1; var4 <= 1; var4++) {
            for (int var5 = -1; var5 <= 1; var5++) {
               for (int var6 = -1; var6 <= 1; var6++) {
                  if (var4 != 0 || var5 != 0 || var6 != 0) {
                     class_2680 var7 = (class_2680)var3.apply(var2.method_10069(var4, var5, var6));
                     if (var7.method_27852(class_2246.field_27159) || var7.method_27852(class_2246.field_27160)) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private fu() {
   }

   public enum m {
      k,
      V,
      O,
      q,
      w;
   }
}
