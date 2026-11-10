package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.TimeChangerModule;
import net.minecraft.class_1937;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1937.class)
public class q8aa931314e3410c7 {
   @Inject(method = "method_8532", at = @At("RETURN"), cancellable = true)
   private void S(CallbackInfoReturnable<Long> var1) {
      TimeChangerModule var2 = TimeChangerModule.e();
      if (this instanceof class_638 && var2 != null) {
         long var3 = (Long)var1.getReturnValue();
         long var5 = var2.A(var3);
         if (var5 != var3) {
            var1.setReturnValue(var5);
         }
      }
   }
}
