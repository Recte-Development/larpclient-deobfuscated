package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.TotemHitModule;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_636.class)
public abstract class q623a6a01740c7650 {
   @Inject(method = "method_2918", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_636;method_2911()V"), require = 1, allow = 1)
   private void u(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      if (var1 == class_310.method_1551().field_1724 && var1 instanceof class_746 var4) {
         TotemHitModule.h(var4, var2);
      }
   }
}
