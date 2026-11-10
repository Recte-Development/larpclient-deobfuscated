package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ct;
import com.larpclient.p2;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.SkyShaderModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
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
public class q65426ab23f147423 {
   @Shadow
   @Final
   private RenderTarget renderTarget;
   @Unique
   private final ct B = new ct();

   @Inject(method = "renderSkyDisc", at = @At("HEAD"), cancellable = true)
   private void t(CallbackInfo var1) {
      if (this.Q()) {
         var1.cancel();
      }
   }

   @Inject(method = "renderEndSky", at = @At("HEAD"), cancellable = true)
   private void J(CallbackInfo var1) {
      if (this.Q()) {
         var1.cancel();
      }
   }

   @Unique
   private boolean Q() {
      RenderTarget var1 = p2.R(this.renderTarget);
      boolean var2 = var1 != this.renderTarget;
      if (CustomSkyModule.i()) {
         return this.B.t(var1, CustomSkyModule.K()) && !var2;
      }

      SkyShaderModule var3 = SkyShaderModule.t();
      return var3 != null && var3.O() && var3.z(var1) && !var2;
   }

   @Unique
   private static boolean y() {
      SkyShaderModule var0 = SkyShaderModule.t();
      return var0 != null && var0.Q();
   }

   @Inject(method = "renderSunMoonAndStars", at = @At("HEAD"), cancellable = true)
   private void K(CallbackInfo var1) {
      if (!p2.p()) {
         if (y()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void X(ClientLevel var1, float var2, Camera var3, SkyRenderState var4, CallbackInfo var5) {
      if (!p2.p()) {
         if (CustomSkyModule.i()) {
            var4.starBrightness = CustomSkyModule.K().I() ? 0.0F : CustomSkyModule.K().K() * var4.rainBrightness;
         }
      }
   }

   @Inject(method = "renderSun", at = @At("HEAD"), cancellable = true)
   private void Z(CallbackInfo var1) {
      if (!p2.p()) {
         if (CustomSkyModule.i() && !CustomSkyModule.K().U()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderMoon", at = @At("HEAD"), cancellable = true)
   private void o(CallbackInfo var1) {
      if (!p2.p()) {
         if (CustomSkyModule.i() && !CustomSkyModule.K().s()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderDarkDisc", at = @At("HEAD"), cancellable = true)
   private void E(CallbackInfo var1) {
      if (!p2.p()) {
         if (CustomSkyModule.i() || y()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "renderSunriseAndSunset", at = @At("HEAD"), cancellable = true)
   private void F(CallbackInfo var1) {
      if (!p2.p()) {
         if (CustomSkyModule.i() || y()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "close", at = @At("HEAD"))
   private void Q(CallbackInfo var1) {
      this.B.close();
   }
}
