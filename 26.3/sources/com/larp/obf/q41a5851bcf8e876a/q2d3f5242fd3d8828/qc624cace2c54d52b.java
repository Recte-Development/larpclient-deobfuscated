package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.q5;
import com.larpclient.vy;
import com.larpclient.wj;
import com.larpclient.module.fb;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class qc624cace2c54d52b {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void a(CallbackInfo var1) {
      if (fb.v(Minecraft.getInstance())) {
         var1.cancel();
      } else if (wj.m().E()) {
         var1.cancel();
      } else {
         Minecraft var2 = Minecraft.getInstance();
         q5 var3 = q5.a();
         if (!var3.n(var2) && !var3.O()) {
            if (var2.player != null
               && var2.hitResult instanceof EntityHitResult var4
               && var4.getEntity() instanceof ItemFrame
               && vy.a().j(var2.player.getMainHandItem())) {
               var1.cancel();
            }
         } else {
            var1.cancel();
         }
      }
   }
}
