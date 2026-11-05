package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tq;
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
public class qd1d07c4e2f6b45d9 {
   @ModifyExpressionValue(method = "extractCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean y(boolean var1) {
      return tq.f() || var1;
   }

   @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
   private void S(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (CustomCrosshairModule.I()) {
         var3.cancel();
      }
   }
}
