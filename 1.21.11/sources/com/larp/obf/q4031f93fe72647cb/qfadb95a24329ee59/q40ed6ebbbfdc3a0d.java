package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.il;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_746.class)
public class q40ed6ebbbfdc3a0d {
   @Inject(method = "method_7290", at = @At("HEAD"), cancellable = true)
   private void O(boolean var1, CallbackInfoReturnable<Boolean> var2) {
      if (!il.X(var1)) {
         var2.setReturnValue(false);
      } else {
         il.w();
      }
   }
}
