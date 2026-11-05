package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._7;
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
public class qcc5066286ac18fe7 {
   @ModifyExpressionValue(
      method = "extractRenderState",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;footer:Lnet/minecraft/network/chat/Component;")
   )
   private Component X(Component var1) {
      return FakePayModule.k(var1);
   }

   @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
   private void G(PlayerInfo var1, CallbackInfoReturnable<Component> var2) {
      var2.setReturnValue(_7.f(var1.getProfile().id(), var1.getProfile().name(), (Component)var2.getReturnValue()));
   }
}
