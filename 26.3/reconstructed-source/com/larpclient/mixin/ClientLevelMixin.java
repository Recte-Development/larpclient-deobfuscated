package com.larpclient.mixin;

import com.larpclient.module.render.HolesESPModule;
import com.larpclient.module.world.BaseFinder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
   @Inject(method = "setServerVerifiedBlockState", at = @At("HEAD"))
   private void H(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      BaseFinder.onBlockUpdate((ClientLevel)this, var1, var2);
   }

   @Inject(method = "setServerVerifiedBlockState", at = @At("TAIL"))
   private void d(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      HolesESPModule.onBlockUpdate((ClientLevel)this, var1);
   }
}
