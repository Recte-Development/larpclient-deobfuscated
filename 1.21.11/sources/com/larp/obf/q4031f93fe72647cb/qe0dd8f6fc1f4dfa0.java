package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_329.class)
public class qe0dd8f6fc1f4dfa0 {
   @ModifyExpressionValue(method = "method_55804", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_304;method_1434()Z"))
   private boolean c(boolean var1) {
      GUIAnimationModule var2 = GUIAnimationModule.Z();
      return var2 != null && var2.c() ? var1 || var2.L() > 0.001 : var1;
   }
}
