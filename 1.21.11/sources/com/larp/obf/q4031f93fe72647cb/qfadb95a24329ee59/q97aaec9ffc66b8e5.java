package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.e7;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_746.class)
public class q97aaec9ffc66b8e5 {
   @WrapOperation(method = "method_3136", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_36454()F"))
   private float r(class_746 var1, Operation<Float> var2) {
      return e7.J(var1);
   }

   @WrapOperation(method = "method_3136", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_36455()F"))
   private float w(class_746 var1, Operation<Float> var2) {
      return e7.w(var1);
   }

   @Inject(method = "method_3136", at = @At("RETURN"))
   private void j(CallbackInfo var1) {
      e7.u((class_746)this);
   }
}
