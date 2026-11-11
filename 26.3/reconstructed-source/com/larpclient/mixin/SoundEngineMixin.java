package com.larpclient.mixin;

import com.larpclient.module.misc.HitSoundsModule;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
   @Inject(method = "play", at = @At("HEAD"), cancellable = true)
   private void X(SoundInstance var1, CallbackInfoReturnable<PlayResult> var2) {
      HitSoundsModule var3 = HitSoundsModule.getInstance();
      if (var3 != null && var3.shouldMute(var1)) {
         var2.setReturnValue(PlayResult.NOT_STARTED);
      }
   }
}
