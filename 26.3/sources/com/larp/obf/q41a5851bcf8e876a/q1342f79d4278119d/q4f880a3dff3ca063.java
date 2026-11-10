package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient.jn;
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
public abstract class q4f880a3dff3ca063 {
   @Shadow
   @Final
   private ClientLevel level;

   @Inject(method = "getBlockState", at = @At("RETURN"), cancellable = true)
   private void y(BlockPos var1, CallbackInfoReturnable<BlockState> var2) {
      var2.setReturnValue(jn.g().A(var1, jn.g().C(var1, (BlockState)var2.getReturnValue())));
   }

   @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
   private void j(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      SpawnerBlockEntity var3 = jn.g().r(this.level, var1);
      if (var3 != null) {
         var2.setReturnValue(var3);
      }
   }
}
