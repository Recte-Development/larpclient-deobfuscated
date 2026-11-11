package com.larpclient.mixin;

import com.larpclient.core.StreamProofManager;
import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.misc.MenuPopupHost;
import com.larpclient.module.misc.WorldMenuModule;
import com.larpclient.util.math.ChargeRamp;
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
public abstract class GuiMixin {
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
         ChargeRamp var4 = MenuPopupHost.getPopup(var3);
         if (var4 != null) {
            var4.finish();
         }
      }
   }

   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void M(Screen var1, CallbackInfo var2) {
      if (var1 instanceof ClickGuiScreen var3 && WorldMenuModule.claimScreen(var3)) {
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
      WorldMenuModule.renderPanel(
         new GuiGraphicsExtractor(this.minecraft, StreamProofManager.selectGuiRenderState(this.guiRenderState), -10000, -10000),
         var1.getGameTimeDeltaPartialTick(false)
      );
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void Q(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      if (ClickGuiScreen.isCloseEffectActive()) {
         ClickGuiScreen.renderCloseEffect(
            new GuiGraphicsExtractor(this.minecraft, StreamProofManager.selectGuiRenderState(this.guiRenderState), -10000, -10000),
            var1.getGameTimeDeltaPartialTick(false)
         );
      }
   }
}
