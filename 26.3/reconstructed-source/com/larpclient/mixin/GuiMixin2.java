package com.larpclient.mixin;

import com.larpclient.util.misc.FiftyFiftyDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin2 {
   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void g(Screen var1, CallbackInfo var2) {
      if (var1 instanceof InventoryScreen) {
         FiftyFiftyDispenser var3 = FiftyFiftyDispenser.getInstance();
         if (var3.shouldBlockInput()) {
            var2.cancel();
         } else {
            Minecraft var4 = Minecraft.getInstance();
            if (var3.isMenuSynced() && var4.gui.screen() == null && var4.player != null) {
               var4.player.closeContainer();
            }
         }
      }
   }
}
