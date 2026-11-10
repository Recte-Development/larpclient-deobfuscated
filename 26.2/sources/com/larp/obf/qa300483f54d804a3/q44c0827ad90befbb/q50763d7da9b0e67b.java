package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.module.w;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractContainerScreen.class, priority = 500)
public abstract class q50763d7da9b0e67b {
   @Shadow
   protected Slot hoveredSlot;
   @Shadow
   @Final
   protected AbstractContainerMenu menu;

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void i(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (w.a(var1, this.menu)) {
         var5.cancel();
      }
   }

   @Inject(method = "extractSlot", at = @At("TAIL"))
   private void r(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      int var6 = w.O(var2, var2 == this.hoveredSlot, this.menu);
      if (var6 != 0) {
         var1.fill(var2.x, var2.y, var2.x + 16, var2.y + 16, var6);
      }
   }
}
