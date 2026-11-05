package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.ep;
import com.larpclient.oz;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class q435127cc3c96a0d0 {
   @Shadow
   @Final
   protected AbstractContainerMenu menu;

   @Inject(method = "init", at = @At("TAIL"))
   private void Q(CallbackInfo var1) {
      if (this.menu instanceof DispenserMenu var2) {
         oz.R().t();
         ep.H().d(Minecraft.getInstance(), var2);
      }
   }
}
