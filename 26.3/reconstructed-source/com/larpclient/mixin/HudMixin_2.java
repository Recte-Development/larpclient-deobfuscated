package com.larpclient.mixin;

import com.larpclient.module.render.RemovalsModule;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HudMixin_2 {
   @Inject(method = "extractScoreboardSidebar", at = @At("HEAD"), cancellable = true)
   private void N(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (RemovalsModule.isRemovalEnabled("scoreboard")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractPortalOverlay", at = @At("HEAD"), cancellable = true)
   private void c(GuiGraphicsExtractor var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.isRemovalEnabled("portal")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractConfusionOverlay", at = @At("HEAD"), cancellable = true)
   private void O(GuiGraphicsExtractor var1, float var2, CallbackInfo var3) {
      if (RemovalsModule.isRemovalEnabled("nausea")) {
         var3.cancel();
      }
   }

   @Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
   private void L(GuiGraphicsExtractor var1, Identifier var2, float var3, CallbackInfo var4) {
      if (RemovalsModule.isRemovalEnabled("pumpkin") && var2.equals(Identifier.withDefaultNamespace("textures/misc/pumpkinblur.png"))) {
         var4.cancel();
      }
   }
}
