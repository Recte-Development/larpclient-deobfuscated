package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.RemovalsModule;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class q84e84461eab1d2e5 {
   @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
   private void P(ParticleOptions var1, double var2, double var4, double var6, double var8, double var10, double var12, CallbackInfoReturnable<Particle> var14) {
      if (var1.getType() == ParticleTypes.RAIN && RemovalsModule.r("weather")) {
         var14.setReturnValue(null);
      }
   }
}
