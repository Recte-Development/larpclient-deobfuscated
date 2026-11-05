package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent.ChatGraphicsAccess;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.gui.components.ChatComponent$1")
public class q8f3e3edd27e01d9e {
   @WrapOperation(
      method = "accept",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;handleMessage(IFLnet/minecraft/util/FormattedCharSequence;)Z"
      )
   )
   private boolean l(ChatGraphicsAccess var1, int var2, float var3, FormattedCharSequence var4, Operation<Boolean> var5, @Local(argsOnly = true) Line var6) {
      GUIAnimationModule var7 = GUIAnimationModule.s();
      Minecraft var8 = Minecraft.getInstance();
      if (var7 != null && var7.n() && !(var8.gui.screen() instanceof ChatScreen)) {
         double var9 = Math.clamp((var8.gui.hud.getGuiTicks() - var6.addedTime() + var8.getDeltaTracker().getGameTimeDeltaPartialTick(false)) / 9.0, 0.0, 1.0);
         float var11 = (float)(1.0 - Math.pow(1.0 - var9, 3.0));
         float var12 = -8.0F * (1.0F - var11);
         var1.updatePose(var1x -> var1x.translate(var12, 0.0F));

         try {
            return (Boolean)var5.call(new Object[]{var1, var2, var3 * var11, var4});
         } finally {
            var1.updatePose(var1x -> var1x.translate(-var12, 0.0F));
         }
      } else {
         return (Boolean)var5.call(new Object[]{var1, var2, var3, var4});
      }
   }
}
