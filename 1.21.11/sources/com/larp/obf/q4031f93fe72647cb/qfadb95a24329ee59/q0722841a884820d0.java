package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.ju;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_490;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_310.class)
public abstract class q0722841a884820d0 {
   @Inject(method = "method_1507", at = @At("HEAD"), cancellable = true)
   private void x(class_437 var1, CallbackInfo var2) {
      if (var1 instanceof class_490) {
         ju var3 = ju.b();
         if (var3.S()) {
            var2.cancel();
         } else {
            class_310 var4 = class_310.method_1551();
            if (var3.a() && var4.field_1755 == null && var4.field_1724 != null) {
               var4.field_1724.method_7346();
            }
         }
      }
   }
}
