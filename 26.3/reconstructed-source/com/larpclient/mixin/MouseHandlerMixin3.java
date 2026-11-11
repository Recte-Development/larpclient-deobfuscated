package com.larpclient.mixin;

import com.larpclient.module.player.FishingRodSwapper;
import com.larpclient.util.inventory.MapSwapInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin3 {
   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int u(Inventory var1, Operation<Integer> var2) {
      return FishingRodSwapper.getInstance().setAutoSwap() ? FishingRodSwapper.getInstance().getActiveSlot() : MapSwapInventory.syncSelectedSlot();
   }
}
