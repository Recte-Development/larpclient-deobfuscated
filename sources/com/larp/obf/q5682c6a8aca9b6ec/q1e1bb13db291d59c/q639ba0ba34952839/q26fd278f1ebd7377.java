package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q639ba0ba34952839;

import com.larpclient.df;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class q26fd278f1ebd7377 {
   @Inject(method = "sendBlockUpdated", at = @At("HEAD"))
   private void H(BlockPos var1, BlockState var2, BlockState var3, int var4, CallbackInfo var5) {
      if (var2 != var3) {
         df.z(var1, var2, var3);
      }
   }
}
