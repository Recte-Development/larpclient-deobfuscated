package com.larp.obf.q41a5851bcf8e876a;

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
public abstract class qd99600b74c3ba4dd {
   @Shadow
   protected Slot hoveredSlot;
   @Unique
   private boolean H;

   @Inject(method = "extractSlot", at = @At("HEAD"))
   private void S(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      GUIAnimationModule var6 = GUIAnimationModule.O();
      this.H = var6 != null && var6.W();
      if (this.H) {
         float var7 = var6.u(var2, var2 == this.hoveredSlot && var2.isActive());
         var1.pose().pushMatrix();
         var1.pose().translate(var2.x + 8.0F, var2.y + 8.0F);
         var1.pose().scale(var7, var7);
         var1.pose().translate(-var2.x - 8.0F, -var2.y - 8.0F);
      }
   }

   @Inject(method = "extractSlot", at = @At("RETURN"))
   private void V(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      if (this.H) {
         var1.pose().popMatrix();
         this.H = false;
      }
   }
}
