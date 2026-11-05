package com.larp.obf.q5682c6a8aca9b6ec.qffa8ebc9988eafc1;

import com.larpclient.f1;
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
public class q85c66625b0f78ff7 {
   @Inject(method = "getInstance", at = @At("RETURN"))
   private void W(Holder<WorldClock> var1, CallbackInfoReturnable<ClientClockInstance> var2) {
      if (var1.is(WorldClocks.OVERWORLD)) {
         ((f1)var2.getReturnValue()).larp$markOverworld();
      }
   }
}
