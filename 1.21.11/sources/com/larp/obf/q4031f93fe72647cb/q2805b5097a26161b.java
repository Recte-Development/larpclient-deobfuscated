package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wp;
import com.larpclient.module.FreecamModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_757.class)
public abstract class q2805b5097a26161b {
   @ModifyExpressionValue(method = "method_3172", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_5498;method_31034()Z"))
   private boolean s(boolean var1) {
      return wp.d() ? FreecamModule.q() : var1;
   }
}
