package com.larp.obf.qa300483f54d804a3;

import com.larpclient.h;
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
public class q163093b67466a2c3<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
      at = @At("RETURN")
   )
   private void d(T var1, S var2, float var3, CallbackInfo var4) {
      try {
         ChamsModule var5 = ChamsModule.C();
         int var6 = var5 != null && var5.isEnabled() ? var5.N(var1) : 0;
         if (var6 == 0 && ESPModule.J()) {
            var6 = ESPModule.x(var1);
         }

         if (var6 != 0) {
            var2.outlineColor = var6;
         }

         AuraModule var7 = AuraModule.V();
         MaceModule var8 = MaceModule.W();
         if ((var7 != null || var8 != null) && var1 == Minecraft.getInstance().player) {
            float var9 = var2.bodyRot + var2.yRot;
            if (var8 != null && var8.B()) {
               var2.bodyRot = var8.A(var2.bodyRot, var9);
            } else if (var7 != null) {
               var2.bodyRot = var7.A(var2.bodyRot, var9);
            }

            var2.yRot = Mth.wrapDegrees(var9 - var2.bodyRot);
         }

         SpinBotModule var11 = SpinBotModule.T();
         if (var11 != null && var1 == Minecraft.getInstance().player) {
            var2.bodyRot = var11.P(var2.bodyRot, var3);
            var2.yRot = var11.q(var2.yRot);
         }

         if (var1 == Minecraft.getInstance().player) {
            Feature3DUIModule.e(var1, var2, var3);
         }
      } catch (Exception var10) {
         h.w("Render", "LivingEntityRendererMixin callback failed", var10);
      }
   }
}
