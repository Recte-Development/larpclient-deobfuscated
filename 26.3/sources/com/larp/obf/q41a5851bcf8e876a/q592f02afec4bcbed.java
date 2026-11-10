package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class q592f02afec4bcbed {
   @Inject(method = "grabMouse()V", at = @At("HEAD"), cancellable = true)
   private void Z(CallbackInfo var1) {
      if (OrderDropperModule.l()) {
         var1.cancel();
      }
   }
}
