package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.dc;
import com.larpclient.f8;
import com.larpclient.qc;
import com.larpclient.sd;
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
public abstract class q42a48b06b126ae14 {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private GuiRenderState guiRenderState;

   @Inject(method = "setScreen", at = @At("HEAD"))
   private void d(Screen var1, CallbackInfo var2) {
      Screen var3 = this.minecraft.gui.screen();
      if (var3 != var1) {
         dc var4 = sd.S(var3);
         if (var4 != null) {
            var4.C();
         }
      }
   }

   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void M(Screen var1, CallbackInfo var2) {
      if (var1 instanceof f8 var3 && WorldMenuModule.I(var3)) {
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
   private void k(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      WorldMenuModule.C(new GuiGraphicsExtractor(this.minecraft, qc.p(this.guiRenderState), -10000, -10000), var1.getGameTimeDeltaPartialTick(false));
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void Q(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      if (f8.H()) {
         f8.E(new GuiGraphicsExtractor(this.minecraft, qc.p(this.guiRenderState), -10000, -10000), var1.getGameTimeDeltaPartialTick(false));
      }
   }
}
