package com.larpclient.mixin;

import com.larpclient.module.player.OrderDropperModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
   @Inject(method = "grabMouse()V", at = @At("HEAD"), cancellable = true)
   private void Z(CallbackInfo var1) {
      if (OrderDropperModule.isAfkActive()) {
         var1.cancel();
      }
   }
}
