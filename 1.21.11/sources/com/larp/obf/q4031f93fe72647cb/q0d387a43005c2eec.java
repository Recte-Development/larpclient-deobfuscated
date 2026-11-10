package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_2561;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_329.class)
public class q0d387a43005c2eec {
   @ModifyExpressionValue(method = "method_55800", at = @At(value = "FIELD", target = "Lnet/minecraft/class_329;field_2018:Lnet/minecraft/class_2561;"))
   private class_2561 a(class_2561 var1) {
      return FakePayModule.X(var1, true);
   }
}
