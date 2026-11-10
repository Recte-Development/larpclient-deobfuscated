package com.larp.obf.q41a5851bcf8e876a.q040cb7981516518c;

import com.larpclient.fd;
import com.larpclient.module.TimeChangerModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.ClientClockManager$ClientClockInstance")
public class qb475da745ab6e6f4 implements fd {
   @Unique
   private boolean b;

   @Override
   public void larp$markOverworld() {
      this.b = true;
   }

   @Inject(method = "totalTicks", at = @At("RETURN"), cancellable = true)
   private void n(CallbackInfoReturnable<Long> var1) {
      if (this.b) {
         TimeChangerModule var2 = TimeChangerModule.p();
         if (var2 != null) {
            long var3 = (Long)var1.getReturnValue();
            long var5 = var2.c(var3);
            if (var5 != var3) {
               var1.setReturnValue(var5);
            }
         }
      }
   }
}
