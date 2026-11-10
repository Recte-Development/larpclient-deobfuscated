package com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e;

import com.larpclient._6;
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
public abstract class q01f5264ec9cee788 {
   @Shadow
   protected Slot hoveredSlot;

   @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
   private void g(MouseButtonEvent var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (_6.H() && this instanceof InventoryScreen) {
         var3.setReturnValue(true);
         if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            _6.D(this.hoveredSlot.getItem());
         }
      }
   }
}
