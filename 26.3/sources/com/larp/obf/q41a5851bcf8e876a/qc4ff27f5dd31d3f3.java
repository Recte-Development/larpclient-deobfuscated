package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.qc;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.FogModule;
import com.larpclient.module.SkyShaderModule;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.util.ARGB;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtmosphericFogEnvironment.class)
public class qc4ff27f5dd31d3f3 {
   @Inject(method = "setupFog", at = @At("RETURN"))
   private void p(FogData var1, Camera var2, ClientLevel var3, float var4, DeltaTracker var5, CallbackInfo var6) {
      if (!qc.t()) {
         CustomSkyModule var7 = CustomSkyModule.p();
         if (CustomSkyModule.C()) {
            float var8 = var7.N();
            var1.environmentalStart *= var8;
            var1.environmentalEnd *= var8;
            var1.renderDistanceStart *= var8;
            var1.renderDistanceEnd *= var8;
            var1.skyEnd *= var8;
            var1.cloudEnd *= var8;
         }

         if (FogModule.n()) {
            var1.skyEnd = Math.max(var1.skyEnd, 512.0F);
         }
      }
   }

   @Inject(method = "getBaseColor", at = @At("RETURN"), cancellable = true)
   private void b(ClientLevel var1, Camera var2, int var3, float var4, CallbackInfoReturnable<Vector3fc> var5) {
      if (!qc.t()) {
         CustomSkyModule var6 = CustomSkyModule.p();
         SkyShaderModule var7 = SkyShaderModule.R();
         int var8;
         if (CustomSkyModule.C()) {
            if (!var6.c()) {
               return;
            }

            var8 = var6.X().horizon();
         } else {
            if (var7 == null || !var7.X()) {
               return;
            }

            var8 = var7.g();
         }

         var5.setReturnValue(new Vector3f(ARGB.redFloat(var8), ARGB.greenFloat(var8), ARGB.blueFloat(var8)));
      }
   }
}
