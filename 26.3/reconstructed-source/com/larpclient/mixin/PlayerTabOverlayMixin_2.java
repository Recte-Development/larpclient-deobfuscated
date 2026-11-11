package com.larpclient.mixin;

import com.larpclient.config.DisplayAliases;
import com.larpclient.module.exploit.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin_2 {
   @ModifyExpressionValue(
      method = "extractRenderState",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;footer:Lnet/minecraft/network/chat/Component;")
   )
   private Component H(Component var1) {
      return FakePayModule.buildErrorMessage(var1);
   }

   @Inject(method = "getNameForDisplay", at = @At("RETURN"), cancellable = true)
   private void n(PlayerInfo var1, CallbackInfoReturnable<Component> var2) {
      var2.setReturnValue(DisplayAliases.rewriteForPlayer(var1.getProfile().id(), var1.getProfile().name(), (Component)var2.getReturnValue()));
   }
}
