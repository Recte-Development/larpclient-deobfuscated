package com.larp.obf.q41a5851bcf8e876a;

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
public class q2bf3a240e4b59189 {
   @Unique
   private boolean d;

   @Inject(method = "extractRenderState", at = @At("HEAD"))
   private void j(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.O();
      this.d = var6 != null && var6.t();
      if (this.d) {
         var1.pose().pushMatrix();
         var1.pose().translate(0.0F, -var1.guiHeight() * (1.0F - var6.S()));
      }
   }

   @Inject(method = "extractRenderState", at = @At("RETURN"))
   private void e(GuiGraphicsExtractor var1, int var2, Scoreboard var3, Objective var4, CallbackInfo var5) {
      if (this.d) {
         var1.pose().popMatrix();
         this.d = false;
      }
   }
}
