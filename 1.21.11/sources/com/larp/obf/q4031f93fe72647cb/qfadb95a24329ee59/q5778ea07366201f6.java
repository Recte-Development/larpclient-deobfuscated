package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.bk;
import com.larpclient.il;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1661;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_312.class)
public class q5778ea07366201f6 {
   @WrapOperation(method = "method_1598", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_67532()I"))
   private int V(class_1661 var1, Operation<Integer> var2) {
      return bk.W().E() ? bk.W().i() : il.I();
   }
}
