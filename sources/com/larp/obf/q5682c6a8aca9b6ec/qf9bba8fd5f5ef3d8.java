package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.px;
import com.larpclient.module.AimAssistModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MouseHandler.class)
public abstract class qf9bba8fd5f5ef3d8 {
   @ModifyArgs(
      method = "turnPlayer(D)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"),
      order = 1100,
      require = 1,
      allow = 1
   )
   private void z(Args var1, double var2) {
      px.k var4 = AimAssistModule.P(Minecraft.getInstance().player, (Double)var1.get(0), (Double)var1.get(1), var2);
      var1.set(0, (Double)var1.get(0) + var4.yaw());
      var1.set(1, (Double)var1.get(1) + var4.pitch());
   }
}
