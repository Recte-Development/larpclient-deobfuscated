package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.NoSlowdownModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class qda845e10f6c5d7bd {
   @Inject(method = "itemUseSpeedMultiplier", at = @At("HEAD"), cancellable = true)
   private void J(CallbackInfoReturnable<Float> var1) {
      NoSlowdownModule var2 = NoSlowdownModule.J();
      if (var2 != null && var2.V()) {
         var1.setReturnValue(1.0F);
      }
   }
}
