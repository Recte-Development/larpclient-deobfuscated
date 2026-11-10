package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.RodSwapModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class q97c6304b7326d1ef {
   @Inject(method = "onScroll(JDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void h(long var1, double var3, double var5, CallbackInfo var7) {
      RodSwapModule.P();
   }
}
