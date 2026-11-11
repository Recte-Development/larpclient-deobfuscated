package com.larpclient.mixin;

import com.larpclient.module.exploit.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class HudMixin_4 {
   @ModifyExpressionValue(
      method = "extractOverlayMessage",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Hud;overlayMessageString:Lnet/minecraft/network/chat/Component;")
   )
   private Component i(Component var1) {
      return FakePayModule.logSimulationFailure(var1, true);
   }
}
