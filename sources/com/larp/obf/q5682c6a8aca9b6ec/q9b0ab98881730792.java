package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.RodSwapModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class q9b0ab98881730792 {
   @Inject(method = "onScroll(JDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void Y(long var1, double var3, double var5, CallbackInfo var7) {
      RodSwapModule.c();
   }
}
