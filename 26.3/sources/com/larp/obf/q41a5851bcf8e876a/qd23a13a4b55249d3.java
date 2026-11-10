package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.jv;
import com.larpclient.module.AimAssistModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MouseHandler.class)
public abstract class qd23a13a4b55249d3 {
   @ModifyArgs(
      method = "turnPlayer(D)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"),
      order = 1100,
      require = 1,
      allow = 1
   )
   private void A(Args var1, double var2) {
      jv.y var4 = AimAssistModule.w(Minecraft.getInstance().player, (Double)var1.get(0), (Double)var1.get(1), var2);
      var1.set(0, (Double)var1.get(0) + var4.yaw());
      var1.set(1, (Double)var1.get(1) + var4.pitch());
   }
}
