package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import com.larpclient.bo;
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
public abstract class qc2c42d77d66f37dc {
   @Redirect(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack D(ItemEntity var1) {
      b8 var2 = b8.k();
      bo.b(var1);
      if (!var2.J()) {
         return var1.getItem();
      } else {
         return var2.o(var1) ? ItemStack.EMPTY : var2.D(var1.getItem());
      }
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void K(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      b8 var5 = b8.k();
      if (var5.J() && var5.A(var1)) {
         var2.isInvisible = true;
         var2.item.clear();
         var2.count = 0;
      }
   }
}
