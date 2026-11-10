package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RemovalsModule;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_638.class)
public class q872f5c53409af77f {
   @Inject(method = "method_31595", at = @At("HEAD"), cancellable = true)
   private void S(class_2338 var1, class_2680 var2, CallbackInfo var3) {
      if (RemovalsModule.o("breakParticles")) {
         var3.cancel();
      }
   }

   @Inject(method = "method_74254", at = @At("HEAD"), cancellable = true)
   private void g(class_2338 var1, class_2350 var2, CallbackInfo var3) {
      if (RemovalsModule.o("breakParticles")) {
         var3.cancel();
      }
   }
}
