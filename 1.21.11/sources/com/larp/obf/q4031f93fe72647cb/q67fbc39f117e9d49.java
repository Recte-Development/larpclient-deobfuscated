package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.NoSlowdownModule;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_746.class)
public class q67fbc39f117e9d49 {
   @Inject(method = "method_75410", at = @At("HEAD"), cancellable = true)
   private void e(CallbackInfoReturnable<Float> var1) {
      NoSlowdownModule var2 = NoSlowdownModule.k();
      if (var2 != null && var2.H()) {
         var1.setReturnValue(1.0F);
      }
   }
}
