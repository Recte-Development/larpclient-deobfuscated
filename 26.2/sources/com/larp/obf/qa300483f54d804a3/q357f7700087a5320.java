package com.larp.obf.qa300483f54d804a3;

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
public class q357f7700087a5320 {
   @Unique
   private boolean T;

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void I(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.a();
      this.T = var6 != null && var6.K();
      if (this.T) {
         var1.pose().pushMatrix();
         var1.pose().translate(0.0F, -var1.guiHeight() * (1.0F - var6.u()));
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void L(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      if (this.T) {
         var1.pose().popMatrix();
         this.T = false;
      }
   }
}
