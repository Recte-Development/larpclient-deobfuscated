package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wi;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.class_12185;
import net.minecraft.class_761;
import net.minecraft.class_12185.class_12186;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_761.class)
public abstract class q13cd8dbf61b8d87f {
   @WrapOperation(method = "method_76647", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_12185;method_75558()Ljava/util/List;"))
   private List<class_12186> x(class_12185 var1, Operation<List<class_12186>> var2) {
      return wi.h((List<class_12186>)var2.call(new Object[]{var1}));
   }
}
