package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.f9;
import com.larpclient.hm;
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
public abstract class qd8e5823e3c894e6e {
   @Shadow
   @Final
   protected AbstractContainerMenu menu;

   @Inject(method = "init", at = @At("TAIL"))
   private void w(CallbackInfo var1) {
      if (this.menu instanceof DispenserMenu var2) {
         hm.O().P();
         f9.W().B(Minecraft.getInstance(), var2);
      }
   }
}
