package com.larp.obf.qbac0b262827a15ac.q1c197ccb5b4bc550;

import com.larpclient.x8;
import com.larpclient.module.TimeChangerModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.ClientClockManager$ClientClockInstance")
public class qd4bf9d73ef00ece9 implements x8 {
   @Unique
   private boolean E;

   @Override
   public void larp$markOverworld() {
      this.E = true;
   }

   @Inject(method = "totalTicks", at = @At("RETURN"), cancellable = true)
   private void e(CallbackInfoReturnable<Long> var1) {
      if (this.E) {
         TimeChangerModule var2 = TimeChangerModule.u();
         if (var2 != null && var2.isRuntimeActive()) {
            var1.setReturnValue(TimeChangerModule.g((Long)var1.getReturnValue(), var2.D()));
         }
      }
   }
}
