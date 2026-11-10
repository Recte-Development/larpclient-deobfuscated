package com.larp.obf.qa300483f54d804a3;

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
public class q1a1342fef7e237ca {
   @Inject(method = "addDestroyBlockEffect", at = @At("HEAD"), cancellable = true)
   private void W(BlockPos var1, BlockState var2, CallbackInfo var3) {
      if (RemovalsModule.n("breakParticles")) {
         var3.cancel();
      }
   }

   @Inject(method = "addBreakingBlockEffect", at = @At("HEAD"), cancellable = true)
   private void i(BlockPos var1, Direction var2, CallbackInfo var3) {
      if (RemovalsModule.n("breakParticles")) {
         var3.cancel();
      }
   }
}
