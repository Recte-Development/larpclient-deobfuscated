package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.RodSwapModule;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class qb93eb47eb20e9091 {
   @Inject(method = "onScroll(JDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void V(long var1, double var3, double var5, CallbackInfo var7) {
      RodSwapModule.f();
   }
}
