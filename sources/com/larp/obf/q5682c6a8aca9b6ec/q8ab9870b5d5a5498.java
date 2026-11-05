package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._5;
import com.larpclient.dy;
import com.larpclient.ly;
import com.larpclient.y8;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class q8ab9870b5d5a5498 {
   @WrapOperation(
      method = "render3dHud",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lcom/mojang/renderpearl/api/textures/GpuTextureView;)V"
      )
   )
   private void z(GameRenderer var1, CameraRenderState var2, PlayerRenderState var3, GpuTextureView var4, Operation<Void> var5) {
      if (var2.isPanoramicMode) {
         ly.d();
         dy.z();
         _5.Q();
      }

      boolean var6 = !var2.isPanoramicMode && y8.Q();
      if (var6) {
         y8.A();

         try {
            var5.call(new Object[]{var1, var2, var3, var4});
         } finally {
            y8.k();
         }
      }

      boolean var7 = !var2.isPanoramicMode && dy.t();
      boolean var8 = !var2.isPanoramicMode && ly.Z();
      boolean var9 = !var2.isPanoramicMode && _5.k();
      boolean var10 = false;

      try {
         if (var6) {
            y8.y(() -> var5.call(new Object[]{var1, var2, var3, var4}));
         } else {
            var5.call(new Object[]{var1, var2, var3, var4});
         }

         var10 = true;
      } finally {
         if (var7) {
            if (var10) {
               dy.c();
            } else {
               dy.z();
            }
         }

         if (var8) {
            if (var10) {
               ly.b();
            } else {
               ly.h();
            }
         }

         if (var9) {
            if (var10) {
               _5.b();
            } else {
               _5.Q();
            }
         }

         if (var6) {
            y8.r();
         }
      }
   }
}
