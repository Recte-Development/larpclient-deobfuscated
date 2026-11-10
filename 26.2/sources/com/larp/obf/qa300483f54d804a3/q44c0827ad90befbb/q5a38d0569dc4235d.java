package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.gj;
import com.larpclient.m6;
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
public abstract class q5a38d0569dc4235d {
   @Shadow
   @Final
   protected AbstractContainerMenu menu;

   @Inject(method = "init", at = @At("TAIL"))
   private void q(CallbackInfo var1) {
      if (this.menu instanceof DispenserMenu var2) {
         m6.W().R();
         gj.c().i(Minecraft.getInstance(), var2);
      }
   }
}
