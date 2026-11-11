package com.larpclient.mixin;

import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.movement.ElytraMotionModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin_6 {
   @Inject(method = "move", at = @At("HEAD"), cancellable = true)
   private void R(MoverType var1, Vec3 var2, CallbackInfo var3) {
      if (this == Minecraft.getInstance().player) {
         AirStuckModule var4 = AirStuckModule.getInstance();
         ElytraMotionModule var5 = ElytraMotionModule.getInstance();
         if (var4 != null && var4.isHolding() || var5 != null && var5.isHolding()) {
            ((Entity)this).setDeltaMovement(Vec3.ZERO);
            var3.cancel();
         }
      }
   }
}
