package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.ki;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public abstract class qf159264a4ff5c7b7 {
   @WrapOperation(
      method = "extractItemHotbar",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack B(Inventory var1, int var2, Operation<ItemStack> var3) {
      int var4 = ki.k() ? ki.t(var2) : var2;
      return var4 < 0 ? ItemStack.EMPTY : ki.T(var4, (ItemStack)var3.call(new Object[]{var1, var4}));
   }

   @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int Y(Inventory var1, Operation<Integer> var2) {
      return ki.k((Integer)var2.call(new Object[]{var1}));
   }
}
