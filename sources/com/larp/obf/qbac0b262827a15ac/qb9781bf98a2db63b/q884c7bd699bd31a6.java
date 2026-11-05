package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.u0;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public abstract class q884c7bd699bd31a6 {
   @WrapOperation(
      method = "extractItemHotbar",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack r(Inventory var1, int var2, Operation<ItemStack> var3) {
      int var4 = u0.w() ? u0.f(var2) : var2;
      return var4 < 0 ? ItemStack.EMPTY : u0.N(var4, (ItemStack)var3.call(new Object[]{var1, var4}));
   }

   @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int Y(Inventory var1, Operation<Integer> var2) {
      return u0.Q((Integer)var2.call(new Object[]{var1}));
   }
}
