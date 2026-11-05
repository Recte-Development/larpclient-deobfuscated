package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d.qa1bcaa5546d63caf.q34316bc46cc078a8;

import com.larpclient.lj;
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
public abstract class q89bed71bc7611763 {
   @Inject(
      target = @Desc(value = "getBlockState", args = {int.class, int.class, int.class}, ret = BlockState.class),
      at = @At("RETURN"),
      cancellable = true,
      remap = false
   )
   private void i(int var1, int var2, int var3, CallbackInfoReturnable<BlockState> var4) {
      lj var5 = lj.j();
      long var6 = BlockPos.asLong(var1, var2, var3);
      var4.setReturnValue(var5.E(var6, var5.i(var6, (BlockState)var4.getReturnValue())));
   }
}
