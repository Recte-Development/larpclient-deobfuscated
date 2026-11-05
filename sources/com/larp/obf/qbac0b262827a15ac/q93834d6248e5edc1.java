package com.larp.obf.qbac0b262827a15ac;

import com.larpclient._r;
import com.larpclient.module.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
public class q93834d6248e5edc1 {
   @ModifyExpressionValue(
      method = "extractRenderState",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;footer:Lnet/minecraft/network/chat/Component;")
   )
   private Component e(Component var1) {
      return FakePayModule.h(var1);
   }

   @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
   private void Q(PlayerInfo var1, CallbackInfoReturnable<Component> var2) {
      var2.setReturnValue(_r.L(var1.getProfile().id(), var1.getProfile().name(), (Component)var2.getReturnValue()));
   }
}
