package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.wy;
import com.larpclient.zo;
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
public class q9e8ec8c5badd1624 {
   @Shadow
   @Final
   private RenderTarget renderTarget;
   @Unique
   private final wy m = new wy();
   @Unique
   private boolean t;

   @Inject(method = "render", at = @At("HEAD"))
   private void r(GpuBufferSlice var1, SkyRenderState var2, CallbackInfo var3) {
      this.t = var2.skybox != Skybox.END && this.u();
   }

   @Inject(method = "renderSkyDisc", at = @At("HEAD"), cancellable = true)
   private void n(CallbackInfo var1) {
      if (this.t) {
         var1.cancel();
      }
   }

   @Unique
   private boolean u() {
      RenderTarget var1 = zo.Z(this.renderTarget);
      boolean var2 = var1 != this.renderTarget;
      if (this.m.t(var1)) {
         return !var2;
      }

      if (CustomSkyModule.a()) {
         return this.m.k(var1, CustomSkyModule.Y()) && !var2;
      }

      SkyShaderModule var3 = SkyShaderModule.U();
      return var3 != null && var3.w() && var3.z(var1) && !var2;
   }

   @Unique
   private static boolean L() {
      SkyShaderModule var0 = SkyShaderModule.U();
      return wy.a() || var0 != null && var0.d();
   }

   @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"), cancellable = true)
   private void J(CallbackInfo var1) {
      if (!zo.l()) {
         if (L()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void s(ClientLevel var1, float var2, Camera var3, SkyRenderState var4, CallbackInfo var5) {
      if (!zo.l()) {
         if (CustomSkyModule.a()) {
            var4.starBrightness = CustomSkyModule.Y().U() ? 0.0F : CustomSkyModule.Y().G() * var4.rainBrightness;
         }
      }
   }

   @Inject(method = "renderSun", at = @At("HEAD"), cancellable = true)
   private void u(CallbackInfo var1) {
      if (!zo.l()) {
         if (CustomSkyModule.a() && !CustomSkyModule.Y().K()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
   private void V(CallbackInfo var1) {
      if (!zo.l()) {
         if (CustomSkyModule.a() && !CustomSkyModule.Y().f()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderDarkDisc", at = @At("HEAD"), cancellable = true)
   private void x(CallbackInfo var1) {
      if (!zo.l()) {
         if (CustomSkyModule.a() || L()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderSunriseAndSunset", at = @At("HEAD"), cancellable = true)
   private void o(CallbackInfo var1) {
      if (!zo.l()) {
         if (CustomSkyModule.a() || L()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "close", at = @At("HEAD"))
   private void w(CallbackInfo var1) {
      this.m.close();
   }
}
