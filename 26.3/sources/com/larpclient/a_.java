package com.larpclient;

import com.larpclient.module.WorldMenuModule;
import com.mojang.blaze3d.platform.TextInputManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public final class a_ {
   private static final Object F = new Object();
   private static String E;

   private a_() {
   }

   public static void v() {
      Minecraft var0 = Minecraft.getInstance();
      String var1 = l(var0);
      TextInputManager var2 = var0.textInputManager();
      if (var1 != null) {
         var2.startTextInput(F);
         if (!var1.equals(E)) {
            _q.c("GUI", "Text input on for {}", var1);
         }

         E = var1;
      } else if (E != null) {
         var2.stopTextInput(F);
         _q.c("GUI", "Text input off after {}", E);
         E = null;
      }
   }

   private static String l(Minecraft var0) {
      Screen var1 = var0.gui.screen();
      if (!(var1 instanceof fs) && !(var1 instanceof de)) {
         return WorldMenuModule.Y() ? "World Menu" : null;
      } else {
         return var1.getClass().getSimpleName();
      }
   }
}
