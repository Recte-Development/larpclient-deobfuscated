package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.d1;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemModelResolver.class)
public abstract class q27eb14a3b34a4f11 {
   @Redirect(
      method = "appendItemLayers",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;")
   )
   private Object P(ItemStack var1, DataComponentType<?> var2) {
      return var2 != DataComponents.ITEM_MODEL ? var1.get(var2) : d1.m(var1).get(DataComponents.ITEM_MODEL);
   }

   @ModifyVariable(method = "updateForTopItem", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private ItemStack d(ItemStack var1) {
      return d1.m(var1);
   }
}
