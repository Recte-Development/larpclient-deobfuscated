package com.larp.obf.q5682c6a8aca9b6ec;

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
public class q2a3869831cd243f0 {
   @Unique
   private boolean k;

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void l(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.s();
      this.k = var6 != null && var6.B();
      if (this.k) {
         var1.pose().pushMatrix();
         var1.pose().translate(0.0F, -var1.guiHeight() * (1.0F - var6.B()));
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void x(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      if (this.k) {
         var1.pose().popMatrix();
         this.k = false;
      }
   }
}
