package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient.jn;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemClusterRenderState.class)
public abstract class qcfe5912e3560647c {
   @ModifyVariable(method = "extractItemGroupRenderState", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private ItemStack y(ItemStack var1) {
      return jn.g().w(var1);
   }
}
