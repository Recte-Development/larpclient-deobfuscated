package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.HolesESPModule;
import com.larpclient.module.to;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class qd2c7cba42c8098b2 {
   @Inject(method = "setServerVerifiedBlockState", at = @At("HEAD"))
   private void t(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      to.y((ClientLevel)this, var1, var2);
   }

   @Inject(method = "setServerVerifiedBlockState", at = @At("TAIL"))
   private void C(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      HolesESPModule.e((ClientLevel)this, var1);
   }
}
