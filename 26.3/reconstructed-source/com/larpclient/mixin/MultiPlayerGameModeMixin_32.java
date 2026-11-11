package com.larpclient.mixin;

import com.larpclient.module.player.PhantomItemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin_32 {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void m(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (PhantomItemManager.dropHeldItem(Minecraft.getInstance(), var2)) {
         var3.cancel();
      }
   }
}
