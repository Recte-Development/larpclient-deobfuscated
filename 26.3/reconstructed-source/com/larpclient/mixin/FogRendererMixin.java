package com.larpclient.mixin;

import com.larpclient.module.render.RemovalsModule;
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
public class FogRendererMixin {
   @Inject(method = "getFogType", at = @At("RETURN"), cancellable = true)
   private void i(Camera var1, CallbackInfoReturnable<FogType> var2) {
      if (var2.getReturnValue() == FogType.WATER && RemovalsModule.isRemovalEnabled("water")
         || var2.getReturnValue() == FogType.LAVA && RemovalsModule.isRemovalEnabled("lava")) {
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
   private boolean H(FogEnvironment var1, FogType var2, Entity var3) {
      return (!(var1 instanceof MobEffectFogEnvironment) || !RemovalsModule.hasScreenEffectFilter()) && var1.isApplicable(var2, var3);
   }
}
