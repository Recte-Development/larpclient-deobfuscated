package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p2;
import com.larpclient.module.SkyShaderModule;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CloudRenderer.class)
public class q356dad023bcfb1fb {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void U(int var1, CloudStatus var2, float var3, int var4, Vec3 var5, long var6, float var8, CallbackInfo var9) {
      if (!p2.p()) {
         SkyShaderModule var10 = SkyShaderModule.t();
         if (var10 != null && var10.Q()) {
            var9.cancel();
         }
      }
   }
}
