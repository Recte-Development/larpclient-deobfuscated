package com.larp.obf.qa300483f54d804a3;

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
public class qf72727fffc7fa311 {
   @Inject(method = "extractScoreboardSidebar", at = @At("HEAD"), cancellable = true)
   private void u(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (RemovalsModule.n("scoreboard")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
   private void k(GuiGraphicsExtractor var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.n("portal")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractConfusionOverlay", at = @At("HEAD"), cancellable = true)
   private void C(GuiGraphicsExtractor var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.n("nausea")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
   private void Z(GuiGraphicsExtractor var1, Identifier var2, float var3, CallbackInfo var4) {
      if (RemovalsModule.n("pumpkin") && var2.equals(Identifier.withDefaultNamespace("textures/misc/pumpkinblur.png"))) {
         var4.cancel();
      }
   }
}
