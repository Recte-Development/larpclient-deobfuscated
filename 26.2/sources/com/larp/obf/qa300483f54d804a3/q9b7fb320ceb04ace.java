package com.larp.obf.qa300483f54d804a3;

import com.larpclient.bu;
import com.larpclient.cd;
import com.larpclient.cf;
import com.larpclient.ct;
import com.larpclient.p2;
import com.larpclient.sa;
import com.larpclient.wa;
import com.larpclient.zj;
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
public class q9b7fb320ceb04ace {
   @Inject(method = "render", at = @At("HEAD"))
   private void U(CallbackInfo var1) {
      p2.n();
      wa.G();
      ct.M();
      SkyShaderModule var2 = SkyShaderModule.t();
      if (var2 != null) {
         var2.p();
      }
   }

   @Inject(method = "extract", at = @At("HEAD"))
   private void w(DeltaTracker var1, boolean var2, CallbackInfo var3) {
      p2.I();
      cf.h(var1.getGameTimeDeltaPartialTick(false));
      wa.t();
      zj.U();
   }

   @ModifyArg(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"
      )
   )
   private Matrix4f C(Matrix4f var1) {
      bu.I(var1);
      return var1;
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void l(CallbackInfo var1) {
      sa.l();
      cd.F();
      wa.j();
   }

   @Inject(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/CommandEncoder;clearDepthTexture(Lcom/mojang/blaze3d/textures/GpuTexture;D)V")
   )
   private void w(CallbackInfo var1) {
      p2.f();
   }

   @Inject(method = "render", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void A(CallbackInfo var1) {
      p2.a();
   }
}
