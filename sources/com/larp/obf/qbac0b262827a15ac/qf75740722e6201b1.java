package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.eb;
import com.larpclient.kx;
import com.larpclient.m5;
import com.larpclient.ty;
import com.larpclient.u1;
import com.larpclient.wy;
import com.larpclient.x6;
import com.larpclient.zo;
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
public class qf75740722e6201b1 {
   @Inject(method = "render", at = @At("HEAD"))
   private void X(CallbackInfo var1) {
      zo.z();
      m5.x();
      wy.s();
      SkyShaderModule var2 = SkyShaderModule.U();
      if (var2 != null) {
         var2.b();
      }
   }

   @Inject(method = "extract", at = @At("HEAD"))
   private void h(DeltaTracker var1, boolean var2, CallbackInfo var3) {
      zo.A();
      ty.H(var1.getGameTimeDeltaPartialTick(false));
      m5.j();
      kx.O();
   }

   @ModifyArg(
      method = "renderLevel",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/ProjectionMatrixBuffer;getBuffer(Lorg/joml/Matrix4f;)Lcom/mojang/renderpearl/api/buffers/GpuBufferSlice;"
      )
   )
   private Matrix4f A(Matrix4f var1) {
      u1.Z(var1);
      return var1;
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void j(CallbackInfo var1) {
      x6.t();
      eb.E();
      m5.f();
   }

   @Inject(
      method = "render3dHud",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/api/commands/CommandEncoder;clearDepthTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;D)V"
      )
   )
   private void x(CallbackInfo var1) {
      zo.k();
   }

   @Inject(method = "render", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/gui/render/GuiRenderer;render()V"))
   private void N(CallbackInfo var1) {
      zo.d();
   }
}
