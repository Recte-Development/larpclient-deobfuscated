package com.larp.obf.qa300483f54d804a3;

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
public class qd1b23d8129639896 {
   @Inject(method = "move", at = @At("HEAD"), cancellable = true)
   private void y(MoverType var1, Vec3 var2, CallbackInfo var3) {
      if (this == Minecraft.getInstance().player) {
         AirStuckModule var4 = AirStuckModule.T();
         ElytraMotionModule var5 = ElytraMotionModule.v();
         if (var4 != null && var4.I() || var5 != null && var5.K()) {
            ((Entity)this).setDeltaMovement(Vec3.ZERO);
            var3.cancel();
         }
      }
   }
}
