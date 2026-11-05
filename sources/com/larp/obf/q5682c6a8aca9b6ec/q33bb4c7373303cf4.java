package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.NoWeatherModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class q33bb4c7373303cf4 {
   @Inject(method = {"getRainLevel", "getThunderLevel"}, at = @At("HEAD"), cancellable = true)
   private void G(float var1, CallbackInfoReturnable<Float> var2) {
      if (this instanceof ClientLevel && NoWeatherModule.b()) {
         var2.setReturnValue(0.0F);
      }
   }
}
