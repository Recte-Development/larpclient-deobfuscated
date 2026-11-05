package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public final class jb {
   private jb() {
   }

   public static void v(Component var0, boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null) {
         if (var1) {
            var2.gui.hud.setOverlayMessage(var0, false);
         } else {
            var2.gui.chatListener().handleSystemMessage(var0, false);
         }
      }
   }
}
