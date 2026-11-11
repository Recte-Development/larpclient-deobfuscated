package com.larpclient.mixin;

import com.larpclient.render.world.OverworldMarker;
import net.minecraft.client.ClientClockManager;
import net.minecraft.client.ClientClockManager.ClientClockInstance;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientClockManager.class)
public class ClientClockManagerMixin {
   @Inject(method = "getInstance", at = @At("RETURN"))
   private void b(Holder<WorldClock> var1, CallbackInfoReturnable<ClientClockInstance> var2) {
      if (var1.is(WorldClocks.OVERWORLD)) {
         ((OverworldMarker)var2.getReturnValue()).larp$markOverworld();
      }
   }
}
