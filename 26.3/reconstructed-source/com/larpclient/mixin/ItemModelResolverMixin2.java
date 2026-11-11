package com.larpclient.mixin;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.exploit.DispenserMinigameSpoofer;
import com.larpclient.util.inventory.DispenserRemap;
import com.larpclient.util.inventory.DispenserSpoofer;
import com.larpclient.util.misc.FiftyFiftyDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemModelResolver.class)
public abstract class ItemModelResolverMixin2 {
   @ModifyVariable(
      method = "updateForLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/LivingEntity;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack x(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, LivingEntity var5) {
      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && FiftyFiftyDispenser.getInstance().hideHeldStack(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      if (var5 == Minecraft.getInstance().player && !var1.isEmpty() && BlackjackRigger.getInstance().hideParkedStack(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      ItemStack var6 = VisualWinnerConfig.getInstance().isHideNames(var5, var1);
      if (var5 == Minecraft.getInstance().player) {
         var6 = DispenserMinigameSpoofer.getActive().spoofStack(var6);
      }

      var6 = DispenserSpoofer.getInstance().spoofForEntity(var5, var6);
      var6 = DispenserRemap.getInstance().remapForPlayer(var5, var6);
      ItemStack var7 = CardForce.getActive().getVisualOverride(var6);
      if (var7 != null) {
         var6 = var7;
      }

      return var6;
   }

   @ModifyVariable(
      method = "updateForNonLiving(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/entity/Entity;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private ItemStack r(ItemStack var1, ItemStackRenderState var2, ItemStack var3, ItemDisplayContext var4, Entity var5) {
      if (var5 instanceof ItemEntity var6) {
         var1 = DispenserMinigameSpoofer.getActive().slotCount(var6, var1);
         var1 = VisualWinnerConfig.getInstance().getTargetId(var6, var1);
         var1 = DispenserSpoofer.getInstance().spoofItemEntity(var6, var1);
         var1 = DispenserRemap.getInstance().remapItemEntity(var6, var1);
      }

      ItemStack var10 = CardForce.getActive().getVisualOverride(var1);
      if (var10 != null) {
         var1 = var10;
      }

      return var1;
   }
}
