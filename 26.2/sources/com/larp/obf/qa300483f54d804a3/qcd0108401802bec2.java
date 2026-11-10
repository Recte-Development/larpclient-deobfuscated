package com.larp.obf.qa300483f54d804a3;

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
public class qcd0108401802bec2 {
   @Shadow
   private int noJumpDelay;

   @Inject(method = "aiStep()V", at = @At("HEAD"))
   private void i(CallbackInfo var1) {
      this.noJumpDelay = Math.min(this.noJumpDelay, NoJumpDelayModule.b((LivingEntity)this, this.noJumpDelay));
   }

   @ModifyConstant(method = "aiStep()V", constant = @Constant(intValue = 10), require = 1, allow = 1)
   private int h(int var1) {
      return NoJumpDelayModule.b((LivingEntity)this, var1);
   }
}
