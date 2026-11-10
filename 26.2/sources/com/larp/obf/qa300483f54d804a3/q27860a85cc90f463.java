package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class q27860a85cc90f463 {
   @Redirect(
      method = "tickEffects",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"),
      require = 1
   )
   private void y(Level var1, ParticleOptions var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (this != Minecraft.getInstance().player) {
         var1.addParticle(var2, var3, var5, var7, var9, var11, var13);
      }
   }
}
