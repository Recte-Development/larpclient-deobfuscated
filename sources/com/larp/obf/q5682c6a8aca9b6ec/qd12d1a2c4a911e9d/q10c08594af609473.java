package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.mx;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public abstract class q10c08594af609473 {
   @WrapOperation(
      method = "extractItemHotbar",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack M(Inventory var1, int var2, Operation<ItemStack> var3) {
      int var4 = mx.U() ? mx.V(var2) : var2;
      return var4 < 0 ? ItemStack.EMPTY : mx.n(var4, (ItemStack)var3.call(new Object[]{var1, var4}));
   }

   @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int o(Inventory var1, Operation<Integer> var2) {
      return mx.z((Integer)var2.call(new Object[]{var1}));
   }
}
