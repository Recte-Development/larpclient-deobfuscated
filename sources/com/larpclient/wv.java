package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public interface wv {
   void larpOpenMenuPopup();

   xt larpMenuPopup();

   boolean larpDrawingMenuPopup();

   static xt P(Screen var0) {
      return var0 instanceof wv var1 ? var1.larpMenuPopup() : null;
   }

   static m8.w K(Screen var0) {
      if (Minecraft.getInstance().level != null) {
         return m8.w.REST;
      }

      xt var1 = P(var0);
      return var1 == null ? m8.w.REST : var1.j();
   }

   static boolean X() {
      Minecraft var0 = Minecraft.getInstance();
      xv var1 = LarpClient.F().f().t();
      return var1.V && var1.A != tf.f.E && (Double)var0.options.panoramaSpeed().get() > 0.0;
   }
}
