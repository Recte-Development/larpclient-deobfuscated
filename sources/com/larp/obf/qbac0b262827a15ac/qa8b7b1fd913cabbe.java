package com.larp.obf.qbac0b262827a15ac;

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
public class qa8b7b1fd913cabbe {
   @Inject(method = "extractScoreboardSidebar", at = @At("HEAD"), cancellable = true)
   private void F(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (RemovalsModule.r("scoreboard")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
   private void r(GuiGraphicsExtractor var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.r("portal")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractConfusionOverlay", at = @At("HEAD"), cancellable = true)
   private void h(GuiGraphicsExtractor var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.r("nausea")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
   private void f(GuiGraphicsExtractor var1, Identifier var2, float var3, CallbackInfo var4) {
      if (RemovalsModule.r("pumpkin") && var2.equals(Identifier.withDefaultNamespace("textures/misc/pumpkinblur.png"))) {
         var4.cancel();
      }
   }
}
