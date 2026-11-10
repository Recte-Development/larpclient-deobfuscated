package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.PerformanceModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_310;
import net.minecraft.class_4063;
import net.minecraft.class_6597;
import net.minecraft.class_7172;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_761.class)
public abstract class q8fac3383ca75c41a {
   @WrapOperation(method = "method_3269", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_7172;method_41753()Ljava/lang/Object;"))
   private Object Q(class_7172<?> var1, Operation<Object> var2) {
      Object var3 = var2.call(new Object[]{var1});
      return var1 == class_310.method_1551().field_1690.method_41798() ? PerformanceModule.b((class_6597)var3) : var3;
   }

   @ModifyExpressionValue(method = "method_22710", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_315;method_1632()Lnet/minecraft/class_4063;"))
   private class_4063 v(class_4063 var1) {
      return PerformanceModule.D(var1);
   }
}
