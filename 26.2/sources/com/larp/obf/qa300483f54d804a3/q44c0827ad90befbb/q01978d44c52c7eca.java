package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.cc;
import com.larpclient.mg;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class q01978d44c52c7eca {
   @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int p(Inventory var1, Operation<Integer> var2) {
      return cc.z().v() ? cc.z().g() : mg.j();
   }
}
