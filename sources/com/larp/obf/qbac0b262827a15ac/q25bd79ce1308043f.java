package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.o7;
import com.larpclient.zo;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AtmosphericFogEnvironment.class)
public class q25bd79ce1308043f {
   @Inject(method = "setupFog", at = @At("RETURN"))
   private void h(FogData var1, Camera var2, ClientLevel var3, float var4, DeltaTracker var5, CallbackInfo var6) {
      if (!zo.l()) {
         CustomSkyModule var7 = CustomSkyModule.Y();
         if (CustomSkyModule.a()) {
            float var8 = var7.x();
            var1.environmentalStart *= var8;
            var1.environmentalEnd *= var8;
            var1.renderDistanceStart *= var8;
            var1.renderDistanceEnd *= var8;
            var1.skyEnd *= var8;
            var1.cloudEnd *= var8;
         }

         if (FogModule.I()) {
            var1.skyEnd = Math.max(var1.skyEnd, 512.0F);
         }
      }
   }

   @Inject(method = "getBaseColor", at = @At("RETURN"), cancellable = true)
   private void Z(ClientLevel var1, Camera var2, int var3, float var4, CallbackInfoReturnable<Vector3fc> var5) {
      Vector3fc var6 = (Vector3fc)var5.getReturnValue();
      Integer var7 = T(ARGB.colorFromFloat(1.0F, var6.x(), var6.y(), var6.z()));
      if (var7 != null) {
         var5.setReturnValue(new Vector3f(ARGB.redFloat(var7), ARGB.greenFloat(var7), ARGB.blueFloat(var7)));
      }
   }

   @Unique
   private static Integer T(int var0) {
      o7.p(var0);
      if (zo.l()) {
         return null;
      }

      CustomSkyModule var1 = CustomSkyModule.Y();
      SkyShaderModule var2 = SkyShaderModule.U();
      int var3;
      if (CustomSkyModule.a()) {
         if (!var1.k()) {
            return null;
         }

         var3 = var1.P().horizon();
      } else {
         if (var2 == null || !var2.w()) {
            return null;
         }

         var3 = var2.e();
      }

      o7.p(var3);
      return var3;
   }
}
