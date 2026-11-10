package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.PerformanceModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_4066;
import net.minecraft.class_638;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_638.class)
public abstract class q409fc5b653a1c4e0 {
   @ModifyExpressionValue(
      method = "method_74918",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_7172;method_41753()Ljava/lang/Object;", ordinal = 0)
   )
   private Object U(Object var1) {
      return var1 instanceof class_4066 var2 ? PerformanceModule.Y(var2) : var1;
   }

   @Mixin(class_761.class)
   public abstract static class qa77dc1105d4804b9 {
      @ModifyExpressionValue(
         method = "method_3252",
         at = @At(value = "INVOKE", target = "Lnet/minecraft/class_7172;method_41753()Ljava/lang/Object;", ordinal = 0)
      )
      private Object w(Object var1) {
         return var1 instanceof class_4066 var2 ? PerformanceModule.Y(var2) : var1;
      }
   }
}
