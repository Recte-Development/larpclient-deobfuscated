package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.d6;
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
public class q328521e031822862 {
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
         SpeedModule.K(var2);
         TargetStrafeModule.m(var2);
         GrimSpeedModule.U(var2);
         AimAssistModule.H(var2);
         WTapModule.M(var2);
         d6.N();
      }
   }
}
