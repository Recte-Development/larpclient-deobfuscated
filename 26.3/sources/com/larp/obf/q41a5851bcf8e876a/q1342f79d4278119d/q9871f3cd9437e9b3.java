package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient.jn;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelEventHandler.class)
public abstract class q9871f3cd9437e9b3 {
   @WrapOperation(
      method = "levelEvent",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0)
   )
   private BlockState v(int var1, Operation<BlockState> var2, int var3, BlockPos var4, int var5) {
      return jn.g().C(var4, (BlockState)var2.call(new Object[]{var1}));
   }
}
