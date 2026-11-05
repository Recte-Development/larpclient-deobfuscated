package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class qde6b9ddd4306ec1b {
   @ModifyExpressionValue(
      method = "extractOverlayMessage",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Hud;overlayMessageString:Lnet/minecraft/network/chat/Component;")
   )
   private Component U(Component var1) {
      return FakePayModule.g(var1, true);
   }
}
