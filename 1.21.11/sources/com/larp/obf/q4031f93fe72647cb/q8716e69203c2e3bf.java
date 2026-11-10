package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.er;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_332.class)
public class q8716e69203c2e3bf {
   @ModifyVariable(method = "method_51439(Lnet/minecraft/class_327;Lnet/minecraft/class_2561;IIIZ)V", at = @At("HEAD"), argsOnly = true)
   private class_2561 o(class_2561 var1) {
      return er.P(var1);
   }
}
