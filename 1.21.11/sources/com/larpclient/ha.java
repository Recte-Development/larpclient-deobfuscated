package com.larpclient;

import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.HeadlessKorbloxModule;
import net.minecraft.class_10034;
import net.minecraft.class_10055;
import net.minecraft.class_11890;
import net.minecraft.class_1799;
import net.minecraft.class_591;

public final class ha {
   private ha() {
   }

   public static void o(class_11890 var0, class_10055 var1) {
      g1 var2 = (g1)var1;
      var2.larp$roblox(0, null);
      HeadlessKorbloxModule var3 = HeadlessKorbloxModule.e();
      if (var3 != null && !var3.isEnabled()) {
         var3 = null;
      }

      int var4 = var3 == null ? 0 : var3.o(var0);
      ChinaHatModule var5 = ChinaHatModule.h();
      if (var4 == 0 && var5 != null && var5.isEnabled() && var5.n(var0.method_5628())) {
         var4 = 1;
      }

      yv var6 = var3 == null ? null : var3.n(var0);
      if (var6 != null && !lm.R()) {
         var6 = null;
      }

      var2.larp$roblox(var4, var6);
      if (var4 == 2) {
         A(var1);
      }
   }

   public static void A(class_10034 var0) {
      var0.field_55309 = class_1799.field_8037;
      var0.field_53467.method_65605();
      var0.field_55315 = null;
      var0.field_55316 = null;
   }

   public static boolean g(class_591 var0, boolean var1, int var2, yv var3) {
      int var4 = var1 && var2 != 0 && var0.field_3398.field_3665 ? 1 : 0;
      if (var4 != 0) {
         var0.field_3398.field_3665 = (boolean)0;
      }

      if (var3 != null) {
         if (var3.right()) {
            var0.field_3392.field_3665 = (boolean)0;
         }

         if (var3.left()) {
            var0.field_3397.field_3665 = (boolean)0;
         }
      }

      return (boolean)var4;
   }

   public static void m(class_10055 var0) {
      ((g1)var0).larp$roblox(0, null);
   }
}
