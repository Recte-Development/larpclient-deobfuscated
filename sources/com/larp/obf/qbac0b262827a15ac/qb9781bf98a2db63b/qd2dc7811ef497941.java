package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.u0;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public abstract class qd2dc7811ef497941 {
   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void l(Inventory var1, int var2, Operation<Void> var3) {
      if (u0.w()) {
         u0.K(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }

   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int l(Inventory var1, Operation<Integer> var2) {
      return u0.Q((Integer)var2.call(new Object[]{var1}));
   }
}
