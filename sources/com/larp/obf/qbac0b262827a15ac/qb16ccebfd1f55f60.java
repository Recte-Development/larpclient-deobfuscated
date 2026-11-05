package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.GUIAnimationModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public class qb16ccebfd1f55f60 {
   @Unique
   private boolean X;

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void Z(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.F();
      this.X = var6 != null && var6.K();
      if (this.X) {
         var1.pose().pushMatrix();
         var1.pose().translate(0.0F, -var1.guiHeight() * (1.0F - var6.I()));
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void N(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      if (this.X) {
         var1.pose().popMatrix();
         this.X = false;
      }
   }
}
