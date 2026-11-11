package com.larpclient.mixin;

import com.larpclient.module.render.Feature3DUIModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Gui.class)
public abstract class GuiMixin_3 {
   @WrapOperation(
      method = "extractRenderState(Lnet/minecraft/client/DeltaTracker;ZZ)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/Hud;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
      )
   )
   private void W(Hud var1, GuiGraphicsExtractor var2, DeltaTracker var3, Operation<Void> var4) {
      if (!Feature3DUIModule.hidesHud()) {
         var4.call(new Object[]{var1, var2, var3});
      }
   }
}
