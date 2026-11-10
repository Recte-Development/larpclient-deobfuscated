package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.NoWeatherModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class qabfa0c399d244e95 {
   @Inject(method = {"getRainLevel", "getThunderLevel"}, at = @At("HEAD"), cancellable = true)
   private void n(float var1, CallbackInfoReturnable<Float> var2) {
      if (this instanceof ClientLevel && NoWeatherModule.U()) {
         var2.setReturnValue(0.0F);
      }
   }
}
