package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ks;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.SwordModelsModule;
import com.larpclient.module.ViewModelModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class q13361d28d03d19da {
   @Inject(method = "getCurrentSwingDuration", at = @At("RETURN"), cancellable = true)
   private void Z(CallbackInfoReturnable<Integer> var1) {
      if (this instanceof LocalPlayer var2) {
         if (this == Minecraft.getInstance().player) {
            SwordModelsModule var9 = SwordModelsModule.N();
            InteractionHand var4 = ks.t(var2);
            InteractionHand var5 = var4 == null ? InteractionHand.MAIN_HAND : var4;
            if (var9 == null || !var9.Q(var2.getItemInHand(var5))) {
               int var6 = (Integer)var1.getReturnValue();
               AnimationsModule var7 = AnimationsModule.I();
               if (var7 != null && var7.isRuntimeActive() && var7.J()) {
                  var6 = var7.a(var6);
               }

               ViewModelModule var8 = ViewModelModule.b();
               if (var8 != null && var8.isRuntimeActive()) {
                  var6 = var8.U(var6);
               }

               if (var6 != (Integer)var1.getReturnValue()) {
                  var1.setReturnValue(var6);
               }
            }
         }
      }
   }
}
