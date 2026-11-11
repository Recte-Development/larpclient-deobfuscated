package com.larpclient.mixin;

import com.larpclient.module.player.PhantomItemManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin_22 {
   @Inject(method = "closeContainer", at = @At("HEAD"))
   private void t(CallbackInfo var1) {
      PhantomItemManager.returnCarried(Minecraft.getInstance());
   }
}
