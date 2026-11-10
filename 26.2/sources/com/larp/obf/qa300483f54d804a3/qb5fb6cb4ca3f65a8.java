package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.HolesESPModule;
import com.larpclient.module.pi;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class qb5fb6cb4ca3f65a8 {
   @Inject(method = "setServerVerifiedBlockState", at = @At("HEAD"))
   private void u(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      pi.R((ClientLevel)this, var1, var2);
   }

   @Inject(method = "setServerVerifiedBlockState", at = @At("TAIL"))
   private void j(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      HolesESPModule.K((ClientLevel)this, var1);
   }
}
