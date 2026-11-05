package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.HitSoundsModule;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class q488c2ef61eb38c47 {
   @Inject(method = "play", at = @At("HEAD"), cancellable = true)
   private void d(SoundInstance var1, CallbackInfoReturnable<PlayResult> var2) {
      HitSoundsModule var3 = HitSoundsModule.q();
      if (var3 != null && var3.H(var1)) {
         var2.setReturnValue(PlayResult.NOT_STARTED);
      }
   }
}
