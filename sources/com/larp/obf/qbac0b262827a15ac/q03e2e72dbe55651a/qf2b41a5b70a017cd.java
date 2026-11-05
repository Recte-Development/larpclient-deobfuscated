package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.c3;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiGraphicsExtractor.class)
public class qf2b41a5b70a017cd {
   @ModifyVariable(
      method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack p(ItemStack var1) {
      return c3.z().r(var1);
   }
}
