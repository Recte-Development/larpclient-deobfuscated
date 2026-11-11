package com.larpclient.mixin;

import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.combat.MaceModule;
import com.larpclient.module.combat.SpinBotModule;
import com.larpclient.module.render.ChamsModule;
import com.larpclient.module.render.ESPModule;
import com.larpclient.module.render.Feature3DUIModule;
import com.larpclient.util.misc.LarpLog;
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
public class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
      at = @At("RETURN")
   )
   private void L(T var1, S var2, float var3, CallbackInfo var4) {
      try {
         ChamsModule var5 = ChamsModule.getInstance();
         int var6 = var5 != null && var5.isEnabled() ? var5.shouldRenderPlayers(var1) : 0;
         if (var6 == 0 && ESPModule.getOutlineColor()) {
            var6 = ESPModule.getOutlineColor(var1);
         }

         if (var6 != 0) {
            var2.outlineColor = var6;
         }

         AuraModule var7 = AuraModule.getInstance();
         MaceModule var8 = MaceModule.getInstance();
         if ((var7 != null || var8 != null) && var1 == Minecraft.getInstance().player) {
            float var9 = var2.bodyRot + var2.yRot;
            if (var8 != null && var8.isSilentAimActive()) {
               var2.bodyRot = var8.adjustLookDelta(var2.bodyRot, var9);
            } else if (var7 != null) {
               var2.bodyRot = var7.smoothYaw(var2.bodyRot, var9);
            }

            var2.yRot = Mth.wrapDegrees(var9 - var2.bodyRot);
         }

         SpinBotModule var11 = SpinBotModule.getInstance();
         if (var11 != null && var1 == Minecraft.getInstance().player) {
            var2.bodyRot = var11.getBodyYaw(var2.bodyRot, var3);
            var2.yRot = var11.getHeadYawOffset(var2.yRot);
         }

         if (var1 == Minecraft.getInstance().player) {
            Feature3DUIModule.applyLookTarget(var1, var2, var3);
         }
      } catch (Exception var10) {
         LarpLog.errorOnce("Render", "LivingEntityRendererMixin callback failed", var10);
      }
   }
}
