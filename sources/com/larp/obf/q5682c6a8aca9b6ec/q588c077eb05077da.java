package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.JesusModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityCollisionContext.class)
public class q588c077eb05077da {
   @Inject(method = "getCollisionShape", at = @At("RETURN"), cancellable = true)
   private void x(BlockState var1, CollisionGetter var2, BlockPos var3, CallbackInfoReturnable<VoxelShape> var4) {
      var4.setReturnValue(JesusModule.v(var1, var2, var3, (EntityCollisionContext)this, (VoxelShape)var4.getReturnValue()));
   }
}
