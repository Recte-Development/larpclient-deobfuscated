package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.iy;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_329.class)
public abstract class q872decc589eb8794 {
   @WrapOperation(method = "method_1759", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_5438(I)Lnet/minecraft/class_1799;"))
   private class_1799 e(class_1661 var1, int var2, Operation<class_1799> var3) {
      int var4 = iy.n() ? iy.E(var2) : var2;
      return var4 < 0 ? class_1799.field_8037 : iy.I(var4, (class_1799)var3.call(new Object[]{var1, var4}));
   }

   @WrapOperation(method = "method_1759", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_67532()I"))
   private int g(class_1661 var1, Operation<Integer> var2) {
      return iy.p((Integer)var2.call(new Object[]{var1}));
   }
}
