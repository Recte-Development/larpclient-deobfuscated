package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.er;
import com.larpclient.module.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_2561;
import net.minecraft.class_355;
import net.minecraft.class_640;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_355.class)
public class qd44542d75d0b2236 {
   @ModifyExpressionValue(method = "method_1919", at = @At(value = "FIELD", target = "Lnet/minecraft/class_355;field_2154:Lnet/minecraft/class_2561;"))
   private class_2561 g(class_2561 var1) {
      return FakePayModule.x(var1);
   }

   @Inject(method = "method_1918", at = @At("RETURN"), cancellable = true)
   private void d(class_640 var1, CallbackInfoReturnable<class_2561> var2) {
      var2.setReturnValue(er.J(var1.method_2966().id(), var1.method_2966().name(), (class_2561)var2.getReturnValue()));
   }
}
