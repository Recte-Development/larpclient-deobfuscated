package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class qc772cd20e40061bd {
   @Inject(method = "grabMouse()V", at = @At("HEAD"), cancellable = true)
   private void C(CallbackInfo var1) {
      if (OrderDropperModule.j()) {
         var1.cancel();
      }
   }
}
