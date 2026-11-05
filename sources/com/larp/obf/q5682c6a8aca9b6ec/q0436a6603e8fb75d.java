package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._0;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.SpeedModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.WTapModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class q0436a6603e8fb75d {
   @Inject(
      method = {
            "absSnapTo(DDD)V",
            "snapTo(DDDFF)V",
            "teleportSetPosition(Lnet/minecraft/world/entity/PositionMoveRotation;Lnet/minecraft/world/entity/PositionMoveRotation;Ljava/util/Set;)V"
      },
      at = @At("RETURN")
   )
   private void t(CallbackInfo var1) {
      if (this instanceof LocalPlayer var2) {
         SpeedModule.P(var2);
         TargetStrafeModule.H(var2);
         GrimSpeedModule.u(var2);
         AimAssistModule.w(var2);
         WTapModule.N(var2);
         _0.q();
      }
   }
}
