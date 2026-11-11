package com.larpclient.gui.misc;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.util.math.ChargeRamp;
import com.larpclient.util.misc.DisplayFormat;
import com.larpclient.util.render.PressAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface MenuPopupHost {
   void larpOpenMenuPopup();

   ChargeRamp larpMenuPopup();

   boolean larpDrawingMenuPopup();

   static ChargeRamp getPopup(Screen var0) {
      return var0 instanceof MenuPopupHost var1 ? var1.larpMenuPopup() : null;
   }

   static PressAnimation.Transform getPopupState(Screen var0) {
      if (Minecraft.getInstance().level != null) {
         return PressAnimation.Transform.REST;
      }

      ChargeRamp var1 = getPopup(var0);
      return var1 == null ? PressAnimation.Transform.REST : var1.getPose();
   }

   static boolean isPanoramaAnimated() {
      Minecraft var0 = Minecraft.getInstance();
      ClientPreferences var1 = LarpClient.getInstance().getConfigManager().getPreferences();
      return var1.motion && var1.menuQuality != DisplayFormat.MenuQuality.OFF && (Double)var0.options.panoramaSpeed().get() > 0.0;
   }
}
