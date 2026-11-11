package com.larpclient.mixin;

import com.larpclient.module.world.AutoAreaMiner;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin4 {
   @Shadow
   protected Slot hoveredSlot;

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void g(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (AutoAreaMiner.findPickaxeSlot() && this instanceof InventoryScreen) {
         var3.setReturnValue(true);
         if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            AutoAreaMiner.setAside(this.hoveredSlot.getItem());
         }
      }
   }
}
