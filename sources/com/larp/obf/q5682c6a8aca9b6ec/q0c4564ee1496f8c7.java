package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.jk;
import com.larpclient.jv;
import com.larpclient.ly;
import com.larpclient.m7;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public class q0c4564ee1496f8c7 {
   @Inject(method = "apply(Lcom/mojang/renderpearl/api/device/GpuDevice;Lnet/minecraft/client/renderer/ShaderManager$PendingResults;)V", at = @At("TAIL"))
   private void f(CallbackInfo var1) {
      jk.H();
      m7.V();
      ly.d();
      jv.M();
   }
}
