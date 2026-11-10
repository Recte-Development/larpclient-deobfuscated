package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.il;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1533;
import net.minecraft.class_1799;
import net.minecraft.class_915;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_915.class)
public class qc7a7562dc4d906c7 {
   @WrapOperation(
      method = "method_62473(Lnet/minecraft/class_1533;Lnet/minecraft/class_10040;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1533;method_6940()Lnet/minecraft/class_1799;")
   )
   private class_1799 R(class_1533 var1, Operation<class_1799> var2) {
      return il.o(var1, (class_1799)var2.call(new Object[]{var1}));
   }
}
