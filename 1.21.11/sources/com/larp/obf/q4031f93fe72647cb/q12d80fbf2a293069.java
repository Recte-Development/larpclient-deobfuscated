package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.t9;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_746.class)
public abstract class q12d80fbf2a293069 {
   @Inject(method = "method_5705(F)F", at = @At("HEAD"), cancellable = true, require = 1)
   private void u(float var1, CallbackInfoReturnable<Float> var2) {
      float var3 = t9.r((class_746)this, var1);
      if (!Float.isNaN(var3)) {
         var2.setReturnValue(var3);
      }
   }

   @Inject(method = "method_5695(F)F", at = @At("HEAD"), cancellable = true, require = 1)
   private void b(float var1, CallbackInfoReturnable<Float> var2) {
      float var3 = t9.u((class_746)this, var1);
      if (!Float.isNaN(var3)) {
         var2.setReturnValue(var3);
      }
   }
}
