package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.RemovalsModule;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class q69a60c6a114205f4 {
   @Inject(method = "extractScoreboardSidebar", at = @At("HEAD"), cancellable = true)
   private void B(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (RemovalsModule.s("scoreboard")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
   private void b(GuiGraphicsExtractor var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.s("portal")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractConfusionOverlay", at = @At("HEAD"), cancellable = true)
   private void O(GuiGraphicsExtractor var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.s("nausea")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
   private void C(GuiGraphicsExtractor var1, Identifier var2, float var3, CallbackInfo var4) {
      if (RemovalsModule.s("pumpkin") && var2.equals(Identifier.withDefaultNamespace("textures/misc/pumpkinblur.png"))) {
         var4.cancel();
      }
   }
}
