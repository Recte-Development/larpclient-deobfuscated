package com.larpclient.mixin;

import com.larpclient.module.misc.WorldMenuModule;
import com.larpclient.module.render.FreecamModule;
import com.larpclient.module.render.ZoomModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin_2 {
   @ModifyArgs(method = "turnPlayer(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
   private void e(Args var1) {
      double var2 = ZoomModule.getSensitivityScale();
      var1.set(0, (Double)var1.get(0) * var2);
      var1.set(1, (Double)var1.get(1) * var2);
   }

   @Inject(method = "onScroll(JDD)V", at = @At("HEAD"), cancellable = true)
   private void u(long var1, double var3, double var5, CallbackInfo var7) {
      Minecraft var8 = Minecraft.getInstance();
      if (var1 == var8.getWindow().handle() && var5 != 0.0) {
         double var9 = (var8.options.discreteMouseScroll().get() ? Math.signum(var5) : var5) * (Double)var8.options.mouseWheelSensitivity().get();
         if (var8.gui.screen() == null
            && var8.gui.overlay() == null
            && (WorldMenuModule.onMouseRelease(var9) || ZoomModule.onScroll(var9) || FreecamModule.onScroll(var9))) {
            var7.cancel();
         }
      }
   }
}
