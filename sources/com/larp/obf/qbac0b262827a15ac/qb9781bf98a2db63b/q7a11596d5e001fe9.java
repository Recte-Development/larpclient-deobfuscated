package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
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
public abstract class q7a11596d5e001fe9 {
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
   private BlockState d(LevelChunk var1, BlockPos var2) {
      if (this.level instanceof ClientLevel && dr.z().O(var2)) {
         int var3 = var1.getSectionIndex(var2.getY());
         return var3 >= 0 && var3 < var1.getSections().length
            ? var1.getSection(var3).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15)
            : Blocks.AIR.defaultBlockState();
      } else {
         return var1.getBlockState(var2);
      }
   }
}
