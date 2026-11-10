package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2586;
import net.minecraft.class_2636;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1937.class)
public abstract class qc3c7ececc057a4dc {
   @Inject(method = "method_8321", at = @At("HEAD"), cancellable = true)
   private void A(class_2338 var1, CallbackInfoReturnable<class_2586> var2) {
      if (this instanceof class_638 var3) {
         class_2636 var5 = cz.T().u(var3, var1);
         if (var5 != null) {
            var2.setReturnValue(var5);
         }
      }
   }
}
