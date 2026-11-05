package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.RemovalsModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class qf8467b6d2f49c9be {
   @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
   private void d(CallbackInfoReturnable<Boolean> var1) {
      if (((Entity)this).level().isClientSide() && RemovalsModule.s("glowing")) {
         var1.setReturnValue(false);
      }
   }
}
