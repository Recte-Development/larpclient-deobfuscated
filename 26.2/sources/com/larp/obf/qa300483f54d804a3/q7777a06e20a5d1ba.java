package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Hud;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class q7777a06e20a5d1ba {
   @ModifyExpressionValue(
      method = "extractOverlayMessage",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/Hud;overlayMessageString:Lnet/minecraft/network/chat/Component;")
   )
   private Component C(Component var1) {
      return FakePayModule.g(var1, true);
   }
}
