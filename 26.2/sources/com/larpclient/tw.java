package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface tw {
   void larpOpenMenuPopup();

   kt larpMenuPopup();

   boolean larpDrawingMenuPopup();

   static kt t(Screen var0) {
      return var0 instanceof tw var1 ? var1.larpMenuPopup() : null;
   }

   static mb.d E(Screen var0) {
      if (Minecraft.getInstance().level != null) {
         return mb.d.REST;
      }

      kt var1 = t(var0);
      return var1 == null ? mb.d.REST : var1.V();
   }

   static boolean D() {
      Minecraft var0 = Minecraft.getInstance();
      hb var1 = LarpClient.R().o().g();
      return var1.L && var1.o != j.v.A && (Double)var0.options.panoramaSpeed().get() > 0.0;
   }
}
