package com.larpclient.mixin;

import com.larpclient.util.world.FakeSpawnerController;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelEventHandler.class)
public abstract class LevelEventHandlerMixin {
   @WrapOperation(
      method = "levelEvent",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;stateById(I)Lnet/minecraft/world/level/block/state/BlockState;", ordinal = 0)
   )
   private BlockState v(int var1, Operation<BlockState> var2, int var3, BlockPos var4, int var5) {
      return FakeSpawnerController.getInstance().getFillerItem(var4, (BlockState)var2.call(new Object[]{var1}));
   }
}
