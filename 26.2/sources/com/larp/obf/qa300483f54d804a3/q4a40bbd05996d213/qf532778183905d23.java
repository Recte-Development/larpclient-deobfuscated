package com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213;

import com.larpclient.ae;
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
public abstract class qf532778183905d23 {
   @Shadow
   protected Slot hoveredSlot;

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void D(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (ae.W() && this instanceof InventoryScreen) {
         var3.setReturnValue(true);
         if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ae.G(this.hoveredSlot.getItem());
         }
      }
   }
}
