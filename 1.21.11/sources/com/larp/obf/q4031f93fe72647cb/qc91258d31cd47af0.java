package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wp;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_744.class)
public class qc91258d31cd47af0 {
   @Inject(method = "method_64054", at = @At("HEAD"), cancellable = true)
   private void p(CallbackInfo var1) {
      if (wp.d()) {
         var1.cancel();
      }
   }
}
