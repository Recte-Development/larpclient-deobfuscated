package com.larpclient.mixin;

import com.larpclient.module.movement.NoSlowdownModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin_8 {
   @Inject(method = "itemUseSpeedMultiplier", at = @At("HEAD"), cancellable = true)
   private void J(CallbackInfoReturnable<Float> var1) {
      NoSlowdownModule var2 = NoSlowdownModule.getInstance();
      if (var2 != null && var2.shouldIgnoreItemSlowdown()) {
         var1.setReturnValue(1.0F);
      }
   }
}
