package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public class qc26b177dbf698db0 {
   @ModifyExpressionValue(method = "extractTabList", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean h(boolean var1) {
      GUIAnimationModule var2 = GUIAnimationModule.F();
      return var2 != null && var2.K() ? var1 || var2.I() > 0.001 : var1;
   }
}
