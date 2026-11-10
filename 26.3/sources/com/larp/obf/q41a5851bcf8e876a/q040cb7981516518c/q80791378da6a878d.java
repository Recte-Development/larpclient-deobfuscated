package com.larp.obf.q41a5851bcf8e876a.q040cb7981516518c;

import com.larpclient.fd;
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
public class q80791378da6a878d {
   @Inject(method = "getInstance", at = @At("RETURN"))
   private void b(Holder<WorldClock> var1, CallbackInfoReturnable<ClientClockInstance> var2) {
      if (var1.is(WorldClocks.OVERWORLD)) {
         ((fd)var2.getReturnValue()).larp$markOverworld();
      }
   }
}
