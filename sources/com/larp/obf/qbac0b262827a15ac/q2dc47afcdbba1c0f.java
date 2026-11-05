package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.FakeLagModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class q2dc47afcdbba1c0f {
   @Shadow
   private int positionReminder;
   @Shadow
   private float xRotLast;
   @Shadow
   private float yRotLast;

   @Inject(method = "sendPosition", at = @At("HEAD"))
   private void G(CallbackInfo var1) {
      if (FakeLagModule.X((LocalPlayer)this)) {
         this.positionReminder = 20;
         this.xRotLast = Float.NaN;
         this.yRotLast = Float.NaN;
      }
   }
}
