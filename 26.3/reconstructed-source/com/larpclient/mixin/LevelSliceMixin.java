package com.larpclient.mixin;

import com.larpclient.util.world.FakeSpawnerController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.world.LevelSlice", remap = false)
public abstract class LevelSliceMixin {
   @Inject(
      target = @Desc(value = "getBlockState", args = {int.class, int.class, int.class}, ret = BlockState.class),
      at = @At("RETURN"),
      cancellable = true,
      remap = false
   )
   private void I(int var1, int var2, int var3, CallbackInfoReturnable<BlockState> var4) {
      FakeSpawnerController var5 = FakeSpawnerController.getInstance();
      long var6 = BlockPos.asLong(var1, var2, var3);
      var4.setReturnValue(var5.trackDerivedStack(var6, var5.readPilesFrom(var6, (BlockState)var4.getReturnValue())));
   }
}
