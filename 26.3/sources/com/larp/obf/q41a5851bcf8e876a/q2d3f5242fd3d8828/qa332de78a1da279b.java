package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.vb;
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
public class qa332de78a1da279b {
   @Shadow
   @Final
   private static EntityDataAccessor<ItemStack> DATA_ITEM;

   @Inject(method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V", at = @At("HEAD"))
   private void L(EntityDataAccessor<?> var1, CallbackInfo var2) {
      ItemFrame var3 = (ItemFrame)this;
      if (var3.level().isClientSide() && DATA_ITEM.equals(var1)) {
         vb.t(var3);
      }
   }

   @Inject(method = "getItem", at = @At("RETURN"), cancellable = true)
   private void H(CallbackInfoReturnable<ItemStack> var1) {
      ItemFrame var2 = (ItemFrame)this;
      if (var2.level().isClientSide()) {
         ItemStack var3 = vb.n(var2);
         if (var3 != null) {
            var1.setReturnValue(var3);
         }
      }
   }
}
