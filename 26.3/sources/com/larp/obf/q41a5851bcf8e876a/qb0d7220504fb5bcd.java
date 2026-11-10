package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.bp;
import com.larpclient.pm;
import com.larpclient.qc;
import com.larpclient.v;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class qb0d7220504fb5bcd {
   @WrapOperation(
      method = "render3dHud",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lcom/mojang/renderpearl/api/textures/GpuTextureView;)V"
      )
   )
   private void m(GameRenderer var1, CameraRenderState var2, PlayerRenderState var3, GpuTextureView var4, Operation<Void> var5) {
      if (var2.isPanoramicMode) {
         pm.C();
         v.H();
         bp.b();
      }

      boolean var6 = !var2.isPanoramicMode && qc.G();
      if (var6) {
         qc.l();

         try {
            var5.call(new Object[]{var1, var2, var3, var4});
         } finally {
            qc.R();
         }
      }

      boolean var7 = !var2.isPanoramicMode && v.l();
      boolean var8 = !var2.isPanoramicMode && pm.y();
      boolean var9 = !var2.isPanoramicMode && bp.M();
      boolean var10 = false;

      try {
         if (var6) {
            qc.o(() -> var5.call(new Object[]{var1, var2, var3, var4}));
         } else {
            var5.call(new Object[]{var1, var2, var3, var4});
         }

         var10 = true;
      } finally {
         if (var7) {
            if (var10) {
               v.c();
            } else {
               v.H();
            }
         }

         if (var8) {
            if (var10) {
               pm.I();
            } else {
               pm.X();
            }
         }

         if (var9) {
            if (var10) {
               bp.k();
            } else {
               bp.b();
            }
         }

         if (var6) {
            qc.N();
         }
      }
   }
}
