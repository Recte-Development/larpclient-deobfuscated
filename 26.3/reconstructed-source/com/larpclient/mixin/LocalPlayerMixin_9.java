package com.larpclient.mixin;

import com.larpclient.module.exploit.FakeLagModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin_9 {
   @Shadow
   private int positionReminder;
   @Shadow
   private float xRotLast;
   @Shadow
   private float yRotLast;

   @Inject(method = "sendPosition", at = @At("HEAD"))
   private void w(CallbackInfo var1) {
      if (FakeLagModule.consumeResync((LocalPlayer)this)) {
         this.positionReminder = 20;
         this.xRotLast = Float.NaN;
         this.yRotLast = Float.NaN;
      }
   }
}
