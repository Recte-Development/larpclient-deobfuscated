package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.NoSlowdownModule;
import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1297.class)
public abstract class q0d363fd82ae09f09 {
   @Inject(method = "method_5844", at = @At("HEAD"), cancellable = true)
   private void V(class_2680 var1, class_243 var2, CallbackInfo var3) {
      NoSlowdownModule var4 = NoSlowdownModule.k();
      if (this == class_310.method_1551().field_1724 && var4 != null && var4.a() && var1.method_27852(class_2246.field_10343)) {
         ((class_1297)this).method_38785();
         var3.cancel();
      }
   }

   @Inject(method = "method_23326", at = @At("RETURN"), cancellable = true)
   private void F(CallbackInfoReturnable<Float> var1) {
      NoSlowdownModule var2 = NoSlowdownModule.k();
      class_1297 var3 = (class_1297)this;
      if (var3 == class_310.method_1551().field_1724 && var2 != null && var2.p()) {
         class_2680 var4 = var3.method_73183().method_8320(var3.method_24515());
         class_2680 var5 = var3.method_73183().method_8320(var3.method_23314());
         if (var4.method_27852(class_2246.field_10114) || var4.method_26204().method_23349() == 1.0F && var5.method_27852(class_2246.field_10114)) {
            var1.setReturnValue(1.0F);
         }
      }
   }
}
