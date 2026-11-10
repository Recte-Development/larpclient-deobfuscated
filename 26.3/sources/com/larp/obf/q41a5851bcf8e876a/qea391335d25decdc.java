package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.h8;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.SwordModelsModule;
import com.larpclient.module.ViewModelModule;
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
public class qea391335d25decdc {
   @Inject(method = "getModifiedSwingDuration", at = @At("RETURN"), cancellable = true)
   private void o(SwingAnimation var1, CallbackInfoReturnable<Integer> var2) {
      if (this instanceof LocalPlayer var3) {
         if (this == Minecraft.getInstance().player) {
            SwordModelsModule var10 = SwordModelsModule.p();
            InteractionHand var5 = h8.J(var3);
            InteractionHand var6 = var5 == null ? InteractionHand.MAIN_HAND : var5;
            if (var10 == null || !var10.E(var3.getItemInHand(var6))) {
               int var7 = (Integer)var2.getReturnValue();
               AnimationsModule var8 = AnimationsModule.a();
               if (var8 != null && var8.isRuntimeActive() && var8.r()) {
                  var7 = var8.k(var7);
               }

               ViewModelModule var9 = ViewModelModule.t();
               if (var9 != null && var9.isRuntimeActive()) {
                  var7 = var9.U(var7);
               }

               if (var7 != (Integer)var2.getReturnValue()) {
                  var2.setReturnValue(var7);
               }
            }
         }
      }
   }
}
