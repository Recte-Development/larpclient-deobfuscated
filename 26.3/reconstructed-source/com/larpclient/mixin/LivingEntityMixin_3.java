package com.larpclient.mixin;

import com.larpclient.module.movement.NoJumpDelayModule;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin_3 {
   @Shadow
   private int noJumpDelay;

   @Inject(method = "aiStep()V", at = @At("HEAD"))
   private void w(CallbackInfo var1) {
      this.noJumpDelay = Math.min(this.noJumpDelay, NoJumpDelayModule.getJumpCooldown((LivingEntity)this, this.noJumpDelay));
   }

   @ModifyConstant(method = "aiStep()V", constant = @Constant(intValue = 10), require = 1, allow = 1)
   private int c(int var1) {
      return NoJumpDelayModule.getJumpCooldown((LivingEntity)this, var1);
   }
}
