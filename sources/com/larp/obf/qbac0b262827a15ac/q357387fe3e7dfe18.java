package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.hw;
import com.larpclient.wv;
import com.larpclient.xt;
import com.larpclient.zo;
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
public abstract class q357387fe3e7dfe18 {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private GuiRenderState guiRenderState;

   @Inject(method = "setScreen", at = @At("HEAD"))
   private void j(Screen var1, CallbackInfo var2) {
      Screen var3 = this.minecraft.gui.screen();
      if (var3 != var1) {
         xt var4 = wv.P(var3);
         if (var4 != null) {
            var4.r();
         }
      }
   }

   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void E(Screen var1, CallbackInfo var2) {
      if (var1 instanceof hw var3 && WorldMenuModule.o(var3)) {
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
   private void L(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      WorldMenuModule.m(new GuiGraphicsExtractor(this.minecraft, zo.t(this.guiRenderState), -10000, -10000), var1.getGameTimeDeltaPartialTick(false));
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void g(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      if (hw.n()) {
         hw.X(new GuiGraphicsExtractor(this.minecraft, zo.t(this.guiRenderState), -10000, -10000), var1.getGameTimeDeltaPartialTick(false));
      }
   }
}
