package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemClusterRenderState.class)
public abstract class q868ee4e8ea050e2c {
   @ModifyVariable(method = "extractItemGroupRenderState", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private ItemStack c(ItemStack var1) {
      return dr.z().v(var1);
   }
}
