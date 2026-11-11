package com.larpclient.mixin;

import com.larpclient.module.combat.AimAssistModule;
import com.larpclient.util.math.AimAssistMath;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin_5 {
   @ModifyArgs(
      method = "turnPlayer(D)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"),
      order = 1100,
      require = 1,
      allow = 1
   )
   private void A(Args var1, double var2) {
      AimAssistMath.MouseDelta var4 = AimAssistModule.applyMouseCorrection(Minecraft.getInstance().player, (Double)var1.get(0), (Double)var1.get(1), var2);
      var1.set(0, (Double)var1.get(0) + var4.yaw());
      var1.set(1, (Double)var1.get(1) + var4.pitch());
   }
}
