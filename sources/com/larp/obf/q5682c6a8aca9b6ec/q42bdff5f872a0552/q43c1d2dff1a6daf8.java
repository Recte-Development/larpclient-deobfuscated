package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.ro;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class q43c1d2dff1a6daf8 {
   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void y(Screen var1, CallbackInfo var2) {
      if (var1 instanceof InventoryScreen) {
         ro var3 = ro.H();
         if (var3.p()) {
            var2.cancel();
         } else {
            Minecraft var4 = Minecraft.getInstance();
            if (var3.h() && var4.gui.screen() == null && var4.player != null) {
               var4.player.closeContainer();
            }
         }
      }
   }
}
