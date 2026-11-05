package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient._a;
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
public class qf4e20a4f0a21671d {
   @Shadow
   @Final
   private static EntityDataAccessor<ItemStack> DATA_ITEM;

   @Inject(method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V", at = @At("HEAD"))
   private void m(EntityDataAccessor<?> var1, CallbackInfo var2) {
      ItemFrame var3 = (ItemFrame)this;
      if (var3.level().isClientSide() && DATA_ITEM.equals(var1)) {
         _a.v(var3);
      }
   }

   @Inject(method = "getItem", at = @At("RETURN"), cancellable = true)
   private void a(CallbackInfoReturnable<ItemStack> var1) {
      ItemFrame var2 = (ItemFrame)this;
      if (var2.level().isClientSide()) {
         ItemStack var3 = _a.q(var2);
         if (var3 != null) {
            var1.setReturnValue(var3);
         }
      }
   }
}
