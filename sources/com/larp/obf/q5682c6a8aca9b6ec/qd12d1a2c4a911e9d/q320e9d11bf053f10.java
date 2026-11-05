package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.lj;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelEventHandler.class)
public abstract class q320e9d11bf053f10 {
   @WrapOperation(
      method = "levelEvent",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0)
   )
   private BlockState k(int var1, Operation<BlockState> var2, int var3, BlockPos var4, int var5) {
      return lj.j().h(var4, (BlockState)var2.call(new Object[]{var1}));
   }
}
