package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class qc312a91eb11db1c5 {
   @ModifyExpressionValue(method = "extractTabList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean m(boolean var1) {
      GUIAnimationModule var2 = GUIAnimationModule.a();
      return var2 != null && var2.K() ? var1 || var2.u() > 0.001 : var1;
   }
}
