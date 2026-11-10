package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.GUIAnimationModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class qa4e2335d6a76f1c0 {
   @Shadow
   protected Slot hoveredSlot;
   @Unique
   private boolean I;

   @Inject(method = "extractSlot", at = @At("HEAD"))
   private void Z(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.a();
      this.I = var6 != null && var6.V();
      if (this.I) {
         float var7 = var6.D(var2, var2 == this.hoveredSlot && var2.isActive());
         var1.pose().pushMatrix();
         var1.pose().translate(var2.x + 8.0F, var2.y + 8.0F);
         var1.pose().scale(var7, var7);
         var1.pose().translate(-var2.x - 8.0F, -var2.y - 8.0F);
      }
   }

   @Inject(method = "extractSlot", at = @At("RETURN"))
   private void i(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      if (this.I) {
         var1.pose().popMatrix();
         this.I = false;
      }
   }
}
