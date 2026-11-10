package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.fb;
import com.larpclient.nl;
import com.larpclient.oj;
import com.larpclient.pm;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public class q5d9a00dd30cc5ffe {
   @Inject(method = "apply(Lcom/mojang/renderpearl/api/device/GpuDevice;Lnet/minecraft/client/renderer/ShaderManager$PendingResults;)V", at = @At("TAIL"))
   private void C(CallbackInfo var1) {
      nl.I();
      oj.G();
      pm.C();
      fb.L();
   }
}
