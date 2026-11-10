package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class q36052a9dddb6a56d {
   @Inject(method = "grabMouse()V", at = @At("HEAD"), cancellable = true)
   private void u(CallbackInfo var1) {
      if (OrderDropperModule.d()) {
         var1.cancel();
      }
   }
}
