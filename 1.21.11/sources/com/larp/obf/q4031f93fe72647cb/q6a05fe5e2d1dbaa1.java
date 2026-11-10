package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wi;
import com.larpclient.module.SkyShaderModule;
import net.minecraft.class_243;
import net.minecraft.class_4063;
import net.minecraft.class_9955;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_9955.class)
public class q6a05fe5e2d1dbaa1 {
   @Inject(method = "method_62168", at = @At("HEAD"), cancellable = true)
   private void v(int var1, class_4063 var2, float var3, class_243 var4, long var5, float var7, CallbackInfo var8) {
      if (!wi.s()) {
         SkyShaderModule var9 = SkyShaderModule.y();
         if (var9 != null && var9.O()) {
            var8.cancel();
         }
      }
   }
}
