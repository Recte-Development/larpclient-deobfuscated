package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.jy;
import com.larpclient.lj;
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
public abstract class q7fe13250ff9496ef {
   @Redirect(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack Y(ItemEntity var1) {
      lj var2 = lj.j();
      jy.w(var1);
      if (!var2.B()) {
         return var1.getItem();
      } else {
         return var2.o(var1) ? ItemStack.EMPTY : var2.x(var1.getItem());
      }
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void y(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      lj var5 = lj.j();
      if (var5.B() && var5.H(var1)) {
         var2.isInvisible = true;
         var2.item.clear();
         var2.count = 0;
      }
   }
}
