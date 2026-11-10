package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.px;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemModelResolver.class)
public abstract class q74dd8c0b79331a83 {
   @Redirect(
      method = "appendItemLayers",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;")
   )
   private Object g(ItemStack var1, DataComponentType<?> var2) {
      return var2 != DataComponents.ITEM_MODEL ? var1.get(var2) : px.q(var1).get(DataComponents.ITEM_MODEL);
   }

   @ModifyVariable(method = "updateForTopItem", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private ItemStack p(ItemStack var1) {
      return px.q(var1);
   }
}
