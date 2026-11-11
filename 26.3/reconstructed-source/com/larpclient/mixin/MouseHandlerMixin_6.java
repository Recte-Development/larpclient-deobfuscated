package com.larpclient.mixin;

import com.larpclient.gui.misc.MenuPopupHost;
import com.larpclient.module.render.GUIAnimationModule;
import com.larpclient.render.overlay.ScreenWarp;
import com.larpclient.util.inventory.ContainerScaleAccessor;
import com.larpclient.util.render.PressAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin_6 {
   @Unique
   private static float w() {
      Screen var0 = Minecraft.getInstance().gui.screen();
      return var0 instanceof AbstractContainerScreen && var0 instanceof ContainerScaleAccessor var1 ? var1.larpContainerScale() : 1.0F;
   }

   @Unique
   private static double[] R(double var0, double var2) {
      Screen var4 = Minecraft.getInstance().gui.screen();
      if (var4 == null) {
         return new double[]{var0, var2};
      }

      ScreenWarp.Warp var5 = ScreenWarp.getWarp(var4);
      if (var5 != null) {
         double[] var6 = var5.inverse(var0, var2);
         var0 = var6[0];
         var2 = var6[1];
      }

      PressAnimation.Transform var10 = MenuPopupHost.getPopupState(var4);
      var0 = var10.localX(var0, var4.width / 2.0);
      var2 = var10.localY(var2, var4.height / 2.0);
      GUIAnimationModule var7 = GUIAnimationModule.getInstance();
      return var7 == null
         ? new double[]{var0, var2}
         : new double[]{var7.easeToward(var0, var4.width / 2.0, w()), var7.easeToward(var2, var4.height / 2.0, w())};
   }

   @Unique
   private static MouseButtonEvent c(MouseButtonEvent var0) {
      double[] var1 = R(var0.x(), var0.y());
      return new MouseButtonEvent(var1[0], var1[1], var0.buttonInfo());
   }

   @ModifyArgs(
      method = "onButton",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z")
   )
   private void n(Args var1) {
      var1.set(0, c((MouseButtonEvent)var1.get(0)));
   }

   @ModifyArgs(
      method = "onButton",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseReleased(Lnet/minecraft/client/input/MouseButtonEvent;)Z")
   )
   private void u(Args var1) {
      var1.set(0, c((MouseButtonEvent)var1.get(0)));
   }

   @ModifyArgs(
      method = "handleAccumulatedMovement",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z")
   )
   private void i(Args var1) {
      MouseButtonEvent var2 = (MouseButtonEvent)var1.get(0);
      double var3 = (Double)var1.get(1);
      double var5 = (Double)var1.get(2);
      double[] var7 = R(var2.x() - var3, var2.y() - var5);
      double[] var8 = R(var2.x(), var2.y());
      var1.set(0, c(var2));
      var1.set(1, var8[0] - var7[0]);
      var1.set(2, var8[1] - var7[1]);
   }

   @ModifyArgs(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseMoved(DD)V"))
   private void B(Args var1) {
      double[] var2 = R((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }

   @ModifyArgs(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z"))
   private void p(Args var1) {
      double[] var2 = R((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }
}
