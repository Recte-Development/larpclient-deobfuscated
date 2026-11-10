package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.module.fb;
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
public abstract class q0b22937f51309d93 {
   @Shadow
   protected Slot hoveredSlot;
   @Shadow
   @Final
   protected AbstractContainerMenu menu;

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void X(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (fb.n(var1, this.menu)) {
         var5.cancel();
      }
   }

   @Inject(method = "extractSlot", at = @At("TAIL"))
   private void Q(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      int var6 = fb.i(var2, var2 == this.hoveredSlot, this.menu);
      if (var6 != 0) {
         var1.fill(var2.x, var2.y, var2.x + 16, var2.y + 16, var6);
      }
   }
}
