package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
import com.larpclient.x7;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class q2e855b958a75e6ca {
   @Redirect(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack p(ItemEntity var1) {
      dr var2 = dr.z();
      x7.P(var1);
      if (!var2.H()) {
         return var1.getItem();
      } else {
         return var2.L(var1) ? ItemStack.EMPTY : var2.v(var1.getItem());
      }
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void g(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      dr var5 = dr.z();
      if (var5.H() && var5.H(var1)) {
         var2.isInvisible = true;
         var2.item.clear();
         var2.count = 0;
      }
   }
}
