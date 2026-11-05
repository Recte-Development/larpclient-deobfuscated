package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.pj;
import com.larpclient.w5;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class qdce4007de0e396f1 {
   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int V(Inventory var1, Operation<Integer> var2) {
      return pj.U().c() ? pj.U().N() : w5.J();
   }
}
