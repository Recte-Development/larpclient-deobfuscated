package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/class_846$class_851")
public class q2388611a7fbfbb9f {
   @Shadow
   private long field_64231;

   @ModifyReturnValue(method = "method_76298", at = @At("RETURN"))
   private float Z(float var1, long var2) {
      GUIAnimationModule var4 = GUIAnimationModule.Z();
      return var4 != null && var4.isEnabled() && !var4.w.getValue() && var4.O() ? (float)var4.I((double)(var2 - this.field_64231) / var4.H()) : var1;
   }
}
