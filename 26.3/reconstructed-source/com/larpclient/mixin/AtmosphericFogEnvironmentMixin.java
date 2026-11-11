package com.larpclient.mixin;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.FogModule;
import com.larpclient.module.render.SkyShaderModule;
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
public class AtmosphericFogEnvironmentMixin {
   @Inject(method = "setupFog", at = @At("RETURN"))
   private void p(FogData var1, Camera var2, ClientLevel var3, float var4, DeltaTracker var5, CallbackInfo var6) {
      if (!StreamProofManager.isHidingInterface()) {
         CustomSkyModule var7 = CustomSkyModule.getInstance();
         if (CustomSkyModule.isActive()) {
            float var8 = var7.getFogDistance();
            var1.environmentalStart *= var8;
            var1.environmentalEnd *= var8;
            var1.renderDistanceStart *= var8;
            var1.renderDistanceEnd *= var8;
            var1.skyEnd *= var8;
            var1.cloudEnd *= var8;
         }

         if (FogModule.getEffectiveEndDistance()) {
            var1.skyEnd = Math.max(var1.skyEnd, 512.0F);
         }
      }
   }

   @Inject(method = "getBaseColor", at = @At("RETURN"), cancellable = true)
   private void b(ClientLevel var1, Camera var2, int var3, float var4, CallbackInfoReturnable<Vector3fc> var5) {
      if (!StreamProofManager.isHidingInterface()) {
         CustomSkyModule var6 = CustomSkyModule.getInstance();
         SkyShaderModule var7 = SkyShaderModule.getInstance();
         int var8;
         if (CustomSkyModule.isActive()) {
            if (!var6.getNebulaColor()) {
               return;
            }

            var8 = var6.getGradient().horizon();
         } else {
            if (var7 == null || !var7.shouldRender()) {
               return;
            }

            var8 = var7.getThemeColor();
         }

         var5.setReturnValue(new Vector3f(ARGB.redFloat(var8), ARGB.greenFloat(var8), ARGB.blueFloat(var8)));
      }
   }
}
