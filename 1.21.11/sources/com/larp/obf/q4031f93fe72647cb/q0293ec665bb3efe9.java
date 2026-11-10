package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.HitFXModule;
import net.minecraft.class_10042;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_922.class)
public class q0293ec665bb3efe9 {
   @Inject(method = "method_62484", at = @At("RETURN"), cancellable = true)
   private void L(class_10042 var1, CallbackInfoReturnable<Integer> var2) {
      HitFXModule var3 = HitFXModule.H();
      if (var1.field_53460 && var3 != null && var3.v()) {
         var2.setReturnValue(var3.m());
      }
   }
}
