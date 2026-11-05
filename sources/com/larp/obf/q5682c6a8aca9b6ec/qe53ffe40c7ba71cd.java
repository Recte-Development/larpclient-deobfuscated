package com.larp.obf.q5682c6a8aca9b6ec;

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
public class qe53ffe40c7ba71cd {
   @Inject(method = "move", at = @At("HEAD"), cancellable = true)
   private void b(MoverType var1, Vec3 var2, CallbackInfo var3) {
      if (this == Minecraft.getInstance().player) {
         AirStuckModule var4 = AirStuckModule.H();
         ElytraMotionModule var5 = ElytraMotionModule.S();
         if (var4 != null && var4.D() || var5 != null && var5.m()) {
            ((Entity)this).setDeltaMovement(Vec3.ZERO);
            var3.cancel();
         }
      }
   }
}
