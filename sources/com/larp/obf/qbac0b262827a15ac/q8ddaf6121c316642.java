package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.XRayModule;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public class q8ddaf6121c316642 {
   @Inject(method = "isSolidRender", at = @At("HEAD"), cancellable = true)
   private void P(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.m()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.z(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "canOcclude", at = @At("HEAD"), cancellable = true)
   private void A(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.m()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.z(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
   private void G(CallbackInfoReturnable<RenderShape> var1) {
      if (XRayModule.m()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.z(var2.getBlock())) {
            var1.setReturnValue(RenderShape.INVISIBLE);
         }
      }
   }
}
