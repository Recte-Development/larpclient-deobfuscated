package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.NoSlowdownModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class qaaf091d25e1a4d5d {
   @Inject(method = "itemUseSpeedMultiplier", at = @At("HEAD"), cancellable = true)
   private void F(CallbackInfoReturnable<Float> var1) {
      NoSlowdownModule var2 = NoSlowdownModule.v();
      if (var2 != null && var2.D()) {
         var1.setReturnValue(1.0F);
      }
   }
}
