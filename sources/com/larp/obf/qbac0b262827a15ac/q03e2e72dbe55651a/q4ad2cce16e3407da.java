package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.c6;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class q4ad2cce16e3407da {
   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void o(Screen var1, CallbackInfo var2) {
      if (var1 instanceof InventoryScreen) {
         c6 var3 = c6.K();
         if (var3.j()) {
            var2.cancel();
         } else {
            Minecraft var4 = Minecraft.getInstance();
            if (var3.q() && var4.gui.screen() == null && var4.player != null) {
               var4.player.closeContainer();
            }
         }
      }
   }
}
