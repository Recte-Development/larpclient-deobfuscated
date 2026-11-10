package com.larp.obf.qa300483f54d804a3;

import com.larpclient.be;
import com.larpclient.ly;
import com.larpclient.p2;
import com.larpclient.u8;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class qe911145f55a8cb6a {
   @WrapOperation(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/GameRenderer;renderItemInHand(Lnet/minecraft/client/renderer/state/level/CameraRenderState;FLorg/joml/Matrix4fc;)V"
      )
   )
   private void f(GameRenderer var1, CameraRenderState var2, float var3, Matrix4fc var4, Operation<Void> var5) {
      if (var2.isPanoramicMode) {
         ly.a();
         be.o();
         u8.f();
      }

      boolean var6 = !var2.isPanoramicMode && p2.X();
      if (var6) {
         p2.V();

         try {
            var5.call(new Object[]{var1, var2, var3, var4});
         } finally {
            p2.O();
         }
      }

      boolean var7 = !var2.isPanoramicMode && be.Q();
      boolean var8 = !var2.isPanoramicMode && ly.K();
      boolean var9 = !var2.isPanoramicMode && u8.K();
      boolean var10 = false;

      try {
         if (var6) {
            p2.m(() -> var5.call(new Object[]{var1, var2, var3, var4}));
         } else {
            var5.call(new Object[]{var1, var2, var3, var4});
         }

         var10 = true;
      } finally {
         if (var7) {
            if (var10) {
               be.u();
            } else {
               be.o();
            }
         }

         if (var8) {
            if (var10) {
               ly.l();
            } else {
               ly.g();
            }
         }

         if (var9) {
            if (var10) {
               u8.H();
            } else {
               u8.f();
            }
         }

         if (var6) {
            p2.F();
         }
      }
   }
}
