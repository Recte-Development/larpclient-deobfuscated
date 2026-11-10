package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface sd {
   void larpOpenMenuPopup();

   dc larpMenuPopup();

   boolean larpDrawingMenuPopup();

   static dc S(Screen var0) {
      return var0 instanceof sd var1 ? var1.larpMenuPopup() : null;
   }

   static fr.z R(Screen var0) {
      if (Minecraft.getInstance().level != null) {
         return fr.z.REST;
      }

      dc var1 = S(var0);
      return var1 == null ? fr.z.REST : var1.h();
   }

   static boolean Z() {
      Minecraft var0 = Minecraft.getInstance();
      i3 var1 = LarpClient.N().h().k();
      return var1.w && var1.Jw != ng.c.T && (Double)var0.options.panoramaSpeed().get() > 0.0;
   }
}
