package com.larpclient.mixin;

import com.larpclient.render.entity.EchoHandRenderer;
import com.larpclient.render.entity.FlameHandsRenderer;
import com.larpclient.render.model.SwordModels;
import com.larpclient.render.overlay.PostProcessing;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public class ShaderManagerMixin {
   @Inject(method = "apply(Lcom/mojang/renderpearl/api/device/GpuDevice;Lnet/minecraft/client/renderer/ShaderManager$PendingResults;)V", at = @At("TAIL"))
   private void C(CallbackInfo var1) {
      PostProcessing.onResourceReload();
      FlameHandsRenderer.reset();
      EchoHandRenderer.release();
      SwordModels.loadAll();
   }
}
