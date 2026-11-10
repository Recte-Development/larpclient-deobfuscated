package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient._8;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Hud.class)
public abstract class qfa8a933c1e0a862d {
   @WrapOperation(
      method = "extractItemHotbar",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getItem(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack x(Inventory var1, int var2, Operation<ItemStack> var3) {
      int var4 = _8.S() ? _8.t(var2) : var2;
      return var4 < 0 ? ItemStack.EMPTY : _8.R(var4, (ItemStack)var3.call(new Object[]{var1, var4}));
   }

   @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int E(Inventory var1, Operation<Integer> var2) {
      return _8.a((Integer)var2.call(new Object[]{var1}));
   }
}
