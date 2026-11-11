package com.larpclient.mixin;

import com.larpclient.module.misc.PerformanceModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ParticleStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin_3 {
   @ModifyExpressionValue(
      method = {"calculateParticleLevel", "tickWeatherEffects"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0)
   )
   private Object b(Object var1) {
      return var1 instanceof ParticleStatus var2 ? PerformanceModule.limitParticles(var2) : var1;
   }
}
