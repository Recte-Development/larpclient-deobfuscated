package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.AimAssistModule;
import com.larpclient.module.WTapModule;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1657.class)
public class q132a7ec48ededcae {
   @Inject(method = "method_7324(Lnet/minecraft/class_1297;)V", at = @At("HEAD"))
   private void B(class_1297 var1, CallbackInfo var2) {
      WTapModule.w((class_1657)this, var1);
   }

   @Inject(
      method = "method_7324(Lnet/minecraft/class_1297;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1657;method_75124()V"),
      require = 1,
      allow = 1
   )
   private void z(class_1297 var1, CallbackInfo var2) {
      WTapModule.x((class_1657)this, var1);
      AimAssistModule.Z((class_1657)this, var1);
   }

   @Inject(method = "method_7324(Lnet/minecraft/class_1297;)V", at = @At("RETURN"))
   private void f(class_1297 var1, CallbackInfo var2) {
      WTapModule.K((class_1657)this, var1);
   }
}
