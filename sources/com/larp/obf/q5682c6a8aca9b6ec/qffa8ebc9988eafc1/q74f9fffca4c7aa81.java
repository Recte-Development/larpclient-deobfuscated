package com.larp.obf.q5682c6a8aca9b6ec.qffa8ebc9988eafc1;

import com.larpclient.f1;
import com.larpclient.module.TimeChangerModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.ClientClockManager$ClientClockInstance")
public class q74f9fffca4c7aa81 implements f1 {
   @Unique
   private boolean b;

   @Override
   public void larp$markOverworld() {
      this.b = true;
   }

   @Inject(method = "totalTicks", at = @At("RETURN"), cancellable = true)
   private void E(CallbackInfoReturnable<Long> var1) {
      if (this.b) {
         TimeChangerModule var2 = TimeChangerModule.p();
         if (var2 != null && var2.isRuntimeActive()) {
            var1.setReturnValue(TimeChangerModule.f((Long)var1.getReturnValue(), var2.a()));
         }
      }
   }
}
