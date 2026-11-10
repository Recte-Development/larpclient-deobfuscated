package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class q9be474bbedd0e6c2 {
   @ModifyExpressionValue(method = "extractTabList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean G(boolean var1) {
      GUIAnimationModule var2 = GUIAnimationModule.O();
      return var2 != null && var2.t() ? var1 || var2.S() > 0.001 : var1;
   }
}
