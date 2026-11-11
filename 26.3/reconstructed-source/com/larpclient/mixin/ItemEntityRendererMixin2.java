package com.larpclient.mixin;

import com.larpclient.util.entity.DroppedItemTracker;
import com.larpclient.util.world.FakeSpawnerController;
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
public abstract class ItemEntityRendererMixin2 {
   @Redirect(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;getItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack S(ItemEntity var1) {
      FakeSpawnerController var2 = FakeSpawnerController.getInstance();
      DroppedItemTracker.checkItem(var1);
      if (!var2.isActive()) {
         return var1.getItem();
      } else {
         return var2.isFakeSpawnerBlock(var1) ? ItemStack.EMPTY : var2.removeSpawnerEntity(var1.getItem());
      }
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void n(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      FakeSpawnerController var5 = FakeSpawnerController.getInstance();
      if (var5.isActive() && var5.isActive(var1)) {
         var2.isInvisible = true;
         var2.item.clear();
         var2.count = 0;
      }
   }
}
