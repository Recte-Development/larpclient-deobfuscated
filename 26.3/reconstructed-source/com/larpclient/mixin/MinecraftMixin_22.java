package com.larpclient.mixin;

import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.world.WorldSelectionPicker;
import com.larpclient.util.misc.FiftyFiftyDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin_22 {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void a(CallbackInfo var1) {
      if (WorldSelectionPicker.handleBlockClick(Minecraft.getInstance())) {
         var1.cancel();
      } else if (FiftyFiftyDispenser.getInstance().shouldCancelInteraction()) {
         var1.cancel();
      } else {
         Minecraft var2 = Minecraft.getInstance();
         BlackjackRigger var3 = BlackjackRigger.getInstance();
         if (!var3.rankForMapId(var2) && !var3.startDraw()) {
            if (var2.player != null
               && var2.hitResult instanceof EntityHitResult var4
               && var4.getEntity() instanceof ItemFrame
               && CardForce.getActive().dumpCards(var2.player.getMainHandItem())) {
               var1.cancel();
            }
         } else {
            var1.cancel();
         }
      }
   }
}
