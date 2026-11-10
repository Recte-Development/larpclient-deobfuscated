package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RemovalsModule;
import net.minecraft.class_2394;
import net.minecraft.class_2398;
import net.minecraft.class_702;
import net.minecraft.class_703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_702.class)
public class qecbb5b921643272b {
   @Inject(method = "method_3056", at = @At("HEAD"), cancellable = true)
   private void h(class_2394 var1, double var2, double var4, double var6, double var8, double var10, double var12, CallbackInfoReturnable<class_703> var14) {
      if (var1.method_10295() == class_2398.field_11242 && RemovalsModule.o("weather")) {
         var14.setReturnValue(null);
      }
   }
}
