package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.iy;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1661;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_312.class)
public abstract class qcbb6d8c82d67578a {
   @WrapOperation(method = "method_1598", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_61496(I)V"))
   private void m(class_1661 var1, int var2, Operation<Void> var3) {
      if (iy.n()) {
         iy.S(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }

   @WrapOperation(method = "method_1598", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_67532()I"))
   private int J(class_1661 var1, Operation<Integer> var2) {
      return iy.p((Integer)var2.call(new Object[]{var1}));
   }
}
