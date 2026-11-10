package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.i;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_2596;
import net.minecraft.class_2846;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_2846.class_2847;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_310.class)
public class q7c680a912ce1b1a7 {
   @WrapOperation(method = "method_1508", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_634;method_52787(Lnet/minecraft/class_2596;)V"))
   private void c(class_634 var1, class_2596<?> var2, Operation<Void> var3) {
      if (!(var2 instanceof class_2846 var4 && var4.method_12363() == class_2847.field_12969 && i.H(class_310.method_1551()))) {
         var3.call(new Object[]{var1, var2});
      }
   }
}
