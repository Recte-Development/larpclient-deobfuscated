package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.c6;
import com.larpclient.kq;
import com.larpclient.ze;
import com.larpclient.module.d;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class q86134fd62b6871e2 {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void o(CallbackInfo var1) {
      if (d.V(Minecraft.getInstance())) {
         var1.cancel();
      } else if (c6.K().E()) {
         var1.cancel();
      } else {
         Minecraft var2 = Minecraft.getInstance();
         kq var3 = kq.I();
         if (!var3.L(var2) && !var3.G()) {
            if (var2.player != null
               && var2.hitResult instanceof EntityHitResult var4
               && var4.getEntity() instanceof ItemFrame
               && ze.c().d(var2.player.getMainHandItem())) {
               var1.cancel();
            }
         } else {
            var1.cancel();
         }
      }
   }
}
