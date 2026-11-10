package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.i;
import net.minecraft.class_1533;
import net.minecraft.class_1799;
import net.minecraft.class_2940;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1533.class)
public class qbf7c69925c778332 {
   @Shadow
   @Final
   private static class_2940<class_1799> field_7130;

   @Inject(method = "method_5674(Lnet/minecraft/class_2940;)V", at = @At("HEAD"))
   private void P(class_2940<?> var1, CallbackInfo var2) {
      class_1533 var3 = (class_1533)this;
      if (var3.method_73183().method_8608() && field_7130.equals(var1)) {
         i.O(var3);
      }
   }

   @Inject(method = "method_6940", at = @At("RETURN"), cancellable = true)
   private void n(CallbackInfoReturnable<class_1799> var1) {
      class_1533 var2 = (class_1533)this;
      if (var2.method_73183().method_8608()) {
         class_1799 var3 = i.t(var2);
         if (var3 != null) {
            var1.setReturnValue(var3);
         }
      }
   }
}
