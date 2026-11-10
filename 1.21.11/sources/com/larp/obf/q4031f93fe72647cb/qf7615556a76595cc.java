package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RemovalsModule;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1297.class)
public class qf7615556a76595cc {
   @Inject(method = "method_5851", at = @At("RETURN"), cancellable = true)
   private void K(CallbackInfoReturnable<Boolean> var1) {
      if (((class_1297)this).method_73183().method_8608() && RemovalsModule.o("glowing")) {
         var1.setReturnValue(false);
      }
   }
}
