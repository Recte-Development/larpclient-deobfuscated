package com.larpclient.mixin;

import com.larpclient.render.world.RealisticWater;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin4 {
   @Inject(method = "sendBlockUpdated", at = @At("HEAD"))
   private void B(BlockPos var1, BlockState var2, BlockState var3, int var4, CallbackInfo var5) {
      if (var2 != var3) {
         RealisticWater.onBlockChange(var1, var2, var3);
      }
   }
}
