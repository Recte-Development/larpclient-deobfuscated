package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.RemovalsModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class q761ce530aaa10b54 {
   @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
   private void L(CallbackInfoReturnable<Boolean> var1) {
      if (((Entity)this).level().isClientSide() && RemovalsModule.r("glowing")) {
         var1.setReturnValue(false);
      }
   }
}
