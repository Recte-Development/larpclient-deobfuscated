package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.ek;
import com.larpclient.m1;
import com.larpclient.xw;
import com.larpclient.zo;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class q567e37c99e04cc66 {
   @WrapOperation(
      method = "render3dHud",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;Lnet/minecraft/client/renderer/state/level/PlayerRenderState;Lcom/mojang/renderpearl/api/textures/GpuTextureView;)V"
      )
   )
   private void N(GameRenderer var1, CameraRenderState var2, PlayerRenderState var3, GpuTextureView var4, Operation<Void> var5) {
      if (var2.isPanoramicMode) {
         m1.f();
         xw.F();
         ek.Q();
      }

      boolean var6 = !var2.isPanoramicMode && zo.T();
      if (var6) {
         zo.c();

         try {
            var5.call(new Object[]{var1, var2, var3, var4});
         } finally {
            zo.s();
         }
      }

      boolean var7 = !var2.isPanoramicMode && xw.z();
      boolean var8 = !var2.isPanoramicMode && m1.x();
      boolean var9 = !var2.isPanoramicMode && ek.q();
      boolean var10 = false;

      try {
         if (var6) {
            zo.Z(() -> var5.call(new Object[]{var1, var2, var3, var4}));
         } else {
            var5.call(new Object[]{var1, var2, var3, var4});
         }

         var10 = true;
      } finally {
         if (var7) {
            if (var10) {
               xw.B();
            } else {
               xw.F();
            }
         }

         if (var8) {
            if (var10) {
               m1.X();
            } else {
               m1.r();
            }
         }

         if (var9) {
            if (var10) {
               ek.c();
            } else {
               ek.Q();
            }
         }

         if (var6) {
            zo.U();
         }
      }
   }
}
