package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class q5f1955b1589c8ec9 {
   @Inject(method = "grabMouse()V", at = @At("HEAD"), cancellable = true)
   private void w(CallbackInfo var1) {
      if (OrderDropperModule.H()) {
         var1.cancel();
      }
   }
}
