package com.larpclient.mixin;

import com.larpclient.util.world.FakeSpawnerController;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemClusterRenderState.class)
public abstract class ItemClusterRenderStateMixin {
   @ModifyVariable(method = "extractItemGroupRenderState", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private ItemStack y(ItemStack var1) {
      return FakeSpawnerController.getInstance().removeSpawnerEntity(var1);
   }
}
