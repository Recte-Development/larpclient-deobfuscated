package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
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
public abstract class q775cdd0541cbbd7e {
   @Shadow
   @Final
   private ClientLevel level;

   @Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
   private void B(BlockPos var1, CallbackInfoReturnable<BlockState> var2) {
      var2.setReturnValue(dr.z().e(var1, dr.z().f(var1, (BlockState)var2.getReturnValue())));
   }

   @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
   private void A(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      SpawnerBlockEntity var3 = dr.z().K(this.level, var1);
      if (var3 != null) {
         var2.setReturnValue(var3);
      }
   }
}
