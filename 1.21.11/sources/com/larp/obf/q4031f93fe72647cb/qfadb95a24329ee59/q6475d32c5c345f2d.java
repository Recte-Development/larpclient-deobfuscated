package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.i;
import net.minecraft.class_310;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_746.class)
public abstract class q6475d32c5c345f2d {
   @Inject(method = "method_7290", at = @At("HEAD"), cancellable = true)
   private void X(boolean var1, CallbackInfoReturnable<Boolean> var2) {
      if (i.E(class_310.method_1551(), var1)) {
         var2.setReturnValue(true);
      }
   }
}
