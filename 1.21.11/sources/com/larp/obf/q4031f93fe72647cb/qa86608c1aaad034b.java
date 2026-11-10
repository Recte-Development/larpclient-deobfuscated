package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.XRayModule;
import net.minecraft.class_2464;
import net.minecraft.class_4970.class_4971;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_4971.class)
public class qa86608c1aaad034b {
   @Inject(method = "method_26216", at = @At("HEAD"), cancellable = true)
   private void Z(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.m()) {
         class_4971 var2 = (class_4971)this;
         if (!XRayModule.r(var2.method_26204())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "method_26225", at = @At("HEAD"), cancellable = true)
   private void R(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.m()) {
         class_4971 var2 = (class_4971)this;
         if (!XRayModule.r(var2.method_26204())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "method_26217", at = @At("HEAD"), cancellable = true)
   private void A(CallbackInfoReturnable<class_2464> var1) {
      if (XRayModule.m()) {
         class_4971 var2 = (class_4971)this;
         if (!XRayModule.r(var2.method_26204())) {
            var1.setReturnValue(class_2464.field_11455);
         }
      }
   }
}
