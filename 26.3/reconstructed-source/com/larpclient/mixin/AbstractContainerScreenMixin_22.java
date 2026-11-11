package com.larpclient.mixin;

import com.larpclient.util.inventory.HotbarSwapQueue;
import com.larpclient.util.world.DispenserRegistry;
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
public abstract class AbstractContainerScreenMixin_22 {
   @Shadow
   @Final
   protected AbstractContainerMenu menu;

   @Inject(method = "init", at = @At("TAIL"))
   private void E(CallbackInfo var1) {
      if (this.menu instanceof DispenserMenu var2) {
         HotbarSwapQueue.getInstance().clearQueue();
         DispenserRegistry.getInstance().onContainerOpened(Minecraft.getInstance(), var2);
      }
   }
}
