package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2586;
import net.minecraft.class_2636;
import net.minecraft.class_2680;
import net.minecraft.class_853;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_853.class)
public abstract class q11c34aed0442aa55 {
   @Shadow
   @Final
   private class_1937 field_4490;

   @Inject(method = "method_8320", at = @At("RETURN"), cancellable = true)
   private void s(class_2338 var1, CallbackInfoReturnable<class_2680> var2) {
      var2.setReturnValue(cz.T().i(var1, cz.T().G(var1, (class_2680)var2.getReturnValue())));
   }

   @Inject(method = "method_8321", at = @At("HEAD"), cancellable = true)
   private void S(class_2338 var1, CallbackInfoReturnable<class_2586> var2) {
      class_2636 var3 = cz.T().u(this.field_4490, var1);
      if (var3 != null) {
         var2.setReturnValue(var3);
      }
   }
}
