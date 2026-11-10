package com.larpclient;

import com.larpclient.module.LARPModule;
import com.larpclient.module.NameChangerModule;
import java.util.UUID;
import net.minecraft.class_310;
import net.minecraft.class_7920;
import net.minecraft.class_8685;
import net.minecraft.class_12079.class_10726;
import net.minecraft.class_12079.class_12081;

public final class he {
   public static ub.k X(UUID var0) {
      LARPModule var1 = LARPModule.f();
      return var1 != null && var1.isOperational() ? ub.o(var0, class_310.method_1551().method_1548().method_44717()) : null;
   }

   public static class_8685 a(UUID var0, class_8685 var1) {
      ub.k var2 = X(var0);
      return var2 == null
         ? var1
         : new class_8685(
            (class_12081)(var2.skin() == null ? var1.comp_1626() : new class_10726(var2.skin(), var2.skin())),
            (class_12081)(var2.hideCape() ? null : (var2.cape() == null ? var1.comp_1627() : new class_10726(var2.cape(), var2.cape()))),
            (class_12081)(var2.hideCape() ? null : (var2.cape() == null ? var1.comp_1628() : new class_10726(var2.cape(), var2.cape()))),
            var2.skin() == null ? var1.comp_1629() : (var2.slim() ? class_7920.field_41122 : class_7920.field_41123),
            var1.comp_1630()
         );
   }

   public static String n(UUID var0, String var1) {
      String var2 = x(var0);
      return var2.isEmpty() ? var1 : var2;
   }

   public static String x(UUID var0) {
      NameChangerModule var1 = NameChangerModule.o();
      String var2 = var1 == null ? "" : var1.u();
      if (!var2.isEmpty()) {
         class_310 var3 = class_310.method_1551();
         if (var0.equals(var3.method_1548().method_44717()) || var3.field_1724 != null && var0.equals(var3.field_1724.method_5667())) {
            return var2;
         }
      }

      ub.k var4 = X(var0);
      return var4 == null ? "" : var4.name();
   }

   public static boolean k() {
      LARPModule var0 = LARPModule.f();
      NameChangerModule var1 = NameChangerModule.o();
      return var0 != null && var0.isOperational() || var1 != null && var1.isOperational();
   }
}
