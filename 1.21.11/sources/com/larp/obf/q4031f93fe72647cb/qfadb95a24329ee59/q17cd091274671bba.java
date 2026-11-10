package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.ju;
import com.larpclient.q7;
import com.larpclient.tw;
import com.larpclient.module.at;
import net.minecraft.class_1533;
import net.minecraft.class_310;
import net.minecraft.class_3966;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_310.class)
public abstract class q17cd091274671bba {
   @Inject(method = "method_1583", at = @At("HEAD"), cancellable = true)
   private void S(CallbackInfo var1) {
      if (at.i(class_310.method_1551())) {
         var1.cancel();
      } else if (ju.b().B()) {
         var1.cancel();
      } else {
         class_310 var2 = class_310.method_1551();
         tw var3 = tw.s();
         if (!var3.W(var2) && !var3.E()) {
            if (var2.field_1724 != null
               && var2.field_1765 instanceof class_3966 var4
               && var4.method_17782() instanceof class_1533
               && q7.L().s(var2.field_1724.method_6047())) {
               var1.cancel();
            }
         } else {
            var1.cancel();
         }
      }
   }
}
