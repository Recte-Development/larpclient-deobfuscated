package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.sg;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiGraphicsExtractor.class)
public class q0773393814360526 {
   @ModifyVariable(
      method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack M(ItemStack var1) {
      return sg.G().b(var1);
   }
}
