package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.FreecamModule;
import com.larpclient.module.WorldMenuModule;
import com.larpclient.module.ZoomModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MouseHandler.class)
public abstract class qbdf705c0da722c75 {
   @ModifyArgs(method = "turnPlayer(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
   private void h(Args var1) {
      double var2 = ZoomModule.u();
      var1.set(0, (Double)var1.get(0) * var2);
      var1.set(1, (Double)var1.get(1) * var2);
   }

   @Inject(method = "onScroll(JDD)V", at = @At("HEAD"), cancellable = true)
   private void H(long var1, double var3, double var5, CallbackInfo var7) {
      Minecraft var8 = Minecraft.getInstance();
      if (var1 == var8.getWindow().handle() && var5 != 0.0) {
         double var9 = (var8.options.discreteMouseScroll().get() ? Math.signum(var5) : var5) * (Double)var8.options.mouseWheelSensitivity().get();
         if (var8.gui.screen() == null && var8.gui.overlay() == null && (WorldMenuModule.s(var9) || ZoomModule.W(var9) || FreecamModule.R(var9))) {
            var7.cancel();
         }
      }
   }
}
