package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.TimeChangerModule;
import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.minecraft.world.clock.WorldClocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientClockManager.class)
public class qb475615f913d5cfb {
   @Inject(method = "getTotalTicks", at = @At("RETURN"), cancellable = true)
   private void g(Holder<WorldClock> var1, CallbackInfoReturnable<Long> var2) {
      TimeChangerModule var3 = TimeChangerModule.H();
      if (var3 != null && var1.is(WorldClocks.OVERWORLD)) {
         long var4 = (Long)var2.getReturnValue();
         long var6 = var3.k(var4);
         if (var6 != var4) {
            var2.setReturnValue(var6);
         }
      }
   }
}
