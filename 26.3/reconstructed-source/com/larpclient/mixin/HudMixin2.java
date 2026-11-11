package com.larpclient.mixin;

import com.larpclient.hud.VisualInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public abstract class HudMixin2 {
   @WrapOperation(
      method = "extractItemHotbar",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack x(Inventory var1, int var2, Operation<ItemStack> var3) {
      int var4 = VisualInventory.isActive() ? VisualInventory.getPhysicalSlot(var2) : var2;
      return var4 < 0 ? ItemStack.EMPTY : VisualInventory.displayStack(var4, (ItemStack)var3.call(new Object[]{var1, var4}));
   }

   @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int E(Inventory var1, Operation<Integer> var2) {
      return VisualInventory.getSelectedSlot((Integer)var2.call(new Object[]{var1}));
   }
}
