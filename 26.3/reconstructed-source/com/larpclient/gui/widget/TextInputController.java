package com.larpclient.gui.widget;

import com.larpclient.gui.screen.LarpTitleScreen;
import com.larpclient.gui.screen.WidgetScreen;
import com.larpclient.module.misc.WorldMenuModule;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.platform.TextInputManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public final class TextInputController {
   private static final Object iNPUT_OWNER = new Object();
   private static String activeScreenName;

   private TextInputController() {
   }

   public static void update() {
      Minecraft var0 = Minecraft.getInstance();
      String var1 = getTextInputScreenName(var0);
      TextInputManager var2 = var0.textInputManager();
      if (var1 != null) {
         var2.startTextInput(iNPUT_OWNER);
         if (!var1.equals(activeScreenName)) {
            LarpLog.debug("GUI", "Text input on for {}", var1);
         }

         activeScreenName = var1;
      } else if (activeScreenName != null) {
         var2.stopTextInput(iNPUT_OWNER);
         LarpLog.debug("GUI", "Text input off after {}", activeScreenName);
         activeScreenName = null;
      }
   }

   private static String getTextInputScreenName(Minecraft var0) {
      Screen var1 = var0.gui.screen();
      if (!(var1 instanceof WidgetScreen) && !(var1 instanceof LarpTitleScreen)) {
         return WorldMenuModule.isOccluded() ? "World Menu" : null;
      } else {
         return var1.getClass().getSimpleName();
      }
   }
}
