package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.mx;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public abstract class q029e20b93415ed40 {
   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void U(Inventory var1, int var2, Operation<Void> var3) {
      if (mx.U()) {
         mx.P(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }

   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int T(Inventory var1, Operation<Integer> var2) {
      return mx.z((Integer)var2.call(new Object[]{var1}));
   }
}
