package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p2;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.FogModule;
import com.larpclient.module.SkyShaderModule;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtmosphericFogEnvironment.class)
public class q27b691bdce203a31 {
   @Inject(method = "setupFog", at = @At("RETURN"))
   private void a(FogData var1, Camera var2, ClientLevel var3, float var4, DeltaTracker var5, CallbackInfo var6) {
      if (!p2.p()) {
         CustomSkyModule var7 = CustomSkyModule.K();
         if (CustomSkyModule.i()) {
            float var8 = var7.R();
            var1.environmentalStart *= var8;
            var1.environmentalEnd *= var8;
            var1.renderDistanceStart *= var8;
            var1.renderDistanceEnd *= var8;
            var1.skyEnd *= var8;
            var1.cloudEnd *= var8;
         }

         if (FogModule.l()) {
            var1.skyEnd = Math.max(var1.skyEnd, 512.0F);
         }
      }
   }

   @Inject(method = "getBaseColor", at = @At("RETURN"), cancellable = true)
   private void L(ClientLevel var1, Camera var2, int var3, float var4, CallbackInfoReturnable<Integer> var5) {
      if (!p2.p()) {
         CustomSkyModule var6 = CustomSkyModule.K();
         SkyShaderModule var7 = SkyShaderModule.t();
         if (CustomSkyModule.i()) {
            if (!var6.p()) {
               return;
            }

            var5.setReturnValue(var6.m().horizon());
         } else if (var7 != null && var7.O()) {
            var5.setReturnValue(var7.C());
         }
      }
   }
}
