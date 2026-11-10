package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.pw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class q68de9a78b716508e {
   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void k(Screen var1, CallbackInfo var2) {
      if (var1 instanceof InventoryScreen) {
         pw var3 = pw.S();
         if (var3.C()) {
            var2.cancel();
         } else {
            Minecraft var4 = Minecraft.getInstance();
            if (var3.X() && var4.gui.screen() == null && var4.player != null) {
               var4.player.closeContainer();
            }
         }
      }
   }
}
