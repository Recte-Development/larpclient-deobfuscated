package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.NoFallModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_2596;
import net.minecraft.class_634;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_746.class)
public class qa57292d4fcaa3838 {
   @ModifyArg(
      method = "method_3136",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2828$class_2830;<init>(Lnet/minecraft/class_243;FFZZ)V"),
      index = 3,
      require = 1
   )
   private boolean s(boolean var1) {
      return NoFallModule.V(var1);
   }

   @ModifyArg(
      method = "method_3136",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2828$class_2829;<init>(Lnet/minecraft/class_243;ZZ)V"),
      index = 1,
      require = 1
   )
   private boolean C(boolean var1) {
      return NoFallModule.V(var1);
   }

   @ModifyArg(method = "method_3136", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2828$class_2831;<init>(FFZZ)V"), index = 2, require = 1)
   private boolean R(boolean var1) {
      return NoFallModule.V(var1);
   }

   @ModifyArg(method = "method_3136", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2828$class_5911;<init>(ZZ)V"), index = 0, require = 1)
   private boolean j(boolean var1) {
      return NoFallModule.V(var1);
   }

   @WrapOperation(method = "method_6007", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_634;method_52787(Lnet/minecraft/class_2596;)V"))
   private void Z(class_634 var1, class_2596<?> var2, Operation<Void> var3) {
      if (!NoFallModule.g(var2)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "method_3136", at = @At("TAIL"))
   private void L(CallbackInfo var1) {
      NoFallModule.r((class_746)this);
   }
}
