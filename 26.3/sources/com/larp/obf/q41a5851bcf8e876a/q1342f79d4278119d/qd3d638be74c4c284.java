package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient.jn;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelChunk.class)
public abstract class qd3d638be74c4c284 {
   @Shadow
   @Final
   private Level level;

   @Redirect(
      method = {"createBlockEntity", "promotePendingBlockEntity", "setBlockEntity"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/chunk/LevelChunk;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState U(LevelChunk var1, BlockPos var2) {
      if (this.level instanceof ClientLevel && jn.g().x(var2)) {
         int var3 = var1.getSectionIndex(var2.getY());
         return var3 >= 0 && var3 < var1.getSections().length
            ? var1.getSection(var3).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15)
            : Blocks.AIR.defaultBlockState();
      } else {
         return var1.getBlockState(var2);
      }
   }
}
