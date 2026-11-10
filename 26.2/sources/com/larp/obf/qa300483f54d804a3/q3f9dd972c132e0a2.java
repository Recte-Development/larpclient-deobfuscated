package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.XRayModule;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public class q3f9dd972c132e0a2 {
   @Inject(method = "isSolidRender", at = @At("HEAD"), cancellable = true)
   private void x(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.z()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.Z(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "canOcclude", at = @At("HEAD"), cancellable = true)
   private void n(CallbackInfoReturnable<Boolean> var1) {
      if (XRayModule.z()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.Z(var2.getBlock())) {
            var1.setReturnValue(false);
         }
      }
   }

   @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
   private void N(CallbackInfoReturnable<RenderShape> var1) {
      if (XRayModule.z()) {
         BlockStateBase var2 = (BlockStateBase)this;
         if (!XRayModule.Z(var2.getBlock())) {
            var1.setReturnValue(RenderShape.INVISIBLE);
         }
      }
   }
}
