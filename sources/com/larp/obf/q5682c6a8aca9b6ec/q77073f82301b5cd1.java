package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.RemovalsModule;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.client.renderer.fog.environment.MobEffectFogEnvironment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class q77073f82301b5cd1 {
   @Inject(method = "getFogType", at = @At("RETURN"), cancellable = true)
   private void B(Camera var1, CallbackInfoReturnable<FogType> var2) {
      if (var2.getReturnValue() == FogType.WATER && RemovalsModule.s("water") || var2.getReturnValue() == FogType.LAVA && RemovalsModule.s("lava")) {
         var2.setReturnValue(FogType.ATMOSPHERIC);
      }
   }

   @Redirect(
      method = {"computeFogColor", "setupFog"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/fog/environment/FogEnvironment;isApplicable(Lnet/minecraft/world/level/material/FogType;Lnet/minecraft/world/entity/Entity;)Z"
      ),
      require = 2,
      allow = 2
   )
   private boolean G(FogEnvironment var1, FogType var2, Entity var3) {
      return (!(var1 instanceof MobEffectFogEnvironment) || !RemovalsModule.O()) && var1.isApplicable(var2, var3);
   }
}
