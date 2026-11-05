package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.PerformanceModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ParticleStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public abstract class qf07dd5d7ce1c90c9 {
   @ModifyExpressionValue(
      method = {"calculateParticleLevel", "tickWeatherEffects"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0)
   )
   private Object r(Object var1) {
      return var1 instanceof ParticleStatus var2 ? PerformanceModule.m(var2) : var1;
   }
}
