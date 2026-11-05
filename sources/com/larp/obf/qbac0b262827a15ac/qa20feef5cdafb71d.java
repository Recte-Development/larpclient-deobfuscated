package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.mu;
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
public class qa20feef5cdafb71d {
   @Inject(
      method = {
            "absSnapTo(DDD)V",
            "snapTo(DDDFF)V",
            "teleportSetPosition(Lnet/minecraft/world/entity/PositionMoveRotation;Lnet/minecraft/world/entity/PositionMoveRotation;Ljava/util/Set;)V"
      },
      at = @At("RETURN")
   )
   private void h(CallbackInfo var1) {
      if (this instanceof LocalPlayer var2) {
         SpeedModule.w(var2);
         TargetStrafeModule.W(var2);
         GrimSpeedModule.K(var2);
         AimAssistModule.o(var2);
         WTapModule.F(var2);
         mu.s();
      }
   }
}
