package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.PerformanceModule;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_761.class)
public abstract class qb75aea66a4902ab5 {
   @ModifyArg(method = "method_72917", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1297;method_5840(D)V"))
   private double D(double var1) {
      return PerformanceModule.z(var1);
   }
}
