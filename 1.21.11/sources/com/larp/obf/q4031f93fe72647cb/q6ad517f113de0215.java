package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wi;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_757.class)
public abstract class q6ad517f113de0215 {
   @WrapOperation(method = "method_3192", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_47413(Lnet/minecraft/class_332;IIF)V"))
   private void P(class_437 var1, class_332 var2, int var3, int var4, float var5, Operation<Void> var6) {
      if (wi.Q(var1)) {
         var6.call(new Object[]{var1, wi.E(), var3, var4, var5});
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
         if (wi.W()) {
            var6.call(new Object[]{var1, wi.E(), var3, var4, var5});
         }
      }
   }
}
