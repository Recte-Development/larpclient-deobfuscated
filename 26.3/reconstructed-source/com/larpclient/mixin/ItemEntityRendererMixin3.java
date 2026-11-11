package com.larpclient.mixin;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.exploit.DispenserMinigameSpoofer;
import com.larpclient.module.player.FishingRodSwapper;
import com.larpclient.util.inventory.DispenserRemap;
import com.larpclient.util.inventory.DispenserSpoofer;
import com.larpclient.util.inventory.MapSwapInventory;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin3 {
   @ModifyArg(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;extractItemGroupRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/item/ItemModelResolver;)V"
      ),
      index = 1
   )
   private ItemStack S(Entity var1, ItemStack var2, ItemModelResolver var3) {
      ItemEntity var4 = (ItemEntity)var1;
      var2 = MapSwapInventory.getEntityDisplayStack(var4, var2);
      var2 = DispenserMinigameSpoofer.getActive().slotCount(var4, var2);
      var2 = VisualWinnerConfig.getInstance().getTargetId(var4, var2);
      ItemStack var5 = CardForce.getActive().getVisualOverride(var2);
      if (var5 != null) {
         var2 = var5;
      }

      var2 = DispenserSpoofer.getInstance().spoofItemEntity(var4, var2);
      var2 = DispenserRemap.getInstance().remapItemEntity(var4, var2);
      return FishingRodSwapper.getInstance().isAutoSwapReady(var4.getId(), var2);
   }
}
