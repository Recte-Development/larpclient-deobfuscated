package com.larp.obf.q5682c6a8aca9b6ec;

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
public class q370388cc13477e29 {
   @Inject(method = "addDestroyBlockEffect", at = @At("HEAD"), cancellable = true)
   private void b(BlockPos var1, BlockState var2, CallbackInfo var3) {
      if (RemovalsModule.s("breakParticles")) {
         var3.cancel();
      }
   }

   @Inject(method = "addBreakingParticles", at = @At("HEAD"), cancellable = true)
   private void I(BlockPos var1, Direction var2, BlockState var3, CallbackInfo var4) {
      if (RemovalsModule.s("breakParticles")) {
         var4.cancel();
      }
   }
}
