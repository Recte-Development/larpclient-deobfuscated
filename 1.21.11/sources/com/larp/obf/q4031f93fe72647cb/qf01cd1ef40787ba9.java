package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.NoBlockRotationModule;
import net.minecraft.class_2338;
import net.minecraft.class_4970.class_4971;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_4971.class)
public abstract class qf01cd1ef40787ba9 {
   @Inject(method = "method_26190(Lnet/minecraft/class_2338;)J", at = @At("HEAD"), cancellable = true)
   private void M(class_2338 var1, CallbackInfoReturnable<Long> var2) {
      if (NoBlockRotationModule.M()) {
         var2.setReturnValue(42L);
      }
   }
}
