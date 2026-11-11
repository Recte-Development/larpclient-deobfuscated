package com.larpclient.mixin;

import com.larpclient.module.combat.AimAssistModule;
import com.larpclient.module.combat.WTapModule;
import com.larpclient.module.movement.GrimSpeedModule;
import com.larpclient.module.movement.SpeedModule;
import com.larpclient.module.movement.TargetStrafeModule;
import com.larpclient.render.particle.CometTrailRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin_2 {
   @Inject(
      method = {
            "absSnapTo(DDD)V",
            "snapTo(DDDFF)V",
            "teleportSetPosition(Lnet/minecraft/world/entity/PositionMoveRotation;Lnet/minecraft/world/entity/PositionMoveRotation;Ljava/util/Set;)V"
      },
      at = @At("RETURN")
   )
   private void l(CallbackInfo var1) {
      if (this instanceof LocalPlayer var2) {
         SpeedModule.onPlayerTeleport(var2);
         TargetStrafeModule.onPositionCorrection(var2);
         GrimSpeedModule.onPositionCorrection(var2);
         AimAssistModule.resetFor(var2);
         WTapModule.onPositionCorrection(var2);
         CometTrailRenderer.reset();
      }
   }
}
