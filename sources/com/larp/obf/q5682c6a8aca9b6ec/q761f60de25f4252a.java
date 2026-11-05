package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class q761f60de25f4252a {
   @ModifyExpressionValue(method = "extractTabList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean T(boolean var1) {
      GUIAnimationModule var2 = GUIAnimationModule.s();
      return var2 != null && var2.B() ? var1 || var2.B() > 0.001 : var1;
   }
}
