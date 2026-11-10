package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_746;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_759.class)
public abstract class qda2a208273b85291 {
   @Shadow
   private class_1799 field_4047;
   @Shadow
   private class_1799 field_4048;

   @Inject(method = "method_3215", at = @At("HEAD"), cancellable = true)
   private void M(class_1268 var1, CallbackInfo var2) {
      if (cz.T().H(var1)) {
         class_746 var3 = class_310.method_1551().field_1724;
         if (var1 == class_1268.field_5808) {
            this.field_4047 = var3.method_6047();
         } else {
            this.field_4048 = var3.method_6079();
         }

         var2.cancel();
      }
   }

   @Inject(method = "method_3220", at = @At("HEAD"))
   private void G(CallbackInfo var1) {
      cz var2 = cz.T();
      class_746 var3 = class_310.method_1551().field_1724;
      if (var2.H(class_1268.field_5808)) {
         this.field_4047 = var3.method_6047();
      }

      if (var2.H(class_1268.field_5810)) {
         this.field_4048 = var3.method_6079();
      }
   }
}
