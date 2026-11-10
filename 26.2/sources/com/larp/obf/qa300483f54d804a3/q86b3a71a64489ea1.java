package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ze;
import com.larpclient.module.AimAssistModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MouseHandler.class)
public abstract class q86b3a71a64489ea1 {
   @ModifyArgs(
      method = "turnPlayer(D)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"),
      order = 1100,
      require = 1,
      allow = 1
   )
   private void W(Args var1, double var2) {
      ze.i var4 = AimAssistModule.b(Minecraft.getInstance().player, (Double)var1.get(0), (Double)var1.get(1), var2);
      var1.set(0, (Double)var1.get(0) + var4.yaw());
      var1.set(1, (Double)var1.get(1) + var4.pitch());
   }
}
