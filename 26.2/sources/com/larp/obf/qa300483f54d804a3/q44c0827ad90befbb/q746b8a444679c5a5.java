package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.lh;
import com.larpclient.pw;
import com.larpclient.we;
import com.larpclient.module.w;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class q746b8a444679c5a5 {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void Y(CallbackInfo var1) {
      if (w.d(Minecraft.getInstance())) {
         var1.cancel();
      } else if (pw.S().u()) {
         var1.cancel();
      } else {
         Minecraft var2 = Minecraft.getInstance();
         lh var3 = lh.I();
         if (!var3.i(var2) && !var3.w()) {
            if (var2.player != null
               && var2.hitResult instanceof EntityHitResult var4
               && var4.getEntity() instanceof ItemFrame
               && we.Y().q(var2.player.getMainHandItem())) {
               var1.cancel();
            }
         } else {
            var1.cancel();
         }
      }
   }
}
