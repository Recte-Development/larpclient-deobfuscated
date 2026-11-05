package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class q34781c1f20e3babd {
   @Redirect(
      method = "tickEffects",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
      require = 1
   )
   private void x(Level var1, ParticleOptions var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (this != Minecraft.getInstance().player) {
         var1.addParticle(var2, var3, var5, var7, var9, var11, var13);
      }
   }
}
