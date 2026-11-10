package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.cr;
import com.larpclient.module.AutoClickerModule;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_304;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_310.class)
public abstract class q2970322d21bd761f {
   @Inject(method = "method_1536", at = @At("HEAD"), cancellable = true)
   private void G(CallbackInfoReturnable<Boolean> var1) {
      AutoClickerModule var2 = AutoClickerModule.s();
      if (var2 != null && var2.X() && !var2.T() && !cr.j()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "method_1583", at = @At("HEAD"), cancellable = true)
   private void e(CallbackInfo var1) {
      AutoClickerModule var2 = AutoClickerModule.s();
      if (var2 != null && var2.h() && !var2.T() && !cr.j()) {
         var1.cancel();
      }
   }

   @WrapMethod(method = "method_1590")
   private void j(boolean var1, Operation<Void> var2) {
      AutoClickerModule var3 = AutoClickerModule.s();
      if (var3 == null) {
         var2.call(new Object[]{var1});
      } else {
         var3.f(var1x -> var2.call(new Object[]{var1x}), var1);
      }
   }

   @WrapOperation(method = "method_1508", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_304;method_1434()Z"))
   private boolean Y(class_304 var1, Operation<Boolean> var2) {
      AutoClickerModule var3 = AutoClickerModule.s();
      return (Boolean)var2.call(new Object[]{var1}) || var1 == class_310.method_1551().field_1690.field_1904 && var3 != null && var3.D();
   }
}
