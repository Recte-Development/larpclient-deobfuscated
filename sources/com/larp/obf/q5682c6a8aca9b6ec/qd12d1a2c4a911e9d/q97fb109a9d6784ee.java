package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.lj;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSectionRegion.class)
public abstract class q97fb109a9d6784ee {
   @Shadow
   @Final
   private ClientLevel level;

   @Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
   private void D(BlockPos var1, CallbackInfoReturnable<BlockState> var2) {
      var2.setReturnValue(lj.j().r(var1, lj.j().h(var1, (BlockState)var2.getReturnValue())));
   }

   @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
   private void k(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      SpawnerBlockEntity var3 = lj.j().v(this.level, var1);
      if (var3 != null) {
         var2.setReturnValue(var3);
      }
   }
}
