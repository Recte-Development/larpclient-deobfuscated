package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.FakeLagModule;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class qf71d40a87aef2b79 {
   @Shadow
   private int positionReminder;
   @Shadow
   private float xRotLast;
   @Shadow
   private float yRotLast;

   @Inject(method = "sendPosition", at = @At("HEAD"))
   private void w(CallbackInfo var1) {
      if (FakeLagModule.W((LocalPlayer)this)) {
         this.positionReminder = 20;
         this.xRotLast = Float.NaN;
         this.yRotLast = Float.NaN;
      }
   }
}
