package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.RemovalsModule;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class q844c0bbea3abca66 {
   @Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
   private void E(CallbackInfoReturnable<Boolean> var1) {
      if (((Entity)this).level().isClientSide() && RemovalsModule.G("glowing")) {
         var1.setReturnValue(false);
      }
   }
}
