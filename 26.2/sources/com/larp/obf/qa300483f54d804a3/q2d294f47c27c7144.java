package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.PerformanceModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ParticleStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public abstract class q2d294f47c27c7144 {
   @ModifyExpressionValue(
      method = {"calculateParticleLevel", "tickWeatherEffects"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0)
   )
   private Object u(Object var1) {
      return var1 instanceof ParticleStatus var2 ? PerformanceModule.A(var2) : var1;
   }
}
