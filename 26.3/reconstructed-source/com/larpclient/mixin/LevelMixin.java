package com.larpclient.mixin;

import com.larpclient.module.render.NoWeatherModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {
   @Inject(method = {"getRainLevel", "getThunderLevel"}, at = @At("HEAD"), cancellable = true)
   private void n(float var1, CallbackInfoReturnable<Float> var2) {
      if (this instanceof ClientLevel && NoWeatherModule.isActive()) {
         var2.setReturnValue(0.0F);
      }
   }
}
