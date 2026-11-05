package com.larp.obf.q5682c6a8aca9b6ec;

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
public class q47a0b877d7bda6c9 {
   @Shadow
   private int noJumpDelay;

   @Inject(method = "aiStep()V", at = @At("HEAD"))
   private void I(CallbackInfo var1) {
      this.noJumpDelay = Math.min(this.noJumpDelay, NoJumpDelayModule.o((LivingEntity)this, this.noJumpDelay));
   }

   @ModifyConstant(method = "aiStep()V", constant = @Constant(intValue = 10), require = 1, allow = 1)
   private int u(int var1) {
      return NoJumpDelayModule.o((LivingEntity)this, var1);
   }
}
