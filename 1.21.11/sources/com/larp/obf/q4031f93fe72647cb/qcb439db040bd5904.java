package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.FakeFPSModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_11610;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_11610.class)
public class qcb439db040bd5904 {
   @ModifyExpressionValue(method = "method_72751", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_310;method_47599()I"))
   private int Q(int var1) {
      return FakeFPSModule.l(var1);
   }
}
