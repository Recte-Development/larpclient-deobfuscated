package com.larp.obf.qa300483f54d804a3;

import com.larpclient.hf;
import com.larpclient.kt;
import com.larpclient.p2;
import com.larpclient.tw;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class qed96018cfbc50113 {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private GuiRenderState guiRenderState;

   @Inject(method = "setScreen", at = @At("HEAD"))
   private void I(Screen var1, CallbackInfo var2) {
      Screen var3 = this.minecraft.gui.screen();
      if (var3 != var1) {
         kt var4 = tw.t(var3);
         if (var4 != null) {
            var4.O();
         }
      }
   }

   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void X(Screen var1, CallbackInfo var2) {
      if (var1 instanceof hf var3 && WorldMenuModule.K(var3)) {
         var2.cancel();
         if (this.minecraft.gui.screen() != null) {
            this.minecraft.gui.setScreen(null);
         }
      }
   }

   @Inject(
      method = "extractRenderState",
      at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;reset()V")
   )
   private void w(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      WorldMenuModule.d(new GuiGraphicsExtractor(this.minecraft, p2.M(this.guiRenderState), -10000, -10000), var1.getGameTimeDeltaPartialTick(false));
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void q(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      if (hf.m()) {
         hf.V(new GuiGraphicsExtractor(this.minecraft, p2.M(this.guiRenderState), -10000, -10000), var1.getGameTimeDeltaPartialTick(false));
      }
   }
}
