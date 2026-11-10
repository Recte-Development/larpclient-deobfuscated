package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_9959;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_9959.class)
public abstract class q2d32fd13422e35e3 {
   @WrapOperation(
      method = "method_62194",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2248;method_9531(I)Lnet/minecraft/class_2680;", ordinal = 0)
   )
   private class_2680 L(int var1, Operation<class_2680> var2, int var3, class_2338 var4, int var5) {
      return cz.T().G(var4, (class_2680)var2.call(new Object[]{var1}));
   }
}
