package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p_;
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
public class q197e18e478191104 {
   @ModifyExpressionValue(method = "extractCrosshair", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean l(boolean var1) {
      return p_.V() || var1;
   }

   @Inject(method = "extractCrosshair", at = @At("HEAD"), cancellable = true)
   private void N(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (CustomCrosshairModule.p()) {
         var3.cancel();
      }
   }
}
