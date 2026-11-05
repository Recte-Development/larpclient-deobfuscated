package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.w5;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemFrameRenderer.class)
public class q210e3c31885328c5 {
   @WrapOperation(
      method = "extractRenderState(Lnet/minecraft/world/entity/decoration/ItemFrame;Lnet/minecraft/client/renderer/entity/state/ItemFrameRenderState;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;getItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack L(ItemFrame var1, Operation<ItemStack> var2) {
      return w5.q(var1, (ItemStack)var2.call(new Object[]{var1}));
   }
}
