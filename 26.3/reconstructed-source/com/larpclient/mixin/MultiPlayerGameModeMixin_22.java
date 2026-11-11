package com.larpclient.mixin;

import com.larpclient.util.inventory.MapSwapInventory;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin_22 {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void F(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (!MapSwapInventory.canUseSelected(var2)) {
         var3.cancel();
      } else {
         MapSwapInventory.update();
      }
   }
}
