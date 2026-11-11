package com.larpclient.mixin;

import com.larpclient.module.render.TimeChangerModule;
import com.larpclient.render.world.OverworldMarker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.client.ClientClockManager$ClientClockInstance")
public class ClientClockManagerClientClockInstanceMixin implements OverworldMarker {
   @Unique
   private boolean b;

   @Override
   public void larp$markOverworld() {
      this.b = true;
   }

   @Inject(method = "totalTicks", at = @At("RETURN"), cancellable = true)
   private void n(CallbackInfoReturnable<Long> var1) {
      if (this.b) {
         TimeChangerModule var2 = TimeChangerModule.getInstance();
         if (var2 != null) {
            long var3 = (Long)var1.getReturnValue();
            long var5 = var2.getAdjustedTime(var3);
            if (var5 != var3) {
               var1.setReturnValue(var5);
            }
         }
      }
   }
}
