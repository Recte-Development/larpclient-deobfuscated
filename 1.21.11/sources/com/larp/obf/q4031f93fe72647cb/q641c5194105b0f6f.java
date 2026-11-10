package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.HitSoundsModule;
import net.minecraft.class_1113;
import net.minecraft.class_1140;
import net.minecraft.class_1140.class_11518;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1140.class)
public class q641c5194105b0f6f {
   @Inject(method = "method_4854", at = @At("HEAD"), cancellable = true)
   private void A(class_1113 var1, CallbackInfoReturnable<class_11518> var2) {
      HitSoundsModule var3 = HitSoundsModule.B();
      if (var3 != null && var3.D(var1)) {
         var2.setReturnValue(class_11518.field_60956);
      }
   }
}
