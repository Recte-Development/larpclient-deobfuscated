package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.wj;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class qf22f3508ead99192 {
   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void g(Screen var1, CallbackInfo var2) {
      if (var1 instanceof InventoryScreen) {
         wj var3 = wj.m();
         if (var3.Q()) {
            var2.cancel();
         } else {
            Minecraft var4 = Minecraft.getInstance();
            if (var3.M() && var4.gui.screen() == null && var4.player != null) {
               var4.player.closeContainer();
            }
         }
      }
   }
}
