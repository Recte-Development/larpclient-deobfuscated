package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.Feature3DUIModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class q87c085178a871ceb {
   @WrapOperation(
      method = "extractRenderState(Lnet/minecraft/client/DeltaTracker;ZZ)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/Hud;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
      )
   )
   private void E(Hud var1, GuiGraphicsExtractor var2, DeltaTracker var3, Operation<Void> var4) {
      if (!Feature3DUIModule.H()) {
         var4.call(new Object[]{var1, var2, var3});
      }
   }
}
