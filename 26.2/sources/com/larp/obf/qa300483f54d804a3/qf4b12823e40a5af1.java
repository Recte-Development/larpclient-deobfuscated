package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.HitSoundsModule;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundEngine.PlayResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class qf4b12823e40a5af1 {
   @Inject(method = "play", at = @At("HEAD"), cancellable = true)
   private void j(SoundInstance var1, CallbackInfoReturnable<PlayResult> var2) {
      HitSoundsModule var3 = HitSoundsModule.v();
      if (var3 != null && var3.E(var1)) {
         var2.setReturnValue(PlayResult.NOT_STARTED);
      }
   }
}
