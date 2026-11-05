package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.NoWeatherModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class qe2b1261d9d69cdfc {
   @Inject(method = {"getRainLevel", "getThunderLevel"}, at = @At("HEAD"), cancellable = true)
   private void R(float var1, CallbackInfoReturnable<Float> var2) {
      if (this instanceof ClientLevel && NoWeatherModule.F()) {
         var2.setReturnValue(0.0F);
      }
   }
}
