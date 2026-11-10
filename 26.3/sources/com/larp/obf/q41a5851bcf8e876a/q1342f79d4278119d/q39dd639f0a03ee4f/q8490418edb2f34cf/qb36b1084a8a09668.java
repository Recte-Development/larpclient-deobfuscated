package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d.q39dd639f0a03ee4f.q8490418edb2f34cf;

import com.larpclient.jn;
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
public abstract class qb36b1084a8a09668 {
   @Inject(
      target = @Desc(value = "getBlockState", args = {int.class, int.class, int.class}, ret = BlockState.class),
      at = @At("RETURN"),
      cancellable = true,
      remap = false
   )
   private void I(int var1, int var2, int var3, CallbackInfoReturnable<BlockState> var4) {
      jn var5 = jn.g();
      long var6 = BlockPos.asLong(var1, var2, var3);
      var4.setReturnValue(var5.q(var6, var5.s(var6, (BlockState)var4.getReturnValue())));
   }
}
