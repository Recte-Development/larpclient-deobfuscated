package com.larpclient;

import com.larpclient.module.AuraModule;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_3966;

public final class e5 {
   public static class_1309 U(String var0) {
      class_310 var1 = class_310.method_1551();
      class_1309 var2 = null;
      if (var0.equals("Auto") || var0.equals("Aura")) {
         AuraModule var3 = AuraModule.R();
         if (var3 != null) {
            var2 = var3.B();
         }
      }

      if (var2 == null && (var0.equals("Auto") || var0.equals("Combat"))) {
         var2 = wc.X();
      }

      if (var2 == null
         && (var0.equals("Auto") || var0.equals("Crosshair"))
         && var1.field_1765 instanceof class_3966 var6
         && var6.method_17782() instanceof class_1309 var4) {
         var2 = var4;
      }

      return var2 != null
            && var2.method_5805()
            && var2.method_73183() == var1.field_1687
            && var1.field_1724 != null
            && var2.method_5858(var1.field_1724) < 4096.0
         ? var2
         : null;
   }
}
