package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.pj;
import com.larpclient.qc;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.SkyShaderModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class qda3077a8d7cd8d68 {
   @Shadow
   @Final
   private RenderTarget renderTarget;
   @Unique
   private final pj u = new pj();
   @Unique
   private boolean V;

   @Inject(method = "render", at = @At("HEAD"))
   private void K(GpuBufferSlice var1, SkyRenderState var2, CallbackInfo var3) {
      this.V = this.j();
   }

   @Inject(method = "renderSkyDisc", at = @At("HEAD"), cancellable = true)
   private void a(CallbackInfo var1) {
      if (this.V) {
         var1.cancel();
      }
   }

   @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
   private void V(CallbackInfo var1) {
      if (this.V) {
         var1.cancel();
      }
   }

   @Unique
   private boolean j() {
      RenderTarget var1 = qc.c(this.renderTarget);
      boolean var2 = var1 != this.renderTarget;
      if (CustomSkyModule.C()) {
         return this.u.U(var1, CustomSkyModule.p()) && !var2;
      }

      SkyShaderModule var3 = SkyShaderModule.R();
      return var3 != null && var3.X() && var3.z(var1) && !var2;
   }

   @Unique
   private static boolean q() {
      SkyShaderModule var0 = SkyShaderModule.R();
      return var0 != null && var0.A();
   }

   @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"), cancellable = true)
   private void S(CallbackInfo var1) {
      if (!qc.t()) {
         if (q()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void p(ClientLevel var1, float var2, Camera var3, SkyRenderState var4, CallbackInfo var5) {
      if (!qc.t()) {
         if (CustomSkyModule.C()) {
            var4.starBrightness = CustomSkyModule.p().k() ? 0.0F : CustomSkyModule.p().O() * var4.rainBrightness;
         }
      }
   }

   @Inject(method = "renderSun", at = @At("HEAD"), cancellable = true)
   private void v(CallbackInfo var1) {
      if (!qc.t()) {
         if (CustomSkyModule.C() && !CustomSkyModule.p().W()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
   private void i(CallbackInfo var1) {
      if (!qc.t()) {
         if (CustomSkyModule.C() && !CustomSkyModule.p().F()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderDarkDisc", at = @At("HEAD"), cancellable = true)
   private void N(CallbackInfo var1) {
      if (!qc.t()) {
         if (CustomSkyModule.C() || q()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderSunriseAndSunset", at = @At("HEAD"), cancellable = true)
   private void w(CallbackInfo var1) {
      if (!qc.t()) {
         if (CustomSkyModule.C() || q()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "close", at = @At("HEAD"))
   private void o(CallbackInfo var1) {
      this.u.close();
   }
}
