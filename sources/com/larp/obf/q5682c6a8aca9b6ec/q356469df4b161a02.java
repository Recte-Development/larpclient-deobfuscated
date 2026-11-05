package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.XRayModule;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public class q356469df4b161a02 {
   @Inject(method = "isSolidRender", at = @At("HEAD"), cancellable = true)
   private void g(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.q()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.c(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "canOcclude", at = @At("HEAD"), cancellable = true)
   private void U(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.q()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.c(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
   private void P(CallbackInfoReturnable<RenderShape> var1) {
      if (XRayModule.q()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.c(var2.getBlock())) {
            var1.setReturnValue(RenderShape.INVISIBLE);
         }
      }
   }
}
