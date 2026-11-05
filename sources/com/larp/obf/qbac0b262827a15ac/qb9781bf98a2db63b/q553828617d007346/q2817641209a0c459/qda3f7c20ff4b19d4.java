package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b.q553828617d007346.q2817641209a0c459;

import com.larpclient.dr;
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
public abstract class qda3f7c20ff4b19d4 {
   @Inject(
      target = @Desc(value = "getBlockState", args = {int.class, int.class, int.class}, ret = BlockState.class),
      at = @At("RETURN"),
      cancellable = true,
      remap = false
   )
   private void u(int var1, int var2, int var3, CallbackInfoReturnable<BlockState> var4) {
      dr var5 = dr.z();
      long var6 = BlockPos.asLong(var1, var2, var3);
      var4.setReturnValue(var5.x(var6, var5.E(var6, (BlockState)var4.getReturnValue())));
   }
}
