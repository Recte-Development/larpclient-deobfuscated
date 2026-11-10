package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemClusterRenderState.class)
public abstract class q61a6d3a5de89f824 {
   @ModifyVariable(method = "extractItemGroupRenderState", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private ItemStack v(ItemStack var1) {
      return b8.k().D(var1);
   }
}
