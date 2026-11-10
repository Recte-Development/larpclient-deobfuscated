package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.AirStuckModule;
import com.larpclient.module.ElytraMotionModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class qc0f08d6b70ada6e1 {
   @Inject(method = "move", at = @At("HEAD"), cancellable = true)
   private void R(MoverType var1, Vec3 var2, CallbackInfo var3) {
      if (this == Minecraft.getInstance().player) {
         AirStuckModule var4 = AirStuckModule.Z();
         ElytraMotionModule var5 = ElytraMotionModule.n();
         if (var4 != null && var4.J() || var5 != null && var5.Q()) {
            ((Entity)this).setDeltaMovement(Vec3.ZERO);
            var3.cancel();
         }
      }
   }
}
