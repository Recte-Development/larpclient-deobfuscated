package com.larpclient;

import com.larpclient.module.FreecamModule;
import com.larpclient.module.Module;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_10185;
import net.minecraft.class_243;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_3675.class_306;
import org.lwjgl.glfw.GLFW;

public final class j {
   public static boolean F(String var0) {
      LarpClient var1 = LarpClient.b();
      if (var1 != null && var1.G() != null) {
         Module var2 = var1.G().j(var0);
         return var2 != null && var2.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean k() {
      class_310 var0 = class_310.method_1551();
      return var0.field_1687 != null && var0.field_1724 != null && var0.field_1724.method_5805() && !wp.d();
   }

   public static boolean t() {
      return k();
   }

   public static boolean W(class_304 var0) {
      class_310 var1 = class_310.method_1551();
      class_306 var2 = KeyBindingHelper.getBoundKeyOf(var0);
      if (var2.method_1444() < 0) {
         return false;
      }

      return (boolean)(switch (var2.method_1442()) {
         case field_1668 -> GLFW.glfwGetKey(var1.method_22683().method_4490(), s2.H(var2.method_1444())) == 1 ? 1 : 0;
         case field_1672 -> GLFW.glfwGetMouseButton(var1.method_22683().method_4490(), s2.I(var2.method_1444())) == 1 ? 1 : 0;
         default -> 0;
      });
   }

   public static class_243 i() {
      class_315 var0 = class_310.method_1551().field_1690;
      return I(var0.field_1894.method_1434(), var0.field_1881.method_1434(), var0.field_1913.method_1434(), var0.field_1849.method_1434());
   }

   public static class_243 h() {
      if (!wp.d()) {
         return i();
      }

      class_10185 var0 = FreecamModule.b();
      return I(var0.comp_3159(), var0.comp_3160(), var0.comp_3161(), var0.comp_3162());
   }

   private static class_243 I(boolean var0, boolean var1, boolean var2, boolean var3) {
      class_310 var4 = class_310.method_1551();
      double var5 = (var0 ? 1 : 0) - (var1 ? 1 : 0);
      double var7 = (var2 ? 1 : 0) - (var3 ? 1 : 0);
      double var9 = Math.toRadians(ff.H(var4.field_1724.method_36454()));
      return new class_243(-Math.sin(var9) * var5 + Math.cos(var9) * var7, 0.0, Math.cos(var9) * var5 + Math.sin(var9) * var7).method_1029();
   }

   public static class_243 N(class_243 var0, class_243 var1, double var2) {
      class_243 var4 = var1.method_1020(var0);
      return var0.method_1019(var4.method_1033() > var2 ? var4.method_1029().method_1021(var2) : var4);
   }

   private j() {
   }
}
