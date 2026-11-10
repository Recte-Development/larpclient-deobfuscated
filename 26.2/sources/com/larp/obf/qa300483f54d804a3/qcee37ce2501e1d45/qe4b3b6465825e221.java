package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.ki;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public abstract class qe4b3b6465825e221 {
   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void N(Inventory var1, int var2, Operation<Void> var3) {
      if (ki.k()) {
         ki.x(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }

   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int q(Inventory var1, Operation<Integer> var2) {
      return ki.k((Integer)var2.call(new Object[]{var1}));
   }
}
