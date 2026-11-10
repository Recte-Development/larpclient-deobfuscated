package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q6780db403bf6e5d8;

import com.larpclient.v0;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class qc91f8307a211d876 {
   @Inject(method = "sendBlockUpdated", at = @At("HEAD"))
   private void B(BlockPos var1, BlockState var2, BlockState var3, int var4, CallbackInfo var5) {
      if (var2 != var3) {
         v0.u(var1, var2, var3);
      }
   }
}
