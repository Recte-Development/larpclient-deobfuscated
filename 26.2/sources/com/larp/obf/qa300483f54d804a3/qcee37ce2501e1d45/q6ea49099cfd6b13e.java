package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
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
public abstract class q6ea49099cfd6b13e {
   @Shadow
   @Final
   private ClientLevel level;

   @Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
   private void s(BlockPos var1, CallbackInfoReturnable<BlockState> var2) {
      var2.setReturnValue(b8.k().T(var1, b8.k().n(var1, (BlockState)var2.getReturnValue())));
   }

   @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
   private void V(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      SpawnerBlockEntity var3 = b8.k().n(this.level, var1);
      if (var3 != null) {
         var2.setReturnValue(var3);
      }
   }
}
