package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.mm;
import com.larpclient.ro;
import com.larpclient.ug;
import com.larpclient.module.a_;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class q0f359a01407c815b {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfo var1) {
      if (a_.N(Minecraft.getInstance())) {
         var1.cancel();
      } else if (ro.H().E()) {
         var1.cancel();
      } else {
         Minecraft var2 = Minecraft.getInstance();
         mm var3 = mm.W();
         if (!var3.p(var2) && !var3.K()) {
            if (var2.player != null
               && var2.hitResult instanceof EntityHitResult var4
               && var4.getEntity() instanceof ItemFrame
               && ug.x().l(var2.player.getMainHandItem())) {
               var1.cancel();
            }
         } else {
            var1.cancel();
         }
      }
   }
}
