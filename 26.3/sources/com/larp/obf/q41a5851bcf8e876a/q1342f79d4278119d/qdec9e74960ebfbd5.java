package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient.jn;
import com.larpclient.qb;
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
public abstract class qdec9e74960ebfbd5 {
   @Redirect(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack S(ItemEntity var1) {
      jn var2 = jn.g();
      qb.a(var1);
      if (!var2.a()) {
         return var1.getItem();
      } else {
         return var2.c(var1) ? ItemStack.EMPTY : var2.w(var1.getItem());
      }
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void n(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      jn var5 = jn.g();
      if (var5.a() && var5.a(var1)) {
         var2.isInvisible = true;
         var2.item.clear();
         var2.count = 0;
      }
   }
}
