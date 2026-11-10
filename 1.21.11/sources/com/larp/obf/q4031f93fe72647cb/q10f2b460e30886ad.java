package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.cr;
import com.larpclient.wp;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoToolModule;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_636.class)
public class q10f2b460e30886ad {
   @Shadow
   private void method_2911() {
      throw new AssertionError();
   }

   @Inject(method = "method_2918", at = @At("HEAD"), cancellable = true)
   private void k(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      if (!cr.G(var2)) {
         var3.cancel();
      }
   }

   @Inject(method = {"method_2896", "method_2919", "method_2905", "method_2917"}, at = @At("HEAD"), cancellable = true)
   private void P(CallbackInfoReturnable<class_1269> var1) {
      if (AuraModule.d() || !cr.l()) {
         var1.setReturnValue(class_1269.field_5811);
      }
   }

   @Inject(method = "method_75407", at = @At("HEAD"), cancellable = true)
   private void g(CallbackInfo var1) {
      if (!cr.l()) {
         var1.cancel();
      }
   }

   @Inject(method = {"method_2910", "method_2902"}, at = @At("HEAD"), cancellable = true)
   private void U(class_2338 var1, class_2350 var2, CallbackInfoReturnable<Boolean> var3) {
      if (wp.d()) {
         var3.setReturnValue(false);
      } else if (AutoToolModule.M(var1)) {
         this.method_2911();
      }
   }
}
