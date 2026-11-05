package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.mv;
import com.larpclient.r3;
import com.larpclient.rk;
import com.larpclient.ys;
import com.larpclient.module.GUIAnimationModule;
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
public abstract class q5d3972e30ca43861 {
   @Unique
   private static float t() {
      Screen var0 = Minecraft.getInstance().gui.screen();
      return var0 instanceof AbstractContainerScreen && var0 instanceof mv var1 ? var1.larpContainerScale() : 1.0F;
   }

   @Unique
   private static double[] R(double var0, double var2) {
      Screen var4 = Minecraft.getInstance().gui.screen();
      if (var4 == null) {
         return new double[]{var0, var2};
      }

      rk.f var5 = rk.t(var4);
      if (var5 != null) {
         double[] var6 = var5.inverse(var0, var2);
         var0 = var6[0];
         var2 = var6[1];
      }

      r3.h var10 = ys.Z(var4);
      var0 = var10.localX(var0, var4.width / 2.0);
      var2 = var10.localY(var2, var4.height / 2.0);
      GUIAnimationModule var7 = GUIAnimationModule.s();
      return var7 == null ? new double[]{var0, var2} : new double[]{var7.E(var0, var4.width / 2.0, t()), var7.E(var2, var4.height / 2.0, t())};
   }

   @Unique
   private static MouseButtonEvent G(MouseButtonEvent var0) {
      double[] var1 = R(var0.x(), var0.y());
      return new MouseButtonEvent(var1[0], var1[1], var0.buttonInfo());
   }

   @ModifyArgs(
      method = "onButton",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z")
   )
   private void c(Args var1) {
      var1.set(0, G((MouseButtonEvent)var1.get(0)));
   }

   @ModifyArgs(
      method = "onButton",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseReleased(Lnet/minecraft/client/input/MouseButtonEvent;)Z")
   )
   private void Z(Args var1) {
      var1.set(0, G((MouseButtonEvent)var1.get(0)));
   }

   @ModifyArgs(
      method = "handleAccumulatedMovement",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z")
   )
   private void J(Args var1) {
      MouseButtonEvent var2 = (MouseButtonEvent)var1.get(0);
      double var3 = (Double)var1.get(1);
      double var5 = (Double)var1.get(2);
      double[] var7 = R(var2.x() - var3, var2.y() - var5);
      double[] var8 = R(var2.x(), var2.y());
      var1.set(0, G(var2));
      var1.set(1, var8[0] - var7[0]);
      var1.set(2, var8[1] - var7[1]);
   }

   @ModifyArgs(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseMoved(DD)V"))
   private void n(Args var1) {
      double[] var2 = R((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }

   @ModifyArgs(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z"))
   private void v(Args var1) {
      double[] var2 = R((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }
}
