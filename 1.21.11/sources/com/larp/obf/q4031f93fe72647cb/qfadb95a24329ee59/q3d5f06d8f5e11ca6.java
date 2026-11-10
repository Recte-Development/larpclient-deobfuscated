package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.ju;
import com.larpclient.tw;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_746.class)
public class q3d5f06d8f5e11ca6 {
   @Inject(method = "method_7290", at = @At("HEAD"), cancellable = true)
   private void U(boolean var1, CallbackInfoReturnable<Boolean> var2) {
      if (ju.b().S() || tw.s().x()) {
         var2.setReturnValue(false);
      }
   }
}
