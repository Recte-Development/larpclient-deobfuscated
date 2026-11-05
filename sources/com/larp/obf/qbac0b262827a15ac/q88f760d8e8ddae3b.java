package com.larp.obf.qbac0b262827a15ac;

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
public class q88f760d8e8ddae3b {
   @Inject(method = "move", at = @At("HEAD"), cancellable = true)
   private void l(MoverType var1, Vec3 var2, CallbackInfo var3) {
      if (this == Minecraft.getInstance().player) {
         AirStuckModule var4 = AirStuckModule.X();
         ElytraMotionModule var5 = ElytraMotionModule.d();
         if (var4 != null && var4.U() || var5 != null && var5.z()) {
            ((Entity)this).setDeltaMovement(Vec3.ZERO);
            var3.cancel();
         }
      }
   }
}
