package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.XRayModule;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public class q40bfa3fed2264809 {
   @Inject(method = "isSolidRender", at = @At("HEAD"), cancellable = true)
   private void b(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.S()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.g(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "canOcclude", at = @At("HEAD"), cancellable = true)
   private void c(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.S()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.g(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
   private void o(CallbackInfoReturnable<RenderShape> var1) {
      if (XRayModule.S()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.g(var2.getBlock())) {
            var1.setReturnValue(RenderShape.INVISIBLE);
         }
      }
   }
}
