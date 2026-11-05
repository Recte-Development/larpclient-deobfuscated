package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.m1;
import com.larpclient.m5;
import com.larpclient.ma;
import com.larpclient.zs;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public class qb5667e066e652239 {
   @Inject(method = "apply(Lcom/mojang/renderpearl/api/device/GpuDevice;Lnet/minecraft/client/renderer/ShaderManager$PendingResults;)V", at = @At("TAIL"))
   private void u(CallbackInfo var1) {
      m5.s();
      ma.B();
      m1.f();
      zs.Q();
   }
}
