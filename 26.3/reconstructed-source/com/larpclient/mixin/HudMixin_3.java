package com.larpclient.mixin;

import com.larpclient.module.render.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class HudMixin_3 {
   @ModifyExpressionValue(method = "extractTabList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean G(boolean var1) {
      GUIAnimationModule var2 = GUIAnimationModule.getInstance();
      return var2 != null && var2.isPlayerListAnimationActive() ? var1 || var2.getPlayerListProgress() > 0.001 : var1;
   }
}
