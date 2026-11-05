package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.mt;
import com.larpclient.module.AuraModule;
import com.larpclient.module.ChamsModule;
import com.larpclient.module.ESPModule;
import com.larpclient.module.Feature3DUIModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.SpinBotModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class q7a46be3080631387<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
      at = @At("RETURN")
   )
   private void P(T var1, S var2, float var3, CallbackInfo var4) {
      try {
         ChamsModule var5 = ChamsModule.u();
         int var6 = var5 == null ? 0 : var5.F(var1);
         if (var6 == 0) {
            var6 = ESPModule.f(var1);
         }

         if (var6 != 0) {
            var2.outlineColor = var6;
         }

         AuraModule var7 = AuraModule.S();
         MaceModule var8 = MaceModule.c();
         if ((var7 != null || var8 != null) && var1 == Minecraft.getInstance().player) {
            float var9 = var2.bodyRot + var2.yRot;
            if (var8 != null && var8.k()) {
               var2.bodyRot = var8.R(var2.bodyRot, var9);
            } else if (var7 != null) {
               var2.bodyRot = var7.y(var2.bodyRot, var9);
            }

            var2.yRot = Mth.wrapDegrees(var9 - var2.bodyRot);
         }

         SpinBotModule var11 = SpinBotModule.L();
         if (var11 != null && var1 == Minecraft.getInstance().player) {
            var2.bodyRot = var11.D(var2.bodyRot, var3);
            var2.yRot = var11.I(var2.yRot);
         }

         Feature3DUIModule.w(var1, var2, var3);
      } catch (Exception var10) {
         mt.B("Render", "LivingEntityRendererMixin callback failed", var10);
      }
   }
}
