package com.larp.obf.qa300483f54d804a3;

import com.larpclient.l_;
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
public class q7c8e5d35c608f225 {
   @Inject(
      method = {
            "absSnapTo(DDD)V",
            "snapTo(DDDFF)V",
            "teleportSetPosition(Lnet/minecraft/world/entity/PositionMoveRotation;Lnet/minecraft/world/entity/PositionMoveRotation;Ljava/util/Set;)V"
      },
      at = @At("RETURN")
   )
   private void q(CallbackInfo var1) {
      if (this instanceof LocalPlayer var2) {
         SpeedModule.C(var2);
         TargetStrafeModule.X(var2);
         GrimSpeedModule.a(var2);
         AimAssistModule.k(var2);
         WTapModule.W(var2);
         l_.f();
      }
   }
}
