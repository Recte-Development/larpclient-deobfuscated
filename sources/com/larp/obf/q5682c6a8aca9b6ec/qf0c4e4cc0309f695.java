package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.y8;
import com.larpclient.yv;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.SkyShaderModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class qf0c4e4cc0309f695 {
   @Shadow
   @Final
   private RenderTarget renderTarget;
   @Unique
   private final yv o = new yv();
   @Unique
   private boolean K;

   @Inject(method = "render", at = @At("HEAD"))
   private void p(GpuBufferSlice var1, SkyRenderState var2, CallbackInfo var3) {
      this.K = var2.skybox != Skybox.END && this.I();
   }

   @Inject(method = "renderSkyDisc", at = @At("HEAD"), cancellable = true)
   private void n(CallbackInfo var1) {
      if (this.K) {
         var1.cancel();
      }
   }

   @Unique
   private boolean I() {
      RenderTarget var1 = y8.r(this.renderTarget);
      boolean var2 = var1 != this.renderTarget;
      if (this.o.E(var1)) {
         return !var2;
      }

      if (CustomSkyModule.i()) {
         return this.o.y(var1, CustomSkyModule.Y()) && !var2;
      }

      SkyShaderModule var3 = SkyShaderModule.B();
      return var3 != null && var3.z() && var3.M(var1) && !var2;
   }

   @Unique
   private static boolean m() {
      SkyShaderModule var0 = SkyShaderModule.B();
      return yv.K() || var0 != null && var0.Y();
   }

   @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"), cancellable = true)
   private void a(CallbackInfo var1) {
      if (!y8.J()) {
         if (m()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void K(ClientLevel var1, float var2, Camera var3, SkyRenderState var4, CallbackInfo var5) {
      if (!y8.J()) {
         if (CustomSkyModule.i()) {
            var4.starBrightness = CustomSkyModule.Y().C() ? 0.0F : CustomSkyModule.Y().p() * var4.rainBrightness;
         }
      }
   }

   @Inject(method = "renderSun", at = @At("HEAD"), cancellable = true)
   private void Y(CallbackInfo var1) {
      if (!y8.J()) {
         if (CustomSkyModule.i() && !CustomSkyModule.Y().E()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
   private void O(CallbackInfo var1) {
      if (!y8.J()) {
         if (CustomSkyModule.i() && !CustomSkyModule.Y().w()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderDarkDisc", at = @At("HEAD"), cancellable = true)
   private void k(CallbackInfo var1) {
      if (!y8.J()) {
         if (CustomSkyModule.i() || m()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderSunriseAndSunset", at = @At("HEAD"), cancellable = true)
   private void T(CallbackInfo var1) {
      if (!y8.J()) {
         if (CustomSkyModule.i() || m()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "close", at = @At("HEAD"))
   private void c(CallbackInfo var1) {
      this.o.close();
   }
}
