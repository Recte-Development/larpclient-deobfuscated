package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.qe1de85825286c614;

import com.larpclient.b9;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class q0b6ae74844fa4f61 {
   @Inject(method = "sendBlockUpdated", at = @At("HEAD"))
   private void B(BlockPos var1, BlockState var2, BlockState var3, int var4, CallbackInfo var5) {
      if (var2 != var3) {
         b9.w(var1, var2, var3);
      }
   }
}
