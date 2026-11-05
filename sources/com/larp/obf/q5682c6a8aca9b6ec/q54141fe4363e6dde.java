package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.p0;
import com.larpclient.y0;
import com.larpclient.y8;
import com.larpclient.ys;
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
public abstract class q54141fe4363e6dde {
   @Shadow
   @Final
   private Minecraft minecraft;
   @Shadow
   @Final
   private GuiRenderState guiRenderState;

   @Inject(method = "setScreen", at = @At("HEAD"))
   private void M(Screen var1, CallbackInfo var2) {
      Screen var3 = this.minecraft.gui.screen();
      if (var3 != var1) {
         y0 var4 = ys.p(var3);
         if (var4 != null) {
            var4.z();
         }
      }
   }

   @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
   private void F(Screen var1, CallbackInfo var2) {
      if (var1 instanceof p0 var3 && WorldMenuModule.F(var3)) {
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
   private void n(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      WorldMenuModule.O(new GuiGraphicsExtractor(this.minecraft, y8.p(this.guiRenderState), -10000, -10000), var1.getGameTimeDeltaPartialTick(false));
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void M(DeltaTracker var1, boolean var2, boolean var3, CallbackInfo var4) {
      if (p0.H()) {
         p0.j(new GuiGraphicsExtractor(this.minecraft, y8.p(this.guiRenderState), -10000, -10000), var1.getGameTimeDeltaPartialTick(false));
      }
   }
}
