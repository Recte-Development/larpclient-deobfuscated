package com.larp.obf.qbac0b262827a15ac;

import com.larpclient._8;
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
public class qb1ad2f0489025200 {
   @Inject(method = "getModifiedSwingDuration", at = @At("RETURN"), cancellable = true)
   private void X(SwingAnimation var1, CallbackInfoReturnable<Integer> var2) {
      if (this instanceof LocalPlayer var3) {
         if (this == Minecraft.getInstance().player) {
            SwordModelsModule var8 = SwordModelsModule.n();
            InteractionHand var5 = _8.M(var3);
            InteractionHand var6 = var5 == null ? InteractionHand.MAIN_HAND : var5;
            if (var8 == null || !var8.V(var3.getItemInHand(var6))) {
               AnimationsModule var7 = AnimationsModule.g();
               if (var7 != null && var7.isRuntimeActive() && var7.P()) {
                  var2.setReturnValue(var7.t((Integer)var2.getReturnValue()));
               }
            }
         }
      }
   }
}
