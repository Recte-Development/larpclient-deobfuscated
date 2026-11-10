package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RemovalsModule;
import net.minecraft.class_2960;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_329.class)
public class q9f5d25d953215067 {
   @Inject(method = "method_55803", at = @At("HEAD"), cancellable = true)
   private void h(class_332 var1, class_9779 var2, CallbackInfo var3) {
      if (RemovalsModule.o("scoreboard")) {
         var3.cancel();
      }
   }

   @Inject(method = "method_1746", at = @At("HEAD"), cancellable = true)
   private void M(class_332 var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.o("portal")) {
         var3.cancel();
      }
   }

   @Inject(method = "method_61980", at = @At("HEAD"), cancellable = true)
   private void Q(class_332 var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.o("nausea")) {
         var3.cancel();
      }
   }

   @Inject(method = "method_31977", at = @At("HEAD"), cancellable = true)
   private void F(class_332 var1, class_2960 var2, float var3, CallbackInfo var4) {
      if (RemovalsModule.o("pumpkin") && var2.equals(class_2960.method_60656("textures/misc/pumpkinblur.png"))) {
         var4.cancel();
      }
   }
}
