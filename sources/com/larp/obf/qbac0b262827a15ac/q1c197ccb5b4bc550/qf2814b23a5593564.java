package com.larp.obf.qbac0b262827a15ac.q1c197ccb5b4bc550;

import com.larpclient.x8;
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
public class qf2814b23a5593564 {
   @Inject(method = "getInstance", at = @At("RETURN"))
   private void H(Holder<WorldClock> var1, CallbackInfoReturnable<ClientClockInstance> var2) {
      if (var1.is(WorldClocks.OVERWORLD)) {
         ((x8)var2.getReturnValue()).larp$markOverworld();
      }
   }
}
