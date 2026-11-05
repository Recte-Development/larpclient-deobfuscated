package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.lj;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemClusterRenderState.class)
public abstract class q5c2ef59822e88d0d {
   @ModifyVariable(method = "extractItemGroupRenderState", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private ItemStack X(ItemStack var1) {
      return lj.j().x(var1);
   }
}
