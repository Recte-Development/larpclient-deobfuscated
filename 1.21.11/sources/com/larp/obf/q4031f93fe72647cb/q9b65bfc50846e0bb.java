package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.FakePlayerModule;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_636.class)
public abstract class q9b65bfc50846e0bb {
   @Inject(
      method = "method_2918",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_634;method_52787(Lnet/minecraft/class_2596;)V"),
      require = 1,
      allow = 1
   )
   private void Y(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      if (var1 == class_310.method_1551().field_1724) {
         FakePlayerModule.a(var2);
      }
   }
}
