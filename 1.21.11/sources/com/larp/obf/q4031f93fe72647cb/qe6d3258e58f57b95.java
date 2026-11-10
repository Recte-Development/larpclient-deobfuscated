package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_312.class)
public abstract class qe6d3258e58f57b95 {
   @Inject(method = "method_1612()V", at = @At("HEAD"), cancellable = true)
   private void L(CallbackInfo var1) {
      if (OrderDropperModule.R()) {
         var1.cancel();
      }
   }
}
