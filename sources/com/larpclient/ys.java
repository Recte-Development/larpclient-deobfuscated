package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface ys {
   void larpOpenMenuPopup();

   y0 larpMenuPopup();

   boolean larpDrawingMenuPopup();

   static y0 p(Screen var0) {
      return var0 instanceof ys var1 ? var1.larpMenuPopup() : null;
   }

   static r3.h Z(Screen var0) {
      if (Minecraft.getInstance().level != null) {
         return r3.h.REST;
      }

      y0 var1 = p(var0);
      return var1 == null ? r3.h.REST : var1.Z();
   }

   static boolean O() {
      Minecraft var0 = Minecraft.getInstance();
      fs var1 = LarpClient.y().v().E();
      return var1.o && var1.S != xb.s.C && (Double)var0.options.panoramaSpeed().get() > 0.0;
   }
}
