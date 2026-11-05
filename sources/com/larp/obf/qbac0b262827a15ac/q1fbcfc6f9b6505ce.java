package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.HitSoundsModule;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class q1fbcfc6f9b6505ce {
   @Inject(method = "play", at = @At("HEAD"), cancellable = true)
   private void v(SoundInstance var1, CallbackInfoReturnable<PlayResult> var2) {
      HitSoundsModule var3 = HitSoundsModule.o();
      if (var3 != null && var3.x(var1)) {
         var2.setReturnValue(PlayResult.NOT_STARTED);
      }
   }
}
