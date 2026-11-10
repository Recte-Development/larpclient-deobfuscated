package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.NoSlowdownModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class qf716818d4e1cb54b {
   @Inject(method = "itemUseSpeedMultiplier", at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfoReturnable<Float> var1) {
      NoSlowdownModule var2 = NoSlowdownModule.Q();
      if (var2 != null && var2.Q()) {
         var1.setReturnValue(1.0F);
      }
   }
}
