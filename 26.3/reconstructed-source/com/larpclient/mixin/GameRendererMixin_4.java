package com.larpclient.mixin;

import com.larpclient.core.StreamProofManager;
import com.larpclient.render.entity.EchoHandRenderer;
import com.larpclient.render.entity.HandChamsRenderer;
import com.larpclient.render.overlay.ViewModelOutline;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin_4 {
   @WrapOperation(
      method = "render3dHud",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lcom/mojang/renderpearl/api/textures/GpuTextureView;)V"
      )
   )
   private void m(GameRenderer var1, CameraRenderState var2, PlayerRenderState var3, GpuTextureView var4, Operation<Void> var5) {
      if (var2.isPanoramicMode) {
         EchoHandRenderer.release();
         HandChamsRenderer.reset();
         ViewModelOutline.close();
      }

      boolean var6 = !var2.isPanoramicMode && StreamProofManager.statusText();
      if (var6) {
         StreamProofManager.notifyUser();

         try {
            var5.call(new Object[]{var1, var2, var3, var4});
         } finally {
            StreamProofManager.getOverlayGraphics();
         }
      }

      boolean var7 = !var2.isPanoramicMode && HandChamsRenderer.begin();
      boolean var8 = !var2.isPanoramicMode && EchoHandRenderer.beginCapture();
      boolean var9 = !var2.isPanoramicMode && ViewModelOutline.beginFrame();
      boolean var10 = false;

      try {
         if (var6) {
            StreamProofManager.runInLayer(() -> var5.call(new Object[]{var1, var2, var3, var4}));
         } else {
            var5.call(new Object[]{var1, var2, var3, var4});
         }

         var10 = true;
      } finally {
         if (var7) {
            if (var10) {
               HandChamsRenderer.end();
            } else {
               HandChamsRenderer.reset();
            }
         }

         if (var8) {
            if (var10) {
               EchoHandRenderer.finishFrame();
            } else {
               EchoHandRenderer.reset();
            }
         }

         if (var9) {
            if (var10) {
               ViewModelOutline.render();
            } else {
               ViewModelOutline.close();
            }
         }

         if (var6) {
            StreamProofManager.createOverlay();
         }
      }
   }
}
