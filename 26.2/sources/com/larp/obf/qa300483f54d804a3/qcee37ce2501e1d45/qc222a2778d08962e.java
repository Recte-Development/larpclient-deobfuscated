package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelEventHandler.class)
public abstract class qc222a2778d08962e {
   @WrapOperation(
      method = "levelEvent",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0)
   )
   private BlockState Q(int var1, Operation<BlockState> var2, int var3, BlockPos var4, int var5) {
      return b8.k().n(var4, (BlockState)var2.call(new Object[]{var1}));
   }
}
