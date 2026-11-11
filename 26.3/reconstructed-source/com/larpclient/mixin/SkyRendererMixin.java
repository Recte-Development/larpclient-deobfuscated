package com.larpclient.mixin;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.SkyShaderModule;
import com.larpclient.render.sky.SkyRenderer2;
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
public class SkyRendererMixin {
   @Shadow
   @Final
   private RenderTarget renderTarget;
   @Unique
   private final SkyRenderer2 u = new SkyRenderer2();
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
      RenderTarget var1 = StreamProofManager.tick(this.renderTarget);
      boolean var2 = var1 != this.renderTarget;
      if (CustomSkyModule.isActive()) {
         return this.u.render(var1, CustomSkyModule.getInstance()) && !var2;
      }

      SkyShaderModule var3 = SkyShaderModule.getInstance();
      return var3 != null && var3.shouldRender() && var3.renderSky(var1) && !var2;
   }

   @Unique
   private static boolean q() {
      SkyShaderModule var0 = SkyShaderModule.getInstance();
      return var0 != null && var0.getAppearanceSettings();
   }

   @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"), cancellable = true)
   private void S(CallbackInfo var1) {
      if (!StreamProofManager.isHidingInterface()) {
         if (q()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void p(ClientLevel var1, float var2, Camera var3, SkyRenderState var4, CallbackInfo var5) {
      if (!StreamProofManager.isHidingInterface()) {
         if (CustomSkyModule.isActive()) {
            var4.starBrightness = CustomSkyModule.getInstance().getNebulaFlowStrength()
               ? 0.0F
               : CustomSkyModule.getInstance().getStarBrightness() * var4.rainBrightness;
         }
      }
   }

   @Inject(method = "renderSun", at = @At("HEAD"), cancellable = true)
   private void v(CallbackInfo var1) {
      if (!StreamProofManager.isHidingInterface()) {
         if (CustomSkyModule.isActive() && !CustomSkyModule.getInstance().isShowVanillaSun()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
   private void i(CallbackInfo var1) {
      if (!StreamProofManager.isHidingInterface()) {
         if (CustomSkyModule.isActive() && !CustomSkyModule.getInstance().isShowVanillaMoon()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderDarkDisc", at = @At("HEAD"), cancellable = true)
   private void N(CallbackInfo var1) {
      if (!StreamProofManager.isHidingInterface()) {
         if (CustomSkyModule.isActive() || q()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderSunriseAndSunset", at = @At("HEAD"), cancellable = true)
   private void w(CallbackInfo var1) {
      if (!StreamProofManager.isHidingInterface()) {
         if (CustomSkyModule.isActive() || q()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "close", at = @At("HEAD"))
   private void o(CallbackInfo var1) {
      this.u.close();
   }
}
