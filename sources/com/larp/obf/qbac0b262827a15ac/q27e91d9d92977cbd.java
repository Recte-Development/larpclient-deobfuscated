package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.NoJumpDelayModule;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class q27e91d9d92977cbd {
   @Shadow
   private int noJumpDelay;

   @Inject(method = "aiStep()V", at = @At("HEAD"))
   private void A(CallbackInfo var1) {
      this.noJumpDelay = Math.min(this.noJumpDelay, NoJumpDelayModule.T((LivingEntity)this, this.noJumpDelay));
   }

   @ModifyConstant(method = "aiStep()V", constant = @Constant(intValue = 10), require = 1, allow = 1)
   private int A(int var1) {
      return NoJumpDelayModule.T((LivingEntity)this, var1);
   }
}
