package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wi;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.FogModule;
import com.larpclient.module.SkyShaderModule;
import net.minecraft.class_11398;
import net.minecraft.class_4184;
import net.minecraft.class_638;
import net.minecraft.class_7285;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_11398.class)
public class qd04bcfb780a21b94 {
   @Inject(method = "method_42591", at = @At("RETURN"))
   private void A(class_7285 var1, class_4184 var2, class_638 var3, float var4, class_9779 var5, CallbackInfo var6) {
      if (!wi.s()) {
         CustomSkyModule var7 = CustomSkyModule.M();
         if (CustomSkyModule.h()) {
            float var8 = var7.f();
            var1.field_60582 *= var8;
            var1.field_60584 *= var8;
            var1.field_60583 *= var8;
            var1.field_60585 *= var8;
            var1.field_60099 *= var8;
            var1.field_60100 *= var8;
         }

         if (FogModule.V()) {
            var1.field_60099 = Math.max(var1.field_60099, 512.0F);
         }
      }
   }

   @Inject(method = "method_71654", at = @At("RETURN"), cancellable = true)
   private void O(class_638 var1, class_4184 var2, int var3, float var4, CallbackInfoReturnable<Integer> var5) {
      if (!wi.s()) {
         CustomSkyModule var6 = CustomSkyModule.M();
         SkyShaderModule var7 = SkyShaderModule.y();
         if (CustomSkyModule.h()) {
            if (!var6.z()) {
               return;
            }

            var5.setReturnValue(var6.Y().horizon());
         } else if (var7 != null && var7.g()) {
            var5.setReturnValue(var7.u());
         }
      }
   }
}
