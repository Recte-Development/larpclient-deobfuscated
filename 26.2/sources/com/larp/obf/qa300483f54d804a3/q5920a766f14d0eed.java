package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.FakeLagModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class q5920a766f14d0eed {
   @Shadow
   private int positionReminder;
   @Shadow
   private float xRotLast;
   @Shadow
   private float yRotLast;

   @Inject(method = "sendPosition", at = @At("HEAD"))
   private void q(CallbackInfo var1) {
      if (FakeLagModule.B((LocalPlayer)this)) {
         this.positionReminder = 20;
         this.xRotLast = Float.NaN;
         this.yRotLast = Float.NaN;
      }
   }
}
