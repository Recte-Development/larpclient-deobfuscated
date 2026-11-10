package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wp;
import com.larpclient.module.FreecamModule;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1297.class)
public abstract class q1ba4eae92e59b30d {
   @Inject(method = "method_5756", at = @At("HEAD"), cancellable = true)
   private void c(class_1657 var1, CallbackInfoReturnable<Boolean> var2) {
      if (wp.d() && FreecamModule.z() && this == class_310.method_1551().field_1724 && var1 == this) {
         var2.setReturnValue(false);
      }
   }
}
