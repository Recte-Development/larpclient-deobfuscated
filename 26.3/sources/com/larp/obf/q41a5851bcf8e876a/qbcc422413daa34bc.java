package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ij;
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
public class qbcc422413daa34bc {
   @ModifyExpressionValue(method = "extractCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean y(boolean var1) {
      return ij.y() || var1;
   }

   @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
   private void T(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (CustomCrosshairModule.K()) {
         var3.cancel();
      }
   }
}
