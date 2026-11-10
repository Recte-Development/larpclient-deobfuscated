package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.qm;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemFrameRenderer.class)
public class qa4c3a123117004d8 {
   @WrapOperation(
      method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;getItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack L(ItemFrame var1, Operation<ItemStack> var2) {
      return qm.m(var1, (ItemStack)var2.call(new Object[]{var1}));
   }
}
