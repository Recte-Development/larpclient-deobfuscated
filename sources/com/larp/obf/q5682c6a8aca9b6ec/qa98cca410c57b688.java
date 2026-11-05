package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.PerformanceModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ParticleStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public abstract class qa98cca410c57b688 {
   @ModifyExpressionValue(
      method = {"calculateParticleLevel", "tickWeatherEffects"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0)
   )
   private Object x(Object var1) {
      return var1 instanceof ParticleStatus var2 ? PerformanceModule.i(var2) : var1;
   }
}
