package com.larpclient.mixin;

import com.larpclient.module.render.AnimationsModule;
import com.larpclient.module.render.SwordModelsModule;
import com.larpclient.module.render.ViewModelModule;
import com.larpclient.util.entity.SwingHelper;
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
public class LivingEntityMixin_5 {
   @Inject(method = "getModifiedSwingDuration", at = @At("RETURN"), cancellable = true)
   private void o(SwingAnimation var1, CallbackInfoReturnable<Integer> var2) {
      if (this instanceof LocalPlayer var3) {
         if (this == Minecraft.getInstance().player) {
            SwordModelsModule var10 = SwordModelsModule.getInstance();
            InteractionHand var5 = SwingHelper.getSwingHand(var3);
            InteractionHand var6 = var5 == null ? InteractionHand.MAIN_HAND : var5;
            if (var10 == null || !var10.shouldReplace(var3.getItemInHand(var6))) {
               int var7 = (Integer)var2.getReturnValue();
               AnimationsModule var8 = AnimationsModule.getInstance();
               if (var8 != null && var8.isRuntimeActive() && var8.isSlowSwing()) {
                  var7 = var8.getSwingDuration(var7);
               }

               ViewModelModule var9 = ViewModelModule.getInstance();
               if (var9 != null && var9.isRuntimeActive()) {
                  var7 = var9.scaleSwingDuration(var7);
               }

               if (var7 != (Integer)var2.getReturnValue()) {
                  var2.setReturnValue(var7);
               }
            }
         }
      }
   }
}
