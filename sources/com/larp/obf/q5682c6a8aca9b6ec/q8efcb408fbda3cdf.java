package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.ea;
import com.larpclient.fo;
import com.larpclient.h_;
import com.larpclient.jk;
import com.larpclient.mc;
import com.larpclient.mu;
import com.larpclient.y8;
import com.larpclient.yv;
import com.larpclient.module.SkyShaderModule;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class q8efcb408fbda3cdf {
   @Inject(method = "render", at = @At("HEAD"))
   private void w(CallbackInfo var1) {
      y8.f();
      jk.L();
      yv.B();
      SkyShaderModule var2 = SkyShaderModule.B();
      if (var2 != null) {
         var2.l();
      }
   }

   @Inject(method = "extract", at = @At("HEAD"))
   private void Z(DeltaTracker var1, boolean var2, CallbackInfo var3) {
      y8.P();
      ea.r(var1.getGameTimeDeltaPartialTick(false));
      jk.p();
      h_.I();
   }

   @ModifyArg(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;"
      )
   )
   private Matrix4f L(Matrix4f var1) {
      fo.f(var1);
      return var1;
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void r(CallbackInfo var1) {
      mc.S();
      mu.J();
      jk.S();
   }

   @Inject(
      method = "render3dHud",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/api/commands/CommandEncoder;clearDepthTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;D)V"
      )
   )
   private void k(CallbackInfo var1) {
      y8.e();
   }

   @Inject(method = "render", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void V(CallbackInfo var1) {
      y8.p();
   }
}
