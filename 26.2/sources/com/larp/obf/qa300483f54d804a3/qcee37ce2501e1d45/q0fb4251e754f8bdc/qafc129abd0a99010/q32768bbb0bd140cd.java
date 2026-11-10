package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45.q0fb4251e754f8bdc.qafc129abd0a99010;

import com.larpclient.b8;
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
public abstract class q32768bbb0bd140cd {
   @Inject(
      target = @Desc(value = "getBlockState", args = {int.class, int.class, int.class}, ret = BlockState.class),
      at = @At("RETURN"),
      cancellable = true,
      remap = false
   )
   private void V(int var1, int var2, int var3, CallbackInfoReturnable<BlockState> var4) {
      b8 var5 = b8.k();
      long var6 = BlockPos.asLong(var1, var2, var3);
      var4.setReturnValue(var5.N(var6, var5.n(var6, (BlockState)var4.getReturnValue())));
   }
}
