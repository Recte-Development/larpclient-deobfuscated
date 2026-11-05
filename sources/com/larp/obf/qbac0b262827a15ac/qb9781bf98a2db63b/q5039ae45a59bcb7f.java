package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelEventHandler.class)
public abstract class q5039ae45a59bcb7f {
   @WrapOperation(
      method = "levelEvent",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0)
   )
   private BlockState g(int var1, Operation<BlockState> var2, int var3, BlockPos var4, int var5) {
      return dr.z().f(var4, (BlockState)var2.call(new Object[]{var1}));
   }
}
