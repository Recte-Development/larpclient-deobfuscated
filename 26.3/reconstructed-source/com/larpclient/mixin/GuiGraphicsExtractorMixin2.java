package com.larpclient.mixin;

import com.larpclient.module.misc.CasinoRigManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiGraphicsExtractor.class)
public class GuiGraphicsExtractorMixin2 {
   @ModifyVariable(
      method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack R(ItemStack var1) {
      return CasinoRigManager.getInstance().applyVisualOverrides(var1);
   }
}
