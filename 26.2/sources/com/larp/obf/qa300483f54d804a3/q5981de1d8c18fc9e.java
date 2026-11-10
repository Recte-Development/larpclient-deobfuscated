package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.NoWeatherModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class q5981de1d8c18fc9e {
   @Inject(method = {"getRainLevel", "getThunderLevel"}, at = @At("HEAD"), cancellable = true)
   private void a(float var1, CallbackInfoReturnable<Float> var2) {
      if (this instanceof ClientLevel && NoWeatherModule.R()) {
         var2.setReturnValue(0.0F);
      }
   }
}
