package com.larpclient.mixin;

import com.larpclient.module.player.PhantomItemManager;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {
   @Shadow
   @Final
   private static EntityDataAccessor<ItemStack> DATA_ITEM;

   @Inject(method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V", at = @At("HEAD"))
   private void L(EntityDataAccessor<?> var1, CallbackInfo var2) {
      ItemFrame var3 = (ItemFrame)this;
      if (var3.level().isClientSide() && DATA_ITEM.equals(var1)) {
         PhantomItemManager.clearFrameItem(var3);
      }
   }

   @Inject(method = "getItem", at = @At("RETURN"), cancellable = true)
   private void H(CallbackInfoReturnable<ItemStack> var1) {
      ItemFrame var2 = (ItemFrame)this;
      if (var2.level().isClientSide()) {
         ItemStack var3 = PhantomItemManager.getFrameItem(var2);
         if (var3 != null) {
            var1.setReturnValue(var3);
         }
      }
   }
}
