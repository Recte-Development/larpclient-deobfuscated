package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.HitFXModule;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class q7a14d345e15c54a3 {
   @Inject(method = "createParticle", at = @At("HEAD"), cancellable = true)
   private void N(ParticleOptions var1, double var2, double var4, double var6, double var8, double var10, double var12, CallbackInfoReturnable<Particle> var14) {
      ParticleType var15 = var1.getType();
      HitFXModule var16 = HitFXModule.q();
      if (var16 != null && var16.isRuntimeActive()) {
         if (var15 == ParticleTypes.SMOKE
            || var15 == ParticleTypes.LARGE_SMOKE
            || var15 == ParticleTypes.WHITE_SMOKE
            || var15 == ParticleTypes.CAMPFIRE_COSY_SMOKE
            || var15 == ParticleTypes.CAMPFIRE_SIGNAL_SMOKE
            || var15 == ParticleTypes.CRIT
            || var15 == ParticleTypes.ENCHANTED_HIT
            || var15 == ParticleTypes.DAMAGE_INDICATOR
            || var15 == ParticleTypes.SWEEP_ATTACK) {
            var14.setReturnValue(null);
         }
      }
   }
}
