package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.rr;
import com.larpclient.module.CustomCrosshairModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class qec76bd66ef5af295 {
   @ModifyExpressionValue(method = "extractCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean B(boolean var1) {
      return rr.o() || var1;
   }

   @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
   private void i(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (CustomCrosshairModule.S()) {
         var3.cancel();
      }
   }
}
