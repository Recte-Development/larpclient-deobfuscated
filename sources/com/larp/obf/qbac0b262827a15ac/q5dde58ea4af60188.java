package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.RemovalsModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class q5dde58ea4af60188 {
   @Inject(method = "addDestroyBlockEffect", at = @At("HEAD"), cancellable = true)
   private void r(BlockPos var1, BlockState var2, CallbackInfo var3) {
      if (RemovalsModule.r("breakParticles")) {
         var3.cancel();
      }
   }

   @Inject(method = "addBreakingParticles", at = @At("HEAD"), cancellable = true)
   private void A(BlockPos var1, Direction var2, BlockState var3, CallbackInfo var4) {
      if (RemovalsModule.r("breakParticles")) {
         var4.cancel();
      }
   }
}
