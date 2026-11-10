package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.RodSwapModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class q7316e3eb2501a250 {
   @Inject(method = "onScroll(JDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void o(long var1, double var3, double var5, CallbackInfo var7) {
      RodSwapModule.q();
   }
}
