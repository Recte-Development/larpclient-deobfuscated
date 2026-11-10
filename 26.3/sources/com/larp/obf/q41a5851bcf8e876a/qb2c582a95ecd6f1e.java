package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.a_;
import com.larpclient.bj;
import com.larpclient.br;
import com.larpclient.he;
import com.larpclient.hz;
import com.larpclient.nl;
import com.larpclient.pj;
import com.larpclient.qc;
import com.larpclient.u;
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
public class qb2c582a95ecd6f1e {
   @Inject(method = "render", at = @At("HEAD"))
   private void l(CallbackInfo var1) {
      qc.F();
      nl.u();
      pj.D();
      SkyShaderModule var2 = SkyShaderModule.R();
      if (var2 != null) {
         var2.k();
      }
   }

   @Inject(method = "extract", at = @At("HEAD"))
   private void i(DeltaTracker var1, boolean var2, CallbackInfo var3) {
      qc.c();
      hz.H(var1.getGameTimeDeltaPartialTick(false));
      nl.Z();
      he.t();
      a_.v();
   }

   @ModifyArg(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;"
      )
   )
   private Matrix4f n(Matrix4f var1) {
      bj.v(var1);
      return var1;
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void j(CallbackInfo var1) {
      u.C();
      br.j();
      nl.x();
   }

   @Inject(
      method = "render3dHud",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/api/commands/CommandEncoder;clearDepthTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;D)V"
      )
   )
   private void v(CallbackInfo var1) {
      qc.G();
   }

   @Inject(method = "render", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void k(CallbackInfo var1) {
      qc.w();
   }
}
