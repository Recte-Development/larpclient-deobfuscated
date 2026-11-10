package com.larp.obf.qa300483f54d804a3;

import com.larpclient.lk;
import com.larpclient.m_;
import com.larpclient.mb;
import com.larpclient.tw;
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
public abstract class q28673ac726b466aa {
   @Unique
   private static float Q() {
      Screen var0 = Minecraft.getInstance().gui.screen();
      return var0 instanceof AbstractContainerScreen && var0 instanceof lk var1 ? var1.larpContainerScale() : 1.0F;
   }

   @Unique
   private static double[] A(double var0, double var2) {
      Screen var4 = Minecraft.getInstance().gui.screen();
      if (var4 == null) {
         return new double[]{var0, var2};
      }

      m_.g var5 = m_.U(var4);
      if (var5 != null) {
         double[] var6 = var5.inverse(var0, var2);
         var0 = var6[0];
         var2 = var6[1];
      }

      mb.d var10 = tw.E(var4);
      var0 = var10.localX(var0, var4.width / 2.0);
      var2 = var10.localY(var2, var4.height / 2.0);
      GUIAnimationModule var7 = GUIAnimationModule.a();
      return var7 == null ? new double[]{var0, var2} : new double[]{var7.d(var0, var4.width / 2.0, Q()), var7.d(var2, var4.height / 2.0, Q())};
   }

   @Unique
   private static MouseButtonEvent x(MouseButtonEvent var0) {
      double[] var1 = A(var0.x(), var0.y());
      return new MouseButtonEvent(var1[0], var1[1], var0.buttonInfo());
   }

   @ModifyArgs(
      method = "onButton",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseClicked(Lnet/minecraft/client/input/MouseButtonEvent;Z)Z")
   )
   private void F(Args var1) {
      var1.set(0, x((MouseButtonEvent)var1.get(0)));
   }

   @ModifyArgs(
      method = "onButton",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseReleased(Lnet/minecraft/client/input/MouseButtonEvent;)Z")
   )
   private void y(Args var1) {
      var1.set(0, x((MouseButtonEvent)var1.get(0)));
   }

   @ModifyArgs(
      method = "handleAccumulatedMovement",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseDragged(Lnet/minecraft/client/input/MouseButtonEvent;DD)Z")
   )
   private void O(Args var1) {
      MouseButtonEvent var2 = (MouseButtonEvent)var1.get(0);
      double var3 = (Double)var1.get(1);
      double var5 = (Double)var1.get(2);
      double[] var7 = A(var2.x() - var3, var2.y() - var5);
      double[] var8 = A(var2.x(), var2.y());
      var1.set(0, x(var2));
      var1.set(1, var8[0] - var7[0]);
      var1.set(2, var8[1] - var7[1]);
   }

   @ModifyArgs(method = "handleAccumulatedMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseMoved(DD)V"))
   private void A(Args var1) {
      double[] var2 = A((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }

   @ModifyArgs(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z"))
   private void W(Args var1) {
      double[] var2 = A((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }
}
