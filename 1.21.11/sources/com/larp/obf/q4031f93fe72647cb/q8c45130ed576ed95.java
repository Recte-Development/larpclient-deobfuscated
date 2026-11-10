package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wi;
import com.larpclient.module.CustomSkyModule;
import net.minecraft.class_243;
import net.minecraft.class_4063;
import net.minecraft.class_761;
import net.minecraft.class_9909;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_761.class)
public class q8c45130ed576ed95 {
   @Inject(method = "method_62204", at = @At("HEAD"), cancellable = true)
   private void x(class_9909 var1, class_4063 var2, class_243 var3, long var4, float var6, int var7, float var8, CallbackInfo var9) {
      if (CustomSkyModule.h() && !CustomSkyModule.M().w() && !wi.s()) {
         var9.cancel();
      }
   }
}
