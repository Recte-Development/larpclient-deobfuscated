package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.hp;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.SwordModelsModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.SwingAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class q80bf8127c7ffe054 {
   @Inject(method = "getModifiedSwingDuration", at = @At("RETURN"), cancellable = true)
   private void e(SwingAnimation var1, CallbackInfoReturnable<Integer> var2) {
      if (this instanceof LocalPlayer var3) {
         if (this == Minecraft.getInstance().player) {
            SwordModelsModule var8 = SwordModelsModule.t();
            InteractionHand var5 = hp.k(var3);
            InteractionHand var6 = var5 == null ? InteractionHand.MAIN_HAND : var5;
            if (var8 == null || !var8.p(var3.getItemInHand(var6))) {
               AnimationsModule var7 = AnimationsModule.K();
               if (var7 != null && var7.isRuntimeActive() && var7.U()) {
                  var2.setReturnValue(var7.Y((Integer)var2.getReturnValue()));
               }
            }
         }
      }
   }
}
